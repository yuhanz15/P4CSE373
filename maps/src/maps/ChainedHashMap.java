
package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;


/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ChainedHashMap<K, V> extends AbstractIterableMap<K, V> {
    // TO DO: define reasonable default values for each of the following three fields
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 0.5;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 10;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 10;

    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    AbstractIterableMap<K, V>[] chains;
    private final double loadFactor;
    private final int chainCapacity;
    private int size;


    // You're encouraged to add extra fields (and helper methods) though!

    /**
     * Constructs a new ChainedHashMap with default resizing load factor threshold,
     * default initial chain count, and default initial chain capacity.
     */
    public ChainedHashMap() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    /**
     * Constructs a new ChainedHashMap with the given parameters.
     *
     * @param resizingLoadFactorThreshold the load factor threshold for resizing. When the load factor
     *                                    exceeds this value, the hash table resizes. Must be > 0.
     * @param initialChainCount the initial number of chains for your hash table. Must be > 0.
     * @param chainInitialCapacity the initial capacity of each ArrayMap chain created by the map.
     *                             Must be > 0.
     */
    public ChainedHashMap(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        // TO DO:
        this.loadFactor = resizingLoadFactorThreshold;
        this.chainCapacity = chainInitialCapacity;
        this.size = 0;
        this.chains = createArrayOfChains(initialChainCount);
        for (int i = 0; i < initialChainCount; i++) {
            this.chains[i] = createChain(chainCapacity);
        }
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code AbstractIterableMap<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     * @see ArrayMap createArrayOfEntries method for more background on why we need this method
     */
    @SuppressWarnings("unchecked")
    private AbstractIterableMap<K, V>[] createArrayOfChains(int arraySize) {
        return (AbstractIterableMap<K, V>[]) new AbstractIterableMap[arraySize];
    }

    /**
     * Returns a new chain.
     *
     * This method will be overridden by the grader so that your ChainedHashMap implementation
     * is graded using our solution ArrayMaps.
     *
     * Note: You do not need to modify this method.
     */
    protected AbstractIterableMap<K, V> createChain(int initialSize) {
        return new ArrayMap<>(initialSize);
    }

    private int hashCodeAbs(Object key) {
        int hash = 0;
        if (key != null) {
            hash = Math.abs(key.hashCode() % chains.length);
        }
        return hash;
    }

    @Override
    public V get(Object key) {
        int hashCode = hashCodeAbs(key);
        if (chains[hashCode] == null || !containsKey(key)) {
            return null;
        }
        return chains[hashCode].get(key);
    }

    @Override
    public V put(K key, V value) {
        int hashCode = hashCodeAbs(key);
        V currentV;

        if (chains[hashCode] == null) {
            chains[hashCode] = createChain(chainCapacity);
        }

        int chainSize = chains[hashCode].size();
        currentV = chains[hashCode].put(key, value);
        if (chainSize != chains[hashCode].size()) { //if two elements has same key with different value
            size += 1;                               //, size doesn't change
        }

        double lambda = 1.0 * size / chains.length;
        if (lambda > loadFactor) {
            AbstractIterableMap<K, V>[] newChains = createArrayOfChains(chains.length * 2);
            for (int i = 0; i < chains.length; i++) {
                if (chains[i] != null) {
                    for (Map.Entry<K, V> each : chains[i]) {
                        int newHashCode = Math.abs(each.getKey().hashCode()) % newChains.length;
                        if (newChains[newHashCode] == null) {
                            newChains[newHashCode] = createChain(chainCapacity);
                        }
                        newChains[newHashCode].put(each.getKey(), each.getValue());
                    }
                }
            }
            this.chains = newChains;
        }
        return currentV;
    }

    @Override
    public V remove(Object key) {
        int hashCode = 0;
        if (key != null) {
            hashCode = Math.abs(key.hashCode() % chains.length);
        }
        if (chains[hashCode] == null) {
            return null;
        }
        V previousV = chains[hashCode].remove(key);
        if (previousV != null) {
            size -= 1;
        }
        return previousV;
    }

    @Override
    public void clear() {
        chains = createArrayOfChains(chains.length);
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int hashCode = hashCodeAbs(key);
        if (chains[hashCode] == null) {
            return false;
        }
        return chains[hashCode].containsKey(key);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ChainedHashMapIterator<>(this.chains);
    }

    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedHashMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final AbstractIterableMap<K, V>[] chains;
        // You may add more fields and constructor parameters
        private int index;
        private Iterator<Map.Entry<K, V>> chainMap;

        public ChainedHashMapIterator(AbstractIterableMap<K, V>[] chains) {
            this.chains = chains;
            this.index = 0;
            if (chains[index] != null) {
                chainMap = chains[index].iterator();
            }
        }

        @Override
        public boolean hasNext() {
            while (index < chains.length) {
                if (index == chains.length - 1) {
                    return false;
                }
                if (chainMap != null && chainMap.hasNext()) {
                    return true;
                }
                index += 1;
                if (chains[index] != null) {
                    chainMap = chains[index].iterator();
                } else {
                    chainMap = null;
                }
            }
            return false;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (hasNext()) {
                return chainMap.next();
            }
            throw new NoSuchElementException();
        }
    }
}
