// controller/StudentController.java
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
@RequestMapping("/api/student")
@Validated
public class StudentController {

    @Autowired
    private UserMapper userMapper;


    @PostMapping("/login")
    public CommonResponse<Map<String, Object>> login(@RequestBody @Valid UserLoginRequest request) {
        try {
            log.info("学生登录请求: {}", request.getUsername());

            // 查询学生用户
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", request.getUsername())
                    .eq("user_type", "student");
            User user = userMapper.selectOne(queryWrapper);

            if (user == null) {
                log.warn("学生登录失败: 用户不存在 - {}", request.getUsername());
                return CommonResponse.error(400, "用户名或密码错误");
            }

            if (!request.getPassword().equals(user.getPassword())) {
                log.warn("学生登录失败: 密码错误 - {}", request.getUsername());
                return CommonResponse.error(400, "用户名或密码错误");
            }

            // 返回学生信息
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("name", user.getName());
            userInfo.put("college", user.getCollege());
            userInfo.put("mobile", user.getMobile());
            userInfo.put("email", user.getEmail());
            userInfo.put("introduction", user.getIntroduction());

            log.info("学生登录成功: {}", user.getUsername());
            return CommonResponse.success("登录成功", userInfo);

        } catch (Exception e) {
            log.error("学生登录异常: ", e);
            return CommonResponse.error(500, "登录失败: " + e.getMessage());
        }
    }

    /**
     * 获取学生个人信息
     */
    @GetMapping("/profile/{id}")
    public CommonResponse<Map<String, Object>> getProfile(@PathVariable Long id) {
        try {
            User user = userMapper.selectById(id);
            if (user == null || !"student".equals(user.getUserType())) {
                return CommonResponse.error(404, "学生不存在");
            }

            Map<String, Object> profile = new HashMap<>();
            profile.put("userId", user.getId());
            profile.put("username", user.getUsername());
            profile.put("name", user.getName());
            profile.put("college", user.getCollege());
            profile.put("mobile", user.getMobile());
            profile.put("email", user.getEmail());
            profile.put("introduction", user.getIntroduction());

            return CommonResponse.success(profile);

        } catch (Exception e) {
            log.error("获取学生信息异常: ", e);
            return CommonResponse.error(500, "获取信息失败");
        }
    }

    /**
     * 测试接口
     */
    @GetMapping("/test")
    public CommonResponse<String> test() {
        return CommonResponse.success("学生服务正常");
    }
}
