package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 0;
    List<PriorityNode<T>> items;
    // TO DO: add fields as necessary
    HashMap<T, Integer> map;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        // TO DO: add code as necessary
        map = new HashMap<>();
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.

    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> a1 = items.get(a);
        PriorityNode<T> b1 = items.get(b);
        items.set(a, b1);
        items.set(b, a1);
        map.put(a1.getItem(), b);
        map.put(b1.getItem(), a);
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException();
        }
        items.add(new PriorityNode<>(item, priority));
        map.put(item, size() - 1);
        percolateUp(size() - 1);
    }

    private void percolateUp(int index) {
        if (index > 2) {
            if (index > START_INDEX) {
                if (items.get(index).getPriority() < items.get((index - 1) / 2).getPriority()) {
                    swap(index, (index - 1) / 2);
                    percolateUp((index - 1) / 2);
                }
            }
        }
        if (index <= 2 && index > 0) {
            if (items.get(index).getPriority() < items.get(0).getPriority()) {
                swap(index, 0);
            }
        }

    }

    @Override
    public boolean contains(T item) {
        return map.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (size() <= START_INDEX) {
            throw new NoSuchElementException();
        }
        return items.get(START_INDEX).getItem();
    }

    @Override
    public T removeMin() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        T top = peekMin();
        if (size() == 1) {
            map.remove(top);
            items.remove(START_INDEX);
            return top;
        }
        swap(START_INDEX, size() - 1);
        items.remove(size() - 1);
        map.remove(top);
        percolateDown(START_INDEX);
        return top;
    }

    private void percolateDown(int index) {
        int findIndex = smallOne(index);
        if (items.get(index).getPriority() > items.get(findIndex).getPriority()) {
            swap(index, findIndex);
            percolateDown(findIndex);
        }
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }
        int index = map.get(item);
        items.get(index).setPriority(priority);
        map.put(item, index);
        percolateUp(index);
        percolateDown(index);
    }

    private int smallOne(int index) {
        int left = 2 * index + 1;
        int right = left + 1;
        if (left <= size() - 1) {
            if (left == size() - 1) {
                return left;
            }
            if (items.get(left).getPriority() < items.get(right).getPriority()) {
                return left;
            } else {
                return right;
            }
        }
        return index;
    }


    @Override
    public int size() {
        return items.size();
    }

}
