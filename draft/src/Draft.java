/** cs61b draft */

public class Draft {
    /*public static void main(String[] args) {
        System.out.println("Hello, World!");
    }*/


    /*public static void main(String[] args) {
        for (int x = 0; x < 10; x++) {
            System.out.println(x);
        }
    }*/


    /*public static int larger(int x, int y){
        if (x > y) {
            return x;
        }
        return y;
    }

    public static void main(String[] args) {
        System.out.println(larger(-10, 3));
    }*/


    /*public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            System.out.println("*".repeat(i + 1));
        }
    }*/


    public static void drawTriangle(int N) {
        for (int i = 0; i < N; i++) {
            System.out.println("*".repeat(i + 1));
        }
    }

    public static void main(String[] args) {
        drawTriangle(10);
    }
}