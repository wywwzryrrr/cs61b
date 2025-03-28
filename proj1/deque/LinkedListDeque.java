package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    private static class Node<T> {
        private T value;
        private Node<T> next;
        private Node<T> prev;

        Node(T v, Node<T> n, Node<T> p) {
            value = v;
            next = n;
            prev = p;
        }
    }

    private Node<T> sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node<T>(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    @Override
    public void addFirst(T item) {
        Node<T> n = new Node<>(item, sentinel.next, sentinel);
        sentinel.next.prev = n;
        sentinel.next = n;
        size += 1;
    }

    @Override
    public void addLast(T item) {
        Node<T> n = new Node<>(item, sentinel, sentinel.prev);
        sentinel.prev.next = n;
        sentinel.prev = n;
        size += 1;
    }

    @Override
    public boolean isEmpty() {
        return sentinel.next == sentinel;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node<T> n = sentinel.next;
        StringBuilder result = new StringBuilder("[");
        while (n != sentinel) {
            result.append(n.value);
            if (n.next != sentinel) {
                result.append(", ");
            }
            n = n.next;
        }
        result.append("]");
        System.out.println(result);
    }

    @Override
    public T removeFirst() {
        if (size > 0) {
            Node<T> n = sentinel.next;
            sentinel.next = n.next;
            n.next.prev = sentinel;
            n.prev = n.next = null;
            size -= 1;
            return n.value;
        }
        return null;
    }

    @Override
    public T removeLast() {
        if (size > 0) {
            Node<T> n = sentinel.prev;
            sentinel.prev = n.prev;
            n.prev.next = sentinel;
            n.next = n.prev = null;
            size -= 1;
            return n.value;
        }
        return null;
    }

    @Override
    /** Returns the index'th value */
    public T get(int index) {
        if (size <= 0 || index < 0 || index >= size) {
            return null;
        }
        Node<T> n = sentinel.next;
        for (int i = 0; i < index; i++) {
            n = n.next;
        }
        return n.value;
    }
    public T getRecursive(int index) {
        if (size > index || index < 0 || index >= size) {
            return getRecursiveHelper(index, sentinel.next);
        }
        return null;
    }
    private T getRecursiveHelper(int index, Node<T> n) {
        if (n == sentinel) {
            return null;
        } else if (index == 0) {
            return n.value;
        } else {
            return getRecursiveHelper(index - 1, n.next);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }
    private class LinkedListDequeIterator implements Iterator<T> {
        private Node<T> current;

        public LinkedListDequeIterator() {
            current = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return current != sentinel;
        }

        @Override
        public T next() {
            T value = current.value;
            current = current.next;
            return value;
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Deque<?>) {
            Deque<?> other = (Deque<?>) o;
            if (size != other.size()) {
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
