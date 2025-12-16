// src/api/position.js
import request from './index'

export const positionApi = {
    // 获取岗位列表
    getPositionList() {
        return request.get('/api/positions')
    },

    // 获取岗位详情
    getPositionDetail(id) {
        return request.get(`/api/positions/${id}`)
    }
}