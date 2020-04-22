package com.zahem.cloud.service;

import com.zahem.cloud.config.AxiosResponse;

public interface ICategoryService {
    public AxiosResponse addNewFiles(String token, String Name, int type, int parentId);
    public AxiosResponse selectAll(String token,int parentId);
    public AxiosResponse delete(String token,int id);
    public AxiosResponse remove(String token,int id);
}
