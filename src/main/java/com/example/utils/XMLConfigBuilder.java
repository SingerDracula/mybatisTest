package com.example.utils;

import com.example.annotations.Select;
import com.example.cfg.Configuration;
import com.example.cfg.SqlMapper;
import com.example.io.Resources;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLConfigBuilder {
    public static Configuration loadConfiguration (InputStream inputStream) {
        try {
            Configuration configuration = new Configuration();
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(inputStream);
            Element rootElement = document.getRootElement();

            /*加载数据库信息*/
            List<Element> propertyElements = rootElement.selectNodes("//property");
            for (Element propertyElement : propertyElements) {
                String name = propertyElement.attributeValue("name");
                if ("driver".equals(name)){
                    String driver = propertyElement.attributeValue("value");
                    configuration.setDriver(driver);
                }
                if ("url".equals(name)){
                    String url = propertyElement.attributeValue("value");
                    System.out.println(url);
                    configuration.setUrl(url);
                }
                if ("username".equals(name)){
                    String username = propertyElement.attributeValue("value");
                    configuration.setUsername(username);
                }
                if ("password".equals(name)){
                    String password = propertyElement.attributeValue("value");
                    configuration.setPassword(password);
                }

            }

            /*加载mapper信息*/
            List<Element> mapperElements = rootElement.selectNodes("//mappers/mapper");
            for (Element mapperElement : mapperElements) {
                Attribute resource = mapperElement.attribute("resource");

                /*走xml*/
                if (resource != null){
                    System.out.println("我的mybatis使用xml");
                    String mapperPath = resource.getValue();
                    Map<String, SqlMapper> sqlMappers = loadMapperConfiguration(mapperPath);
                    configuration.setSqlMappers(sqlMappers);
                }else {
                    /*走注解*/
                    System.out.println("我的mybatis使用注解");
                    String mapperClassPath = mapperElement.attributeValue("class");
                    Map<String, SqlMapper> sqlMappers = loadMapperAnnotation(mapperClassPath);
                    configuration.setSqlMappers(sqlMappers);
                }
            }
            System.out.println(configuration);
            return configuration;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Map<String, SqlMapper> loadMapperAnnotation(String mapperClassPath) throws ClassNotFoundException {
        Map<String, SqlMapper> sqlMappers = new HashMap<String, SqlMapper>();
        Class daoClass = Class.forName(mapperClassPath);
        Method[] methods = daoClass.getMethods();
        for (Method method : methods) {
            boolean isAnnotated = method.isAnnotationPresent(Select.class);
            if (isAnnotated){
                SqlMapper sqlMapper = new SqlMapper();
                Select selectAnno = method.getAnnotation(Select.class);
                String querySql = selectAnno.value();
                sqlMapper.setQueryString(querySql);
                //返回类型
                Type genericReturnType = method.getGenericReturnType();
                if (genericReturnType instanceof ParameterizedType){
                    ParameterizedType pType = (ParameterizedType) genericReturnType;
                    Type[] actualTypeArguments = pType.getActualTypeArguments();
                    Class entityType = (Class) actualTypeArguments[0];
                    String resultType = entityType.getName();
                    sqlMapper.setResultType(resultType);
                }
                String name = method.getName();
                String className = method.getDeclaringClass().getName();
                String key = className + name;
                sqlMappers.put(key,sqlMapper);
            }
        }
        return sqlMappers;
    }

    private static Map<String, SqlMapper> loadMapperConfiguration(String mapperPath) throws IOException{
        Map<String, SqlMapper> sqlMappers = new HashMap<String, SqlMapper>();
        InputStream is = null;
        try {
            is = Resources.getResourceAsStream(mapperPath);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(is);
            Element rootElement = document.getRootElement();
            String namespace = rootElement.attributeValue("namespace");
            List<Element> selectNodes  = rootElement.selectNodes("select");
            for (Element selectNode : selectNodes) {
                String id = selectNode.attributeValue("id");
                String resultType = selectNode.attributeValue("resultType");
                String text = selectNode.getText();
                String key = namespace + id;
                SqlMapper sqlMapper = new SqlMapper();
                sqlMapper.setQueryString(text);
                sqlMapper.setResultType(resultType);
                sqlMappers.put(key,sqlMapper);
            }
            return sqlMappers;
        } catch (RuntimeException | DocumentException e) {
            throw new RuntimeException(e);
        }finally {
            is.close();
        }
    }
}
