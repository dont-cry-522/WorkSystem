-frontend/
├── public/                 # 静态资源（不会被编译）
│   ├── favicon.ico
│   └── logo.png
├── src/
│   ├── api/                # API请求封装
│   │   ├── index.js        # 请求拦截器配置
│   │   ├── student.js      # 学生相关接口
│   │   ├── teacher.js      # 教师相关接口
│   │   └── position.js     # 岗位相关接口
│   ├── assets/             # 静态资源（会被编译）
│   │   ├── css/            # 样式文件
│   │   ├── img/            # 图片资源
│   │   └── js/             # 通用JS
│   ├── components/         # 公共组件
│   │   ├── common/         # 基础组件（按钮、输入框等）
│   │   └── business/       # 业务组件（岗位卡片、考勤表等）
│   ├── router/             # 路由配置
│   │   ├── index.js        # 路由入口
│   │   ├── student.js      # 学生端路由
│   │   └── teacher.js      # 教师端路由
│   ├── store/              # 状态管理（Pinia）
│   │   ├── index.js        # 状态管理入口
│   │   ├── user.js         # 用户状态
│   │   └── position.js     # 岗位相关状态
│   ├── utils/              # 工具函数
│   │   ├── auth.js         # 认证相关
│   │   ├── date.js         # 日期处理
│   │   └── validator.js    # 表单验证
│   ├── views/              # 页面组件
│   │   ├── login/          # 登录页面
│   │   ├── student/        # 学生端页面
│   │   ├── teacher/        # 教师端页面
│   │   └── admin/          # 管理员页面
│   ├── App.vue             # 根组件
│   ├── main.js             # 入口文件
│   └── vite-env.d.ts       # Vite类型定义
├── .env.development        # 开发环境配置
├── .env.production         # 生产环境配置
├── index.html              # HTML入口
├── package.json            # 项目依赖
├── vite.config.js          # Vite配置
└── README.md               # 项目说明