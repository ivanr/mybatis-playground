package com.webkreator.mybatis_playground;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Author {

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
