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
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> normal = new AListNoResizing<>();
        BuggyAList<Integer> buggy = new BuggyAList<>();
        normal.addLast(4);
        normal.addLast(5);
        normal.addLast(6);
        buggy.addLast(4);
        buggy.addLast(5);
        buggy.addLast(6);
        assertEquals(normal.size(), buggy.size());
        assertEquals(normal.removeLast(), buggy.removeLast());
        assertEquals(normal.removeLast(), buggy.removeLast());
        assertEquals(normal.removeLast(), buggy.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int buggySize = B.size();
                assertEquals(size, buggySize);
            } else if (operationNumber == 2) {
                if (L.size() == 0) {
                    continue;
                }
                int get = L.getLast();
                int buggyGet = B.getLast();
                assertEquals(get, buggyGet);
            } else if (operationNumber == 3) {
                if (L.size() == 0) {
                    continue;
                }
                int remove = L.removeLast();
                int buggyRemove = B.removeLast();
                assertEquals(remove, buggyRemove);
            }
        }
    }
}
