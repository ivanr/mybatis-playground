package com.webkreator.mybatis_playground.handlers;

import com.webkreator.mybatis_playground.util.DisableMybatisIntrospection;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

@MappedTypes(DisableMybatisIntrospection.class)
public class StringListAsJsonArray extends AbstractJsonTypeHandler<List<String>> {
}