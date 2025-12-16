<!-- src/views/student/StudentLayout.vue -->
<template>
  <div class="student-layout">
    <!-- 顶部导航栏 -->
    <header class="header">
      <div class="logo">勤工俭学系统</div>
      <div class="user-info">
        <span>{{ userInfo.name }}</span>
        <el-button type="text" @click="handleLogout">退出</el-button>
      </div>
    </header>

    <div class="main-container">
      <!-- 侧边栏菜单 -->
      <aside class="sidebar">
        <el-menu
            :default-active="activeMenu"
            router
            class="menu"
        >
          <el-menu-item index="/student/home">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/student/positions">
            <el-icon><Briefcase /></el-icon>
            <span>岗位列表</span>
          </el-menu-item>
          <el-menu-item index="/student/my-applications">
            <el-icon><Document /></el-icon>
            <span>我的申请</span>
          </el-menu-item>
          <el-menu-item index="/student/profile">
            <el-icon><User /></el-icon>
            <span>个人中心</span>
          </el-menu-item>
        </el-menu>
      </aside>

      <!-- 主要内容区域 - 这里显示具体页面 -->
      <main class="content">
        <router-view></router-view>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  HomeFilled,
  Briefcase,
  Document,
  User
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

// 从localStorage获取用户信息
const userInfo = ref(
    JSON.parse(localStorage.getItem('userInfo') || '{}')
)

// 计算当前激活的菜单
const activeMenu = computed(() => route.path)

// 退出登录
const handleLogout = () => {
  localStorage.clear()
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<style scoped>
.student-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  height: 60px;
  background: #409eff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.header .logo {
  font-size: 20px;
  font-weight: bold;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.main-container {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.sidebar {
  width: 200px;
  background: #f5f7fa;
  border-right: 1px solid #e6e6e6;
}

.menu {
  border-right: none;
}

.content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background: white;
}
</style>