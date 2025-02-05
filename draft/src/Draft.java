import java.util.stream.StreamSupport;

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
    public static class Dog {
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
    }
}