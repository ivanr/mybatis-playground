package com.webkreator.mybatis_playground;

import com.webkreator.mybatis_playground.handlers.StringListAsJsonArray;
import com.webkreator.mybatis_playground.handlers.StringListAsSqlArray;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BookMapper {

    @Insert("INSERT INTO books (isbn,\n" +
            "                   title,\n" +
            "                   author,\n" +
            "                   editors,\n" +
            "                   reviewers)\n" +
            "values (#{isbn},\n" +
            "        #{title},\n" +
            "        CAST(#{author} AS JSON),\n" +
            "        #{editors,typeHandler=StringListAsSqlArray},\n" +
            "        CAST(#{reviewers,typeHandler=StringListAsJsonArray} AS JSON))\n" +
            "        ")
    void insert(Book book);

    @Select("SELECT * FROM books")
    @Results(value = {
            @Result(property = "editors", column = "editors", typeHandler = StringListAsSqlArray.class),
            @Result(property = "reviewers", column = "reviewers", typeHandler = StringListAsJsonArray.class)
    })
    List<Book> selectAll();
}
