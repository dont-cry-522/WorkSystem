# WorkSystem — 高校勤工助学管理系统

> 基于 Spring Boot + Vue 的前后端分离勤工助学管理平台，支持学生岗位申请、教师审批、考勤打卡、JWT 认证等完整业务流程。

[![Java](https://img.shields.io/badge/Java-17+-blue)](https://www.java.com) [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)](https://spring.io/projects/spring-boot) [![Vue](https://img.shields.io/badge/Vue-3.x-brightgreen)](https://vuejs.org) [![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.x-orange)](https://baomidou.com) [![License](https://img.shields.io/badge/License-MIT-yellow)](./LICENSE)

## 项目简介

**WorkSystem** 面向高校勤工助学场景，实现学生、教师、岗位、考勤的全流程数字化管理。前后端完全分离，后端提供 RESTful API + JWT 认证，前端基于 Vue + Element Plus 构建 SPA 应用。

## 核心功能

| 模块 | 说明 |
|------|------|
| 用户认证 | JWT 登录/注册，学生/教师角色区分 |
| 岗位管理 | 岗位发布、编辑、下架（教师端）；岗位浏览与搜索（学生端） |
| 申请管理 | 学生提交岗位申请，教师审核（通过/驳回） |
| 考勤打卡 | 签到签退、考勤记录查询、工时统计 |
| 权限控制 | Spring Security + JWT，接口级角色隔离 |
| 接口文档 | Swagger / Knife4j 自动生成 API 文档 |

## 技术栈

| 层 | 技术 |
|------|------|
| 后端 | Java 17 + Spring Boot 3.x + MyBatis-Plus |
| 前端 | Vue 3.x + Element Plus + Axios |
| 安全 | Spring Security + JWT |
| 数据库 | MySQL |
| 接口文档 | Swagger / Knife4j |
| 构建工具 | Maven（后端）+ Vite（前端） |

## 项目结构

```
WorkSystem/
├── src/main/java/com/worksystem/
│   ├── config/          # Spring Security、MyBatis-Plus、CORS、Swagger 配置
│   ├── controller/      # Common/Student/Teacher/PositionController
│   ├── dto/             # 请求/响应 DTO
│   ├── entity/          # User/Position/PositionApplication/Attendance
│   ├── mapper/          # MyBatis Mapper 接口
│   ├── service/         # 业务层（接口 + 实现）
│   ├── exception/       # 全局异常处理
│   └── util/            # JwtUtil、ResultUtil
├── ui/                  # Vue 前端项目
│   ├── src/             # Vue 组件与页面
│   └── package.json     # 前端依赖
└── src/main/resources/
    ├── application.yml       # 主配置
    └── application-dev.yml   # 开发环境配置
```

## 快速开始

```bash
# 1. 初始化数据库
# 执行 src/main/java/com/worksystem/config/WorkSystem.sql

# 2. 启动后端
mvn spring-boot:run

# 3. 启动前端
cd ui && npm install && npm run dev

# 4. 访问
# 前端 http://localhost:5173
# 接口文档 http://localhost:8080/swagger-ui.html
```

## License

MIT
