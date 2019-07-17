package com.webkreator.mybatis_playground;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
public class Book {

    @Getter
    @Setter
    private String isbn;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private Author author;

    @Getter
    @Setter
    private List<String> editors;

    @Getter
    @Setter
    private List<String> reviewers;
}
