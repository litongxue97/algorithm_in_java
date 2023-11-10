package dataStructure.map;

/**
 * @author : liqinzuo
 * @date : 2023/11/10
 * Description : ThreadLocalMap里面使用了线性探测法解决Hash冲突，故实现之
 */
public class LinearProbingHashMap<K,V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double FACTOR = 0.75;

    private int size;
    private int capacity;
    private K[] keys;
    private V[] values;

    public LinearProbingHashMap() {
        this(DEFAULT_CAPACITY);
    }

    public LinearProbingHashMap(int initialCapacity) {
        this.size = 0;
        this.capacity = initialCapacity;
        this.keys = (K[]) new Object[capacity];
        this.values = (V[]) new Object[capacity];
    }

    public V get(K key) {
        int index = hash(key);
        while (keys[index] != null) {
            if (keys[index].equals(key)) {
                return values[index];
            }
            index = (index + 1) % capacity;
        }
        return null;
    }

    public void put(K key, V value) {
        if (size >= FACTOR * capacity) {
            resize(2 * capacity);
        }

        int index = hash(key);
        while (keys[index] != null) {
            if (keys[index].equals(key)) {
                //覆盖
                values[index] = value;
                return ;
            }
            index = (index + 1) % capacity;
        }

        keys[index] = key;
        values[index] = value;
        size ++;
    }

    private int hash(K key) {
        return key.hashCode() % capacity;
    }

    private void resize(int newCapacity) {
        LinearProbingHashMap<K,V> newHashMap = new LinearProbingHashMap<>();

        for (int i = 0; i < capacity; i ++ ) {
            if (keys[i] != null) {
                newHashMap.put(keys[i], values[i]);
            }
        }

        this.keys = newHashMap.keys;
        this.values = newHashMap.values;
        this.capacity = newCapacity;
    }

    public static void main(String[] args) {
        LinearProbingHashMap<String, Integer> hashMap = new LinearProbingHashMap<>();

        hashMap.put("one", 1);
        hashMap.put("two", 2);
        hashMap.put("three", 3);

        System.out.println("Value for key 'two': " + hashMap.get("two"));
        System.out.println("Value for key 'four': " + hashMap.get("four"));
    }

}
