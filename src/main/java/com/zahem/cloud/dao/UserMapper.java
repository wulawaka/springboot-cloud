package com.zahem.cloud.dao;

import com.zahem.cloud.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int selectByEmailAndPassword(@Param("Email")String Email,@Param("Password")String Password);

    User selectByEmail(String email);

    int insertUser(@Param("Name")String Name,@Param("Email")String Email,@Param("Password")String Password);
}