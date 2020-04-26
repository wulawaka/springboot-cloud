package com.zahem.cloud.service;

import com.zahem.cloud.config.AxiosResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface ICategoryService {
    public AxiosResponse addNewFiles(String token, String Name, int type, int parentId);
    public AxiosResponse selectAll(String token,int parentId);
    public AxiosResponse delete(String token,int id);
    public AxiosResponse remove(String token,int id);
    public AxiosResponse upload(MultipartFile file) throws IOException;
    public InputStream download(String fileName);
}
