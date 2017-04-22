package java_8_Concurrency_Tutorial;

import static VALUE.VALUE.echo;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

public class Part_2_Synchronization_and_Locks {

    ReentrantLock lock = new ReentrantLock();
    private static int count = 0;

    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
//        reentrant_lock();
//
//        read_write_lock();
//
//        stamped_lock_sample();
//
//        demonstrate_optimistic_lock();
//
//        demonstrate_optimistic_lock_with_tryConvertToWriteLock();

    }

    //-----------------------------------------------------------------------------------------------
    /**
     * Stamped lock Java 8 ships with a new kind of lock called StampedLock
     * which also support read and write locks just like in the example above.
     *
     * In contrast to ReadWriteLock the locking methods of a StampedLock return
     * a stamp represented by a long value. You can use these stamps to either
     * release a lock or to check if the lock is still valid.
     *
     * Additionally stamped locks support another lock mode called optimistic
     * locking.
     *
     *
     * Obtaining a read or write lock via readLock() or writeLock() returns a
     * stamp which is later used for unlocking within the finally block. Keep in
     * mind that stamped locks don't implement reentrant characteristics. Each
     * call to lock returns a new stamp and blocks if no lock is available even
     * if the same thread already holds a lock. So you have to pay particular
     * attention not to run into deadlocks.
     *
     * Just like in the previous ReadWriteLock example both read tasks have to
     * wait until the write lock has been released. Then both read tasks print
     * to the console simultaneously because multiple reads doesn't block each
     * other as long as no write-lock is held.
     */
    public static void stamped_lock_sample() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Map<String, String> map = new HashMap<>();
        StampedLock lock = new StampedLock();

        executor.submit(
                () -> {
                    long stamp = lock.writeLock();
                    try {
                        try {
                            //sleep for one second
                            TimeUnit.MILLISECONDS.sleep(1);
                        } catch (InterruptedException ex) {
                            echo("InterruptedException occured");
                        }
                        map.put("foo", "bar");
                    } finally {
                        lock.unlockWrite(stamp);
                    }
                }
        );

        Runnable readTask = () -> {
            long stamp = lock.readLock();
            try {
                System.out.println(map.get("foo"));
                try {
                    //sleep for one second
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    echo("InterruptedException occured");
                }
            } finally {
                lock.unlockRead(stamp);
            }
        };

        executor.submit(readTask);
        executor.submit(readTask);

        stop(executor);
    }

    /**
     * The next example demonstrates optimistic locking:
     *
     * An optimistic read lock is acquired by calling tryOptimisticRead() which
     * always returns a stamp without blocking the current thread, no matter if
     * the lock is actually available. If there's already a write lock active
     * the returned stamp equals zero. You can always check if a stamp is valid
     * by calling lock.validate(stamp).
     *
     * The optimistic lock is valid right after acquiring the lock. In contrast
     * to normal read locks an optimistic lock doesn't prevent other threads to
     * obtain a write lock instantaneously. After sending the first thread to
     * sleep for one second the second thread obtains a write lock without
     * waiting for the optimistic read lock to be released. From this point the
     * optimistic read lock is no longer valid. Even when the write lock is
     * released the optimistic read locks stays invalid.
     *
     * So when working with optimistic locks you have to validate the lock every
     * time after accessing any shared mutable variable to make sure the read
     * was still valid.
     */
    public static void demonstrate_optimistic_lock() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        StampedLock lock = new StampedLock();

        executor.submit(
                () -> {
                    long stamp = lock.tryOptimisticRead();
                    try {
                        System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
                        try {
                            //sleep for one second
                            TimeUnit.MILLISECONDS.sleep(1);
                        } catch (InterruptedException ex) {
                            echo("InterruptedException occured");
                        }
                        System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
                        try {
                            //sleep for one second
                            TimeUnit.MILLISECONDS.sleep(2);
                        } catch (InterruptedException ex) {
                            echo("InterruptedException occured");
                        }
                        System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
                    } finally {
                        lock.unlock(stamp);
                    }
                }
        );

        executor.submit(
                () -> {
                    long stamp = lock.writeLock();
                    try {
                        System.out.println("Write Lock acquired");
                        try {
                            //sleep for one second
                            TimeUnit.MILLISECONDS.sleep(2);
                        } catch (InterruptedException ex) {
                            echo("InterruptedException occured");
                        }
                    } finally {
                        lock.unlock(stamp);
                        System.out.println("Write done");
                    }
                }
        );

        stop(executor);
    }

    /**
     * Sometimes it's useful to convert a read lock into a write lock without
     * unlocking and locking again. StampedLock provides the method
     * tryConvertToWriteLock() for that purpose as seen in the next sample:
     *
     * The task first obtains a read lock and prints the current value of field
     * count to the console. But if the current value is zero we want to assign
     * a new value of 23. We first have to convert the read lock into a write
     * lock to not break potential concurrent access by other threads. Calling
     * tryConvertToWriteLock() doesn't block but may return a zero stamp
     * indicating that no write lock is currently available. In that case we
     * call writeLock() to block the current thread until a write lock is
     * available.
     */
    public static void demonstrate_optimistic_lock_with_tryConvertToWriteLock() {

        ExecutorService executor = Executors.newFixedThreadPool(2);
        StampedLock lock = new StampedLock();

        executor.submit(
                () -> {
                    long stamp = lock.tryOptimisticRead();
                    try {
                        System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
                        try {
                            //sleep for one second
                            TimeUnit.MILLISECONDS.sleep(1);
                        } catch (InterruptedException ex) {
                            echo("InterruptedException occured");
                        }
                        System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
                        try {
                            //sleep for one second
                            TimeUnit.MILLISECONDS.sleep(2);
                        } catch (InterruptedException ex) {
                            echo("InterruptedException occured");
                        }
                        System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
                    } finally {
                        lock.unlock(stamp);
                    }
                }
        );

        executor.submit(
                () -> {
                    long stamp = lock.readLock();
                    try {
                        if (count == 0) {
                            stamp = lock.tryConvertToWriteLock(stamp);
                            if (stamp == 0L) {
                                System.out.println("Could not convert to write lock");
                                stamp = lock.writeLock();
                            }
                            count = 23;
                        }
                        System.out.println(count);
                    } finally {
                        lock.unlock(stamp);
                    }
                }
        );

        stop(executor);
    }

    //-----------------------------------------------------------------------------------------------
    /**
     * The interface ReadWriteLock specifies another type of lock maintaining a
     * pair of locks for read and write access. * The idea behind read-write
     * locks is that it's usually safe to read mutable variables concurrently as
     * long as nobody is writing to this variable. So the read-lock can be held
     * simultaneously by multiple threads as long as no threads hold the
     * write-lock. * This can improve performance and throughput in case that
     * reads are more frequent than writes.
     *
     *
     */
    public static void read_write_lock() {
        /*
        the below code first acquires a write-lock in order to put a new value 
        to the map after sleeping for one second.     
        Before this task has finished two other tasks are being submitted trying 
        to read the entry from the map and sleep for one second:
         */
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Map<String, String> map = new HashMap<>();
        ReadWriteLock lock = new ReentrantReadWriteLock();

        executor.submit(
                () -> {
                    lock.writeLock().lock();
                    try {
                        try {
                            //sleep for one second
                            TimeUnit.MILLISECONDS.sleep(1);
                        } catch (InterruptedException ex) {
                            echo("InterruptedException occured");
                        }
                        map.put("foo", "bar");
                    } finally {
                        lock.writeLock().unlock();
                    }
                }
        );

        /*
        When you execute this code sample you'll notice that both read tasks 
            have to wait the whole second until the write task has finished. 
            After the write lock has been released both read tasks are executed 
            in parallel and print the result simultaneously to the console. 
            They don't have to wait for each other to finish 
            because read-locks can safely be acquired concurrently 
            as long as no write-lock is held by another thread.
         */
        Runnable readTask = () -> {
            lock.readLock().lock();
            try {
                System.out.println(map.get("foo"));
                try {
                    //sleep for one second
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    echo("InterruptedException occured");
                }
            } finally {
                lock.readLock().unlock();
            }
        };

        executor.submit(readTask);
        executor.submit(readTask);

        stop(executor);
    }

    //-----------------------------------------------------------------------------------------------
    /**
     * The class ReentrantLock is a mutual exclusion lock with the same basic
     * behavior as the implicit monitors accessed via the synchronized keyword
     * but with extended capabilities. * As the name suggests this lock
     * implements reentrant characteristics just as implicit monitors.
     *
     * A lock is acquired via lock() and released via unlock(). It's important
     * to wrap your code into a try/finally block to ensure unlocking in case of
     * exceptions. This method is thread-safe just like the synchronized
     * counterpart. If another thread has already acquired the lock subsequent
     * calls to lock() pause the current thread until the lock has been
     * unlocked. Only one thread can hold the lock at any given time.
     *
     * While the first task holds the lock for one second the second task
     * obtains different information about the current state of the lock:
     *
     * The method tryLock() as an alternative to lock() tries to acquire the
     * lock without pausing the current thread. * The boolean result must be
     * used to check if the lock has actually been acquired before accessing any
     * shared mutable variables.
     */
    public static void reentrant_lock() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        ReentrantLock lock = new ReentrantLock();

        Runnable task1 = () -> {
            lock.lock();
            try {
                String name = Thread.currentThread().getName();
                System.out.println("Foo " + name);
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Bar " + name);
            } catch (InterruptedException e) {
                echo("InterruptedException occured");
            } finally {
                lock.unlock();
            }
        };

        executor.submit(task1);

        executor.submit(
                () -> {
                    lock.lock();
                    try {
                        //sleep for one second
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException ex) {
                        echo("InterruptedException occured");
                    } finally {
                        lock.unlock();
                    }
                }
        );

        executor.submit(
                () -> {
                    System.out.println("Locked: " + lock.isLocked());
                    System.out.println("Held by me: " + lock.isHeldByCurrentThread());
                    boolean locked = lock.tryLock();
                    System.out.println("Lock acquired: " + locked);
                }
        );

        stop(executor);
    }

    //-----------------------------------------------------------------------------------------------
    /**
     * stop is a helper method stop method is used for shutting down an executor
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
}
