package com.webkreator.mybatis_playground.handlers;

import org.apache.ibatis.type.MappedTypes;

import java.sql.Connection;
import java.util.List;

@MappedTypes(StringListAsSqlArray.class) // Prevent MyBatis introspection.
public class StringListAsSqlArray extends AbstractArrayTypeHandler<List<String>> {

    @Override
    protected String getDbTypeName(Connection connection) {
        return "TEXT";
    }
}