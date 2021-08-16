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

public class PersistentResultSet<T> implements AutoCloseable, ResultHandler, Iterable<T> {

    private final File temporaryFile;

    private final ObjectOutputStream oos;

    private final Set<InputStream> streams = new HashSet<>();

    public PersistentResultSet() throws IOException {
        this.temporaryFile = Files.createTempFile("mybatis-resultset", ".data").toFile();
        this.oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(temporaryFile)));
    }

    @Override
    @SneakyThrows
    public void handleResult(ResultContext ctx) {
        T data = (T) ctx.getResultObject();
        oos.writeObject(data);
    }

    @Override
    public void close() {

        try {
            temporaryFile.delete();
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
    }

    @Override
    @SneakyThrows
    public Iterator<T> iterator() {
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

        MyIterator(PersistentResultSet prh) throws IOException {
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
