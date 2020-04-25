package com.example.session;

public interface SqlSession {
    <T> T getMapper(Class<T> mapperClass);

    void close();
}
