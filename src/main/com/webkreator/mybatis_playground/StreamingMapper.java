package com.webkreator.mybatis_playground;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.ResultHandler;

public interface StreamingMapper {

    @Select("SELECT id FROM streaming_data ORDER BY id")
    @ResultType(Integer.class)
    @Options(fetchSize = 500)
    void selectAll(ResultHandler handler);
}
