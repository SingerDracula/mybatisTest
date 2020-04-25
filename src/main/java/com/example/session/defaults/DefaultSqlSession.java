package com.example.session.defaults;

import com.example.cfg.SqlMapper;
import com.example.proxy.MapperProxy;
import com.example.session.SqlSession;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class DefaultSqlSession implements SqlSession {
    private Map<String, SqlMapper> sqlMappers;
    private Connection connection;

    public DefaultSqlSession(Map<String, SqlMapper> sqlMappers, Connection connection) {
        this.sqlMappers = sqlMappers;
        this.connection = connection;
    }

    @Override
    public <T> T getMapper(Class<T> mapperClass){
        return (T) Proxy.newProxyInstance(mapperClass.getClassLoader(),new Class[]{mapperClass},new MapperProxy(sqlMappers,connection));
    }

    @Override
    public void close() {
        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
