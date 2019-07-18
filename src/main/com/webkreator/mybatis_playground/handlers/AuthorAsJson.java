package com.webkreator.mybatis_playground.handlers;

import com.webkreator.mybatis_playground.Author;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(Author.class)
public class AuthorAsJson extends AbstractJsonTypeHandler<Author> {
}
