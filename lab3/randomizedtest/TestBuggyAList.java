package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import timingtest.AList;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove(){
        BuggyAList list1 = new BuggyAList();
        AListNoResizing list2 = new AListNoResizing();
        int x = 4; int y = 5; int z = 6;
        list1.addLast(x);list1.addLast(y);list1.addLast(z);
        list2.addLast(x);list2.addLast(y);list2.addLast(z);
        assertEquals(list1.size(), list2.size());
        assertEquals(list1.removeLast(), list2.removeLast());
        assertEquals(list1.removeLast(), list2.removeLast());
        assertEquals(list1.removeLast(), list2.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        BuggyAList<Integer> broken = new BuggyAList<>();

        int N = 50000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0,4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0,100);
                correct.addLast(randVal);
                broken.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int sizeCorrent = correct.size();
                int sizeBroken = broken.size();
                System.out.println("size(" + sizeCorrent + ")");
                System.out.println("size(" + sizeBroken + ")");
                assertEquals(sizeCorrent, sizeBroken);
            } else if (operationNumber == 2) {
                // getLast
                if (correct.size() != 0 && broken.size() != 0) {
                    int correctLast = correct.getLast();
                    int brokenLast = broken.getLast();
                    System.out.println("getLast(" + correctLast + ")");
                    System.out.println("getLast(" + brokenLast + ")");
                    assertEquals(correctLast, brokenLast);
                }
            } else {
                //removeLast
                if (correct.size() != 0 && broken.size() != 0) {
                    int removedCorrect = correct.removeLast();
                    int removedBroken = broken.removeLast();
                    System.out.println("removeLast(" + removedCorrect + ")");
                    System.out.println("removeLast(" + removedBroken + ")");
                    assertEquals(removedCorrect, removedBroken);
                }
            }
        }
    }


}
