// src/router/teacher.js
const routes = [
    {
        path: '/teacher', // 教师端根路径（对应学生端 /student）
        name: 'Teacher', // 路由名称（对应学生端 Student）
        component: () => import('@/views/teacher/TeacherLayout.vue'), // 教师端布局组件（类比学生端 StudentLayout）
        meta: { requiresAuth: true, userType: 'teacher' }, // 鉴权标识（用户类型改为 teacher）
        children: [
            // 1. 教师首页（对应学生端 HomePage）
            {
                path: 'home',
                name: 'TeacherHome',
                component: () => import('@/views/teacher/HomePage.vue'),
                meta: { title: '教师首页' }
            },
            // 2. 岗位管理（教师核心功能：发布/编辑/查看岗位，对应学生端 PositionList）
            {
                path: 'position-manage',
                name: 'PositionManage',
                component: () => import('@/views/teacher/PositionManage.vue'),
                meta: { title: '岗位管理' }
            },
            // 3. 岗位详情（教师视角：查看岗位+申请列表，对应学生端 PositionDetail）
            {
                path: 'position-manage/:id',
                name: 'TeacherPositionDetail',
                component: () => import('@/views/teacher/PositionDetail.vue'),
                meta: { title: '岗位详情' }
            },
            // 4. 申请审核（教师核心功能：审核学生的岗位申请，对应学生端 MyApplications）
            {
                path: 'application-review',
                name: 'ApplicationReview',
                component: () => import('@/views/teacher/ApplicationReview.vue'),
                meta: { title: '申请审核' }
            },
            // 5. 学生管理（可选：教师查看申请的学生信息）
            {
                path: 'student-manage',
                name: 'StudentManage',
                component: () => import('@/views/teacher/StudentManage.vue'),
                meta: { title: '学生管理' }
            },
            // 6. 教师个人中心（对应学生端 ProfilePage）
            {
                path: 'profile',
                name: 'TeacherProfile',
                component: () => import('@/views/teacher/ProfilePage.vue'),
                meta: { title: '个人中心' }
            }
        ]
    }
]

export default routes