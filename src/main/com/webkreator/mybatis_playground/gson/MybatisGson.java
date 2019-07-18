package com.webkreator.mybatis_playground.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// This class holds the common Gson instance that will be used
// by all Gson type handlers. Customise the instance as needed,
// but use it only for the database operations.

public class MybatisGson {

    private static Gson gson = new GsonBuilder().create();

    public static Gson instance() {
        return gson;
    }
}
