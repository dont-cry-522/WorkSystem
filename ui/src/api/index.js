// src/api/index.js 正确版本（请求封装核心）
import axios from 'axios'

// 创建 axios 实例
const request = axios.create({
    // 接口基地址（根据你的后端地址修改，也可通过环境变量配置）
    baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
    // 请求超时时间
    timeout: 10000,
    // 默认请求头
    headers: {
        'Content-Type': 'application/json;charset=utf-8'
    }
})

// 请求拦截器：添加 token（登录后自动携带）
request.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token')
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    (error) => Promise.reject(error)
)

// 响应拦截器：统一处理返回结果
request.interceptors.response.use(
    (response) => {
        // 只返回响应体的 data 部分，简化业务层调用
        return response.data
    },
    (error) => {
        // 统一错误提示（可结合 Element Plus 等 UI 库）
        console.error('请求失败：', error.message || '网络异常')
        return Promise.reject(error)
    }
)

// 导出 axios 实例（供 student.js/teacher.js 导入使用）
export default request