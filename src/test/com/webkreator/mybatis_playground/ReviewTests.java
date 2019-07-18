package com.webkreator.mybatis_playground;

import org.junit.Assert;
import org.junit.Test;

import static com.webkreator.mybatis_playground.BookTests.BOOK_SINGLETON;

public class ReviewTests extends AbstractDatabaseTest {

    @Test
    public void testMapper() {
        // Insert.

        books.insert(BOOK_SINGLETON);
        Review review = Review.builder()
                .isbn(BookTests.ISBN)
                .rating(5)
                .build();
        reviews.insert(review);
        Assert.assertNotNull(review.getReviewId());
        Assert.assertEquals(1, (int) review.getReviewId());

        // Select.

        review = reviews.selectById(1);
        System.out.println(review);
    }
}
