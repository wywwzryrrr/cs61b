package bstmap;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private class BSTNode {
        private K item;
        private V value;
        private int size;
        private BSTNode left, right;

        public BSTNode(K item, V value, int size) {
            this.item = item;
            this.value = value;
            this.size = size;
        }
    }

    private BSTNode root;

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        return getNode(root, key) != null;
    }

    @Override
    public V get(K key) {
        if (key == null) {
            return null;
        }
        return getNode(root, key).value;
    }

    private BSTNode getNode(BSTNode node, K key) {
        if (node == null) {
            return null;
        }
        if (key.compareTo(node.item) < 0) {
            return getNode(node.left, key);
        }else if (key.compareTo(node.item) > 0) {
            return getNode(node.right, key);
        } else {
            return node;
        }
    }

    @Override
    public int size() {
        if (root == null) {
            return 0;
        }
        return root.size;
    }

    @Override
    public void put(K key, V value) {
        
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEach(Consumer<? super K> action) {
        Map61B.super.forEach(action);
    }

    @Override
    public Spliterator<K> spliterator() {
        return Map61B.super.spliterator();
    }

    public BSTNode printInOrder() {
        return null;
    }
}
