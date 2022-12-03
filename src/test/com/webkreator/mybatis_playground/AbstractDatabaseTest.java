package com.webkreator.mybatis_playground;

import com.webkreator.mybatis_playground.util.StrictTransactionsSqlSession;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Before;

import java.io.FileInputStream;
import java.io.Reader;
import java.util.Properties;

public class AbstractDatabaseTest {

    private boolean initialized = false;

    protected SqlSessionFactory sqlSessionFactory;

    protected StrictTransactionsSqlSession sql;

    protected BooksMapper books;

    protected ReviewsMapper reviews;

    protected StreamingMapper streaming;

    @Before
    public void initMigrations() throws Exception {
        // Load properties.

        Properties properties = new Properties();
        properties.load(new FileInputStream("config.properties"));

        // Reset the database on the first invocation.
        if (!initialized) {
            Flyway flyway = Flyway.configure()
                    .dataSource(
                            properties.getProperty("db.url"),
                            properties.getProperty("db.username"),
                            properties.getProperty("db.password"))
                    .locations("com/webkreator/mybatis_playground")
                    .failOnMissingLocations(true)
                    .load();

            flyway.clean();
            flyway.migrate();

            initialized = true;
        }

        // Configure MyBatis.

        String resource = "com/webkreator/mybatis_playground/mybatis.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, properties);
        sql = new StrictTransactionsSqlSession(sqlSessionFactory.openSession(TransactionIsolationLevel.SERIALIZABLE));

        books = sql.getMapper(BooksMapper.class);
        reviews = sql.getMapper(ReviewsMapper.class);
        streaming = sql.getMapper(StreamingMapper.class);
    }

    @After
    public void after() {
        sql.close();
    }
}
