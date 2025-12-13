import { createRouter, createWebHistory } from 'vue-router'
import IndexView from './views/IndexView.vue'

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
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router