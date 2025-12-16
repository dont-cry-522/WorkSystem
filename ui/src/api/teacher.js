// src/api/teacher.js 最终正确版本
import request from './index' // 导入正确的 axios 实例

const teacherApi = {
    // 教师登录
    login(data) {
        return request.post('/api/teacher/login', data)
    },
    // 岗位管理
    position: {
        getList: (params) => request.get('/api/teacher/positions', { params }),
        getDetail: (id) => request.get(`/api/teacher/positions/${id}`),
        add: (data) => request.post('/api/teacher/positions', data),
        edit: (id, data) => request.put(`/api/teacher/positions/${id}`, data),
        delete: (id) => request.delete(`/api/teacher/positions/${id}`)
    },
    // 申请审核
    application: {
        getList: (params) => request.get('/api/teacher/applications', { params }),
        review: (id, data) => request.post(`/api/teacher/applications/${id}/review`, data)
    },
    // 个人中心
    profile: {
        getInfo: () => request.get('/api/teacher/profile'),
        updateInfo: (data) => request.put('/api/teacher/profile', data)
    }
}

export default teacherApi