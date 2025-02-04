import org.junit.Test;

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
    public static int max(int[] m) {
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
    }
}