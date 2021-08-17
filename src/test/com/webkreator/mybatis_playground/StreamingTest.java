package com.webkreator.mybatis_playground;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

public class StreamingTest extends AbstractDatabaseTest {

    @Test
    public void testPersistentList() throws Exception {
        int counter = 0;

        // Use try-with-resources so that the temporary files
        // are automatically removed afterwards.

        try (PersistentList<Integer> results = new PersistentList<>()) {
            // Stream data from MyBatis into a file on disk.
            streaming.selectAll(results);

            // Now read back the results back from the file, one at a time.
            for (Integer value : results) {
                if (++counter != value) {
                    throw new IllegalArgumentException();
                }
            }
        }

        Assert.assertEquals(10_000, counter);
    }

    @Test
    public void testPersistentListIterator() throws Exception {

        try (PersistentList<Integer> results = new PersistentList<>()) {
            streaming.selectAll(results);

            Iterator<Integer> it = results.iterator();

            Assert.assertTrue(it.hasNext());
            Assert.assertTrue(it.hasNext());
            Assert.assertEquals((Integer) 1, it.next());
            Assert.assertEquals((Integer) 2, it.next());
            Assert.assertEquals((Integer) 3, it.next());
            Assert.assertTrue(it.hasNext());
            Assert.assertEquals((Integer) 4, it.next());
            Assert.assertTrue(it.hasNext());
            Assert.assertTrue(it.hasNext());
            Assert.assertEquals((Integer) 5, it.next());

            for (int i = 6; i <= 9999; i++) {
                Assert.assertEquals((Integer) i, it.next());
                Assert.assertTrue(it.hasNext());
            }

            Assert.assertTrue(it.hasNext());

            Assert.assertEquals((Integer) 10000, it.next());

            Assert.assertFalse(it.hasNext());
        }
    }
}
