package com.zahem.cloud.dao;

import com.zahem.cloud.pojo.Category;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKeyAndUserId(@Param("id")int id,@Param("userId")int userId);

    int insertFiles(@Param("userId") int userId,@Param("Name")String Name,@Param("type")int type);

    List<Category> selectAllByUserIdAndParentId(@Param("userId")int userId, @Param("parentId")int parentId);

    int insertFilesById(@Param("userId")int userId,@Param("Name")String Name,@Param("type")int type,@Param("parentId")int parentId);

    String selectByUserIdAndId(@Param("userId")int userId,@Param("Id")int id);

    int selectByFileName(String Name);

    List<Category> selectByStatus(int userId);

    List<Category> selectFilesType(@Param("userId")int userId,@Param("type")int type);
}