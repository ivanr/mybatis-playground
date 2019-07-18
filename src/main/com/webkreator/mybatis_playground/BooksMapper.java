package com.webkreator.mybatis_playground;

import com.webkreator.mybatis_playground.handlers.StringListAsJsonArray;
import com.webkreator.mybatis_playground.handlers.StringListAsSqlArray;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface BooksMapper {

    @Delete("DELETE FROM books B WHERE B.isbn = #{isbn}")
    int delete(Book book);

    @Delete("DELETE FROM books")
    int deleteAll();

    default int deleteAll(Iterable<Book> books) {
        int deleted = 0;
        for (Book book : books) {
            deleted += delete(book);
        }
        return deleted;
    }

    @Delete("DELETE FROM books B WHERE B.isbn = #{isbn}")
    int deleteById(String isbn);

    @Select("SELECT EXISTS (SELECT 1 FROM books B WHERE B.isbn = #{isbn})")
    boolean exists(Book book);

    @Select("SELECT EXISTS (SELECT 1 FROM books B WHERE B.isbn = #{isbn})")
    boolean existsById(String isbn);

    @Insert("INSERT INTO books (isbn,\n" +
            "                   title,\n" +
            "                   author,\n" +
            "                   editors,\n" +
            "                   reviewers,\n" +
            "                   rating)\n" +
            "values (#{isbn},\n" +
            "        #{title},\n" +
            "        CAST(#{author} AS JSON),\n" +
            "        #{editors,typeHandler=StringListAsSqlArray},\n" +
            "        CAST(#{reviewers,typeHandler=StringListAsJsonArray} AS JSON),\n" +
            "        #{rating})\n" +
            "        ")
    int insert(Book book);

    default int insertAll(Iterable<Book> books) {
        int inserted = 0;
        for (Book book : books) {
            inserted += insert(book);
        }
        return inserted;
    }

    @Select("SELECT * FROM books B WHERE B.isbn = #{isbn}")
    @Results(id = "bookMap", value = {
            @Result(property = "editors", column = "editors", typeHandler = StringListAsSqlArray.class),
            @Result(property = "reviewers", column = "reviewers", typeHandler = StringListAsJsonArray.class)
    })
    Book selectById(String isbn);

    @Select("SELECT * FROM books")
    @ResultMap("bookMap")
    List<Book> selectAll();

    @Update("UPDATE books B\n" +
            "SET isbn      = #{isbn},\n" +
            "    title     = #{title},\n" +
            "    author    = CAST(#{author} AS JSON),\n" +
            "    editors   = #{editors,typeHandler=StringListAsSqlArray},\n" +
            "    reviewers = CAST(#{reviewers,typeHandler=StringListAsJsonArray} AS JSON),\n" +
            "    rating    = #{rating}\n" +
            "WHERE B.isbn = #{isbn}")
    int update(Book book);
}
