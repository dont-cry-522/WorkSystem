// service/impl/TeacherServiceImpl.java
package com.worksystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.worksystem.dto.request.UserLoginRequest;
import com.worksystem.dto.response.UserLoginResponse;
import com.worksystem.entity.User;
import com.worksystem.entity.Position;
import com.worksystem.exception.BusinessException;
import com.worksystem.mapper.UserMapper;
import com.worksystem.service.TeacherService;
import com.worksystem.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TeacherServiceImpl extends ServiceImpl<UserMapper, User> implements TeacherService {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        User user = getUserByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        if (!"teacher".equals(user.getUserType())) {
            throw new BusinessException("该账号不是教师账号");
        }

        if (!request.getPassword().equals(user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        log.info("教师登录成功: {}", user.getUsername());
        return new UserLoginResponse((org.apache.catalina.User) user, token);
    }

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return getOne(queryWrapper);
    }

    @Override
    public boolean publishPosition(Position position) {
        log.info("教师发布岗位: {}", position.getPositionName());
        // TODO: 实现发布岗位逻辑
        return true;
    }
}