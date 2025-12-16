<!-- src/views/login/LoginPage.vue -->
<template>
  <div class="login-container">
    <!-- 背景装饰 -->
    <div class="login-bg-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>

    <!-- 登录卡片 -->
    <div class="login-box">
      <!-- 系统logo/标题区 -->
      <div class="login-header">
        <div class="login-logo">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 2L2 7L12 12L22 7L12 2Z" stroke="#667eea" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M2 17L12 22L22 17" stroke="#667eea" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M2 12L12 17L22 12" stroke="#667eea" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
        <h2 class="login-title">勤工俭学管理系统</h2>
        <p class="login-subtitle">便捷高效的勤工俭学服务平台</p>
      </div>

      <!-- 登录表单 -->
      <el-form
          ref="loginFormRef"
          :model="form"
          :rules="rules"
          label-width="0"
          class="login-form"
      >
        <!-- 账号输入框 -->
        <el-form-item prop="username">
          <div class="input-wrapper">
            <el-input
                v-model="form.username"
                placeholder="请输入账号"
                size="large"
                class="login-input"
                prefix-icon="User"
            />
          </div>
        </el-form-item>

        <!-- 密码输入框 -->
        <el-form-item prop="password">
          <div class="input-wrapper">
            <el-input
                v-model="form.password"
                type="password"
                placeholder="请输入密码"
                size="large"
                class="login-input"
                prefix-icon="Lock"
                show-password
            />
          </div>
        </el-form-item>

        <!-- 身份选择 -->
        <el-form-item prop="userType" class="identity-selector">
          <span class="identity-label">登录身份：</span>
          <el-radio-group v-model="form.userType" class="radio-group">
            <el-radio
                label="student"
                class="radio-item"
                border
            >
              <span>学生</span>
            </el-radio>
            <el-radio
                label="teacher"
                class="radio-item"
                border
            >
              <span>教师</span>
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 登录按钮 -->
        <el-form-item class="login-btn-item">
          <el-button
              type="primary"
              @click="handleLogin"
              :loading="loading"
              size="large"
              class="login-btn"
          >
            {{ loading ? '登录中...' : '立即登录' }}
          </el-button>
        </el-form-item>
      </el-form>

<!--      &lt;!&ndash; 底部版权信息 &ndash;&gt;-->
<!--      <div class="login-footer">-->
<!--        <p>© {{ new Date().getFullYear() }} 勤工俭学管理系统 版权所有</p>-->
<!--      </div>-->
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import studentApi from '@/api/student'
import teacherApi from '@/api/teacher'

const router = useRouter()
const loading = ref(false)
// 新增：关联表单ref，用于手动校验
const loginFormRef = ref(null)

const form = reactive({
  username: '',
  password: '',
  userType: 'student'
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  userType: [{ required: true, message: '请选择身份', trigger: 'change' }]
}

const handleLogin = async () => {
  try {
    loading.value = true

    // 第一步：手动触发表单校验，未通过则直接返回
    const valid = await loginFormRef.value.validate()
    if (!valid) {
      ElMessage.warning('请完善登录信息')
      return
    }

    // 第二步：根据身份调用不同接口
    let response
    if (form.userType === 'student') {
      response = await studentApi.login({
        username: form.username,
        password: form.password
      })
    } else {
      response = await teacherApi.login({
        username: form.username,
        password: form.password
      })
    }

    // 第三步：核心修复！校验后端返回码，非200则抛错（进入catch）
    const businessRes = response.data  // 后端把业务数据放在了根data里
    if (businessRes.code !== 200) {
      throw new Error(businessRes.message || '用户名或密码错误')
    }

    // 第四步：仅当登录成功时，才保存信息（增加空值保护）
    const token = businessRes.data?.token || 'mock-token'
    localStorage.setItem('token', token)
    localStorage.setItem('userInfo', JSON.stringify(businessRes.data || {}))
    localStorage.setItem('userType', form.userType)

    // 跳转首页
    const targetPath = form.userType === 'student' ? '/student/home' : '/teacher/home'
    await router.push(targetPath)
    ElMessage.success('登录成功，欢迎回来！')

  } catch (error) {
    console.error('登录失败详情：', error)
    ElMessage.error(error.message || '账号或密码错误，请重试')
  } finally {
    loading.value = false
  }
}

// 新增：清空登录状态（方便测试）
const clearLoginState = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  localStorage.removeItem('userType')
  form.username = ''
  form.password = ''
  ElMessage.success('已清空登录状态，请重新登录')
}
</script>

<style>
/* 全局样式：清除html/body的默认边距，确保全屏 */
html, body, #app {
  height: 100%;
  width: 100%;
  margin: 0;
  padding: 0;
}
</style>

<style scoped>
/* 登录容器 - 强制全屏，无任何留白 */
.login-container {
  height: 100vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

/* 背景装饰圆 */
.login-bg-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  animation: float 8s ease-in-out infinite;
}

.circle-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  right: -100px;
  animation-delay: 0s;
}

.circle-2 {
  width: 200px;
  height: 200px;
  bottom: -80px;
  left: -80px;
  animation-delay: 2s;
}

.circle-3 {
  width: 150px;
  height: 150px;
  top: 50%;
  left: -75px;
  animation-delay: 4s;
}

/* 浮动动画 */
@keyframes float {
  0%, 100% {
    transform: translateY(0) translateX(0);
  }
  50% {
    transform: translateY(-20px) translateX(20px);
  }
}

/* 登录卡片 */
.login-box {
  width: 100%;
  max-width: 450px;
  padding: 40px 30px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(31, 38, 135, 0.15);
  backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  z-index: 10;
  transition: all 0.3s ease;
}

.login-box:hover {
  box-shadow: 0 12px 40px rgba(31, 38, 135, 0.2);
  transform: translateY(-2px);
}

/* 登录头部 */
.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-logo {
  margin: 0 auto 15px;
  padding: 10px;
  border-radius: 50%;
  background: rgba(102, 126, 234, 0.1);
  display: inline-block;
}

.login-title {
  font-size: 24px;
  font-weight: 600;
  color: #2d3748;
  margin-bottom: 8px;
}

.login-subtitle {
  font-size: 14px;
  color: #718096;
  line-height: 1.5;
}

/* 登录表单 */
.login-form {
  margin: 0 auto;
  width: 100%;
}

.input-wrapper {
  position: relative;
  width: 100%;
}

.login-input {
  width: 100%;
  height: 50px;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  padding: 0 15px;
  font-size: 14px;
  transition: all 0.3s ease;
}

.login-input:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

/* 身份选择器 */
.identity-selector {
  margin-bottom: 20px;
  padding: 15px;
  background: rgba(102, 126, 234, 0.05);
  border-radius: 8px;
}

.identity-label {
  font-size: 14px;
  color: #4a5568;
  margin-right: 15px;
  font-weight: 500;
}

.radio-group {
  display: inline-flex;
  gap: 10px;
}

.radio-item {
  padding: 8px 20px;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.radio-item:deep(.el-radio__input.is-checked + .el-radio__label) {
  color: #667eea;
}

.radio-item:deep(.el-radio__input.is-checked .el-radio__inner) {
  background-color: #667eea;
  border-color: #667eea;
}

/* 登录按钮 */
.login-btn-item {
  margin-bottom: 0;
}

.login-btn {
  width: 100%;
  height: 50px;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(102, 126, 234, 0.3);
}

.login-btn:deep(.el-button--primary) {
  --el-button-hover-bg: linear-gradient(135deg, #5a6edb 0%, #6b4298 100%);
  --el-button-active-bg: linear-gradient(135deg, #4f62d8 0%, #603a88 100%);
}

/* 登录底部 */
.login-footer {
  margin-top: 25px;
  text-align: center;
}

.login-footer p {
  font-size: 12px;
  color: #a0aec0;
}

/* 响应式适配 */
@media (max-width: 480px) {
  .login-box {
    padding: 30px 20px;
  }

  .login-title {
    font-size: 20px;
  }

  .radio-group {
    display: flex;
    width: 100%;
    gap: 5px;
  }

  .radio-item {
    flex: 1;
    text-align: center;
    padding: 8px 0;
  }
}

/* 加载动画优化 */
:deep(.el-button--loading .el-button__loading) {
  margin-right: 8px;
}

/* 表单校验提示优化 */
:deep(.el-form-item__error) {
  font-size: 12px;
  padding-top: 4px;
}

/* 输入框前缀图标颜色 */
:deep(.el-input__prefix) {
  color: #a0aec0;
}

:deep(.el-input:focus .el-input__prefix) {
  color: #667eea;
}
</style>