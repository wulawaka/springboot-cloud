package com.zahem.cloud.dao;

import com.zahem.cloud.pojo.Roles;
import org.springframework.stereotype.Component;

@Component
public interface RolesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Roles record);

    int insertSelective(Roles record);

    Roles selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Roles record);

    int updateByPrimaryKey(Roles record);

    String selectUserRole(int userId);
}