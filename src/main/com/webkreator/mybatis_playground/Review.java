package com.webkreator.mybatis_playground;

import lombok.*;

@EqualsAndHashCode
@ToString
@Builder
public class Review {

    @Getter
    private final Integer reviewId;

    @Getter
    private final String isbn;

    @Getter
    private final int rating;
}
