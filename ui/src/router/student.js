// src/router/student.js

const routes = [
    {
        path: '/student',
        name: 'Student',
        component: () => import('@/views/student/StudentLayout.vue'),
        meta: { requiresAuth: true, userType: 'student' },
        children: [
            {
                path: 'home',
                name: 'StudentHome',
                component: () => import('@/views/student/HomePage.vue'),
                meta: { title: '首页' },
                requiresAuth: true, // 需登录才能访问
                userType: 'student'
            },
            {
                path: 'positions',
                name: 'PositionList',
                component: () => import('@/views/student/PositionList.vue'),
                meta: { title: '岗位列表' }
            },
            {
                path: 'positions/:id',
                name: 'PositionDetail',
                component: () => import('@/views/student/PositionDetail.vue'),
                meta: { title: '岗位详情' }
            },
            {
                path: 'my-applications',
                name: 'MyApplications',
                component: () => import('@/views/student/MyApplications.vue'),
                meta: { title: '我的申请' }
            },
            {
                path: 'profile',
                name: 'StudentProfile',
                component: () => import('@/views/student/ProfilePage.vue'),
                meta: { title: '个人中心' }
            }
        ]
    }
]

export default routes