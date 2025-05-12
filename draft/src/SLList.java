public class SLList {
    public static class IntNode {
            public int item;
            public IntNode next;
            public IntNode(int i, IntNode n) {
                item = i;
                next = n;
            }
        }

        private IntNode first;
        private int size;

        public SLList(int x) {
            first = new IntNode(x, null);
            size = 1;
        }

        //** Adds an item to the front of the list. *//*
        public void addFirst(int x) {
            first = new IntNode(x, first);
            size += 1;
        }

        //** Retrieves the front item from the list. *//*
        public int getFirst() {
            return first.item;
        }

        //** Adds an item to the end of the list. *//*
        public void addLast(int x) {
            //* Your Code Here! *//*
            size += 1;
            IntNode p = new IntNode(x, first);
            while (p.next != null) {
                p = p.next;
            }
            p.next = new IntNode(x, null);
        }

        //** Returns the number of items in the list using recursion. *//*
        public int size() {
            //* Your Code Here! *//*
            return size;
        }
}
