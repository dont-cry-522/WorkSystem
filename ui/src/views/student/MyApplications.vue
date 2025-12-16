<template>
  <div class="my-applications">
    <div class="page-header">
      <h2>我的申请记录</h2>
      <el-button type="primary" @click="refreshList">
        <el-icon><Refresh /></el-icon>
        刷新
      </el-button>
    </div>

    <!-- 状态筛选：新增已取消选项 -->
    <div class="status-filter">
      <el-radio-group v-model="activeStatus" @change="filterApplications">
        <el-radio-button label="all">全部</el-radio-button>
        <el-radio-button label="pending">待审核</el-radio-button>
        <el-radio-button label="approved">已通过</el-radio-button>
        <el-radio-button label="rejected">已拒绝</el-radio-button>
        <el-radio-button label="cancelled">已取消</el-radio-button>
      </el-radio-group>
    </div>

    <!-- 申请列表 -->
    <div class="applications-list">
      <el-table :data="filteredApplications" style="width: 100%">
        <el-table-column prop="positionName" label="岗位名称" width="200">
          <template #default="{ row }">
            <div class="position-info">
              <div class="position-name">{{ row.positionName }}</div>
              <div class="company">{{ row.company || '学校' }}</div>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="applicationTime" label="申请时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.applicationTime) }}
          </template>
        </el-table-column>

        <el-table-column prop="salary" label="薪资" width="120">
          <template #default="{ row }">
            {{ row.salary }} 元/小时
          </template>
        </el-table-column>

        <el-table-column prop="applicationStatus" label="申请状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.applicationStatus)" size="small">
              {{ getStatusText(row.applicationStatus) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="workTime" label="工作时间" width="150">
          <template #default="{ row }">
            {{ row.workTime || '时间灵活' }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
                type="primary"
                size="small"
                @click="viewPositionDetail(row.positionId)"
            >
              查看岗位
            </el-button>
            <el-button
                v-if="row.applicationStatus === 'pending'"
                type="danger"
                size="small"
                @click="handleCancel(row.id)"
            >
              取消申请
            </el-button>
            <!-- 新增：已取消状态显示重新申请按钮 -->
            <el-button
                v-if="row.applicationStatus === 'cancelled'"
                type="success"
                size="small"
                @click="handleReApply(row.id)"
            >
              重新申请
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading">
      <el-icon class="loading-icon"><Loading /></el-icon>
      <span>加载中...</span>
    </div>

    <!-- 空状态 -->
    <div v-if="!loading && filteredApplications.length === 0" class="empty-state">
      <el-empty :description="emptyDescription" />
    </div>

    <!-- 分页 -->
    <div v-if="filteredApplications.length > 0" class="pagination">
      <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          layout="total, prev, pager, next"
          :total="total"
          @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Loading } from '@element-plus/icons-vue'
import { studentApi } from '@/api/student'

const router = useRouter()

// 响应式数据
const applications = ref([])
const filteredApplications = ref([])
const loading = ref(false)
const activeStatus = ref('all')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 获取当前用户ID
const currentUser = JSON.parse(localStorage.getItem('userInfo') || '{}')
const studentId = currentUser.userId

// 空状态描述：新增已取消的空状态提示
const emptyDescription = computed(() => {
  if (activeStatus.value === 'all') return '暂无申请记录'
  if (activeStatus.value === 'pending') return '暂无待审核的申请'
  if (activeStatus.value === 'approved') return '暂无已通过的申请'
  if (activeStatus.value === 'rejected') return '暂无被拒绝的申请'
  if (activeStatus.value === 'cancelled') return '暂无已取消的申请' // 新增
  return '暂无数据'
})

// 状态标签类型
const getStatusTagType = (status) => {
  const map = {
    'pending': 'warning',
    'approved': 'success',
    'rejected': 'danger',
    'cancelled': 'info'// 新增：已取消用info标签
  }
  return map[status] || 'info'
}

// 状态显示文本
const getStatusText = (status) => {
  const map = {
    'pending': '待审核',
    'approved': '已通过',
    'rejected': '已拒绝',
    'cancelled': '已取消' // 新增：已取消文本
  }
  return map[status] || '未申请' // 空状态显示未申请
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '-'
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 获取申请记录
const fetchApplications = async () => {
  try {
    loading.value = true

    // ===== 核心：优先读取本地持久化的申请数据 =====
    const persistedApps = localStorage.getItem(`student_applications_${studentId}`)
    if (persistedApps) {
      applications.value = JSON.parse(persistedApps)
      filterApplications()
      total.value = applications.value.length
      loading.value = false
      console.log('从本地缓存加载申请记录:', applications.value)
      return
    }

    // 原接口请求逻辑（无接口时走下面的测试数据）
    const response = await studentApi.getMyApplications(studentId)
    console.log('申请记录响应:', response)

    if (response.code === 200) {
      applications.value = response.data || []
      // 持久化接口返回的数据
      localStorage.setItem(`student_applications_${studentId}`, JSON.stringify(applications.value))
    } else {
      ElMessage.error('获取申请记录失败: ' + response.message)
      // 接口失败时用测试数据并持久化
      applications.value = [
        {
          id: 1,
          positionId: 1,
          positionName: '图书馆管理员',
          company: '学校图书馆',
          salary: 20.0,
          workTime: '周一至周五 14:00-18:00',
          applicationTime: '2025-11-01 10:30:00',
          applicationStatus: 'pending'
        },
        {
          id: 2,
          positionId: 2,
          positionName: '实验室助理',
          company: '计算机实验室',
          salary: 25.0,
          workTime: '周二、周四 15:00-17:00',
          applicationTime: '2025-10-28 14:20:00',
          applicationStatus: 'pending'
        },
        {
          id: 3,
          positionId: 3,
          positionName: '办公室助理',
          company: '教务处',
          salary: 18.0,
          workTime: '周一至周五 9:00-12:00',
          applicationTime: '2024-10-25 09:15:00',
          applicationStatus: 'pending'
        }
      ]
      localStorage.setItem(`student_applications_${studentId}`, JSON.stringify(applications.value))
    }
    filterApplications()
    total.value = applications.value.length
  } catch (error) {
    console.error('获取申请记录异常:', error)
    ElMessage.error('获取申请记录失败')

    // 异常时也读取本地数据（无则初始化）
    const persistedApps = localStorage.getItem(`student_applications_${studentId}`)
    if (persistedApps) {
      applications.value = JSON.parse(persistedApps)
    } else {
      applications.value = [
        {
          id: 1,
          positionId: 1,
          positionName: '图书馆管理员',
          company: '学校图书馆',
          salary: 20.0,
          workTime: '周一至周五 14:00-18:00',
          applicationTime: '2025-11-01 10:30:00',
          applicationStatus: 'pending'
        },
        {
          id: 2,
          positionId: 2,
          positionName: '实验室助理',
          company: '计算机实验室',
          salary: 25.0,
          workTime: '周二、周四 15:00-17:00',
          applicationTime: '2025-10-28 14:20:00',
          applicationStatus: 'pending'
        },
        {
          id: 3,
          positionId: 3,
          positionName: '办公室助理',
          company: '教务处',
          salary: 18.0,
          workTime: '周一至周五 9:00-12:00',
          applicationTime: '2024-10-25 09:15:00',
          applicationStatus: 'pending'
        }
      ]
      localStorage.setItem(`student_applications_${studentId}`, JSON.stringify(applications.value))
    }
    filterApplications()
    total.value = applications.value.length
  } finally {
    loading.value = false
  }
}

// 筛选申请记录：修复筛选逻辑，包含已取消状态
const filterApplications = () => {
  if (activeStatus.value === 'all') {
    filteredApplications.value = [...applications.value] // 深拷贝避免引用问题
  } else {
    filteredApplications.value = applications.value.filter(
        app => app.applicationStatus === activeStatus.value
    )
  }
  total.value = filteredApplications.value.length
  console.log('筛选后数据:', filteredApplications.value, '当前状态:', activeStatus.value)
}

// 刷新列表：强制重新读取本地数据，不是只刷新筛选
const refreshList = () => {
  currentPage.value = 1
  fetchApplications() // 恢复真正的刷新，重新读取localStorage
}

// 查看岗位详情
const viewPositionDetail = (positionId) => {
  router.push(`/student/positions/${positionId}`)
}

// 取消申请逻辑修改（同步持久化到localStorage）
const handleCancel = async (applicationId) => {
  try {
    await ElMessageBox.confirm(
        '确定要取消这个申请吗？',
        '确认取消',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
    )

    // 模拟接口成功
    ElMessage.success('取消申请成功')

    // 更新本地数据 + 持久化
    const index = applications.value.findIndex(app => app.id === applicationId)
    if (index !== -1) {
      applications.value[index].applicationStatus = 'cancelled'
      // ===== 关键：同步到localStorage =====
      localStorage.setItem(`student_applications_${studentId}`, JSON.stringify(applications.value))
      filterApplications() // 立即刷新筛选结果
      console.log('取消申请后数据:', applications.value)
    }

  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消申请异常:', error)
      ElMessage.error('取消申请失败')
    }
  }
}

// 新增：重新申请逻辑
const handleReApply = async (applicationId) => {
  try {
    await ElMessageBox.confirm(
        '确定要重新申请这个岗位吗？',
        '确认重新申请',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        }
    )

    // 模拟接口成功
    ElMessage.success('重新申请成功，等待审核')

    // 更新本地数据 + 持久化
    const index = applications.value.findIndex(app => app.id === applicationId)
    if (index !== -1) {
      applications.value[index].applicationStatus = 'pending'
      applications.value[index].applicationTime = new Date().toISOString() // 更新申请时间
      // 同步到localStorage
      localStorage.setItem(`student_applications_${studentId}`, JSON.stringify(applications.value))
      filterApplications() // 立即刷新筛选结果
    }

  } catch (error) {
    if (error !== 'cancel') {
      console.error('重新申请异常:', error)
      ElMessage.error('重新申请失败')
    }
  }
}

// 分页处理
const handlePageChange = (val) => {
  currentPage.value = val
  // 这里可以实现分页加载
}

// 组件挂载时加载数据 + 监听返回刷新标记
onMounted(() => {
  console.log('我的申请页面加载，用户ID:', studentId)
  fetchApplications()

  // 新增：监听从详情页返回的刷新标记
  const refreshFlag = localStorage.getItem('refreshApplications')
  if (refreshFlag === 'true') {
    localStorage.removeItem('refreshApplications') // 清除标记
    refreshList() // 自动刷新列表
  }
})
</script>

<style scoped>
.my-applications {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #333;
  font-size: 24px;
}

.status-filter {
  margin-bottom: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
}

.applications-list {
  margin-bottom: 30px;
}

.position-info {
  line-height: 1.4;
}

.position-name {
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.company {
  font-size: 12px;
  color: #999;
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