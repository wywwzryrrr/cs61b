public class BST<Key> {
    private Key key;
    private BST left;
    private BST right;

    public BST() {
        this.key = null;
        this.left = null;
        this.right = null;
    }

    public BST(Key key, BST left, BST right) {
        this.key = key;
    }
}
