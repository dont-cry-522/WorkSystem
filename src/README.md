WorkSystem/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── WorkSystem/
│   │   │           ├── Application.java  # 启动类
│   │   │           ├── config/               # 配置类
│   │   │           │   ├── MyBatisPlusConfig.java  # MyBatis-Plus配置
│   │   │           │   ├── SwaggerConfig.java      # Swagger配置
│   │   │           │   ├── WebMvcConfig.java       # MVC配置
│   │   │           │   └── SecurityConfig.java     # 安全配置（JWT等）
│   │   │           ├── controller/            # 控制器层
│   │   │           │   ├── StudentController.java
│   │   │           │   ├── TeacherController.java
│   │   │           │   ├── PositionController.java
│   │   │           │   └── CommonController.java   # 公共接口
│   │   │           ├──dto/
                       ├── request/                # 请求参数DTO
│                      ├── UserLoginRequest.java    # 用户登录请求参数
│                      ├── UserRegisterRequest.java # 用户注册请求参数
│                      └── JobApplyRequest.java     # 岗位申请请求参数
                    │
│   │   │              ├── response/               # 响应结果DTO
                       ├── UserInfoResponse.java    # 用户信息响应
                       ├── JobListResponse.java     # 岗位列表响应
                       ├──  CommonResponse.java      # 通用响应（如成功/失败提示）         # 响应结果DTO
│   │   │           ├── entity/                # 实体类（与数据库表对应）
│   │   │           │   ├── Student.java
│   │   │           │   ├── Teacher.java
│   │   │           │   ├── Position.java
│   │   │           │   └── Attendance.java    # 考勤记录
│   │   │           ├── mapper/                # Mapper接口（MyBatis-Plus）
│   │   │           │   ├── StudentMapper.java
│   │   │           │   ├── TeacherMapper.java
│   │   │           │   └── PositionMapper.java
│   │   │           ├── service/               # 服务层
│   │   │           │   ├── impl/              # 服务实现类
│   │   │           │   ├── StudentService.java
│   │   │           │   ├── TeacherService.java
│   │   │           │   └── PositionService.java
│   │   │           ├── exception/             # 异常处理
│   │   │           │   ├── GlobalExceptionHandler.java  # 全局异常处理器
│   │   │           │   └── BusinessException.java       # 业务异常
│   │   │           └── util/                  # 工具类
│   │   │               ├── JwtUtil.java       # JWT工具
│   │   │               └── ResultUtil.java    # 响应结果工具
│   │   └── resources/
│   │       ├── application.yml               # 主配置文件
│   │       ├── application-dev.yml           # 开发环境配置
│   │       ├── application-prod.yml          # 生产环境配置
│   │       └── mapper/                       # MyBatis映射文件（如需）
│   └── test/                                  # 测试类
│       └── java/
│           └── com/
│               └── WorkSystem/
│                   ├── controller/
│                   └── service/
├── pom.xml                                    # Maven依赖配置
└── README.md                                   # 项目说明