package com.webkreator.mybatis_playground;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BookTests extends AbstractDatabaseTest {

    private static final String BOOK_ISBN = "9781907117046";

    private static final Book staticBook;

    static {
        staticBook = new Book();
        staticBook.setIsbn(BOOK_ISBN);
        staticBook.setTitle("Bulletproof SSL and TLS");
        staticBook.setAuthor(new Author("Ivan", "Ristic"));
        staticBook.setEditors(Arrays.asList("Zaphod", "Beeblebrox"));
        staticBook.setReviewers(Arrays.asList("Arthur", "Dent"));
        staticBook.setRating(4);
    }

    @Test
    public void allTests() {
        // Doesn't exist.

        Assert.assertFalse(mapper.existsById(BOOK_ISBN));
        Assert.assertFalse(mapper.exists(staticBook));

        // Insert (single).

        mapper.insert(staticBook);

        List<Book> books = mapper.selectAll();
        Assert.assertEquals(1, books.size());
        Assert.assertEquals(staticBook, books.get(0));

        Book book = mapper.selectById(BOOK_ISBN);
        Assert.assertNotNull(book);
        Assert.assertEquals(staticBook, book);

        mapper.delete(staticBook);

        // Insert (list).

        mapper.insertAll(Arrays.asList(staticBook));

        // Exists.

        Assert.assertTrue(mapper.existsById(BOOK_ISBN));
        Assert.assertTrue(mapper.exists(staticBook));

        // Delete.

        mapper.delete(staticBook);
        Assert.assertFalse(mapper.existsById(BOOK_ISBN));
        Assert.assertFalse(mapper.exists(staticBook));

        mapper.insert(staticBook);
        mapper.deleteById(BOOK_ISBN);
        Assert.assertFalse(mapper.existsById(BOOK_ISBN));
        Assert.assertFalse(mapper.exists(staticBook));

        mapper.insert(staticBook);
        mapper.deleteAll();
        Assert.assertFalse(mapper.existsById(BOOK_ISBN));
        Assert.assertFalse(mapper.exists(staticBook));

        mapper.insert(staticBook);
        mapper.deleteAll(Arrays.asList(staticBook));
        Assert.assertFalse(mapper.existsById(BOOK_ISBN));
        Assert.assertFalse(mapper.exists(staticBook));

        // Update.

        mapper.insert(staticBook);
        staticBook.setRating(5); // Was 4.
        mapper.update(staticBook);
        book = mapper.selectById(BOOK_ISBN);
        Assert.assertNotNull(book);
        Assert.assertEquals(staticBook, book);
    }
}
