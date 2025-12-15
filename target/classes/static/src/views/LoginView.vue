<template>
  <div class="login-container">
    <div class="container">
      <h1>美食论坛</h1>
      
      <div v-if="message" :class="['message', messageType]">{{ message }}</div>
      
      <!-- 登录表单 -->
      <div id="loginForm" class="form-container" :class="{ active: activeForm === 'login' }">
        <h2>用户登录</h2>
        <form @submit.prevent="handleLogin">
          <div class="form-group">
            <label for="loginUsername">用户名:</label>
            <input type="text" id="loginUsername" v-model="loginForm.username" required>
          </div>
          <div class="form-group">
            <label for="loginPassword">密码:</label>
            <input type="password" id="loginPassword" v-model="loginForm.password" required>
          </div>
          <button type="submit">登录</button>
        </form>
        <div class="switch-form">
          还没有账号？<a href="#" @click.prevent="activeForm = 'register'">立即注册</a>
        </div>
      </div>
      
      <!-- 注册表单 -->
      <div id="registerForm" class="form-container" :class="{ active: activeForm === 'register' }">
        <h2>用户注册</h2>
        <form @submit.prevent="handleRegister">
          <div class="form-group">
            <label for="registerUsername">用户名:</label>
            <input type="text" id="registerUsername" v-model="registerForm.username" required>
          </div>
          <div class="form-group">
            <label for="registerEmail">邮箱:</label>
            <input type="email" id="registerEmail" v-model="registerForm.email" required>
          </div>
          <div class="form-group">
            <label for="registerPassword">密码:</label>
            <input type="password" id="registerPassword" v-model="registerForm.password" required>
          </div>
          <div class="form-group">
            <label for="confirmPassword">确认密码:</label>
            <input type="password" id="confirmPassword" v-model="registerForm.confirmPassword" required>
          </div>
          <div class="form-group">
            <label for="registerNickname">昵称:</label>
            <input type="text" id="registerNickname" v-model="registerForm.nickname" required>
          </div>
          <button type="submit">注册</button>
        </form>
        <div class="switch-form">
          已有账号？<a href="#" @click.prevent="activeForm = 'login'">立即登录</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LoginView',
  data() {
    return {
      activeForm: 'login',
      message: '',
      messageType: '',
      loginForm: {
        username: '',
        password: ''
      },
      registerForm: {
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        nickname: ''
      }
    }
  },
  methods: {
    // 登录处理
    handleLogin() {
      fetch('/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(this.loginForm),
        credentials: 'same-origin'
      })
      .then(response => response.json())
      .then(data => {
        if (data.success) {
          this.showMessage(data.message, 'success');
          // 登录成功后跳转到主页
          setTimeout(() => {
            this.$router.push('/');
          }, 1000);
        } else {
          this.showMessage(data.message, 'error');
        }
      })
      .catch(error => {
        console.error('Error:', error);
        this.showMessage('登录失败，请稍后重试', 'error');
      });
    },
    
    // 注册处理
    handleRegister() {
      if (this.registerForm.password !== this.registerForm.confirmPassword) {
        this.showMessage('两次输入的密码不一致', 'error');
        return;
      }
      
      const user = {
        ...this.registerForm,
        status: 1
      };
      
      fetch('/api/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
      })
      .then(response => response.json())
      .then(data => {
        if (data.success) {
          this.showMessage(data.message, 'success');
          // 注册成功后清空表单并切换到登录表单
          setTimeout(() => {
            this.activeForm = 'login';
            this.registerForm = {
              username: '',
              email: '',
              password: '',
              confirmPassword: '',
              nickname: ''
            };
            this.message = '';
          }, 1500);
        } else {
          this.showMessage(data.message, 'error');
        }
      })
      .catch(error => {
        console.error('Error:', error);
        this.showMessage('注册失败，请稍后重试', 'error');
      });
    },
    
    // 显示消息
    showMessage(text, type) {
      this.message = text;
      this.messageType = type;
    },
    
    // 隐藏消息
    hideMessage() {
      this.message = '';
    }
  }
}
</script>

<style scoped>
.login-container {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 20px;
    background-image: url('/uploads/avatars/rem2.gif');
    background-size: cover;
    background-attachment: fixed;
    background-position: center;
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
}
    
    .container {
        max-width: 400px;
        margin: 50px auto;
        background: rgba(135, 206, 250, 0.2);
        backdrop-filter: blur(10px);
        -webkit-backdrop-filter: blur(10px);
        border-radius: 20px;
        border: 1px solid rgba(135, 206, 250, 0.2);
        box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.2);
        padding: 30px;
        position: relative;
        overflow: hidden;
    }
    
    .container::before {
        content: '';
        position: absolute;
        top: -2px;
        left: -2px;
        right: -2px;
        bottom: -2px;
        background: linear-gradient(45deg, #ff8a00, #e52e71, #22c1c3, #667eea);
        z-index: -1;
        border-radius: 17px;
        animation: borderAnimation 3s linear infinite;
        background-size: 300% 300%;
    }
    
    @keyframes borderAnimation {
        0% { background-position: 0% 50%; }
        50% { background-position: 100% 50%; }
        100% { background-position: 0% 50%; }
    }
    h1 {
        color: white;
        text-align: center;
        margin-bottom: 30px;
        text-shadow: 0 2px 4px rgba(0,0,0,0.3);
    }
    .form-group {
        margin-bottom: 20px;
    }
    label {
        display: block;
        margin-bottom: 5px;
        font-weight: bold;
        color: white;
        text-shadow: 0 1px 2px rgba(0,0,0,0.3);
    }
    input {
        width: 100%;
        padding: 12px;
        border: 1px solid rgba(135, 206, 250, 0.3);
        border-radius: 15px;
        box-sizing: border-box;
        background: rgba(135, 206, 250, 0.3);
        backdrop-filter: blur(5px);
        -webkit-backdrop-filter: blur(5px);
        color: white;
        font-size: 16px;
    }
    
    input::placeholder {
        color: rgba(255, 255, 255, 0.7);
    }
    
    input:focus {
        outline: none;
        border-color: rgba(255, 255, 255, 0.6);
        background: rgba(255, 255, 255, 0.3);
        box-shadow: 0 0 10px rgba(255, 255, 255, 0.2);
    }
    button {
        background: rgba(255, 107, 107, 0.8);
        color: white;
        padding: 12px 20px;
        border: 1px solid rgba(135, 206, 250, 0.3);
        border-radius: 15px;
        cursor: pointer;
        font-size: 16px;
        width: 100%;
        margin-top: 10px;
        backdrop-filter: blur(5px);
        -webkit-backdrop-filter: blur(5px);
        font-weight: bold;
        letter-spacing: 1px;
        transition: all 0.3s ease;
    }
    button:hover {
        background: rgba(255, 107, 107, 1);
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
    }
    button:active {
        transform: translateY(0);
    }
    .switch-form {
        text-align: center;
        margin-top: 20px;
        color: rgba(255, 255, 255, 0.8);
        text-shadow: 0 1px 2px rgba(0,0,0,0.3);
    }
    .switch-form a {
        color: #fff;
        text-decoration: none;
        font-weight: bold;
        padding: 8px 16px;
        border-radius: 20px;
        background: rgba(135, 206, 250, 0.3);
        transition: all 0.3s ease;
    }
    .switch-form a:hover {
        background: rgba(255, 255, 255, 0.2);
        text-decoration: none;
    }
    .form-container {
        display: none;
    }
    .form-container.active {
        display: block;
    }
    .message {
        padding: 15px;
        margin-bottom: 20px;
        border-radius: 8px;
        text-align: center;
        backdrop-filter: blur(10px);
        -webkit-backdrop-filter: blur(10px);
        border: 1px solid rgba(255, 255, 255, 0.18);
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
    }
    .error {
        background: rgba(248, 215, 218, 0.9);
        color: #721c24;
        border: 1px solid rgba(245, 198, 203, 0.5);
    }
    .success {
        background: rgba(212, 237, 218, 0.9);
        color: #155724;
        border: 1px solid rgba(195, 230, 203, 0.5);
    }
    .hidden {
        display: none;
    }
</style>