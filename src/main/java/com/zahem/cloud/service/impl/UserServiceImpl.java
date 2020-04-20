package com.zahem.cloud.service.impl;

import com.zahem.cloud.config.AxiosResponse;
import com.zahem.cloud.config.CustomExprotion;
import com.zahem.cloud.dao.UserMapper;
import com.zahem.cloud.service.IUserService;
import com.zahem.cloud.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

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
        String random= UUID.randomUUID().toString();
        return AxiosResponse.success(random);
    }


}
