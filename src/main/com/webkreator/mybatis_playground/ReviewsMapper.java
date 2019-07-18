package com.webkreator.mybatis_playground;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface ReviewsMapper {

    @Insert("INSERT INTO reviews (isbn,\n" +
            "                     rating)\n" +
            "values (#{isbn},\n" +
            "        #{rating})")
    @Options(useGeneratedKeys = true, keyProperty = "reviewId", keyColumn = "review_id")
    int insert(Review review);
}
