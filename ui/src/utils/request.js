// src/utils/request.js
import axios from 'axios'

// 1. 创建 axios 实例
const request = axios.create({
    // 接口基地址（根据你的后端地址修改，也可以通过环境变量配置）
    baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
    // 请求超时时间
    timeout: 10000,
    // 请求头默认配置
    headers: {
        'Content-Type': 'application/json;charset=utf-8'
    }
})

// 2. 请求拦截器（添加 token、处理请求前逻辑）
request.interceptors.request.use(
    (config) => {
        // 从 localStorage 获取 token，添加到请求头（和你的登录逻辑匹配）
        const token = localStorage.getItem('token')
        if (token) {
            config.headers.Authorization = `Bearer ${token}` // 后端常用的 token 格式
        }
        return config
    },
    (error) => {
        // 请求错误预处理
        return Promise.reject(error)
    }
)

// 3. 响应拦截器（统一处理返回结果、错误提示）
request.interceptors.response.use(
    (response) => {
        // 只返回响应体的 data 部分（简化业务层调用）
        const res = response.data
        // 示例：统一处理后端返回的错误码（根据你的后端规则调整）
        if (res.code !== 200) {
            // 可引入 Element Plus/Message 做错误提示
            // ElMessage.error(res.msg || '请求失败')
            return Promise.reject(res)
        }
        return res
    },
    (error) => {
        // 网络错误/超时等处理
        // ElMessage.error(error.message || '网络异常，请稍后重试')
        return Promise.reject(error)
    }
)

// 导出 request 实例
export default request