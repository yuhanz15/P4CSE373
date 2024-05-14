package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ArrayMap<K, V> extends AbstractIterableMap<K, V> {
    // TO DO: define a reasonable default value for the following field
    private static final int DEFAULT_INITIAL_CAPACITY = 100;
    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    SimpleEntry<K, V>[] entries;
    private int size;

    // You may add extra fields or helper methods though!

    /**
     * Constructs a new ArrayMap with default initial capacity.
     */
    public ArrayMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Constructs a new ArrayMap with the given initial capacity (i.e., the initial
     * size of the internal array).
     *
     * @param initialCapacity the initial capacity of the ArrayMap. Must be > 0.
     */
    public ArrayMap(int initialCapacity) {
        this.entries = this.createArrayOfEntries(initialCapacity);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code Entry<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     */
    @SuppressWarnings("unchecked")
    private SimpleEntry<K, V>[] createArrayOfEntries(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
        arrays and generics interact.
        */
        return (SimpleEntry<K, V>[]) (new SimpleEntry[arraySize]);
    }

    @Override
    public V get(Object key) {
        if (size == 0) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            if (Objects.equals(entries[i].getKey(), key)) {
                return entries[i].getValue();
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        SimpleEntry<K, V> newE = new SimpleEntry<>(key, value);
        for (int i = 0; i < size; i++) {
            if (Objects.equals(entries[i].getKey(), key)) {
                V previousV = entries[i].getValue();
                entries[i].setValue(value);
                return previousV;
            }
        }
        if (entries.length == size) {
            SimpleEntry<K, V>[] createNewOne = createArrayOfEntries(2 * size);
            for (int i = 0; i < size; i++) {
                createNewOne[i] = entries[i];
            }
            this.entries = createNewOne;
        }
        entries[size] = newE;
        size += 1;
        return null;
    }

    private int index(Object key) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(entries[i].getKey(), key)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public V remove(Object key) {
        V previousV = null;
        if (containsKey(key)) {
            if (Objects.equals(entries[size - 1].getKey(), key)) {
                previousV = entries[size - 1].getValue();
            } else {
                previousV = entries[index(key)].getValue();
                entries[index(key)] = entries[size - 1];
            }
            entries[size - 1] = null;
            size -= 1;
        }
        return previousV;
    }

    @Override
    public void clear() {
        entries = createArrayOfEntries(DEFAULT_INITIAL_CAPACITY);
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(entries[i].getKey(), key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: You may or may not need to change this method, depending on whether you
        // add any parameters to the ArrayMapIterator constructor.
        return new ArrayMapIterator<>(this.entries, this.size);
    }

    private static class ArrayMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final SimpleEntry<K, V>[] entries;
        // You may add more fields and constructor parameters
        private int index;
        private int size;

        public ArrayMapIterator(SimpleEntry<K, V>[] entries, int size) {
            this.entries = entries;
            this.index = 0;
            this.size = size;
        }

        @Override
        public boolean hasNext() {
            if (entries.length == 0 || index > size - 1) {
                return false;
            }
            return true;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (this.hasNext()) {
                index += 1;
                return new SimpleEntry<>(entries[index - 1].getKey(), entries[index - 1].getValue());
            } else {
                throw new NoSuchElementException();
            }

        }
    }
}
