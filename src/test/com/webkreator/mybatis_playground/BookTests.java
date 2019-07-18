package com.webkreator.mybatis_playground;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BookTests extends AbstractDatabaseTest {

    public static final String ISBN = "9781907117046";

    public static final Book BOOK_SINGLETON;

    static {
        BOOK_SINGLETON = new Book();
        BOOK_SINGLETON.setIsbn(ISBN);
        BOOK_SINGLETON.setTitle("Bulletproof SSL and TLS");
        BOOK_SINGLETON.setAuthor(new Author("Ivan", "Ristic"));
        BOOK_SINGLETON.setEditors(Arrays.asList("Zaphod", "Beeblebrox"));
        BOOK_SINGLETON.setReviewers(Arrays.asList("Arthur", "Dent"));
        BOOK_SINGLETON.setRating(4);
    }

    @Test
    public void testMapper() {
        // Doesn't exist.

        Assert.assertFalse(books.existsById(ISBN));
        Assert.assertFalse(books.exists(BOOK_SINGLETON));

        // Insert (single).

        books.insert(BOOK_SINGLETON);

        List<Book> books = this.books.selectAll();
        Assert.assertEquals(1, books.size());
        Assert.assertEquals(BOOK_SINGLETON, books.get(0));

        Book book = this.books.selectById(ISBN);
        Assert.assertNotNull(book);
        Assert.assertEquals(BOOK_SINGLETON, book);

        this.books.delete(BOOK_SINGLETON);

        // Insert (list).

        this.books.insertAll(Arrays.asList(BOOK_SINGLETON));

        // Exists.

        Assert.assertTrue(this.books.existsById(ISBN));
        Assert.assertTrue(this.books.exists(BOOK_SINGLETON));

        // Delete.

        this.books.delete(BOOK_SINGLETON);
        Assert.assertFalse(this.books.existsById(ISBN));
        Assert.assertFalse(this.books.exists(BOOK_SINGLETON));

        this.books.insert(BOOK_SINGLETON);
        this.books.deleteById(ISBN);
        Assert.assertFalse(this.books.existsById(ISBN));
        Assert.assertFalse(this.books.exists(BOOK_SINGLETON));

        this.books.insert(BOOK_SINGLETON);
        this.books.deleteAll();
        Assert.assertFalse(this.books.existsById(ISBN));
        Assert.assertFalse(this.books.exists(BOOK_SINGLETON));

        this.books.insert(BOOK_SINGLETON);
        this.books.deleteAll(Arrays.asList(BOOK_SINGLETON));
        Assert.assertFalse(this.books.existsById(ISBN));
        Assert.assertFalse(this.books.exists(BOOK_SINGLETON));

        // Update.

        this.books.insert(BOOK_SINGLETON);
        BOOK_SINGLETON.setRating(5); // Was 4.
        this.books.update(BOOK_SINGLETON);
        book = this.books.selectById(ISBN);
        Assert.assertNotNull(book);
        Assert.assertEquals(BOOK_SINGLETON, book);
    }
}
