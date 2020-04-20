package com.zahem.cloud.dao;

import com.zahem.cloud.pojo.RolesUser;
import org.springframework.stereotype.Component;

@Component
public interface RolesUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RolesUser record);

    int insertSelective(RolesUser record);

    RolesUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RolesUser record);

    int updateByPrimaryKey(RolesUser record);

    int addRolesUser();
}