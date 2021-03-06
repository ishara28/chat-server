package utils;

import java.util.Map;
import java.util.stream.Stream;

public class Utils {
    private static Utils instance;

    private Utils(){}

    public static Utils getInstance(){
        if (instance == null){
            synchronized(Utils.class){
                if (instance == null){
                    instance = new Utils();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    public boolean isValidIdentity(String identity){
        //todo:
        // check if identity is alphanumeric string starting with an upper or lower case character.
        // It must be at least 3 characters and no more than 16 characters long.
//        return /^[A-Za-z]{1}[A-Za-z0-9]{2,15}$/.test(identity);
        return true;
    }

    public <K, V> Stream<K> keys(Map<K, V> map, V value) {
        return map
                .entrySet()
                .stream()
                .filter(entry -> value.equals(entry.getValue()))
                .map(Map.Entry::getKey);
    }

}
