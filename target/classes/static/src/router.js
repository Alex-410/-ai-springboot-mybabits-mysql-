import { createRouter, createWebHistory } from 'vue-router'
import IndexView from './views/IndexView.vue'
import LoginView from './views/LoginView.vue'

// 定义路由配置
const routes = [
  {
    path: '/',
    name: 'index',
    component: IndexView
  },
  {
    path: '/index.html',
    name: 'indexHtml',
    component: IndexView
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/login.html',
    name: 'loginHtml',
    component: LoginView
  },
  {
    path: '/register.html',
    name: 'registerHtml',
    component: LoginView
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router