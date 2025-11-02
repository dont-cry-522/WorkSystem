// service/impl/StudentServiceImpl.java
package com.worksystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.worksystem.dto.request.UserLoginRequest;
import com.worksystem.dto.response.UserLoginResponse;
import com.worksystem.entity.User;
import com.worksystem.exception.BusinessException;
import com.worksystem.mapper.UserMapper;
import com.worksystem.service.StudentService;
import com.worksystem.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StudentServiceImpl extends ServiceImpl<UserMapper, User> implements StudentService {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        User user = getUserByUsername(request.getUsername());
        if (user == null) {
            log.warn("登录失败: 用户不存在, username: {}", request.getUsername());
            throw new BusinessException("用户名或密码错误");
        }

        if (!"student".equals(user.getUserType())) {
            log.warn("登录失败: 用户类型错误, username: {}, userType: {}", request.getUsername(), user.getUserType());
            throw new BusinessException("该账号不是学生账号");
        }

        if (!request.getPassword().equals(user.getPassword())) {
            log.warn("登录失败: 密码错误, username: {}", request.getUsername());
            throw new BusinessException("用户名或密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        log.info("学生登录成功: {}", user.getUsername());
        return new UserLoginResponse((org.apache.catalina.User) user, token);
    }

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return getOne(queryWrapper);
    }

    @Override
    public boolean applyPosition(Long studentId, Long positionId) {
        log.info("学生 {} 申请岗位 {}", studentId, positionId);
        // TODO: 实现申请逻辑
        return true;
    }
}