import java.util.*;

public class BST<K extends Comparable<K>, V> {
    private class BSTNode {
        private K key;
        private V value;
        private int size;
        private BSTNode left, right;

        public BSTNode(K key, V value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
        }
    }

    private BSTNode root;

    /** 由于java内存回收机制，使根节点不可达即可使整棵树不可达，从而触发回收机制，清空整棵树 */
    public void clear() {
        root = null;
    }

    public boolean containsKey(K key) {
        return getNode(root, key) != null;
    }

    public V get(K key) {
        BSTNode node = getNode(root, key);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    private BSTNode getNode(BSTNode node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return getNode(node.left, key);
        } else if (cmp > 0) {
            return getNode(node.right, key);
        } else {
            return node;
        }
    }

    /** 所有节点通过根节点访问，所以直接只需访问root.size即可获取树的总大小 */
    public int size() {
        if (root == null) {
            return 0;
        }
        return root.size;
    }

    public void put(K key, V value) {
        root = put(root, key, value);
    }

    private BSTNode put(BSTNode node, K key, V value) {
        if (node == null) {
            return new BSTNode(key, value, 1);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
            node.size += 1;
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
            node.size += 1;
        } else {
            node.value = value;
        }
        return node;
    }

    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(BSTNode node) {
        if (node == null) {
            return;
        }
        printInOrder(node.left);
        System.out.println(node.key + ": " + node.value);
        printInOrder(node.right);
    }


    /**
         D
         |
         |__F_    // right
         |    |__G
         |    |__E
         |__B_     // left
              |__C
              |__A
     */

    // D B A C F E G
    public void preOrder(BSTNode node) {
        if (root == null) {
            return;
        }
        System.out.println(node.key + ": " + node.value);
        preOrder(node.left);
        preOrder(node.right);
    }

    // A B C D E F G
    public void inOrder(BSTNode node) {
        if (root == null) {
            return;
        }
        inOrder(node.left);
        System.out.println(node.key + ": " + node.value);
        inOrder(node.right);
    }

    // A C B E G F D
    public void postOrder(BSTNode node) {
        if (root == null) {
            return;
        }
        postOrder(node.left);
        postOrder(node.right);
        System.out.println(node.key + ": " + node.value);
    }

    // D B F A C E G
    public void levelOrder(BSTNode node) {
        if (root == null) {
            return;
        }
        Queue<BSTNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            BSTNode current = queue.poll();
            System.out.println(current.key + ": " + current.value);
        }
    }
}
