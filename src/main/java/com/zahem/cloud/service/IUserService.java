package com.zahem.cloud.service;

import com.zahem.cloud.config.AxiosResponse;

public interface IUserService {
    public AxiosResponse login(String Email, String Password);

    public AxiosResponse addUsers(String Name ,String Email,String Password);
}
