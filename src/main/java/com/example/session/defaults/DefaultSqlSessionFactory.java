package com.example.session.defaults;

import com.example.cfg.Configuration;
import com.example.session.SqlSession;
import com.example.session.SqlSessionFactory;
import com.example.utils.DataSourceUtil;

public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration.getSqlMappers(), DataSourceUtil.getConnection(configuration));
    }
}
