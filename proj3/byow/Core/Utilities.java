package byow.Core;

import java.util.Random;


public class Utilities {
    /**
     * extract the seed in input
     * @param input
     * @return
     */
    public static long extractSeed(String input) {
        try {
            return Long.parseLong(input, 1, input.length() - 1, 10);
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid input: " + input);
            return 0;
        }
    }

    /**
     * fix the input string
     *
     * @param input
     * @return
     */
    public static String fixInputString(String input) {
        String s = input.toUpperCase();
        if (!s.startsWith("N") || !s.endsWith("S")) {
            System.out.println("Invalid input: " + input);
            return null;
        }
        return input;
    }


}
