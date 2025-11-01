import { createRouter, createWebHistory } from 'vue-router'
// 可以在这里引入各个页面组件，示例如下
// import LoginView from '../views/LoginView.vue'
// import StudentView from '../views/StudentView.vue'
// import TeacherView from '../views/TeacherView.vue'

const routes = [
    // {
    //   path: '/login',
    //   name: 'login',
    //   component: LoginView
    // },
    // {
    //   path: '/student',
    //   name: 'student',
    //   component: StudentView
    // },
    // {
    //   path: '/teacher',
    //   name: 'teacher',
    //   component: TeacherView
    // },
    // 可以根据实际页面需求添加更多路由
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router