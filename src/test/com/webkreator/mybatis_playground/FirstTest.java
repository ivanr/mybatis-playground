package com.webkreator.mybatis_playground;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FirstTest extends AbstractDatabaseTest {

    @Test
    public void t1_create_books() {
        Book b = new Book();
        b.setIsbn("9781907117046");
        b.setTitle("Bulletproof SSL and TLS");
        b.setAuthor(new Author("Ivan", "Ristic"));
        b.setEditors(Arrays.asList("Zaphod", "Beeblebrox"));
        b.setReviewers(Arrays.asList("Arthur", "Dent"));
        System.out.println(b);
        mapper.insertBook(b);
        sql.commit();
    }

    @Test
    public void t2_get_books() {
        List<Book> books = mapper.selectAllBooks();
        System.out.println(books);
    }
}
