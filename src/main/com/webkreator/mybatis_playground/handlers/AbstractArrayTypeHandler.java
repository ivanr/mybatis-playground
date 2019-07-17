package com.webkreator.mybatis_playground.handlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.Arrays;
import java.util.Collection;

// Adapted from: https://github.com/javaplugs/mybatis-types/blob/master/src/main/java/com/github/javaplugs/mybatis/ArrayTypeHandler.java

// Useful: https://blog.2ndquadrant.com/using-java-arrays-to-insert-retrieve-update-postgresql-arrays/

public abstract class AbstractArrayTypeHandler<T> extends BaseTypeHandler<T> {

    protected abstract String getDbTypeName(Connection connection) throws SQLException;

    protected T fromArray(Array source) throws SQLException {
        return (T) Arrays.asList((Object[]) source.getArray());
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        Array sqlArray = ps.getConnection().createArrayOf(
                getDbTypeName(ps.getConnection()),
                ((Collection) parameter).toArray());
        ps.setArray(i, sqlArray);
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Array array = rs.getArray(columnName);
        if (array == null) {
            return null;
        }

        return fromArray(array);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Array array = rs.getArray(columnIndex);
        if (array == null) {
            return null;
        }

        return fromArray(array);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Array array = cs.getArray(columnIndex);
        if (array == null) {
            return null;
        }

        return fromArray(array);
    }
}
