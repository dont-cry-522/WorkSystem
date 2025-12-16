<!-- src/views/student/ProfilePage.vue -->
<template>
  <div class="profile-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <h2>个人中心</h2>
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item to="/student/home">首页</el-breadcrumb-item>
        <el-breadcrumb-item>个人中心</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <!-- 在ProfilePage.vue中添加 -->
    <!-- 在ProfilePage.vue中添加 -->
    <div class="time-table-section">
      <h3>我的空闲时间</h3>
      <el-table :data="freeTimeList">
        <el-table-column prop="weekday" label="星期" />
        <el-table-column prop="timeSlot" label="时间段" />
        <el-table-column label="操作">
          <template #default="{ row }">
            <el-button size="small" @click="editTimeSlot(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="deleteTimeSlot(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="profile-content">
      <!-- 基本信息卡片 -->
      <el-card class="profile-card">
        <template #header>
          <div class="card-header">
            <div class="header-left">
              <el-icon><User /></el-icon>
              <span>基本信息</span>
            </div>
            <div class="header-right">
              <el-button
                  v-if="!editMode"
                  type="primary"
                  size="small"
                  @click="enterEditMode"
                  :loading="loading"
              >
                <el-icon><Edit /></el-icon>
                编辑信息
              </el-button>
              <div v-else class="edit-buttons">
                <el-button
                    type="success"
                    size="small"
                    @click="handleSave"
                    :loading="saving"
                >
                  <el-icon><Check /></el-icon>
                  保存
                </el-button>
                <el-button
                    size="small"
                    @click="cancelEdit"
                    :disabled="saving"
                >
                  <el-icon><Close /></el-icon>
                  取消
                </el-button>
              </div>
            </div>
          </div>
        </template>

        <!-- 查看模式 -->
        <div v-if="!editMode" class="view-mode">
          <div class="avatar-section">
            <div class="avatar-container">
              <el-avatar :size="100" :src="userInfo.avatar" class="avatar">
                {{ userInfo.name?.charAt(0) || '用' }}
              </el-avatar>
              <div class="avatar-upload" @click="showAvatarUpload = true">
                <el-icon><Camera /></el-icon>
              </div>
            </div>
            <div class="basic-info">
              <h3 class="user-name">{{ userInfo.name }}</h3>
              <p class="user-id">学号：{{ userInfo.username }}</p>
              <p class="user-college">{{ userInfo.college }}</p>
              <p class="user-type">身份：学生</p>
            </div>
          </div>

          <el-divider />

          <div class="detail-info">
            <div class="info-grid">
              <div class="info-item">
                <div class="info-label">
                  <el-icon><Phone /></el-icon>
                  <span>手机号码</span>
                </div>
                <div class="info-value">{{ userInfo.mobile || '未填写' }}</div>
              </div>

              <div class="info-item">
                <div class="info-label">
                  <el-icon><Message /></el-icon>
                  <span>电子邮箱</span>
                </div>
                <div class="info-value">{{ userInfo.email || '未填写' }}</div>
              </div>

              <div class="info-item">
                <div class="info-label">
                  <el-icon><Location /></el-icon>
                  <span>所在学院</span>
                </div>
                <div class="info-value">{{ userInfo.college || '未填写' }}</div>
              </div>

              <div class="info-item">
                <div class="info-label">
                  <el-icon><School /></el-icon>
                  <span>所学专业</span>
                </div>
                <div class="info-value">{{ userInfo.major || '未填写' }}</div>
              </div>

              <div class="info-item full-width">
                <div class="info-label">
                  <el-icon><Document /></el-icon>
                  <span>个人简介</span>
                </div>
                <div class="info-value intro-text">
                  {{ userInfo.introduction || '这个人很懒，什么都没有留下...' }}
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 编辑模式 -->
        <div v-else class="edit-mode">
          <el-form
              ref="profileFormRef"
              :model="editForm"
              :rules="formRules"
              label-width="100px"
              class="profile-form"
              status-icon
          >
            <div class="form-section">
              <h3 class="section-title">基本信息</h3>

              <el-form-item label="姓名" prop="name" required>
                <el-input
                    v-model="editForm.name"
                    placeholder="请输入姓名"
                    clearable
                    :prefix-icon="User"
                    maxlength="20"
                    show-word-limit
                />
              </el-form-item>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="学号" prop="username">
                    <el-input
                        v-model="editForm.username"
                        placeholder="请输入学号"
                        disabled
                        :prefix-icon="Ticket"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="学院" prop="college" required>
                    <el-input
                        v-model="editForm.college"
                        placeholder="请输入学院"
                        clearable
                        :prefix-icon="School"
                    />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-form-item label="专业" prop="major">
                <el-input
                    v-model="editForm.major"
                    placeholder="请输入专业"
                    clearable
                    :prefix-icon="Reading"
                />
              </el-form-item>
            </div>

            <div class="form-section">
              <h3 class="section-title">联系信息</h3>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="手机号码" prop="mobile" required>
                    <el-input
                        v-model="editForm.mobile"
                        placeholder="请输入手机号码"
                        clearable
                        :prefix-icon="Phone"
                        maxlength="11"
                    />
                    <template #error="{ error }">
                      <div class="form-error">{{ error }}</div>
                    </template>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="电子邮箱" prop="email">
                    <el-input
                        v-model="editForm.email"
                        placeholder="请输入电子邮箱"
                        clearable
                        :prefix-icon="Message"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
            </div>

            <div class="form-section">
              <h3 class="section-title">个人介绍</h3>

              <el-form-item label="个人简介" prop="introduction">
                <el-input
                    v-model="editForm.introduction"
                    type="textarea"
                    :rows="4"
                    placeholder="请介绍一下自己，如兴趣爱好、特长技能等..."
                    maxlength="500"
                    show-word-limit
                    resize="none"
                />
              </el-form-item>

              <el-form-item label="技能标签" prop="skills">
                <el-tag
                    v-for="(skill, index) in editForm.skills"
                    :key="index"
                    closable
                    @close="removeSkill(index)"
                    class="skill-tag"
                >
                  {{ skill }}
                </el-tag>
                <el-input
                    v-if="skillInputVisible"
                    ref="skillInputRef"
                    v-model="skillInputValue"
                    size="small"
                    @keyup.enter="addSkill"
                    @blur="addSkill"
                    class="skill-input"
                    placeholder="输入技能后按回车"
                />
                <el-button v-else size="small" @click="showSkillInput">
                  + 添加技能
                </el-button>
              </el-form-item>
            </div>

            <div class="form-actions">
              <el-button
                  type="success"
                  @click="handleSave"
                  :loading="saving"
                  size="large"
                  class="save-button"
              >
                <el-icon><Check /></el-icon>
                保存修改
              </el-button>
              <el-button
                  @click="cancelEdit"
                  :disabled="saving"
                  size="large"
              >
                <el-icon><Close /></el-icon>
                取消编辑
              </el-button>
            </div>
          </el-form>
        </div>
      </el-card>

      <!-- 头像上传对话框 -->
      <el-dialog
          v-model="showAvatarUpload"
          title="更换头像"
          width="400px"
          append-to-body
      >
        <div class="avatar-upload-dialog">
          <div class="avatar-preview">
            <el-avatar :size="120" :src="avatarPreview">
              {{ userInfo.name?.charAt(0) || '用' }}
            </el-avatar>
          </div>

          <div class="upload-actions">
            <el-upload
                ref="uploadRef"
                class="avatar-uploader"
                action="#"
                :show-file-list="false"
                :before-upload="beforeAvatarUpload"
                :http-request="handleAvatarUpload"
                accept="image/*"
            >
              <el-button type="primary">
                <el-icon><Upload /></el-icon>
                选择图片
              </el-button>
            </el-upload>
            <div class="upload-tips">
              <p>支持 JPG、PNG 格式，大小不超过 2MB</p>
              <p>建议尺寸：200×200 像素</p>
            </div>
          </div>
        </div>

        <template #footer>
          <div class="dialog-footer">
            <el-button @click="showAvatarUpload = false">取消</el-button>
            <el-button type="primary" @click="confirmAvatar" :loading="uploading">
              确定
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, nextTick, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  User,
  Edit,
  Check,
  Close,
  Camera,
  Phone,
  Message,
  Location,
  School,
  Document,
  Ticket,
  Reading,
  Upload
} from '@element-plus/icons-vue'
import { studentApi } from '@/api/student'

const router = useRouter()

// 响应式数据
const editMode = ref(false)
const loading = ref(false)
const saving = ref(false)
const uploading = ref(false)
const showAvatarUpload = ref(false)
const profileFormRef = ref(null)
const uploadRef = ref(null)
const skillInputRef = ref(null)
const skillInputVisible = ref(false)
const skillInputValue = ref('')
const avatarPreview = ref('')

// 用户信息
const userInfo = ref({})

// 编辑表单
const editForm = reactive({
  name: '',
  username: '',
  mobile: '',
  email: '',
  college: '',
  major: '',
  introduction: '',
  skills: [],
  avatar: ''
})

// 表单验证规则
const formRules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度在2-20个字符', trigger: 'blur' }
  ],
  username: [
    { required: true, message: '请输入学号', trigger: 'blur' },
    { pattern: /^\d+$/, message: '学号只能包含数字', trigger: 'blur' }
  ],
  mobile: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  college: [
    { required: true, message: '请输入学院', trigger: 'blur' },
    { min: 2, max: 50, message: '学院名称长度在2-50个字符', trigger: 'blur' }
  ],
  major: [
    { min: 2, max: 50, message: '专业名称长度在2-50个字符', trigger: 'blur' }
  ],
  introduction: [
    { max: 500, message: '个人简介不能超过500个字符', trigger: 'blur' }
  ]
}

// 初始化用户信息
const initUserInfo = () => {
  const storedInfo = localStorage.getItem('userInfo')
  if (storedInfo) {
    userInfo.value = JSON.parse(storedInfo)
    // 同步到编辑表单
    Object.keys(editForm).forEach(key => {
      if (userInfo.value[key] !== undefined) {
        editForm[key] = userInfo.value[key]
      }
    })

    // 如果没有技能数据，初始化默认技能
    if (!editForm.skills || editForm.skills.length === 0) {
      editForm.skills = ['沟通能力', '团队协作', '认真负责']
    }
  }
}

// 进入编辑模式
const enterEditMode = () => {
  editMode.value = true
  // 备份原始数据，用于取消编辑时恢复
  initUserInfo()
}

// 取消编辑
const cancelEdit = async () => {
  if (saving.value) return

  try {
    await ElMessageBox.confirm(
        '确定要取消编辑吗？所有修改都将丢失。',
        '确认取消',
        {
          confirmButtonText: '确定',
          cancelButtonText: '继续编辑',
          type: 'warning'
        }
    )

    // 恢复原始数据
    initUserInfo()
    editMode.value = false

  } catch (error) {
    // 用户点击了继续编辑，什么都不做
  }
}

// 显示技能输入框
const showSkillInput = () => {
  skillInputVisible.value = true
  nextTick(() => {
    skillInputRef.value?.focus()
  })
}

// 添加技能
const addSkill = () => {
  if (skillInputValue.value.trim()) {
    if (!editForm.skills.includes(skillInputValue.value.trim())) {
      editForm.skills.push(skillInputValue.value.trim())
    }
  }
  skillInputVisible.value = false
  skillInputValue.value = ''
}

// 移除技能
const removeSkill = (index) => {
  editForm.skills.splice(index, 1)
}

// 头像上传前的校验
const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
    return false
  }

  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB！')
    return false
  }

  // 预览图片
  const reader = new FileReader()
  reader.readAsDataURL(file)
  reader.onload = (e) => {
    avatarPreview.value = e.target.result
  }

  return true
}

// 处理头像上传
const handleAvatarUpload = async ({ file }) => {
  try {
    uploading.value = true

    // 这里应该调用上传图片的API
    // 暂时模拟上传成功
    await new Promise(resolve => setTimeout(resolve, 1000))

    // 更新预览
    editForm.avatar = avatarPreview.value
    userInfo.value.avatar = avatarPreview.value

    ElMessage.success('头像上传成功')

  } catch (error) {
    ElMessage.error('头像上传失败')
  } finally {
    uploading.value = false
  }
}

// 确认头像修改
const confirmAvatar = () => {
  if (avatarPreview.value) {
    editForm.avatar = avatarPreview.value
    userInfo.value.avatar = avatarPreview.value
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
  }
  showAvatarUpload.value = false
}

// 保存个人信息
const handleSave = async () => {
  try {
    // 表单验证
    const valid = await profileFormRef.value.validate()
    if (!valid) {
      ElMessage.warning('请检查表单填写是否正确')
      return
    }

    saving.value = true

    // 确认保存
    await ElMessageBox.confirm(
        '确定要保存修改吗？',
        '确认保存',
        {
          confirmButtonText: '确定保存',
          cancelButtonText: '再检查一下',
          type: 'info'
        }
    )

    // 这里调用更新个人信息的API
    const response = await studentApi.updateProfile(userInfo.value.userId, editForm)

    if (response.code === 200) {
      // 更新本地存储的用户信息
      const updatedInfo = {
        ...userInfo.value,
        ...editForm,
        updateTime: new Date().toISOString()
      }

      localStorage.setItem('userInfo', JSON.stringify(updatedInfo))
      userInfo.value = updatedInfo

      editMode.value = false

      ElMessage.success({
        message: '个人信息更新成功！',
        duration: 2000,
        showClose: true
      })
    } else {
      ElMessage.error('保存失败: ' + (response.message || '请稍后重试'))
    }

  } catch (error) {
    if (error !== 'cancel') {
      console.error('保存个人信息失败:', error)

      if (error.response) {
        // API错误
        ElMessage.error(`保存失败: ${error.response.data?.message || '服务器错误'}`)
      } else if (error.message) {
        // 其他错误
        ElMessage.error(`保存失败: ${error.message}`)
      } else {
        ElMessage.error('保存失败，请重试')
      }
    }
  } finally {
    saving.value = false
  }
}

// 加载统计数据
const loadStats = () => {
  // 这里可以加载申请统计等数据
}

// 组件挂载时加载数据
onMounted(() => {
  console.log('个人中心页面加载')
  initUserInfo()
  loadStats()
})
</script>

<style scoped>
.profile-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 30px;
}

.page-header h2 {
  margin: 0 0 15px 0;
  color: #303133;
  font-size: 28px;
  font-weight: bold;
}

.breadcrumb {
  color: #909399;
}

.profile-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 10px;
  font-weight: bold;
  color: #303133;
}

.header-left .el-icon {
  color: #409eff;
}

.edit-buttons {
  display: flex;
  gap: 10px;
}

.view-mode .avatar-section {
  display: flex;
  align-items: center;
  gap: 40px;
  margin-bottom: 30px;
}

.avatar-container {
  position: relative;
}

.avatar {
  border: 3px solid #409eff;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-size: 36px;
  font-weight: bold;
}

.avatar-upload {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 32px;
  height: 32px;
  background: #409eff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  cursor: pointer;
  transition: all 0.3s;
  border: 2px solid white;
}

.avatar-upload:hover {
  background: #79bbff;
  transform: scale(1.1);
}

.basic-info {
  flex: 1;
}

.user-name {
  margin: 0 0 10px 0;
  font-size: 24px;
  color: #303133;
}

.user-id,
.user-college,
.user-type {
  margin: 8px 0;
  color: #606266;
  font-size: 15px;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 25px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  transition: all 0.3s;
}

.info-item:hover {
  background: #e9ecef;
  transform: translateY(-2px);
}

.info-item.full-width {
  grid-column: 1 / -1;
}

.info-label {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #909399;
  font-size: 14px;
}

.info-label .el-icon {
  font-size: 16px;
}

.info-value {
  color: #303133;
  font-size: 16px;
  font-weight: 500;
}

.intro-text {
  line-height: 1.6;
  white-space: pre-line;
}

.edit-mode {
  padding: 10px 0;
}

.profile-form {
  max-width: 800px;
  margin: 0 auto;
}

.form-section {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.form-section:last-child {
  border-bottom: none;
}

.section-title {
  margin: 0 0 20px 0;
  color: #303133;
  font-size: 18px;
  font-weight: bold;
  padding-left: 10px;
  border-left: 4px solid #409eff;
}

.form-error {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 5px;
}

.skill-tag {
  margin-right: 10px;
  margin-bottom: 10px;
}

.skill-input {
  width: 120px;
}

.form-actions {
  text-align: center;
  padding-top: 30px;
  margin-top: 30px;
  border-top: 1px solid #f0f0f0;
}

.save-button {
  min-width: 150px;
}

.avatar-upload-dialog {
  text-align: center;
}

.avatar-preview {
  margin-bottom: 30px;
}

.upload-actions {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.upload-tips {
  color: #909399;
  font-size: 12px;
  text-align: center;
}

.upload-tips p {
  margin: 5px 0;
}

.dialog-footer {
  text-align: center;
}

@media (max-width: 768px) {
  .avatar-section {
    flex-direction: column;
    text-align: center;
    gap: 20px;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }

  .form-section .el-row .el-col {
    margin-bottom: 20px;
  }
}
</style>