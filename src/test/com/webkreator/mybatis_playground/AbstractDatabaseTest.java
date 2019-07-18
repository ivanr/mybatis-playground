package com.webkreator.mybatis_playground;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Before;

import java.io.FileInputStream;
import java.io.Reader;
import java.util.Properties;

public class AbstractDatabaseTest {

    private static boolean initialized = false;

    protected SqlSessionFactory sqlSessionFactory;

    protected SqlSession sql;

    protected BooksMapper books;

    protected ReviewsMapper reviews;

    @Before
    public void initMigrations() throws Exception {
        // Load properties.

        Properties properties = new Properties();
        properties.load(new FileInputStream("config.properties"));

        // Reset the database on the first invocation.
        if (!initialized) {

            Flyway flyway = new Flyway();
            flyway.setDataSource(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.username"),
                    properties.getProperty("db.password"));

            String packageName = this.getClass().getName().substring(0, this.getClass().getName().lastIndexOf("."));
            flyway.setLocations("classpath:" + packageName);

            flyway.clean();
            flyway.migrate();

            initialized = true;
        }

        // Configure MyBatis.

        String resource = "com/webkreator/mybatis_playground/mybatis.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, properties);
        sql = sqlSessionFactory.openSession(true);

        books = sql.getMapper(BooksMapper.class);
        reviews = sql.getMapper(ReviewsMapper.class);
    }

    @After
    public void after() {
        sql.close();
    }
}
