<template>
  <div class="position-detail">
    <!-- 页面头部：核心修复 → 加 v-if="position" 保护，避免 null 时渲染 -->
    <el-page-header @back="goBack" title="返回" v-if="position">
      <template #content>
        <div class="header-content">
          <span class="position-title">{{ position.positionName }}</span>
          <el-tag
              :type="getStatusTagType(position.recruitmentStatus)"
              size="large"
              class="status-tag"
          >
            {{ getStatusText(position.recruitmentStatus) }}
          </el-tag>
        </div>
      </template>
    </el-page-header>

    <!-- 加载状态：提前渲染，避免空页面 -->
    <div v-if="loading" class="loading-container full-loading">
      <el-icon class="loading-icon"><Loading /></el-icon>
      <span>加载岗位详情中...</span>
    </div>

    <div class="detail-content" v-else>
      <!-- 岗位信息 -->
      <div v-if="position" class="position-info">
        <!-- 基本信息卡片 -->
        <el-card class="basic-info-card">
          <div class="card-grid">
            <div class="info-item">
              <el-icon><OfficeBuilding /></el-icon>
              <div class="item-content">
                <div class="item-label">工作单位</div>
                <div class="item-value">{{ position.company || '学校' }}</div>
              </div>
            </div>

            <div class="info-item">
              <el-icon><Money /></el-icon>
              <div class="item-content">
                <div class="item-label">薪资待遇</div>
                <div class="item-value">{{ position.salary }} 元/小时</div>
              </div>
            </div>

            <div class="info-item">
              <el-icon><Clock /></el-icon>
              <div class="item-content">
                <div class="item-label">工作时间</div>
                <div class="item-value">{{ position.workTime || '时间灵活' }}</div>
              </div>
            </div>

            <div class="info-item">
              <el-icon><User /></el-icon>
              <div class="item-content">
                <div class="item-label">需要人数</div>
                <div class="item-value">{{ position.requiredCount || '若干' }} 人</div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 工作内容详情 -->
        <el-card class="detail-card">
          <template #header>
            <div class="card-header">
              <el-icon><Document /></el-icon>
              <span>工作内容</span>
            </div>
          </template>

          <div class="work-content">
            <div v-if="position.workContent" class="content-text">
              {{ position.workContent }}
            </div>
            <div v-else class="empty-content">
              <el-empty description="暂无工作内容说明" :image-size="60" />
            </div>
          </div>
        </el-card>

        <!-- 上岗要求 -->
        <el-card class="detail-card">
          <template #header>
            <div class="card-header">
              <el-icon><List /></el-icon>
              <span>上岗要求</span>
            </div>
          </template>

          <div class="requirements">
            <ul v-if="position.requirements && position.requirements.length > 0" class="requirements-list">
              <li v-for="(req, index) in position.requirements" :key="index" class="requirement-item">
                <el-icon><Check /></el-icon>
                <span>{{ req }}</span>
              </li>
            </ul>
            <div v-else class="empty-requirements">
              <el-empty description="暂无具体要求" :image-size="60" />
            </div>
          </div>
        </el-card>

        <!-- 联系信息 -->
        <el-card class="detail-card">
          <template #header>
            <div class="card-header">
              <el-icon><Phone /></el-icon>
              <span>联系信息</span>
            </div>
          </template>

          <div class="contact-info">
            <div class="contact-item">
              <el-icon><User /></el-icon>
              <div class="contact-content">
                <div class="contact-label">负责人</div>
                <div class="contact-value">{{ position.contactPerson || '待通知' }}</div>
              </div>
            </div>

            <div class="contact-item">
              <el-icon><PhoneFilled /></el-icon>
              <div class="contact-content">
                <div class="contact-label">联系电话</div>
                <div class="contact-value">{{ position.contactPhone || '待通知' }}</div>
              </div>
            </div>

            <div class="contact-item">
              <el-icon><Location /></el-icon>
              <div class="contact-content">
                <div class="contact-label">工作地点</div>
                <div class="contact-value">{{ position.location || '待通知' }}</div>
              </div>
            </div>

            <div class="contact-item">
              <el-icon><Message /></el-icon>
              <div class="contact-content">
                <div class="contact-label">备注</div>
                <div class="contact-value">{{ position.remark || '无' }}</div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 申请状态和按钮 -->
        <div class="action-section">
          <el-card class="action-card">
            <div class="action-content">
              <div v-if="hasApplied" class="applied-status">
                <el-result
                    :icon="applicationInfo.applicationStatus === 'pending' ? 'warning' : 'success'"
                    title="已申请该岗位"
                    sub-title="请等待审核结果"
                >
                  <template #extra>
                    <div class="status-info">
                      <p>申请时间：{{ formatDate(applicationInfo.applicationTime) }}</p>
                      <p>申请状态：{{ getStatusText(applicationInfo.applicationStatus) }}</p>
                    </div>
                    <!-- 新增：取消申请按钮（仅待审核时显示） -->
                    <div v-if="applicationInfo.applicationStatus === 'pending'" class="cancel-btn-wrap">
                      <el-button
                          type="danger"
                          size="default"
                          @click="handleCancelApply"
                          class="cancel-apply-btn"
                      >
                        <el-icon><Close /></el-icon>
                        取消申请
                      </el-button>
                    </div>
                  </template>
                </el-result>
              </div>

              <div v-else class="apply-actions">
                <div class="action-buttons">
                  <el-button
                      type="primary"
                      size="large"
                      :disabled="!canApply || applying"
                      :loading="applying"
                      @click="handleApply"
                      class="apply-button"
                  >
                    <el-icon><Check /></el-icon>
                    立即申请
                  </el-button>
                  <!-- 在PositionDetail.vue申请时弹出选择 -->
                  <el-dialog v-model="showTimeSelection" title="选择工作时间">
                    <div v-for="slot in position.availableTimeSlots" :key="slot.id">
                      <el-checkbox v-model="selectedTimeSlots" :label="slot.id">
                        {{ slot.weekday }} {{ slot.startTime }}-{{ slot.endTime }}
                      </el-checkbox>
                    </div>
                  </el-dialog>

                  <el-button
                      size="large"
                      @click="goBack"
                      class="back-button"
                  >
                    <el-icon><Back /></el-icon>
                    返回列表
                  </el-button>
                </div>

                <div v-if="!canApply" class="apply-tips">
                  <el-alert
                      type="warning"
                      :title="getApplyTips()"
                      show-icon
                      :closable="false"
                  />
                </div>
              </div>
            </div>
          </el-card>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-container">
        <el-result icon="error" title="岗位不存在" sub-title="该岗位可能已被删除或不存在">
          <template #extra>
            <el-button type="primary" @click="goBack">返回列表</el-button>
          </template>
        </el-result>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  OfficeBuilding,
  Money,
  Clock,
  User,
  Document,
  List,
  Check,
  Phone,
  PhoneFilled,
  Location,
  Message,
  Back,
  Loading,
  Close // 新增：关闭图标
} from '@element-plus/icons-vue'
import { positionApi } from '@/api/position'
import { studentApi } from '@/api/student'

const route = useRoute()
const router = useRouter()

// 响应式数据
const position = ref(null)
const loading = ref(false)
const applying = ref(false)
const hasApplied = ref(false)
const applicationInfo = ref({})
// 新增：时间选择弹窗相关
const showTimeSelection = ref(false)
const selectedTimeSlots = ref([])
// 修复：路由参数转数字 + 校验
const positionId = Number(route.params.id)

// 获取当前用户信息
const currentUser = JSON.parse(localStorage.getItem('userInfo') || '{}')
const studentId = currentUser.userId
// 新增：本地存储key（和列表页保持一致）
const appStorageKey = `student_applications_${studentId}`

// 计算属性：是否可以申请
const canApply = computed(() => {
  if (!position.value) return false
  if (hasApplied.value) return false
  return position.value.recruitmentStatus === 'recruiting'
})

// 获取岗位详情
const fetchPositionDetail = async () => {
  // 修复：先校验 ID 有效性
  if (isNaN(positionId) || positionId <= 0) {
    ElMessage.error('无效的岗位ID')
    position.value = null
    loading.value = false
    return
  }

  try {
    loading.value = true

    // 1. 获取岗位详情
    const response = await positionApi.getPositionDetail(positionId)

    if (response.code === 200 && response.data) {
      position.value = {
        ...response.data,
        // 模拟更多数据
        requirements: [
          '认真负责，有责任心',
          '能够按时到岗',
          '遵守相关规章制度',
          '有相关经验者优先'
        ],
        contactPerson: '李老师',
        contactPhone: '13800138000',
        location: '学校图书馆三楼',
        requiredCount: 3,
        remark: '需要自备水杯，提供工作证明'
      }

      // 2. 检查是否已申请（改用localStorage）
      await checkApplicationStatus()
    } else {
      ElMessage.error('获取岗位详情失败')
      position.value = null // 明确置空
    }
  } catch (error) {
    console.error('获取岗位详情异常:', error)
    ElMessage.error('获取岗位详情失败')

    // 测试数据
    position.value = {
      id: positionId,
      positionId: positionId, // 新增：和列表页字段对齐
      positionName: '图书馆管理员',
      company: '学校图书馆',
      salary: 20.0,
      workTime: '周一至周五 14:00-18:00',
      workContent: `1. 负责图书馆日常书籍整理与归位工作
2. 维护阅览室秩序，保持环境整洁
3. 协助读者查找书籍和办理借阅手续
4. 参与图书馆的日常管理工作
5. 每周工作20小时，按月结算工资`,
      recruitmentStatus: 'recruiting',
      requirements: [
        '认真负责，有责任心',
        '能够按时到岗',
        '遵守相关规章制度',
        '有相关经验者优先'
      ],
      contactPerson: '李老师',
      contactPhone: '13800138000',
      location: '学校图书馆三楼',
      requiredCount: 3,
      remark: '需要自备水杯，提供工作证明'
    }

    // 检查申请状态（改用localStorage）
    await checkApplicationStatus()
  } finally {
    loading.value = false
  }
}

// 检查申请状态（核心修改：改用localStorage数据）
const checkApplicationStatus = async () => {
  try {
    // 读取列表页持久化的申请数据
    const persistedApps = localStorage.getItem(appStorageKey)
    const applications = persistedApps ? JSON.parse(persistedApps) : []

    // 匹配当前岗位的申请记录（字段对齐：positionId）
    const myApplication = applications.find(app => app.positionId == positionId)
    if (myApplication) {
      hasApplied.value = true
      applicationInfo.value = myApplication
    } else {
      hasApplied.value = false
      applicationInfo.value = {}
    }
  } catch (error) {
    console.error('检查申请状态异常:', error)
  }
}

// 状态标签类型（新增cancelled状态）
const getStatusTagType = (status) => {
  const map = {
    'recruiting': 'success',
    'full': 'warning',
    'closed': 'info',
    'pending': 'warning',
    'approved': 'success',
    'rejected': 'danger',
    'cancelled': 'info' // 新增：已取消
  }
  return map[status] || 'info'
}

// 状态显示文本（新增cancelled状态）
const getStatusText = (status) => {
  const map = {
    'recruiting': '招聘中',
    'full': '已满员',
    'closed': '已关闭',
    'pending': '待审核',
    'approved': '已通过',
    'rejected': '已拒绝',
    'cancelled': '已取消' // 新增：已取消
  }
  return map[status] || status
}

// 申请提示信息
const getApplyTips = () => {
  if (!position.value) return ''

  if (position.value.recruitmentStatus === 'full') {
    return '该岗位已满员，暂时无法申请'
  } else if (position.value.recruitmentStatus === 'closed') {
    return '该岗位已关闭招聘'
  } else if (hasApplied.value) {
    return '您已经申请过该岗位'
  }
  return '点击按钮申请该岗位'
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

// 申请岗位（核心修改：同步到localStorage）
const handleApply = async () => {
  // 修复：申请前校验 position 存在
  if (!position.value) {
    ElMessage.warning('岗位信息加载异常，无法申请')
    return
  }

  try {
    applying.value = true

    // 确认申请
    await ElMessageBox.confirm(
        `确定要申请"${position.value.positionName}"岗位吗？\n\n薪资：${position.value.salary}元/小时\n时间：${position.value.workTime}\n地点：${position.value.location}`,
        '确认申请',
        {
          confirmButtonText: '确定申请',
          cancelButtonText: '再考虑一下',
          type: 'warning',
          confirmButtonClass: 'confirm-apply-btn',
          cancelButtonClass: 'cancel-apply-btn'
        }
    )

    // 模拟接口成功（真实项目替换为studentApi.applyPosition）
    // const response = await studentApi.applyPosition(studentId, positionId)

    // 构造申请记录
    const newApplication = {
      id: Date.now(), // 临时ID
      positionId: positionId,
      positionName: position.value.positionName,
      company: position.value.company,
      salary: position.value.salary,
      workTime: position.value.workTime,
      applicationTime: new Date().toISOString(),
      applicationStatus: 'pending'
    }

    // 读取本地数据并更新
    const applications = JSON.parse(localStorage.getItem(appStorageKey) || '[]')
    applications.push(newApplication)
    localStorage.setItem(appStorageKey, JSON.stringify(applications))

    // 更新页面状态
    hasApplied.value = true
    applicationInfo.value = newApplication
    // 可选：更新岗位招聘状态
    position.value.recruitmentStatus = 'full'

    ElMessage.success({
      message: '申请成功！请等待审核结果',
      duration: 3000,
      showClose: true
    })

  } catch (error) {
    if (error !== 'cancel') {
      console.error('申请岗位异常:', error)
      ElMessage.error('申请失败，请稍后重试')
    }
  } finally {
    applying.value = false
  }
}

// 新增：取消申请逻辑
const handleCancelApply = async () => {
  try {
    await ElMessageBox.confirm(
        '确定要取消这个申请吗？取消后可重新申请',
        '确认取消',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
    )

    // 读取本地数据并更新状态
    const applications = JSON.parse(localStorage.getItem(appStorageKey) || '[]')
    const index = applications.findIndex(app => app.positionId == positionId && app.applicationStatus === 'pending')

    if (index !== -1) {
      applications[index].applicationStatus = 'cancelled'
      localStorage.setItem(appStorageKey, JSON.stringify(applications))

      // 更新页面状态
      applicationInfo.value.applicationStatus = 'cancelled'
      ElMessage.success('取消申请成功！')
    } else {
      ElMessage.error('取消失败：未找到申请记录')
    }

  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消申请异常:', error)
      ElMessage.error('取消申请失败，请稍后重试')
    }
  }
}

// 返回上一页（修复：返回我的申请列表页）
const goBack = () => {
  router.push('/student/my-applications').then(() => {
    // 标记需要刷新列表
    localStorage.setItem('refreshApplications', 'true')
  })
}

// 组件挂载时加载数据
onMounted(() => {
  console.log('加载岗位详情，ID:', positionId, '用户ID:', studentId)
  fetchPositionDetail()
})
</script>

<style scoped>
.position-detail {
  min-height: 100vh;
  background: #f5f7fa;
}

/* 新增：全局加载样式 */
.full-loading {
  width: 100%;
  padding: 80px 0;
  text-align: center;
  color: #666;
}

.loading-icon {
  font-size: 24px;
  margin-right: 10px;
  animation: rotate 1s linear infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.header-content {
  display: flex;
  align-items: center;
  gap: 15px;
}

.position-title {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
}

.status-tag {
  margin-left: 10px;
}

.detail-content {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.loading-container {
  padding: 40px;
  background: white;
  border-radius: 8px;
}

.basic-info-card {
  margin-bottom: 20px;
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  transition: all 0.3s;
}

.info-item:hover {
  background: #e9ecef;
  transform: translateY(-2px);
}

.info-item .el-icon {
  font-size: 28px;
  color: #409eff;
}

.item-content {
  flex: 1;
}

.item-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.item-value {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.detail-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 10px;
  font-weight: bold;
  color: #303133;
}

.card-header .el-icon {
  color: #409eff;
}

.work-content {
  line-height: 1.8;
  color: #606266;
  white-space: pre-line;
  font-size: 15px;
}

.empty-content {
  padding: 20px 0;
}

.requirements-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.requirement-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.requirement-item:last-child {
  border-bottom: none;
}

.requirement-item .el-icon {
  color: #67c23a;
}

.empty-requirements {
  padding: 20px 0;
}

.contact-info {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 15px;
}

.contact-item .el-icon {
  font-size: 20px;
  color: #409eff;
}

.contact-content {
  flex: 1;
}

.contact-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.contact-value {
  font-size: 16px;
  color: #303133;
  font-weight: 500;
}

.action-section {
  margin: 30px 0;
}

.action-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.action-content {
  color: white;
}

.applied-status {
  text-align: center;
}

.status-info {
  text-align: left;
  background: rgba(255, 255, 255, 0.1);
  padding: 20px;
  border-radius: 8px;
  margin-top: 20px;
}

.status-info p {
  margin: 10px 0;
  font-size: 16px;
}

/* 新增：取消申请按钮样式 */
.cancel-btn-wrap {
  margin-top: 20px;
  text-align: center;
}

.cancel-apply-btn {
  width: 160px;
  height: 40px;
}

.apply-actions {
  text-align: center;
}

.action-buttons {
  display: flex;
  gap: 20px;
  justify-content: center;
  margin-bottom: 20px;
}

.apply-button {
  min-width: 150px;
  height: 50px;
  font-size: 16px;
}

.back-button {
  min-width: 150px;
  height: 50px;
  font-size: 16px;
}

.apply-tips {
  max-width: 600px;
  margin: 0 auto;
}

.empty-container {
  padding: 60px 0;
}

/* 确认申请按钮样式 */
:deep(.confirm-apply-btn) {
  background-color: #409eff !important;
  border-color: #409eff !important;
}

:deep(.cancel-apply-btn) {
  color: #606266 !important;
}

@media (max-width: 768px) {
  .card-grid {
    grid-template-columns: 1fr;
  }

  .action-buttons {
    flex-direction: column;
  }

  .apply-button,
  .back-button {
    width: 100%;
  }
}
</style>