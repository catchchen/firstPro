package com.ch.dao;

import com.ch.pojo.entity.User;
import com.ch.web.model.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chenk
 * @date 2022/3/2
 * @description 功能
 */
@Mapper
public interface UserDao {

    List<User> selectUserList();

    List<User> selectUsersSortByGradle();

    User selectUserById(@Param("id") Integer userId);
}
