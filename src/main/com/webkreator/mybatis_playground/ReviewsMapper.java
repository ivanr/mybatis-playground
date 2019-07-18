package com.webkreator.mybatis_playground;

import com.webkreator.mybatis_playground.handlers.StringListAsJsonArray;
import com.webkreator.mybatis_playground.handlers.StringListAsSqlArray;
import org.apache.ibatis.annotations.*;

public interface ReviewsMapper {

    @Insert("INSERT INTO reviews (isbn,\n" +
            "                     rating)\n" +
            "values (#{isbn},\n" +
            "        #{rating})")
    @Options(useGeneratedKeys = true, keyProperty = "reviewId", keyColumn = "review_id")
    int insert(Review review);

    @Select("SELECT * FROM reviews R WHERE R.review_id = #{reviewId}")
    Review selectById(int reviewId);
}
