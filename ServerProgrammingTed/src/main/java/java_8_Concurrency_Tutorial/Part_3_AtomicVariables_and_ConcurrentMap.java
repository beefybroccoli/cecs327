package java_8_Concurrency_Tutorial;

import static part_2_assignment_version_final.object.VALUE.echo;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiFunction;
import java.util.function.LongBinaryOperator;

import java.util.stream.IntStream;


/*
source : http://winterbe.com/posts/2015/05/22/java8-concurrency-tutorial-atomic-concurrent-map-examples/
 */
public class Part_3_AtomicVariables_and_ConcurrentMap {

    public static void main(String[] args) {

        demo_atomic_intger_accumulateAndGet();
//        demo_atomic_integer();
//        concurrent_map();
//        concurrent_hash_map();

    }

    //--------------------------------------------------------------------------
    /**
     * We use the same example map for demonstrating purposes but this time we
     * work upon the concrete implementation ConcurrentHashMap instead of the
     * interface ConcurrentMap, so we can access all public methods from this
     * class:
     *
     *
     * Java 8 introduces three kinds of parallel operations: forEach, search and
     * reduce. Each of those operations are available in four forms accepting
     * functions with keys, values, entries and key-value pair arguments.
     *
     * All of those methods use a common first argument called
     * parallelismThreshold. This threshold indicates the minimum collection
     * size when the operation should be executed in parallel. E.g. if you pass
     * a threshold of 500 and the actual size of the map is 499 the operation
     * will be performed sequentially on a single thread. In the next examples
     * we use a threshold of one to always force parallel execution for
     * demonstrating purposes.
     *
     */
    public static void concurrent_hash_map() {

        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("foo", "bar");
        map.put("han", "solo");
        map.put("r2", "d2");
        map.put("c3", "p0");

        /**
         * The method forEach() is capable of iterating over the key-value pairs
         * of the map in parallel. The lambda expression of type BiConsumer is
         * called with the key and value of the current iteration step. In order
         * to visualize parallel execution we print the current threads name to
         * the console. Keep in mind that in my case the underlying ForkJoinPool
         * uses up to a maximum of three threads.
         *
         * // key: r2; value: d2; thread: main // key: foo; value: bar; thread:
         * ForkJoinPool.commonPool-worker-1 // key: han; value: solo; thread:
         * ForkJoinPool.commonPool-worker-2 // key: c3; value: p0; thread: main
         */
        map.forEach(1, (key, value)
                -> System.out.printf("key: %s; value: %s; thread: %s\n",
                        key, value, Thread.currentThread().getName()));

        /**
         * The method search() accepts a BiFunction returning a non-null search
         * result for the current key-value pair or null if the current
         * iteration doesn't match the desired search criteria. As soon as a
         * non-null result is returned further processing is suppressed. Keep in
         * mind that ConcurrentHashMap is unordered. The search function should
         * not depend on the actual processing order of the map. If multiple
         * entries of the map match the given search function the result may be
         * non-deterministic.
         *
         * // ForkJoinPool.commonPool-worker-2 // main //
         * ForkJoinPool.commonPool-worker-3 // Result: bar
         */
        String result = map.search(1, (key, value) -> {
            System.out.println(Thread.currentThread().getName());
            if ("foo".equals(key)) {
                return value;
            }
            return null;
        });
        System.out.println("Result: " + result);

        /**
         * Here's another example searching solely on the values of the map:
         */
        result = map.searchValues(1, value -> {
            System.out.println(Thread.currentThread().getName());
            if (value.length() > 3) {
                return value;
            }
            return null;
        });

        System.out.println("Result: " + result);

        /**
         * Reduce The method reduce() already known from Java 8 Streams accepts
         * two lambda expressions of type BiFunction. The first function
         * transforms each key-value pair into a single value of any type. The
         * second function combines all those transformed values into a single
         * result, ignoring any possible null values.
         */
        result = map.reduce(1,
                (key, value) -> {
                    System.out.println("Transform: " + Thread.currentThread().getName());
                    return key + "=" + value;
                },
                (s1, s2) -> {
                    System.out.println("Reduce: " + Thread.currentThread().getName());
                    return s1 + ", " + s2;
                });

        System.out.println("Result: " + result);
    }

    //--------------------------------------------------------------------------
    /**
     * The interface ConcurrentMap extends the map interface and defines one of
     * the most useful concurrent collection types. Java 8 introduces functional
     * programming by adding new methods to this interface.
     */
    public static void concurrent_map() {
        ConcurrentMap<String, String> map = new ConcurrentHashMap<>();
        map.put("foo", "bar");
        map.put("han", "solo");
        map.put("r2", "d2");
        map.put("c3", "p0");

        /**
         * The method forEach() accepts a lambda expression of type BiConsumer
         * with both the key and value of the map passed as parameters. It can
         * be used as a replacement to for-each loops to iterate over the
         * entries of the concurrent map. The iteration is performed
         * sequentially on the current thread.
         */
        map.forEach((key, value) -> System.out.printf("%s = %s\n", key, value));

        /**
         * The method putIfAbsent() puts a new value into the map only if no
         * value exists for the given key. At least for the ConcurrentHashMap
         * implementation of this method is thread-safe just like put() so you
         * don't have to synchronize when accessing the map concurrently from
         * different threads:
         */
        String value = map.putIfAbsent("c3", "p1");
        System.out.println(value);    // p0

        /**
         * The method getOrDefault() returns the value for the given key. In
         * case no entry exists for this key the passed default value is
         * returned:
         */
        value = map.getOrDefault("hi", "there");
        System.out.println(value);    // there

        /**
         * The method replaceAll() accepts a lambda expression of type
         * BiFunction. BiFunctions take two parameters and return a single
         * value. In this case the function is called with the key and the value
         * of each map entry and returns a new value to be assigned for the
         * current key:
         */
        map.replaceAll(new BiFunction<String, String, String>() {
            @Override
            public String apply(String key, String value) {
                return "r2".equals(key) ? "d3" : value;
            }
        });
        System.out.println(map.get("r2")); // d3

        /**
         * Instead of replacing all values of the map compute() let's us
         * transform a single entry. The method accepts both the key to be
         * computed and a bi-function to specify the transformation of the
         * value.
         */
        map.compute("foo", new BiFunction<String, String, String>() {
            @Override
            public String apply(String key, String value) {
                return value + value;
            }
        });
        System.out.println(map.get("foo"));   // barbar

        /**
         * the method merge() can be utilized to unify a new value with an
         * existing value in the map. Merge accepts a key, the new value to be
         * merged into the existing entry and a bi-function to specify the
         * merging behavior of both values:
         */
        map.merge("foo", "boo", (oldVal, newVal) -> newVal + " was " + oldVal);
        System.out.println(map.get("foo"));   // boo was foo

//        computeIfAbsent();
//        computeIfPresent();
    }

    /**
     * computeIfAbsent In addition to compute() two variants exist:
     * computeIfAbsent() and computeIfPresent(). The functional parameters of
     * these methods only get called if the key is absent or present
     * respectively.
     */
    public static void concurrent_map_computeIfAbsent() {
        Map<Integer, Integer> cache
                = new ConcurrentHashMap<>();
        System.out.println(
                "f(" + 25 + ") = " + fibonacci(cache, 25));
    }

    /**
     * computeIfPresent In addition to compute() two variants exist:
     * computeIfAbsent() and computeIfPresent(). The functional parameters of
     * these methods only get called if the key is absent or present
     * respectively.
     */
    public static void concurrent_map_computeIfPresent() {
        Map<Integer, Integer> cache
                = new ConcurrentHashMap<>();
        System.out.println(
                "f(" + 25 + ") = " + fibonacci(cache, 25));
    }

    /**
     * computeIfAbsent's helper method
     *
     * @param i
     * @return
     */
    public static int fibonacci(Map<Integer, Integer> cache, int i) {
        if (i == 0) {
            return i;
        }

        if (i == 1) {
            return 1;
        }

        return (int) cache.computeIfAbsent(i, (key) -> {
            System.out.println(
                    "Slow calculation of " + key);

            return fibonacci(cache, i - 2) + fibonacci(cache, i - 1);
        });
    }

    //--------------------------------------------------------------------------
    /**
     * LongAdder The class LongAdder as an alternative to AtomicLong can be used
     * to consecutively add values to a number.
     *
     * LongAdder provides methods add() and increment() just like the atomic
     * number classes and is also thread-safe. But instead of summing up a
     * single result this class maintains a set of variables internally to
     * reduce contention over threads. The actual result can be retrieved by
     * calling sum() or sumThenReset().
     *
     * This class is usually preferable over atomic numbers when updates from
     * multiple threads are more common than reads. This is often the case when
     * capturing statistical data, e.g. you want to count the number of requests
     * served on a web server. The drawback of LongAdder is higher memory
     * consumption because a set of variables is held in-memory.
     */
    public static void long_adder() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        LongAdder adder = new LongAdder();

        IntStream.range(0, 1000)
                .forEach(
                        i -> executor.submit(adder::increment)
                );

        stop(executor);

        System.out.println(adder.sumThenReset());   // => 1000
    }

    /**
     * LongAccumulator is a more generalized version of LongAdder. Instead of
     * performing simple add operations the class LongAccumulator builds around
     * a lambda expression of type LongBinaryOperator as demonstrated in this
     * code sample:
     *
     * We create a LongAccumulator with the function 2 * x + y and an initial
     * value of one. With every call to accumulate(i) both the current result
     * and the value i are passed as parameters to the lambda expression.
     *
     * A LongAccumulator just like LongAdder maintains a set of variables
     * internally to reduce contention over threads.
     */
    public static void long_accumulator() {
        LongBinaryOperator op = (x, y) -> 2 * x + y;
        LongAccumulator accumulator = new LongAccumulator(op, 1L);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, 10)
                .forEach(
                        i -> executor.submit(
                                () -> accumulator.accumulate(i)
                        )
                );

        stop(executor);

        System.out.println(accumulator.getThenReset());     // => 2539
    }

    //--------------------------------------------------------------------------
    /**
     * The package java.concurrent.atomic contains many useful classes to
     * perform atomic operations. An operation is atomic when you can safely
     * perform the operation in parallel on multiple threads
     *
     * Internally, the atomic classes make heavy use of compare-and-swap (CAS),
     * an atomic instruction directly supported by most modern CPUs. Those
     * instructions usually are much faster than synchronizing via locks. So my
     * advice is to prefer atomic classes over locks in case you just have to
     * change a single mutable variable concurrently.
     */
    public static void demo_atomic_integer() {
        AtomicInteger atomicInt = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, 1000)
                .forEach(
                        i -> executor.submit(atomicInt::incrementAndGet)
                );

        stop(executor);

        System.out.println(atomicInt.get());    // => 1000

        demo_atomic_intger_updateAndGet();

        demo_atomic_intger_accumulateAndGet();
    }

    /**
     * By using AtomicInteger as a replacement for Integer we're able to
     * increment the number concurrently in a thread-safe manor without
     * synchronizing the access to the variable. The method incrementAndGet() is
     * an atomic operation so we can safely call this method from multiple
     * threads.
     *
     * AtomicInteger supports various kinds of atomic operations. The method
     * updateAndGet() accepts a lambda expression in order to perform arbitrary
     * arithmetic operations upon the integer:
     */
    public static void demo_atomic_intger_updateAndGet() {
        AtomicInteger atomicInt = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, 1000)
                .forEach(
                        i -> {
                            Runnable task = ()
                            -> atomicInt.updateAndGet(n -> n + 2);
                            executor.submit(task);
                        }
                );

        stop(executor);

        System.out.println(atomicInt.get());    // => 2000
    }

    /**
     * The method accumulateAndGet() accepts another kind of lambda expression
     * of type IntBinaryOperator. We use this method to sum up all values from 0
     * to 1000 concurrently in demo_atomic_intger_accumulateAndGet method:
     */
    public static void demo_atomic_intger_accumulateAndGet() {
        AtomicInteger atomicInt = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, 1000)
                .forEach(
                        i -> {
                            Runnable task = ()
                            -> atomicInt.accumulateAndGet(i, (n, m) -> n + m);
                            executor.submit(task);
                        }
                );

        stop(executor);

        System.out.println(atomicInt.get());    // => 499500
    }

    //--------------------------------------------------------------------------
    /**
     * helper method that stop ExecutorService
     *
     * @param executor
     */
    public static void stop(ExecutorService executor) {
        try {
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("termination interrupted");
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("killing non-finished tasks");
            }
            executor.shutdownNow();
        }
    }

    /**
     * helper method that put a thread to sleep for # seconds
     *
     * @param seconds
     */
    public static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
