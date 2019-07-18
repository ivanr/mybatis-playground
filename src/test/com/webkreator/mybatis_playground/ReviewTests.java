package com.webkreator.mybatis_playground;

import org.junit.Assert;
import org.junit.Test;

import static com.webkreator.mybatis_playground.BookTests.BOOK_SINGLETON;

public class ReviewTests extends AbstractDatabaseTest {

    @Test
    public void testMapper() {
        books.insert(BOOK_SINGLETON);
        Review review = new Review(BookTests.ISBN, 5);
        reviews.insert(review);
        Assert.assertNotNull(review.getReviewId());
        Assert.assertEquals(1, (int) review.getReviewId());
    }
}
