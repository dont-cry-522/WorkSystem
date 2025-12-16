// src/api/student.js 最终正确版本
// 现在导入的是 api/index.js 里的 axios 实例，而非错误的接口代码
import axios from 'axios'
import request from '@/api/index'

export const studentApi = {
    // 学生登录
    login(data) {
        return request.post('/api/student/login', data)
    },
    // 获取学生信息
    getProfile(id) {
        return request.get(`/api/student/profile/${id}`)
    },
    // 申请岗位
    applyPosition(studentId, positionId) {
        return request.post(`/api/student/${studentId}/apply-position/${positionId}`)
    },
    // 获取我的申请记录
    getMyApplications(studentId) {
        return request.get(`/api/student/${studentId}/my-applications`)
    },
    // 更新个人信息
    updateProfile(id, data) {
        return request.put(`/api/student/profile/${id}`, data)
    }
}

// 统一默认导出（匹配 LoginPage.vue 的 import studentApi from '@/api/student'）
export default {
    login(data) {
        return axios.post('/api/student/login', data)
    }
}