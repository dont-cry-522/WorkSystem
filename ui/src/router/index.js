import { createRouter, createWebHistory } from 'vue-router'
// import studentRoutes from './student' // 暂未使用，可保留
// import teacherRoutes from './teacher' // 暂未使用，可保留

const routes = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/login/LoginPage.vue'),
        meta: { title: '登录' }
    },
    {
        path: '/student',
        name: 'Student',
        component: () => import('@/views/student/StudentLayout.vue'),
        meta: {
            requiresAuth: true, // 需要登录才能访问
            userType: 'student', // 限制仅学生可访问
            title: '学生中心'
        },
        children: [
            {
                path: 'home',
                name: 'StudentHome',
                component: () => import('@/views/student/HomePage.vue'),
                meta: { title: '学生首页' }
            },
            // 修正1：删除多余的点（path: 'positions' . → path: 'positions'）
            // 修正2：统一命名规范，补充meta信息
            {
                path: 'positions',
                name: 'PositionsList',
                component: () => import('@/views/student/PositionList.vue'),
                meta: { title: '岗位列表' }
            },
            {
                path: 'positions/:id',
                name: 'positionDetail',
                component: () => import('@/views/student/PositionDetail.vue'),
                meta: { title: '岗位详情' }
            },
            // 补充：我的申请/个人中心路由（适配HomePage.vue的跳转）
            {
                path: 'my-applications',
                name: 'StudentApplications',
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

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 路由守卫（优化后）
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    const userType = localStorage.getItem('userType') // 登录时需存储 userType: 'student'/'teacher'

    // 设置页面标题
    document.title = to.meta.title || '勤工俭学系统'

    // 1. 已登录访问登录页 → 重定向到对应角色首页
    if (to.path === '/login' && token && from.path !== '/') {
        next(userType === 'student' ? '/student/home' : '/teacher/home')
        return
    }

    // 2. 需要登录的页面 → 检查token
    if (to.meta.requiresAuth && !token) {
        next('/login')
        return
    }

    // 3. 核心补充：限制跨角色访问（学生不能进教师路由，反之亦然）
    if (to.meta.requiresAuth && token) {
        // 目标路由要求的用户类型 和 当前用户类型不匹配
        if (to.meta.userType && to.meta.userType !== userType) {
            // 重定向到当前用户的首页
            next(userType === 'student' ? '/student/home' : '/teacher/home')
            return
        }
    }

    next()
})

export default router