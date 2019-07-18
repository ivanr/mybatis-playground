package com.webkreator.mybatis_playground.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webkreator.mybatis_playground.MybatisGson;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractJsonTypeHandler<T> extends BaseTypeHandler<T> {

    protected final static Gson gson = MybatisGson.instance();

    protected Type getType() {
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
        return pt.getActualTypeArguments()[0];
    }

    private T fromJson(String json) {
        return gson.fromJson(json, getType());
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T t, JdbcType jdbcType) throws SQLException {
        ps.setString(i, gson.toJson(t));
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return fromJson(rs.getString(columnName));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return fromJson(rs.getString(columnIndex));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return fromJson(cs.getString(columnIndex));
    }
}
