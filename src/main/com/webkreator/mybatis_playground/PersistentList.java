package com.webkreator.mybatis_playground;

import lombok.SneakyThrows;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import java.io.*;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class PersistentList<T> implements AutoCloseable, ResultHandler, Iterable<T> {

    // The temporary file used as backing storage.
    private final File temporaryFile;

    // The object output stream that serializes the objects into the file.
    private final ObjectOutputStream oos;

    // These are the input streams used by the iterators; we keep
    // track of them so that we can close them when we're done.
    private final Set<InputStream> streams = new HashSet<>();

    /**
     * Creates a new persistent list backed by a temporary file created
     * in the default location. A list must be closed to trigger resource
     * clean-up.
     *
     * @throws IOException
     */
    public PersistentList() throws IOException {
        this.temporaryFile = Files.createTempFile("persistent-list", ".data").toFile();
        this.oos = new ObjectOutputStream(new BufferedOutputStream(
                new FileOutputStream(temporaryFile)));
    }

    /**
     * Writes to the list from a streaming MyBatis result set.
     *
     * @param ctx
     */
    @Override
    @SneakyThrows
    public void handleResult(ResultContext ctx) {
        write((T) ctx.getResultObject());
    }

    /**
     * Writes one element to the list.
     *
     * @param data
     * @throws IOException
     */
    public void write(T data) throws IOException {
        oos.writeObject(data);
    }

    /**
     * Cleans up the resources. Best used with try-with-resources.
     */
    @Override
    public void close() {

        try {
            oos.close();
        } catch (Exception e) {
            // Ignore.
        }

        for (InputStream is : streams) {
            try {
                is.close();
            } catch (IOException e) {
                // Ignore.
            }
        }

        try {
            temporaryFile.delete();
        } catch (Exception e) {
            // Ignore.
        }
    }

    @Override
    @SneakyThrows
    public synchronized Iterator<T> iterator() {
        // Close the output stream when the first iterator is created.
        if (streams.size() == 0) {
            oos.close();
        }

        return new MyIterator<T>(this);
    }

    private void rememberToClose(InputStream ois) {
        streams.add(ois);
    }

    public class MyIterator<T> implements Iterator<T> {

        private final ObjectInputStream ois;

        private T next;

        MyIterator(PersistentList prh) throws IOException {
            ois = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream(prh.temporaryFile)));
            prh.rememberToClose(ois);
        }

        @SneakyThrows
        private T readNext() throws IOException {
            return (T) ois.readObject();
        }

        @Override
        public boolean hasNext() {
            // To determine if there's another element,
            // we need to read from the stream.

            // If we've already read and have the next
            // element, then we clearly have next.
            if (next != null) {
                return true;
            }

            // Otherwise, read from the stream.
            try {
                next = readNext();
                return true;
            } catch (IOException e) {
                // No next.
                return false;
            }
        }

        @Override
        public T next() {
            // If we already have the next element, return it now.
            if (next != null) {
                T value = next;
                next = null;
                return value;
            }

            // Otherwise, attempt to fetch the next element.
            try {
                return readNext();
            } catch (IOException e) {
                throw new NoSuchElementException();
            }
        }
    }
}
