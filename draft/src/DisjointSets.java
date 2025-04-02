public class DisjointSets {
    public static class QuickUnionDS {
        private int[] parent;

        public QuickUnionDS(int num) {
            parent = new int[num];
            for (int i = 0; i < num; i++) {
                parent[i] = -1;
            }
        }

        private int root(int p) {
            while (parent[p] >= 0) {
                p = parent[p];
            }
            return p;
        }

        public void connect(int p, int q) {
            int i = root(p);
            int j = root(q);
            parent[i] = j;
        }

        public boolean isConnected(int p, int q) {
            return root(p) == root(q);
        }
    }
}
