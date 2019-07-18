package com.webkreator.mybatis_playground;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Review {

    @Getter
    @Setter
    private Integer reviewId;

    @Getter
    @Setter
    private String isbn;

    @Getter
    @Setter
    private int rating;

    public Review(String isbn, int rating) {
        this.isbn = isbn;
        this.rating = rating;
    }
}
