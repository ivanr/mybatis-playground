package com.webkreator.mybatis_playground;

import org.junit.Assert;
import org.junit.Test;

public class StreamingTest extends AbstractDatabaseTest {

    @Test
    public void testMapper() throws Exception {
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
}
