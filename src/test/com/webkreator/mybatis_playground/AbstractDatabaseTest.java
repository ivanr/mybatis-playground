package com.webkreator.mybatis_playground;

import org.flywaydb.core.Flyway;
import org.junit.Before;

import java.io.FileInputStream;
import java.util.Properties;

public class AbstractDatabaseTest {

    @Before
    public void initMigrations() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream("config.properties"));

        Flyway flyway = new Flyway();
        flyway.setDataSource(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password"));

        String packageName = this.getClass().getName().substring(0, this.getClass().getName().lastIndexOf("."));
        flyway.setLocations("classpath:" + packageName);

        flyway.clean();
        flyway.migrate();
    }
}
