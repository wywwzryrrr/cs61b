/*
import org.junit.Test;
import static org.junit.Assert.*;


public class AListTest {
    @Test
    public void testEmptySize() {
        Draft.AList L = new Draft.AList();
        assertEquals(0, L.size());
    }

    @Test
    public void testAddAndSize() {
        Draft.AList L = new Draft.AList();
        L.addLast(99);
        L.addLast(99);
        assertEquals(2, L.size());
    }


    @Test
    public void testAddAndGetLast() {
        Draft.AList L = new Draft.AList();
        L.addLast(99);
        assertEquals(99, L.getLast());
        L.addLast(36);
        assertEquals(36, L.getLast());
    }


    @Test
    public void testGet() {
        Draft.AList L = new Draft.AList();
        L.addLast(99);
        assertEquals(99, L.get(0));
        L.addLast(36);
        assertEquals(99, L.get(0));
        assertEquals(36, L.get(1));
    }


    @Test
    public void testRemove() {
        Draft.AList L = new Draft.AList();
        L.addLast(99);
        assertEquals(99, L.get(0));
        L.addLast(36);
        assertEquals(99, L.get(0));
        L.removeLast();
        assertEquals(99, L.getLast());
        L.addLast(100);
        assertEquals(100, L.getLast());
        assertEquals(2, L.size());
    }

    */
/** Tests insertion of a large number of items.*//*

    @Test
    public void testMegaInsert() {
        Draft.AList L = new Draft.AList();
        int N = 1000000;
        for (int i = 0; i < N; i += 1) {
            L.addLast(i);
        }

        for (int i = 0; i < N; i += 1) {
            L.addLast(L.get(i));
        }
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", AListTest.class);
    }
} */
