package com.webkreator.mybatis_playground.handlers;

import com.webkreator.mybatis_playground.util.DisableMybatisIntrospection;
import org.apache.ibatis.type.MappedTypes;

import java.sql.Connection;
import java.util.List;

@MappedTypes(DisableMybatisIntrospection.class)
public class StringListAsSqlArray extends AbstractArrayTypeHandler<List<String>> {

    @Override
    protected String getDbTypeName(Connection connection) {
        return "TEXT";
    }
}