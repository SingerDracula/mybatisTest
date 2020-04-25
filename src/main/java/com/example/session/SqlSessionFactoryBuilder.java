package com.example.session;

import com.example.session.defaults.DefaultSqlSessionFactory;
import com.example.utils.XMLConfigBuilder;

import java.io.InputStream;

public class SqlSessionFactoryBuilder {
    public SqlSessionFactory builder (InputStream inputStream) {
        return new DefaultSqlSessionFactory(
                XMLConfigBuilder.loadConfiguration(inputStream)
        );
    }
}
