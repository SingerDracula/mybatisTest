package com.example.cfg;

import java.util.HashMap;
import java.util.Map;

public class Configuration {
    private String driver;
    private String url;
    private String username;
    private String password;
    private Map<String,SqlMapper> sqlMappers = new HashMap<>();

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, SqlMapper> getSqlMappers() {
        return sqlMappers;
    }

    public void setSqlMappers(Map<String, SqlMapper> sqlMappers) {
        this.sqlMappers = sqlMappers;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "driver='" + driver + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sqlMappers=" + sqlMappers +
                '}';
    }
}
