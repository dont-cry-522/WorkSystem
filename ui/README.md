ui
├── public/                   # 静态资源（不被Webpack处理）
│   ├── logo.png
│   └── vite.svg
├── src/
│   ├── api/                  # 接口请求封装
│   │   ├── index.js
│   │   ├── position.js
│   │   ├── student.js
│   │   └── teacher.js
│   ├── assets/               # 静态资源（被Webpack处理）
│   │   └── css/
│   ├── components/           # 公共组件
│   │   ├── business/         # 业务组件
│   │   └── common/           # 通用组件（如Login.vue）
│   ├── router/               # 路由配置
│   │   ├── index.js
│   │   ├── student.js
│   │   └── teacher.js
│   ├── utils/                # 工具类
│   │   └── request.js        # axios封装
│   ├── views/                # 页面级组件
│   │   ├── error/            # 错误页面
│   │   │   └── 404.vue
│   │   ├── login/            # 登录页
│   │   │   └── LoginPage.vue
│   │   ├── student/          # 学生模块页面
│   │   │   ├── HomePage.vue
│   │   │   ├── MyApplication.vue
│   │   │   ├── PositionDetail.vue
│   │   │   ├── PositionList.vue
│   │   │   ├── ProfilePage.vue
│   │   │   └── StudentLayout.vue # 学生布局组件
│   │   └── teacher/          # 教师模块页面
│   │       ├── ApplicationReview.vue
│   │       ├── HomePage.vue
│   │       ├── PositionDetail.vue
│   │       ├── PositionManage.vue
│   │       ├── ProfilePage.vue
│   │       ├── StudentManage.vue
│   │       └── TeacherLayout.vue # 教师布局组件
│   ├── App.vue               # 根组件
│   └── main.js               # 项目入口文件
├── .env.development          # 开发环境变量
├── .env.production           # 生产环境变量
├── index.html                # 入口HTML
├── package.json              # 依赖配置
└── vite.config.js            # Vite配置文件