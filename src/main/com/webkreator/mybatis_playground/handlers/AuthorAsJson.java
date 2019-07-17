package com.webkreator.mybatis_playground.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webkreator.mybatis_playground.Author;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AuthorAsJson extends BaseTypeHandler<Author> {

    private static Gson gson = new GsonBuilder().create();

    private Author fromJson(String json) {
        return gson.fromJson(json, Author.class);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Author author, JdbcType jdbcType) throws SQLException {
        ps.setString(i, gson.toJson(author));
    }

    @Override
    public Author getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return fromJson(rs.getString(columnName));
    }

    @Override
    public Author getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return fromJson(rs.getString(columnIndex));
    }

    @Override
    public Author getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return fromJson(cs.getString(columnIndex));
    }
}
