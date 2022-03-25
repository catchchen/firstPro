package com.ch.service.impl;

import com.ch.dao.UserDao;
import com.ch.pojo.entity.User;

import com.ch.pojo.params.UserLoginParam;
import com.ch.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenk
 * @date 2022/2/23 21:33
 * @description Userd
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    /**
     *
     * @param userLogin 登录的参数
     * @return 返回User pojo类
     */
    @Override
    public User userLogin(UserLoginParam userLogin) {
        Assert.notNull(userLogin,"Login param must not be null");

//        String username = userLogin.getUsername();

//        User login = userDao.selectUserByUsernameAndPassword(userLogin);

//        final User user;
//        user = Validator.isEmail(username)?
//                userDao.getByEmail(username) :
//                userDao.getByUsername(username);
//        log.info("user login:"+userLogin.getUsername());
//
//        if (!userDao.passwordMatch(user,userLogin.getPassword())){
//            // 用户密码不匹配
////            log.warn("");
//            throw new BadRequestException(Constants.MISS_MATCH_TIP);
//        }
        return new User();
    }

    @Override
    public List<User> getUsers() {
        // page 分页
        return null;
    }

    @Override
    public User getUser(Integer id) {
        return userDao.selectUserById(id);
    }

    @Override
    public int addUser(User user) {
        return 0;
    }

    @Override
    public int changeUserInfo(User user) {
        return 0;
    }

    @Override
    public int removeUser(Integer uid) {
        return 0;
    }

}
