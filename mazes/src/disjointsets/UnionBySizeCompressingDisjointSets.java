package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */
    HashMap<T, Integer> index; // the integer in ids represents the index of each element
    private int size;


    public UnionBySizeCompressingDisjointSets() {
        this.index = new HashMap<>();
        this.pointers = new ArrayList<>();
        this.size = 0;
    }


    public void makeSet(T item) {
        index.put(item, size);
        pointers.add(-1);
        size += 1;
    }


    public int findSet(T item) {
        if (!index.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int i = index.get(item);
        List<Integer> newOne = new LinkedList<>();
        while (pointers.get(i) >= 0) {
            newOne.add(i);
            i = pointers.get(i);
        }

        for (int j : newOne) {
            pointers.set(j, i);
        }
        return i;
    }


    public boolean union(T item1, T item2) {
        if (!(index.containsKey(item1) && index.containsKey(item2))) {
            throw new IllegalArgumentException();
        }
        int index1 = findSet(item1);
        int index2 = findSet(item2);
        if (index1 == index2) {
            return false;
        }
        int weight1 = Math.abs(pointers.get(index1));
        int weight2 = Math.abs(pointers.get(index2));
        if (weight1 >= weight2) {
            pointers.set(index2, index1);
            pointers.set(index1, -1 * (weight1 + weight2));
        } else {
            pointers.set(index1, index2);
            pointers.set(index2, -1 * (weight1 + weight2));
        }
        return true;
    }
}
