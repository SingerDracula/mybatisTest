package com.example.cfg;

public class SqlMapper {
    /*查询语句*/
    private String QueryString;
    /*结果类型*/
    private String resultType;

    public String getQueryString() {
        return QueryString;
    }

    public String getResultType() {
        return resultType;
    }

    public void setQueryString(String queryString) {
        QueryString = queryString;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
}
