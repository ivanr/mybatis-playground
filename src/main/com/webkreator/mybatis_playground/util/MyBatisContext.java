package com.webkreator.mybatis_playground.util;

import com.webkreator.mybatis_playground.BooksMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * Use this wrapper to simplify MyBatis operations in three ways: 1) use try-with-resources
 * to avoid forgetting to close a connection; 2) enforce strict transactions via StrictSqlSession;
 * and 3) make it slightly more pleasant to get mappers.
 */
public class MyBatisContext implements AutoCloseable {

    private SqlSessionFactory sessionFactory;

    private SqlSession session;

    public MyBatisContext(SqlSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private BooksMapper books;

    public BooksMapper books() {
        if (books == null) {
            this.books = session().getMapper(BooksMapper.class);
        }

        return books;
    }

    public SqlSession session() {
        if (session == null) {
            this.session = new StrictSqlSession(sessionFactory.openSession());
        }

        return session;
    }

    @Override
    public void close() {
        if (session != null) {
            session.close();
        }
    }
}
