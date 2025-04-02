package flik;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;


public class TestHorribleSteve {
    @Test
    public void test1() {
        assertFalse(Integer.valueOf(128) == Integer.valueOf(128));
    }

    @Test
    public void test2() {
        assertTrue(Integer.valueOf(128).equals(Integer.valueOf(128)));
    }
}