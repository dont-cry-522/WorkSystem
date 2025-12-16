<!-- src/views/student/HomePage.vue -->
<template>
  <div class="home-page">
    <!-- 欢迎横幅 -->
    <div class="welcome-banner">
      <h1>欢迎回来，{{ userInfo.name || '同学' }}！</h1>
      <p>{{ userInfo.college || '未完善信息' }} · {{ getGreeting() }}</p>
    </div>

    <!-- 快速操作卡片 -->
    <div class="quick-actions">
      <el-card class="action-card" @click="goToPositions">
        <div class="card-content">
          <el-icon class="card-icon"><Briefcase /></el-icon>
          <div>
            <h3>浏览岗位</h3>
            <p>查看所有可申请的勤工俭学岗位</p>
          </div>
        </div>
      </el-card>

      <el-card class="action-card" @click="goToApplications">
        <div class="card-content">
          <el-icon class="card-icon"><Document /></el-icon>
          <div>
            <h3>我的申请</h3>
            <p>查看和管理我的岗位申请</p>
          </div>
        </div>
      </el-card>

      <el-card class="action-card" @click="goToProfile">
        <div class="card-content">
          <el-icon class="card-icon"><User /></el-icon>
          <div>
            <h3>个人中心</h3>
            <p>查看和编辑个人信息</p>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 系统公告 -->
    <div class="announcements">
      <h2>系统公告</h2>
      <el-card>
        <p>1. 图书馆管理员岗位正在招聘，待遇优厚</p>
        <p>2. 请及时完善个人信息，方便岗位匹配</p>
        <p>3. 申请通过后请按时到岗，遵守工作纪律</p>
      </el-card>
    </div>

    <!-- 统计信息 -->
    <div class="stats">
      <h2>我的统计</h2>
      <div class="stats-grid">
        <div class="stat-item">
          <div class="stat-value">{{ applicationCount }}</div>
          <div class="stat-label">已申请岗位</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ approvedCount }}</div>
          <div class="stat-label">已通过申请</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ totalHours }}</div>
          <div class="stat-label">累计工时(小时)</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Briefcase, Document, User } from '@element-plus/icons-vue'

// 核心修复：默认导入（和 student.js 导出方式匹配）
import studentApi from '@/api/student'

const router = useRouter()

// 用户信息 - 增加空值保护，避免JSON解析失败
const userInfo = ref({})
const applicationCount = ref(0)
const approvedCount = ref(0)
const totalHours = ref(0)

// 获取问候语
const getGreeting = () => {
  const hour = new Date().getHours()
  if (hour < 12) return '上午好'
  if (hour < 18) return '下午好'
  return '晚上好'
}

// 导航方法 - 增加路由跳转错误捕获
const goToPositions = async () => {
  try {
    await router.push('/student/positions')
  } catch (error) {
    ElMessage.error('页面跳转失败，请稍后重试')
    console.error('跳转岗位列表失败：', error)
  }
}

const goToApplications = async () => {
  try {
    await router.push('/student/my-applications')
  } catch (error) {
    ElMessage.error('页面跳转失败，请稍后重试')
    console.error('跳转我的申请失败：', error)
  }
}

const goToProfile = async () => {
  try {
    await router.push('/student/profile')
  } catch (error) {
    ElMessage.error('页面跳转失败，请稍后重试')
    console.error('跳转个人中心失败：', error)
  }
}

// 初始化数据 - 完善错误处理和真实API调用逻辑
onMounted(async () => {
  try {
    // 从localStorage获取用户信息（增加异常捕获）
    const storedUserInfo = localStorage.getItem('userInfo')
    if (storedUserInfo) {
      try {
        userInfo.value = JSON.parse(storedUserInfo)
      } catch (parseError) {
        console.error('解析用户信息失败：', parseError)
        userInfo.value = {}
        localStorage.removeItem('userInfo') // 清除损坏的存储数据
      }
    } else {
      // 无用户信息，跳回登录页
      ElMessage.warning('请先登录')
      await router.push('/login')
      return
    }

    // 调用真实API获取统计数据（开发阶段可注释，用模拟数据）
    // const statsResponse = await studentApi.getStudentStats({
    //   userId: userInfo.value.id,
    //   token: localStorage.getItem('token')
    // })
    // if (statsResponse && statsResponse.data) {
    //   applicationCount.value = statsResponse.data.applicationCount
    //   approvedCount.value = statsResponse.data.approvedCount
    //   totalHours.value = statsResponse.data.totalHours
    // }

    // 模拟数据（开发阶段使用）
    applicationCount.value = 3
    approvedCount.value = 1
    totalHours.value = 24

  } catch (error) {
    console.error('初始化首页数据失败:', error)
    ElMessage.error('数据加载失败，请刷新页面')
    // 模拟数据兜底，避免页面空白
    applicationCount.value = 0
    approvedCount.value = 0
    totalHours.value = 0
  }
})
</script>

<style scoped>
.home-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  min-height: 100vh;
  background-color: #f9fafc;
}

.welcome-banner {
  background: linear-gradient(135deg, #409eff, #67c23a);
  color: white;
  padding: 30px;
  border-radius: 12px;
  margin-bottom: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.welcome-banner h1 {
  margin: 0 0 10px 0;
  font-size: 28px;
  font-weight: 600;
}

.welcome-banner p {
  margin: 0;
  opacity: 0.9;
  font-size: 16px;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.action-card {
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #e8f4f9;
  border-radius: 12px;
}

.action-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(64, 158, 255, 0.12);
  border-color: #409eff;
}

.card-content {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 10px 0;
}

.card-icon {
  font-size: 32px;
  color: #409eff;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #ecf5ff;
  border-radius: 50%;
}

.announcements,
.stats {
  margin-bottom: 30px;
}

h2 {
  margin-bottom: 15px;
  color: #1f2937;
  font-size: 20px;
  font-weight: 600;
  position: relative;
  padding-left: 10px;
}

h2::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 20px;
  background-color: #409eff;
  border-radius: 2px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.stat-item {
  background: #ffffff;
  padding: 25px 20px;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: transform 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 16px rgba(64, 158, 255, 0.1);
}

.stat-value {
  font-size: 36px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 8px;
  line-height: 1;
}

.stat-label {
  color: #6b7280;
  font-size: 14px;
}

/* 响应式适配 */
@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
    gap: 15px;
  }

  .quick-actions {
    grid-template-columns: 1fr;
  }

  .welcome-banner h1 {
    font-size: 24px;
  }

  .stat-value {
    font-size: 28px;
  }
}
</style>