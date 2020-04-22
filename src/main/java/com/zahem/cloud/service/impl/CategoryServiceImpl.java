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
            AxiosResponse.error("创建失败");
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
    public AxiosResponse delete(String token,int id){
        //登录校验部分
        Boolean hasToken = redisClient.hasKey(token);
        if(hasToken == false){
            AxiosResponse.error(CustomExprotion.USER_NOT_LOGIN);
        }

        int result = categoryMapper.deleteByPrimaryKey(id);
        if (result == 0){
            AxiosResponse.error("删除出错");
        }
        return AxiosResponse.success("删除成功");
    }
}
