// controller/StudentController.java
package com.worksystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.worksystem.dto.request.UserLoginRequest;
import com.worksystem.dto.response.CommonResponse;
import com.worksystem.entity.Position;
import com.worksystem.entity.PositionApplication;
import com.worksystem.entity.User;
import com.worksystem.mapper.UserMapper;
import com.worksystem.mapper.PositionMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.worksystem.mapper.PositionApplicationMapper;




import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/student")
@Validated
public class StudentController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PositionMapper positionMapper;
    @Autowired
    private PositionApplicationMapper positionApplicationMapper;

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
     * 更新学生个人信息（新增：处理前端保存个人信息的请求）
     */
    /**
     * 更新学生个人信息（修复空指针版）
     */
    @PutMapping("/profile/{id}")
    public CommonResponse updateProfile(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> profileData) { // 允许参数为null
        try {
            log.info("更新学生{}的个人信息: {}", id, profileData);

            // 1. 校验学生是否存在（核心：student为null直接返回）
            User student = userMapper.selectById(id);
            if (student == null || !"student".equals(student.getUserType())) {
                log.warn("学生{}不存在或非学生类型", id);
                return CommonResponse.error(404, "学生不存在");
            }

            // 2. 校验profileData是否为null（避免空指针）
            if (profileData == null) {
                log.warn("学生{}的更新参数为空", id);
                return CommonResponse.error(400, "更新参数不能为空");
            }

            // 3. 安全更新字段（先判空，再调用toString，避免空指针）
            if (profileData.containsKey("name") && profileData.get("name") != null) {
                student.setName(profileData.get("name").toString());
            }
            if (profileData.containsKey("mobile") && profileData.get("mobile") != null) {
                student.setMobile(profileData.get("mobile").toString());
            }
            if (profileData.containsKey("email") && profileData.get("email") != null) {
                student.setEmail(profileData.get("email").toString());
            }
            if (profileData.containsKey("college") && profileData.get("college") != null) {
                student.setCollege(profileData.get("college").toString());
            }
            if (profileData.containsKey("introduction") && profileData.get("introduction") != null) {
                // 先确认User实体类有introduction字段和setIntroduction方法
                student.setIntroduction(profileData.get("introduction").toString());
            }

            // 4. 执行数据库更新
            int updateResult = userMapper.updateById(student);
            if (updateResult > 0) {
                log.info("学生{}个人信息更新成功", id);
                return CommonResponse.success("个人信息保存成功");
            } else {
                log.warn("学生{}个人信息更新失败（数据库无变更）", id);
                return CommonResponse.error(500, "个人信息保存失败（无数据变更）");
            }

        } catch (Exception e) {
            log.error("更新学生{}个人信息异常", id, e); // 打印完整堆栈，定位具体行
            return CommonResponse.error(500, "保存失败: " + e.getMessage());
        }
    }
    // controller/StudentController.java 新增方法
    /**
     * 申请岗位
     */

    @PostMapping("/{studentId}/apply-position/{positionId}")
    public CommonResponse applyPosition(
            @PathVariable Long studentId,
            @PathVariable Long positionId) {

        try {
            log.info("学生 {} 申请岗位 {}", studentId, positionId);

            // 1. 检查学生是否存在
            User student = userMapper.selectById(studentId);
            if (student == null || !"student".equals(student.getUserType())) {
                return CommonResponse.error(400, "学生不存在");
            }

            // 2. 检查岗位是否存在且正在招聘
            // 修正：用注入的实例调用，而非类名
            Position position = (Position) positionMapper.selectById(positionId);
            if (position == null) {
                return CommonResponse.error(400, "岗位不存在");
            }
            if (!"recruiting".equals(position.getRecruitmentStatus())) {
                return CommonResponse.error(400, "该岗位已停止招聘");
            }

            // 3. 检查是否已经申请过
            QueryWrapper<PositionApplication> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("student_id", studentId)
                    .eq("position_id", positionId);
            // 修正：用注入的实例调用selectCount
            Long count = positionApplicationMapper.selectCount(queryWrapper);
            if (count > 0) {
                return CommonResponse.error(400, "您已经申请过该岗位");
            }

            // 4. 创建申请记录
            PositionApplication application = new PositionApplication();
            application.setStudentId(studentId);
            application.setPositionId(positionId);
            application.setApplicationStatus("pending");
            application.setApplicationTime(LocalDateTime.now());

            // 修正：用注入的实例调用insert
            int result = positionApplicationMapper.insert(application);
            if (result > 0) {
                log.info("申请成功: 学生 {} -> 岗位 {}", studentId, positionId);
                return CommonResponse.success("申请成功，等待审核");
            } else {
                return CommonResponse.error(500, "申请失败");
            }

        } catch (Exception e) {
            log.error("申请岗位异常: 学生{} 岗位{}", studentId, positionId, e); // 补充上下文，便于排查
            return CommonResponse.error(500, "申请失败: " + e.getMessage());
        }
    }

}
