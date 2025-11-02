// controller/TeacherController.java
package com.worksystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.worksystem.dto.request.UserLoginRequest;
import com.worksystem.dto.response.CommonResponse;
import com.worksystem.entity.User;
import com.worksystem.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/teacher")
@Validated
public class TeacherController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 教师登录
     */
    @PostMapping("/login")
    public CommonResponse login(@RequestBody @Valid UserLoginRequest request) {
        log.info("教师登录请求: {}", request.getUsername());

        // 查询教师用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", request.getUsername())
                .eq("user_type", "teacher");
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            return CommonResponse.error(400, "用户名或密码错误");
        }

        if (!request.getPassword().equals(user.getPassword())) {
            return CommonResponse.error(400, "用户名或密码错误");
        }

        // 返回教师信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("name", user.getName());
        userInfo.put("college", user.getCollege());
        userInfo.put("mobile", user.getMobile());
        userInfo.put("email", user.getEmail());

        log.info("教师登录成功: {}", user.getUsername());
        return CommonResponse.success("登录成功", userInfo);
    }

    /**
     * 获取教师个人信息
     */
    @GetMapping("/profile/{id}")
    public CommonResponse getProfile(@PathVariable Long id) {
        User user = userMapper.selectById(id);
        if (user == null || !"teacher".equals(user.getUserType())) {
            return CommonResponse.error(404, "教师不存在");
        }

        Map<String, Object> profile = new HashMap<>();
        profile.put("userId", user.getId());
        profile.put("username", user.getUsername());
        profile.put("name", user.getName());
        profile.put("college", user.getCollege());
        profile.put("mobile", user.getMobile());
        profile.put("email", user.getEmail());

        return CommonResponse.success(profile);
    }

    /**
     * 测试接口
     */
    @GetMapping("/test")
    public CommonResponse test() {
        return CommonResponse.success("教师服务正常");
    }
}