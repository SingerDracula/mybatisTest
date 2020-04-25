package com.example.dao;

import com.example.annotations.Select;
import com.example.entity.User;

import java.util.List;

public interface IUserMapper {
    @Select("select * from user")
    List<User> findAll();
}
