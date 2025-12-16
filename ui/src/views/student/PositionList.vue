<!-- src/views/student/PositionList.vue -->
<template>
  <div class="position-list">
    <!-- 搜索和筛选区域 -->
    <div class="filter-section">
      <el-input
          v-model="searchKeyword"
          placeholder="搜索岗位名称或公司"
          class="search-input"
          @keyup.enter="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>

      <el-select v-model="filterStatus" placeholder="招聘状态" class="status-select">
        <el-option label="全部" value="all" />
        <el-option label="招聘中" value="recruiting" />
        <el-option label="已满员" value="full" />
        <el-option label="已关闭" value="closed" />
      </el-select>

      <el-button type="primary" @click="handleSearch">
        搜索
      </el-button>
      <el-button @click="handleReset">
        重置
      </el-button>
    </div>

    <!-- 岗位列表 -->
    <div class="position-grid">
      <div
          v-for="position in positions"
          :key="position.id"
          class="position-card"
      >
        <el-card shadow="hover" @click="viewPositionDetail(position.id)">
          <div class="card-header">
            <span class="position-name">{{ position.positionName }}</span>
            <el-tag
                :type="getStatusTagType(position.recruitmentStatus)"
                size="small"
            >
              {{ getStatusText(position.recruitmentStatus) }}
            </el-tag>
          </div>

          <div class="card-content">
            <div class="info-item">
              <el-icon><OfficeBuilding /></el-icon>
              <span class="info-text">{{ position.company || '学校' }}</span>
            </div>

            <div class="info-item">
              <el-icon><Money /></el-icon>
              <span class="info-text">{{ position.salary }} 元/小时</span>
            </div>

            <div class="info-item">
              <el-icon><Clock /></el-icon>
              <span class="info-text">{{ position.workTime || '时间灵活' }}</span>
            </div>
          </div>

          <div class="card-footer">
            <el-button
                type="primary"
                size="small"
                :disabled="position.recruitmentStatus !== 'recruiting'"
                @click.stop="handleApply(position)"
            >
              立即申请
            </el-button>

            <el-button
                type="info"
                size="small"
                @click.stop="viewPositionDetail(position.id)"
            >
              查看详情
            </el-button>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading">
      <el-icon class="loading-icon"><Loading /></el-icon>
      <span>加载中...</span>
    </div>

    <!-- 空状态 -->
    <div v-if="!loading && positions.length === 0" class="empty-state">
      <el-empty description="暂无岗位数据" />
    </div>

    <!-- 分页 -->
    <div v-if="positions.length > 0" class="pagination">
      <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Search,
  OfficeBuilding,
  Money,
  Clock,
  Loading
} from '@element-plus/icons-vue'
import { positionApi } from '@/api/position'
import { studentApi } from '@/api/student'

const router = useRouter()

// 响应式数据
const positions = ref([])
const loading = ref(false)
const searchKeyword = ref('')
const filterStatus = ref('all')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 获取当前用户ID
const currentUser = JSON.parse(localStorage.getItem('userInfo') || '{}')
const studentId = currentUser.userId

// 状态标签类型
const getStatusTagType = (status) => {
  const map = {
    'recruiting': 'success',
    'full': 'warning',
    'closed': 'info'
  }
  return map[status] || 'info'
}

// 状态显示文本
const getStatusText = (status) => {
  const map = {
    'recruiting': '招聘中',
    'full': '已满员',
    'closed': '已关闭'
  }
  return map[status] || status
}

// 获取岗位列表
const fetchPositions = async () => {
  try {
    loading.value = true

    const response = await positionApi.getPositionList()
    console.log('岗位列表响应:', response)

    if (response.code === 200) {
      positions.value = response.data || []
      total.value = positions.value.length

      // 应用筛选
      applyFilters()
    } else {
      ElMessage.error('获取岗位列表失败: ' + response.message)
    }
  } catch (error) {
    console.error('获取岗位列表异常:', error)
    ElMessage.error('获取岗位列表失败')

    // 测试数据
    positions.value = [
      {
        id: 1,
        positionName: '图书馆管理员',
        company: '学校图书馆',
        salary: 20.0,
        workTime: '周一至周五 14:00-18:00',
        recruitmentStatus: 'recruiting',
        workContent: '整理图书、维护阅览室秩序、协助借还书'
      },
      {
        id: 2,
        positionName: '实验室助理',
        company: '计算机实验室',
        salary: 25.0,
        workTime: '周二、周四 15:00-17:00',
        recruitmentStatus: 'recruiting',
        workContent: '维护实验设备、准备实验材料、保持实验室整洁'
      },
      {
        id: 3,
        positionName: '办公室助理',
        company: '教务处',
        salary: 18.0,
        workTime: '周一至周五 9:00-12:00',
        recruitmentStatus: 'recruiting',
        workContent: '文件整理、资料录入、接待来访'
      }
    ]
    total.value = positions.value.length
  } finally {
    loading.value = false
  }
}

// 应用筛选
const applyFilters = () => {
  let filtered = positions.value

  // 按状态筛选
  if (filterStatus.value !== 'all') {
    filtered = filtered.filter(item => item.recruitmentStatus === filterStatus.value)
  }

  // 按关键词搜索
  if (searchKeyword.value.trim()) {
    const keyword = searchKeyword.value.toLowerCase()
    filtered = filtered.filter(item =>
        item.positionName.toLowerCase().includes(keyword) ||
        (item.company && item.company.toLowerCase().includes(keyword)) ||
        (item.workContent && item.workContent.toLowerCase().includes(keyword))
    )
  }

  positions.value = filtered
  total.value = positions.value.length
}

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1
  fetchPositions()
}

// 重置筛选
const handleReset = () => {
  searchKeyword.value = ''
  filterStatus.value = 'all'
  currentPage.value = 1
  fetchPositions()
}

// 查看岗位详情
const viewPositionDetail = (positionId) => {
  router.push(`/student/positions/${positionId}`)
}

// 申请岗位
const handleApply = async (position) => {
  try {
    // 确认申请
    await ElMessageBox.confirm(
        `确定要申请"${position.positionName}"岗位吗？`,
        '确认申请',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
    )

    // 调用申请接口
    const response = await studentApi.applyPosition(studentId, position.id)

    if (response.code === 200) {
      ElMessage.success('申请成功，等待审核')

      // 更新岗位状态
      const index = positions.value.findIndex(p => p.id === position.id)
      if (index !== -1) {
        positions.value[index].recruitmentStatus = 'full' // 或者其他状态
      }
    } else {
      ElMessage.error('申请失败: ' + response.message)
    }

  } catch (error) {
    if (error !== 'cancel') {
      console.error('申请岗位异常:', error)
      ElMessage.error('申请失败')
    }
  }
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchPositions()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchPositions()
}

// 组件挂载时加载数据
onMounted(() => {
  console.log('PositionList组件加载，用户ID:', studentId)
  fetchPositions()
})
</script>

<style scoped>
.position-list {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.filter-section {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  align-items: center;
  flex-wrap: wrap;
}

.search-input {
  width: 300px;
}

.status-select {
  width: 150px;
}

.position-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.position-card {
  cursor: pointer;
  transition: transform 0.3s;
}

.position-card:hover {
  transform: translateY(-5px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.position-name {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  flex: 1;
  margin-right: 10px;
}

.card-content {
  margin-bottom: 20px;
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  color: #666;
}

.info-item .el-icon {
  margin-right: 8px;
  color: #409eff;
}

.info-text {
  font-size: 14px;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
}

.loading {
  text-align: center;
  padding: 40px;
  color: #999;
}

.loading-icon {
  margin-right: 10px;
  animation: rotate 1s linear infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.empty-state {
  text-align: center;
  padding: 60px 0;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}
</style>