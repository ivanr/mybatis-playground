package com.webkreator.mybatis_playground;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MybatisGson {

    private static Gson gson = new GsonBuilder().create();

    public static Gson instance() {
        return gson;
    }
}
