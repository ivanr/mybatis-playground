package com.webkreator.mybatis_playground;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FirstTest extends AbstractDatabaseTest {

    private static final Book staticBook;

    static {
        staticBook = new Book();
        staticBook.setIsbn("9781907117046");
        staticBook.setTitle("Bulletproof SSL and TLS");
        staticBook.setAuthor(new Author("Ivan", "Ristic"));
        staticBook.setEditors(Arrays.asList("Zaphod", "Beeblebrox"));
        staticBook.setReviewers(Arrays.asList("Arthur", "Dent"));
    }

    @Test
    public void t1_create_books() {
        mapper.insertBook(staticBook);
        sql.commit();
    }

    @Test
    public void t2_get_books() {
        List<Book> books = mapper.selectAllBooks();
        //System.out.println(books);
        Assert.assertEquals(1, books.size());
        Assert.assertEquals(staticBook, books.get(0));
    }
}
