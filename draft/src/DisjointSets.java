public class DisjointSets {
    public static class QuickUnionDS {
        private int[] parent;

        public QuickUnionDS(int num) {
            parent = new int[num];
            for (int i = 0; i < num; i++) {
                parent[i] = -1;
            }
        }

        private int find(int p) {
            while (parent[p] >= 0) {
                p = parent[p];
            }
            return p;
        }

        private int findWithPathCompression(int p) {
            int root = p;
            while (root != parent[root]) {
                root = parent[root];
            }
            while (p != root) {
                int newp = parent[p];
                parent[p] = root;
                p = newp;
            }
            return root;
        }

        public void connect(int p, int q) {
            int i = find(p);
            int j = find(q);
            parent[i] = j;
        }

        public boolean isConnected(int p, int q) {
            return find(p) == find(q);
        }
    }

    public static void main(String[] args) {
        QuickUnionDS a = new QuickUnionDS(5);
        System.out.println(a.find(0));
        System.out.println(a.find(1));
        a.connect(1, 0);
        System.out.println(a.parent[0]);
        System.out.println(a.parent[1]);
    }
}
