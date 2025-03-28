package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class MaxArrayDequeTest {
    @Test
    public void testMaxArrayDeque1() {
        MaxArrayDeque<Integer> m = new MaxArrayDeque<Integer>(Comparator.naturalOrder());
        m.addFirst(3);
        m.addFirst(2);
        m.addFirst(1);
        int expected = 3;
        int actual = m.max();
        assertEquals(expected, actual);
    }
}
