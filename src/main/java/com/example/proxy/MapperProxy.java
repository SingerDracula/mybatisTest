package com.example.proxy;

import com.example.cfg.SqlMapper;
import com.example.utils.Executor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;

public class MapperProxy implements InvocationHandler {

    private Map<String, SqlMapper> sqlMappers;
    private Connection connection;

    public MapperProxy(Map<String, SqlMapper> sqlMappers, Connection connection) {
        this.sqlMappers = sqlMappers;
        this.connection = connection;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        String name = method.getName();
        String className = method.getDeclaringClass().getName();
        SqlMapper sqlMapper = sqlMappers.get(className + name);
        return new Executor().selectList(sqlMapper, connection);
    }
}
