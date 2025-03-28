
package deque;

import org.junit.Test;

import java.util.LinkedHashSet;

import static org.junit.Assert.*;

public class ArrayDequeTest {
    /** {4, 3, 2, 1} */
    public ArrayDeque<Integer> dequeShort() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        deque.addFirst(4);
        return deque;
    }

    /** {9, 8, 7, 6, 5, 4, 3, 2, 1} */
    public ArrayDeque<Integer> dequeLong() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        deque.addFirst(4);
        deque.addFirst(5);
        deque.addFirst(6);
        deque.addFirst(7);
        deque.addFirst(8);
        deque.addFirst(9);
        return deque;
    }
    
    @Test
    public void testSize() {
        ArrayDeque<Integer> deque = dequeShort();
        assertEquals(4, deque.size());
    }

    @Test
    public void testPeekFirstNoResize() {
        ArrayDeque<Integer> deque = dequeShort();
        int value = deque.get(0);
        assertEquals(4, value);
    }

    @Test
    public void testPeekLastNoResize() {
        ArrayDeque<Integer> deque = dequeShort();
        int valueLast = deque.get(deque.size() - 1);
        assertEquals(1, valueLast);
    }

    @Test
    public void testGetArbitraryElement() {
        ArrayDeque<Integer> deque = dequeShort();
        int value1 = deque.get(0);
        int value2 = deque.get(1);
        assertEquals(4, value1);
        assertEquals(3, value2);
    }

    @Test
    public void testPrintDeque() {
        ArrayDeque<Integer> deque = dequeShort();
        String expected = "[4, 3, 2, 1]";
        deque.printDeque();
    }

    @Test
    public void testPrintEmptyDeque() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        String expected = "[]";
        deque.printDeque();
    }

    @Test
    public void testRemoveFirstNoResize() {
        ArrayDeque<Integer> deque = dequeShort();
        int expected1 = deque.removeFirst();
        assertEquals(expected1, 4);
        int expected2 = deque.removeFirst();
        assertEquals(expected2, 3);
        int expected3 = deque.removeFirst();
        assertEquals(expected3, 2);
    }

    @Test
    public void testRemoveLastNoResize() {
        ArrayDeque<Integer> deque = dequeShort();
        int expected1 = deque.removeLast();
        assertEquals(expected1, 1);
        int expected2 = deque.removeLast();
        assertEquals(expected2, 2);
        int expected3 = deque.removeLast();
        assertEquals(expected3, 3);
    }

    @Test
    public void testAddFirstResize() {
        ArrayDeque<Integer> deque = dequeLong();
        int value1 = deque.get(0);
        assertEquals(9, value1);
        int value2 = deque.get(deque.size() - 1);
        assertEquals(1, value2);
    }

    @Test
    public void testAddLastResize() {
        ArrayDeque<Integer> deque = dequeLong();
        int value1 = deque.get(0);
        assertEquals(9, value1);
        int value2 = deque.get(deque.size() - 1);
        assertEquals(1, value2);
    }

    @Test
    public void testEqualStringDeque() {
        ArrayDeque<String> lld1 = new ArrayDeque<>();
        ArrayDeque<String> lld2 = new ArrayDeque<>();

        lld1.addLast("front");
        lld1.addLast("middle");
        lld1.addLast("back");

        lld2.addLast("front");
        lld2.addLast("middle");
        lld2.addLast("back");

        assertTrue(lld1.equals(lld2));
    }

    @Test
    public void testEqualEmptyDeque() {
        ArrayDeque<Integer> deque1 = new ArrayDeque<>();
        ArrayDeque<Integer> deque2 = new ArrayDeque<>();
        assertTrue(deque1.equals(deque2));
    }

    @Test
    public void testEqualResizeDeque1() {
        ArrayDeque<Integer> deque1 = dequeLong();
        ArrayDeque<Integer> deque2 = dequeLong();
        assertTrue(deque1.equals(deque2));
        deque1.removeFirst();
        deque2.removeFirst();
        assertTrue(deque1.equals(deque2));
        deque1.removeLast();
        deque2.removeLast();
        assertTrue(deque1.equals(deque2));
        deque1.removeFirst();
        deque2.removeFirst();
        assertTrue(deque1.equals(deque2));
        deque1.removeLast();
        deque2.removeLast();
        assertTrue(deque1.equals(deque2));
        deque1.removeFirst();
        deque2.removeFirst();
        assertTrue(deque1.equals(deque2));
        deque1.removeLast();
        deque2.removeLast();
        assertTrue(deque1.equals(deque2));
        deque1.removeFirst();
        deque2.removeFirst();
        assertTrue(deque1.equals(deque2));
        deque1.removeLast();
        deque2.removeLast();
        assertTrue(deque1.equals(deque2));
        deque1.removeLast();
        deque2.removeLast();
        assertTrue(deque1.equals(deque2));
    }

    @Test
    public void testEqualResizeDeque2() {
        ArrayDeque<Integer> deque1 = dequeShort();
        ArrayDeque<Integer> deque2 = dequeShort();
        assertTrue(deque1.equals(deque2));
        deque1.addFirst(1);
        deque2.addFirst(1);
        assertTrue(deque1.equals(deque2));
        deque1.addFirst(2);
        deque2.addFirst(2);
        assertTrue(deque1.equals(deque2));
        deque1.addFirst(3);
        deque2.addFirst(3);
        assertTrue(deque1.equals(deque2));
        deque1.addFirst(4);
        deque2.addFirst(4);
        assertTrue(deque1.equals(deque2));
        deque1.addFirst(5);
        deque2.addFirst(5);
        assertTrue(deque1.equals(deque2));
        deque1.addFirst(6);
        deque2.addFirst(6);
        assertTrue(deque1.equals(deque2));
    }

    @Test
    public void testEqualDifferentDeque() {
        ArrayDeque<Integer> deque1 = new ArrayDeque<>();
        LinkedListDeque<Integer> deque2 = new LinkedListDeque<>();
        deque1.addFirst(1);
        deque2.addFirst(1);
        assertTrue(deque1.equals(deque2));
    }
}