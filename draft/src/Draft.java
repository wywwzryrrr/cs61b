import jh61b.junit.In;

import java.sql.ClientInfoStatus;

/** cs61b draft */

public class Draft {
    /** Hello World! */
    /*public static void main(String[] args) {
        System.out.println("Hello, World!");
    }*/


    /** for loop */
    /* public static void main(String[] args) {
        for (int x = 0; x < 10; x++) {
            System.out.println(x);
        }
    }*/


    /** print the larger number */
    /*public static int larger(int x, int y){
        if (x > y) {
            return x;
        }
        return y;
    }

    public static void main(String[] args) {
        System.out.println(larger(-10, 3));
    }*/


    /** draw a triangle */
    /*public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            System.out.println("*".repeat(i + 1));
        }
    }*/


    /** draw an unlimited triangle */
    /*public static void drawTriangle(int N) {
        for (int i = 0; i < N; i++) {
            System.out.println("*".repeat(i + 1));
        }
    }

    public static void main(String[] args) {
        drawTriangle(10);
    }*/


    /** find the max num in an array */
    /*public static int max(int[] m) {
        int max_val = 0;
        for (int i = 0; i < m.length; i++) {
            if (m[i] > max_val) {
                max_val = m[i];
            }
        }
        return max_val;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{9, 2, 5, 12, 22, 10, 6};
        int maxValue = max(nums);
        System.out.println(maxValue);
    }*/


    /**
     * sum the nums in an array
     */
    /*public static int sum(int[] m) {
        int sum = 0;
        for (int i = 0; i < m.length; i++) {
            sum += m[i];
        }
        return sum;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 3};
        int result = sum(nums);
        System.out.println(result);
    }*/


    /**
     sum the nums from itself to i+n in an array
     */
    /*public static void windowPosSum(int[] a, int n) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] < 0) {
                continue;
            }
            for (int j = i + 1; j <= i + n && j < a.length; j++) {
                a[i] += a[j];
            }
        }
    }

    public static void main(String[] args) {
        int[] a = {1, 2, -3, 4, 5, 4};
        int n = 3;
        windowPosSum(a, n);

        // Should print 4, 8, -3, 13, 9, 4
        System.out.println(java.util.Arrays.toString(a));
    }*/


    /** creating dog class */
    /*public static class Dog {
        int weight;

        Dog (int w) {
            weight = w;
        }

        public void makeNoise() {
            if (weight < 10) {
                System.out.println("yipyipyip!");
            } else if (weight < 30) {
                System.out.println("bark.");
            } else {
                System.out.println("woof!");
            }
        }
    }

    public static void main(String[] args) {
        Dog d1 = new Dog(5);
        Dog d2 = new Dog(10);
        Dog d3 = new Dog(30);
        d1.makeNoise();
        d2.makeNoise();
        d3.makeNoise();
    }*/


    /*public static class Intlist{
        public int first;
        public Intlist rest;

        public Intlist(int f, Intlist r){
            first = f;
            rest = r;
        }

        *//** return the size of a list *//*
        public int size(){
            if (rest == null){
                return 1;
            }
            return 1 + rest.size();
        }

        *//** return the i'th item in a list *//*
        public int get(int i){
            if (i == 0) {
                return first;
            }
            return rest.get(i - 1);
        }

        public static void main(String[] args) {
            Intlist L = new Intlist(1, null);
            L = new Intlist(3, L);
            L = new Intlist(2, L);
            System.out.println(L.get(0));
            System.out.println(L.get(1));
            System.out.println(L.get(2));
            System.out.println(L.size());
        }
    }*/


    /*public static class SLList {
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

        *//** Adds an item to the front of the list. *//*
        public void addFirst(int x) {
            first = new IntNode(x, first);
            size += 1;
        }

        *//** Retrieves the front item from the list. *//*
        public int getFirst() {
            return first.item;
        }

        *//** Adds an item to the end of the list. *//*
        public void addLast(int x) {
            *//* Your Code Here! *//*
            size += 1;
            IntNode p = new IntNode(x, first);
            while (p.next != null) {
                p = p.next;
            }
            p.next = new IntNode(x, null);
        }

        *//** Returns the number of items in the list using recursion. *//*
        public int size() {
            *//* Your Code Here! *//*
            return size;
        }

        public static void main(String[] args) {
            SLList L = new SLList(20);
            L.addFirst(15);
            L.addLast(10);
            int x = L.getFirst();
            System.out.println(x);
            int size = L.size();
            System.out.println(size);
        }
    }*/


    /*public static class Sort {
        public static void sort(String[] x) {
            sort(x, 0);
        }

        private static void sort(String[] x, int start) {
            if (start == x.length) {
                return;
            }
            int smallestIndex = findSmallest(x, start);
            swap(x, start, smallestIndex);
            sort(x, start + 1);
        }

        public static int findSmallest(String[] x, int start) {
            int samllestIndex = start;
            for (int i = start; i < x.length; i++) {
                int cmp = x[i].compareTo(x[samllestIndex]);
                if (cmp < 0) {
                    samllestIndex = i;
                }
            }
            return samllestIndex;
        }

        public static void swap(String[] x, int a, int b) {
            String tmp = x[a];
            x[a] = x[b];
            x[b] = tmp;
        }
    }*/


    /*public static class AList {
        *//** Creates an empty list. *//*
        private int[] items;
        private int size;

        public AList() {
            items = new int[100];
            size = 0;
        }

        *//** Inserts X into the back of the list. *//*
        public void addLast(int x) {
            items[size] = x;
            size++;
        }

        *//** Returns the item from the back of the list. *//*
        public int getLast() {
            return items[size - 1];
        }

        *//** Gets the ith item in the list (0 is the front). *//*
        public int get(int i) {
            return items[i];
        }

        *//** Returns the number of items in the list. *//*
        public int size() {
            return size;
        }

        *//** Deletes item from back of the list and
         * returns deleted item. *//*
        public int removeLast() {
            int last = getLast();
            size--;
            return last;
        }
    }*/

    /*public static void printParty(int N) {
        for (int i = 1; i <= N; i *= 2) {
            for (int j = 0; j < i; j++) {
                System.out.println("hello");
                int ZUG = 1 + 1;
            }
        }
    }

    public static int f3(int n) {
        if (n <= 1) {
            return 1;
        }
        return f3(n - 1) + f3(n - 1);
    }

    public static int binarySearch(String[] sorts, String x, int lo, int hi) {
        if (lo > hi) {
            return -1;
        }
        int mid = lo + (hi - lo) / 2;
        int cmp = x.compareTo(sorts[mid]);
        if (cmp < 0) {
            return binarySearch(sorts, x, lo, mid - 1);
        }
        else if (cmp > 0) {
            return binarySearch(sorts, x, mid + 1, hi);
        }
        else {
            return mid;
        }
    }

    public static void main(String[] args) {
        printParty(3);
        System.out.println(f3(1));
        System.out.println(f3(2));
        System.out.println(f3(3));
        System.out.println(f3(4));
    }*/



}