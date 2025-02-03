/** cs61b draft */

public class Main {
    /*public static void main(String[] args) {
        System.out.println("Hello, World!");
    }*/


    /*public static void main(String[] args) {
        for (int x = 0; x < 10; x++) {
            System.out.println(x);
        }
    }*/


    public static int larger(int x, int y){
        if (x > y) {
            return x;
        }
        return y;
    }

    public static void main(String[] args) {
        System.out.println(larger(-10, 3));
    }
}