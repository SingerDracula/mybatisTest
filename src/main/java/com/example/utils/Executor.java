package com.example.utils;

import com.example.cfg.SqlMapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Executor {
    public <E> List<E> selectList(SqlMapper sqlMapper, Connection connection) {
        ResultSet resultSet = null;
        try {
            String queryString = sqlMapper.getQueryString();
            String resultType = sqlMapper.getResultType();
            Class entityClass = Class.forName(resultType);
            ArrayList<E> list = new ArrayList<>();
            PreparedStatement ps = connection.prepareStatement(queryString);
            resultSet = ps.executeQuery();
            while (resultSet.next()){
                E entity = (E) entityClass.getDeclaredConstructor().newInstance();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int count = metaData.getColumnCount();
                for (int i = 1; i <= count; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(columnName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, entityClass);
                    Method writeMethod = propertyDescriptor.getWriteMethod();
                    writeMethod.invoke(entity,columnValue);
                }
                list.add(entity);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            release(connection,resultSet);
        }


    }

    private void release(Connection connection,ResultSet resultSet){
        if (resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
