package com.zahem.cloud.service.impl;

import com.zahem.cloud.config.AxiosResponse;
import com.zahem.cloud.config.CustomExprotion;
import com.zahem.cloud.dao.CategoryMapper;
import com.zahem.cloud.pojo.Category;
import com.zahem.cloud.service.ICategoryService;
import com.zahem.cloud.utils.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Resource
    private RedisClient redisClient;

    /**
     * 首先校验token，生成userId
     * 从前端获取父节点，如果没有则默认为0（根节点），目的是显示当前目录的所有信息
     * 如果获取上面的信息中的SortOrder为0说明是根节点，那就创建父节点为0的文件夹或其他的文件。
     * 否则就获取前端点击的那个文件夹的id，并把其父节点变成从前端获取的id
     *
     * @param token
     * @return
     */
    public AxiosResponse addNewFiles(String token,String Name,int type,int parentId,int CategoryId){
        //登录校验部分
        Boolean hasToken = redisClient.hasKey(token);
        if(hasToken == false){
            AxiosResponse.error(CustomExprotion.USER_NOT_LOGIN);
        }
        //获得userId
        Object userId = redisClient.get(token, "userId");
        //获取userid和父节点
        Category category = categoryMapper.selectAllByUserIdAndParentId((Integer) userId, parentId);
        //进行判断
        if(category.getSortOrder() == 0){
            int result=categoryMapper.insertFiles((Integer) userId,Name,type);
        }else{
            int result = categoryMapper.insertFilesById((Integer) userId, Name, type, CategoryId);
            if (result == 0){
                return AxiosResponse.error("创建出问题");
            }
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
    public AxiosResponse selectAll(String token,int parentId){
        Boolean hasToken = redisClient.hasKey(token);
        if(hasToken == false){
            AxiosResponse.error(CustomExprotion.USER_NOT_LOGIN);
        }
        Object userId = redisClient.get(token, "userId");
        Category category = categoryMapper.selectAllByUserIdAndParentId((Integer) userId, parentId);
        return AxiosResponse.success(category);
    }
}
