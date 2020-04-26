package com.zahem.cloud.service.impl;

import com.zahem.cloud.config.AxiosResponse;
import com.zahem.cloud.config.CustomExprotion;
import com.zahem.cloud.dao.CategoryMapper;
import com.zahem.cloud.pojo.Category;
import com.zahem.cloud.service.ICategoryService;
import com.zahem.cloud.utils.FTPUtil;
import com.zahem.cloud.utils.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@Service
@Slf4j
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Resource
    private RedisClient redisClient;
    @Autowired
    private FTPUtil ftpUtil;

    /**
     *
     * @param token 验证的token
     * @param Name  新建文件的名字，如果type=0，并且没有名字则默认为“新建文件夹”
     * @param type  类型，在数据库说明里
     * @param parentId  从前端获取节点，如果没有则默认0根目录
     * @return
     */
    @Override
    public AxiosResponse addNewFiles(String token, String Name, int type, int parentId){
        //登录校验部分
        Boolean hasToken = redisClient.hasKey(token);
        if(hasToken == false){
            AxiosResponse.error(CustomExprotion.USER_NOT_LOGIN);
        }
        //获得userId
        Object userId = redisClient.get(token, "userId");

        int result = categoryMapper.insertFilesById((Integer) userId, Name, type, parentId);
        if (result == 0){
            AxiosResponse.error(CustomExprotion.HANDLE_ERROR);
        }
        return AxiosResponse.success("创建成功");
    }

    /**
     * 查看当前用户的内容
     * 从前端获取token如果没有则parentID为0
     * 前端点击某个文件夹，传给后端parentId和token
     *
     * @param token
     * @param parentId
     * @return
     */
    @Override
    public AxiosResponse selectAll(String token,int parentId){
        Boolean hasToken = redisClient.hasKey(token);
        if(hasToken == false){
            AxiosResponse.error(CustomExprotion.USER_NOT_LOGIN);
        }
        Object userId = redisClient.get(token, "userId");
        Category category = categoryMapper.selectAllByUserIdAndParentId((Integer) userId, parentId);
        return AxiosResponse.success(category);
    }

    /**
     * 彻底删除垃圾回收箱中的内容
     * @param token 验证是否登录
     * @param id    删除的id
     * @return
     */
    @Override
    public AxiosResponse delete(String token,int id){
        //登录校验部分
        Boolean hasToken = redisClient.hasKey(token);
        if(hasToken == false){
            AxiosResponse.error(CustomExprotion.USER_NOT_LOGIN);
        }

        int result = categoryMapper.deleteByPrimaryKey(id);
        if (result == 0){
            AxiosResponse.error(CustomExprotion.HANDLE_ERROR);
        }
        return AxiosResponse.success("删除成功");
    }

    /**
     * 标记废弃
     * @param token 验证是否登录
     * @param id    当前的id
     * @return
     */
    @Override
    public AxiosResponse remove(String token,int id){
        //登录校验部分
        Boolean hasToken = redisClient.hasKey(token);
        if(hasToken == false){
            AxiosResponse.error(CustomExprotion.USER_NOT_LOGIN);
        }

        //获得userId
        Object userId = redisClient.get(token, "userId");

        int result = categoryMapper.updateByPrimaryKeyAndUserId(id, (Integer) userId);
        if (result == 0){
            AxiosResponse.error(CustomExprotion.HANDLE_ERROR);
        }
        return AxiosResponse.success("移入垃圾箱成功");
    }

    /**
     * 上传文件
     * @param file
     * @return
     * @throws IOException
     */
    @Override
    public AxiosResponse upload(MultipartFile file) throws IOException {
        int type = 6;
        InputStream inputStream=file.getInputStream();

        String fileName=file.getOriginalFilename();

        String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
        String prefix=fileName.substring(0,fileName.indexOf("."));
        switch (suffix){
            case "jpg":
            case "png":
                type=5;
                break;
            case "rmvb":
            case "mp4":
                type=3;
                break;
            case "mp3":
            case "WAV":
            case "MIDI":
                type=1;
                break;
            case "txt":
            case "md":
                type=2;
                break;
            case "zip":
            case "rar":
            case "7z":
                type=4;
                break;
        }

        Category category=new Category();
        category.setUserId(1);
        category.setParentId(0);
        category.setName(prefix);
        category.setType(type);
        int result = categoryMapper.insert(category);
        if (result == 0){
            return AxiosResponse.error(CustomExprotion.HANDLE_ERROR);
        }
//        String finalName=UUID.randomUUID().toString();

        boolean flag = ftpUtil.upload(fileName,inputStream);
        if (flag == false){
            return AxiosResponse.error("上传失败");
        }
        return AxiosResponse.success("上传成功");
    }
    @Override
    public InputStream download(String fileName){
        InputStream inputStream = ftpUtil.downFile(fileName);
        return inputStream;
    }



}
