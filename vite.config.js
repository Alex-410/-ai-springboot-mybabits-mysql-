import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  root: './src/main/resources/static',
  build: {
    outDir: '../../../../dist',
    emptyOutDir: true,
    rollupOptions: {
      input: {
        index: resolve(__dirname, './src/main/resources/static/index.html'),
        login: resolve(__dirname, './src/main/resources/static/login.html'),
        register: resolve(__dirname, './src/main/resources/static/register.html'),
        admin: resolve(__dirname, './src/main/resources/static/admin.html'),
        manage: resolve(__dirname, './src/main/resources/static/manage.html'),
        favorites: resolve(__dirname, './src/main/resources/static/favorites.html'),
        comments: resolve(__dirname, './src/main/resources/static/comments.html'),
        profile: resolve(__dirname, './src/main/resources/static/profile.html'),
        post: resolve(__dirname, './src/main/resources/static/post.html'),
        other: resolve(__dirname, './src/main/resources/static/other.html')
      }
    }
  },
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/uploads': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})