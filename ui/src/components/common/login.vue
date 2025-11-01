<template>
  <div class="login-bg">
    <div class="login-container">
      <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="90px"
          class="login-form"
      >
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="form.remember">记住我</el-checkbox>
        </el-form-item>
        <div class="login-footer">
          <a href="#" class="forgot-link">忘记密码？</a>
        </div>
        <el-form-item class="login-btn-item">
          <el-button
              type="primary"
              :loading="loading"
              class="login-btn"
              @click="onSubmit"
          >登录</el-button>
        </el-form-item>
        <div class="register-tip">
          如果没有账号，请先<a href="#" class="register-link">注册</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.login-bg {
  min-height: 100vh;
  background: linear-gradient(135deg, #6dd5ed 0%, #2193b0 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}
.login-container {
  width: 480px;
  padding: 56px 48px 40px 48px;
  background: #fff;
  border-radius: 20px;
  box-shadow: 0 8px 32px rgba(33,147,176,0.18);
  display: flex;
  flex-direction: column;
  align-items: center;
}
.login-form {
  width: 100%;
}
.el-form-item {
  margin-bottom: 32px;
  font-size: 18px;
}
.el-input, .el-button {
  border-radius: 10px;
  font-size: 18px;
}
.login-btn-item {
  display: flex;
  justify-content: center;
  margin-top: 32px;
}
.login-btn {
  width: 220px;
  height: 52px;
  font-size: 20px;
  font-weight: bold;
  letter-spacing: 1px;
}
.login-footer {
  text-align: right;
  margin-top: -16px;
  margin-bottom: 8px;
}
.forgot-link {
  color: #2193b0;
  font-size: 16px;
  font-weight: 500;
  text-decoration: none;
  transition: color 0.2s;
}
.forgot-link:hover {
  color: #6dd5ed;
  text-decoration: underline;
}
.register-tip {
  text-align: center;
  margin-top: 18px;
  font-size: 16px;
  color: #666;
}
.register-link {
  color: #2193b0;
  text-decoration: none;
  margin-left: 4px;
}
.register-link:hover {
  color: #6dd5ed;
  text-decoration: underline;
}
</style>
