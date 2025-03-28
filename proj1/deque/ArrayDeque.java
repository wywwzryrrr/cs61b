package deque;

import java.util.Iterator;

import static java.lang.Math.floorMod;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private T[] items;
    private int size;
    private int capacity;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        size = 0;
        capacity = 8;
        items = (T[]) new Object[capacity];
        nextFirst = 0;
        nextLast = 1;
    }

    private int indexIncrement(int index) {
        return floorMod(index + 1, items.length);
    }

    private int indexDecrement(int index) {
        return floorMod(index - 1, items.length);
    }

    @Override
    public void addFirst(T item) {
        if (isFull()) {
            resize(capacity * 2);
        }
        nextFirst = indexDecrement(nextFirst);
        items[nextFirst] = item;
        if (size == 0) {
            nextLast = indexIncrement(nextFirst);
        }
        size++;
    }

    @Override
    public void addLast(T item) {
        if (isFull()) {
            resize(capacity * 2);
        }
        items[nextLast] = item;
        nextLast = indexIncrement(nextLast);
        if (size == 0) {
            nextFirst = indexDecrement(nextLast);
        }
        size++;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private boolean isFull() {
        return size == capacity;
    }

    @Override
    public int size() {
        return size;
    }

    private void resize(int newCapacity) {
        T[] newItems = (T[]) new Object[newCapacity];
        capacity = newCapacity;
        for (int i = 0; i < size; i++) {
            T value = get(i);
            newItems[i] = value;
        }
        items = newItems;
        nextFirst = 0;
        nextLast = size;
    }

    @Override
    public void printDeque() {
        if (isEmpty()) {
            System.out.println("[]");
            return;
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size - 1; i++) {
            sb.append(get(i)+ ", ");
        }
        sb.append(get(size - 1) + "]");
        System.out.println(sb);
    }

    @Override
    public T removeFirst() {
        if (size <= capacity / 4 && capacity >= 16) {
            resize(capacity / 2);
        }
        if (size > 0) {
            T value = get(0);
            nextFirst = indexIncrement(nextFirst);
            size--;
            return value;
        }
        return null;
    }

    @Override
    public T removeLast() {
        if (size <= capacity / 4 && capacity >= 16) {
            resize(capacity / 2);
        }
        if (size > 0) {
            T value = get(size - 1);
            nextLast = indexDecrement(nextLast);
            size--;
            return value;
        }
        return null;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size || size == 0) {
            return null;
        }
        int currentIndex = nextFirst;
        currentIndex = floorMod(currentIndex + index, capacity);
        return items[currentIndex];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }
    private class ArrayDequeIterator implements Iterator<T> {
        private int currentIndex;

        public ArrayDequeIterator() {
            currentIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public T next() {
            T value = get(currentIndex);
            currentIndex++;
            return value;
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Deque<?>) {
            Deque<?> other = (Deque<?>) o;
            if (this.size() != other.size()) {
                return false;
            }
            for (int i = 0; i < this.size(); i++) {
                T selfvalue = (T) this.get(i);
                T othervalue = (T) other.get(i);
                if (!selfvalue.equals(othervalue)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
