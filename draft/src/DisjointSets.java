public class DisjointSets {
    public static class QuickUnionDS {
        private int[] parent;

        public QuickUnionDS(int num) {
            parent = new int[num];
            for (int i = 0; i < num; i++) {
                parent[i] = -1; // 初始化为根节点，子树大小为1
            }
        }

        /* 不使用路径压缩 **/
        private int find(int p) {
            int root = p;
            if (parent[root] < 0) {
                return root;
            } else {
                return find(parent[root]);
            }
        }

        private int findWithPathCompression(int p) {
            int root = p;
            if (parent[root] < 0) {
                return root;
            } else {
                return parent[root] = findWithPathCompression(parent[root]);
            }
        }

        public void connect(int p, int q) {
            int rootx = findWithPathCompression(p);
            int rooty = findWithPathCompression(q);
            if (rootx != rooty) { // 只有当根不同时才合并
                // 比较子树大小（负值越小，子树越大）
                if (parent[rooty] < parent[rootx]) { // rooty 的树更大
                    parent[rootx] = rooty;
                } else if (parent[rooty] > parent[rootx]) { // rootx 的树更大
                    parent[rooty] = rootx;
                } else { // 大小相等，选择 rooty 作为根并增加其大小
                    parent[rootx] = rooty;
                    parent[rooty]--; // 增加 rooty 的子树大小
                }
            }
        }

        public boolean isConnected(int p, int q) {
            return findWithPathCompression(p) == findWithPathCompression(q);
        }
    }

    public static void main(String[] args) {
        QuickUnionDS b = new QuickUnionDS(5);
        System.out.println("Root of 4: " + b.findWithPathCompression(4)); // 4
        System.out.println("Root of 1: " + b.findWithPathCompression(1)); // 1
        b.connect(1, 4);
        System.out.println("Parent array at 1: " + b.parent[1]); // 4
        System.out.println("Parent array at 4: " + b.parent[4]); // -2
        System.out.println("Are 1 and 4 connected? " + b.isConnected(1, 4)); // true
    }
}
