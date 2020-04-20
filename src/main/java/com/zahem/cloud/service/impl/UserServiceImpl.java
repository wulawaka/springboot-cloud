package com.zahem.cloud.service.impl;

import com.zahem.cloud.config.AxiosResponse;
import com.zahem.cloud.config.CustomExprotion;
import com.zahem.cloud.dao.RolesMapper;
import com.zahem.cloud.dao.UserMapper;
import com.zahem.cloud.pojo.User;
import com.zahem.cloud.service.IUserService;
import com.zahem.cloud.utils.MD5Utils;
import com.zahem.cloud.utils.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RolesMapper rolesMapper;

    @Resource
    private RedisClient redisClient;

    /**
     * 登录
     * 输入的密码进行MD5加密，用加密的密码登录，生成随机UUID作为token
     * @param Email
     * @param Password
     * @return
     */
    public AxiosResponse login(String Email,String Password){
        String md5Password = MD5Utils.md5Password(Password);
        int result = userMapper.selectByEmailAndPassword(Email, md5Password);
        if (result == 0){
            return AxiosResponse.error(CustomExprotion.USER_INPUT_ERROR);
        }
        //根据邮箱查询用户信息
        User user = userMapper.selectByEmail(Email);
        //根据用户Id查询用户权限
        String userRoles = rolesMapper.selectUserRole(user.getId());
        //生成随机UUID
        String random= UUID.randomUUID().toString();
        //存入redis中
        redisClient.set(random,"userId",user.getId());
        redisClient.set(random,"userName",user.getName());
        redisClient.set(random,"userEmail",user.getEmail());
        redisClient.set(random,"userRoles",userRoles);

        return AxiosResponse.success(random);
    }


}
