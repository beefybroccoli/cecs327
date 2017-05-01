package datastructure.concurrent_hash_map;

import static part_2_assignment_version_final.object.VALUE.echo;
import java.util.concurrent.ConcurrentHashMap;

public class Concurrent_Hash_Map_Example {

    public static void main(String[] args) {
        ConcurrentHashMap<String, String> premiumPhone = new ConcurrentHashMap<String, String>();
        premiumPhone.put("Apple", "iPhone6");
        premiumPhone.put("HTC", "HTC one");
        premiumPhone.put("Samsung", "S6");

        String[] key = {"Apple", "HTC", "Samsung", "Other"};

        for (int i = 0; i < key.length; i++) {
            echo("Key " + key[i] + ", value " + premiumPhone.remove(key[i]) + ", after remove size = "
                    + premiumPhone.size());
        }

    }

}
