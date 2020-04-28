package com.zahem.cloud.controller;

import com.zahem.cloud.config.AxiosResponse;
import com.zahem.cloud.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    //查看所有文件
    @RequestMapping("/all")
    public AxiosResponse selectAll(String token, @RequestParam(defaultValue = "0",value = "parentId")int parentId){
        AxiosResponse axiosResponse = categoryService.selectAll(token, parentId);
        return axiosResponse;
    }

    //新建文件夹
    @RequestMapping("/addfile")
    public AxiosResponse addFile(String token,
                                 @RequestParam(defaultValue = "新建文件夹") String Name,
                                 @RequestParam(defaultValue = "0") int type,
                                 @RequestParam(defaultValue = "0",value = "parentId") int parentId){
        AxiosResponse axiosResponse = categoryService.addNewFiles(token, Name, type, parentId);
        return axiosResponse;
    }
    //删除标记废弃内容
    @RequestMapping("/delete")
    public AxiosResponse deleteFiles(String token,int id){
        AxiosResponse axiosResponse=categoryService.delete(token,id);
        return axiosResponse;
    }

    //标记废弃
    @RequestMapping("/rubbish")
    public AxiosResponse removeRubbish(String token,int id){
        AxiosResponse axiosResponse=categoryService.remove(token, id);
        return axiosResponse;
    }
    //上传
    @RequestMapping("/upload")
    public AxiosResponse upload(@RequestParam("file") MultipartFile file,String token) throws IOException {
        AxiosResponse upload = categoryService.upload(file,token);
        return upload;
    }

    /**
     * 下载文件
     * 根据前端传回来的文件ID进行下载
     * @param token 验证用户
     * @param Id    文件Id
     * @return
     * @throws IOException
     */
    @RequestMapping("/download")
    public AxiosResponse download(String token,int Id) throws IOException {
        AxiosResponse download = categoryService.download(token,Id);
        return AxiosResponse.success();
    }

//    @RequestMapping("/alipay")
//    public AxiosResponse test(){
//        AliPayConfig.aliPayTest();
//        return AxiosResponse.success();
//    }




}
