package com.webkreator.mybatis_playground.handlers;

import com.webkreator.mybatis_playground.Author;

import java.lang.reflect.Type;

public class AuthorAsJson extends AbstractJsonTypeHandler<Author> {

    @Override
    protected Type getType() {
        return Author.class;
    }
}
