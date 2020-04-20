package com.zahem.cloud.controller;

import com.zahem.cloud.config.AxiosResponse;
import com.zahem.cloud.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private IUserService userService;

    /**
     * 登录
     * @param Email
     * @param Password
     * @return
     */
    @RequestMapping("/login")
    public AxiosResponse logins(String Email,String Password){
        AxiosResponse response = userService.login(Email, Password);
        return response;
    }

}
