<template>
  <div>
    <div class="header">
      <div class="logo">美食论坛</div>
      <div class="user-info">
        <span id="welcomeMessage">{{ welcomeMessage }}</span>
        <img id="userAvatar" class="user-avatar" :class="{ hidden: !isLoggedIn }" :src="userAvatarUrl" alt="用户头像">
        <button id="logoutButton" :class="{ hidden: !isLoggedIn }" @click="logout">注销</button>
      </div>
      <div id="authSection" class="auth-section" :class="{ hidden: isLoggedIn }">
        <input type="text" id="username" v-model="loginForm.username" placeholder="用户名" required>
        <input type="password" id="password" v-model="loginForm.password" placeholder="密码" required>
        <button id="loginButton" @click="login">登录</button>
        <button id="registerButton" @click="goToRegister">注册</button>
      </div>
    </div>
    
    <div class="nav-links" id="navLinks" v-show="isLoggedIn">
      <a href="index.html" class="active">首页</a>
      <a href="profile.html">个人资料</a>
      <a href="comments.html">评论管理</a>
      <a href="favorites.html">我的收藏</a>
    </div>

    <div class="container">
      <div class="search-section">
        <input type="text" id="searchInput" v-model="searchForm.keyword" placeholder="搜索帖子...">
        <select id="categoryFilter" v-model="searchForm.categoryId">
          <option value="">所有分类</option>
          <option v-for="category in categories" :key="category.id" :value="category.id">{{ category.name }}</option>
        </select>
        <button id="searchButton" class="btn" @click="searchPosts">搜索</button>
        <button id="clearSearchButton" class="btn" @click="clearSearch">清除</button>
      </div>

      <div id="posts" class="posts">
        <div v-for="postWithUser in posts" :key="postWithUser.post.id" class="post">
          <div class="post-header">
            <div class="user-info">
              <img :src="postWithUser.user && postWithUser.user.avatar ? postWithUser.user.avatar : '/uploads/avatars/default.png'" alt="用户头像" class="user-avatar">
              <div class="user-details">
                <span class="user-nickname">{{ postWithUser.user ? postWithUser.user.nickname : '未知用户' }}</span>
                <span class="post-category">{{ postWithUser.categoryName || '未知分类' }}</span>
                <span class="post-date">{{ formatDate(postWithUser.post.createdAt) }}</span>
              </div>
            </div>
            <div class="post-actions">
              <button class="like-btn" :data-post-id="postWithUser.post.id" @click="likePost(postWithUser.post.id, $event)">
                <span class="heart-icon">❤</span>
                <span class="like-count">{{ postWithUser.post.likeCount || 0 }}</span>
              </button>
              <button class="favorite-btn" :data-post-id="postWithUser.post.id" @click="favoritePost(postWithUser.post.id, $event)">
                <span class="star-icon">★</span>
                <span class="favorite-count">{{ postWithUser.post.favoriteCount || 0 }}</span>
              </button>
            </div>
          </div>
          <h3 class="post-title">{{ postWithUser.post.title }}</h3>
          <p class="post-content">{{ postWithUser.post.content }}</p>
          
          <!-- 图片显示 -->
          <div v-if="postWithUser.post.imageUrls && postWithUser.post.imageUrls.length > 0" :class="getImageClass(postWithUser.post.imageUrls.length)">
            <img v-for="(url, index) in postWithUser.post.imageUrls" :key="index" :src="url" alt="帖子图片" :class="url.toLowerCase().endsWith('.gif') ? 'post-image gif-image' : 'post-image'">
          </div>

          <div class="comments-section" :id="`comments-${postWithUser.post.id}`">
            <h4>评论</h4>
            <div class="comments-list" :id="`comments-list-${postWithUser.post.id}`">
              <!-- 评论列表将通过子组件或方法渲染 -->
            </div>
            <form class="comment-form" :data-post-id="postWithUser.post.id" @submit.prevent="submitComment(postWithUser.post.id, $event)">
              <textarea class="comment-input" placeholder="写下你的评论..." required></textarea>
              <button type="submit">发表评论</button>
            </form>
          </div>
        </div>
      </div>

      <!-- 分页控件 -->
      <div v-if="totalPages > 1" class="pagination posts-pagination">
        <div class="pagination-controls">
          <button @click="loadPosts(currentPage - 1)" v-if="currentPage > 1">上一页</button>
          <span v-for="i in totalPages" :key="i" :class="{ 'current-page': i === currentPage }" @click="loadPosts(i)">
            {{ i }}
          </span>
          <button @click="loadPosts(currentPage + 1)" v-if="currentPage < totalPages">下一页</button>
        </div>
      </div>
    </div>

    <button id="floatingPostButton" class="floating-button" @click="goToPost">+</button>
  </div>
</template>

<script>
export default {
  name: 'IndexView',
  data() {
    return {
      // 登录状态
      isLoggedIn: false,
      welcomeMessage: '欢迎访问美食论坛',
      userAvatarUrl: '/uploads/avatars/default.png',
      
      // 登录表单
      loginForm: {
        username: '',
        password: ''
      },
      
      // 帖子数据
      posts: [],
      categories: [],
      
      // 搜索表单
      searchForm: {
        keyword: '',
        categoryId: ''
      },
      
      // 分页信息
      currentPage: 1,
      totalPages: 1,
      pageSize: 2
    }
  },
  mounted() {
    this.checkLoginStatus();
    this.loadCategories();
    this.loadPosts();
  },
  methods: {
    // 检查登录状态
    checkLoginStatus() {
      fetch('/api/auth/status')
        .then(response => response.json())
        .then(data => {
          if (data.loggedIn) {
            // 用户已登录
            this.isLoggedIn = true;
            this.welcomeMessage = `欢迎, ${data.user.username}!`;
            this.userAvatarUrl = data.user.avatar || '/uploads/avatars/default.png';
          } else {
            // 用户未登录
            this.isLoggedIn = false;
            this.welcomeMessage = '欢迎访问美食论坛';
          }
        })
        .catch(error => {
          console.error('Error:', error);
        });
    },
    
    // 登录
    login() {
      fetch('/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(this.loginForm)
      })
      .then(response => response.json())
      .then(data => {
        if (data.success) {
          // 登录成功，刷新页面
          location.reload();
        } else {
          alert(data.message);
        }
      })
      .catch(error => {
        console.error('Error:', error);
        alert('登录失败');
      });
    },
    
    // 注销
    logout() {
      fetch('/api/auth/logout', {
        method: 'POST',
        credentials: 'same-origin'
      })
      .then(response => response.json())
      .then(data => {
        if (data.success) {
          // 注销成功，刷新页面
          location.reload();
        }
      })
      .catch(error => {
        console.error('Error:', error);
      });
    },
    
    // 跳转到注册页面
    goToRegister() {
      window.location.href = 'register.html';
    },
    
    // 跳转到发布帖子页面
    goToPost() {
      // 检查用户是否已登录
      fetch('/api/auth/status')
        .then(response => response.json())
        .then(data => {
          if (data.loggedIn) {
            // 用户已登录，允许跳转到发布页面
            window.location.href = 'post.html';
          } else {
            // 用户未登录，显示提示信息
            alert('请先登录再发布帖子');
          }
        })
        .catch(error => {
          console.error('Error:', error);
          alert('检查登录状态失败');
        });
    },
    
    // 加载分类数据
    loadCategories() {
      fetch('/api/categories')
        .then(response => response.json())
        .then(categories => {
          this.categories = categories;
        })
        .catch(error => {
          console.error('Error loading categories:', error);
        });
    },
    
    // 加载帖子数据
    loadPosts(page = 1, size = this.pageSize) {
      fetch(`/api/posts/page-with-user?page=${page}&size=${size}`)
        .then(response => response.json())
        .then(pageResult => {
          this.posts = pageResult.data;
          this.currentPage = pageResult.page;
          this.totalPages = pageResult.totalPages;
        })
        .catch(error => {
          console.error('Error:', error);
        });
    },
    
    // 搜索帖子
    searchPosts() {
      let url = '/api/posts/search/page';
      const params = [];
      
      if (this.searchForm.keyword) {
        params.push(`keyword=${encodeURIComponent(this.searchForm.keyword)}`);
      }
      
      if (this.searchForm.categoryId) {
        params.push(`categoryId=${this.searchForm.categoryId}`);
      }
      
      // 添加分页参数
      params.push(`page=1`);
      params.push(`size=${this.pageSize}`);
      
      if (params.length > 0) {
        url += '?' + params.join('&');
      }
      
      fetch(url)
        .then(response => response.json())
        .then(pageResult => {
          this.posts = pageResult.data;
          this.currentPage = pageResult.page;
          this.totalPages = pageResult.totalPages;
        })
        .catch(error => {
          console.error('Error:', error);
        });
    },
    
    // 清除搜索
    clearSearch() {
      this.searchForm.keyword = '';
      this.searchForm.categoryId = '';
      this.loadPosts();
    },
    
    // 格式化日期
    formatDate(dateString) {
      const createdAt = new Date(dateString);
      return `${createdAt.getFullYear()}-${String(createdAt.getMonth() + 1).padStart(2, '0')}-${String(createdAt.getDate()).padStart(2, '0')}`;
    },
    
    // 获取图片容器类名
    getImageClass(imageCount) {
      let imagesClass = 'post-images';
      if (imageCount === 1) {
        imagesClass += ' single-image';
      } else if (imageCount === 2) {
        imagesClass += ' two-images';
      } else if (imageCount === 4) {
        imagesClass += ' four-images';
      }
      return imagesClass;
    },
    
    // 点赞帖子
    likePost(postId, event) {
      const likeCountEl = event.target.closest('.like-btn').querySelector('.like-count');
      
      // 发送请求到后端更新点赞数
      fetch(`/api/posts/${postId}/like`, { method: 'POST' })
        .then(response => response.text())
        .then(result => {
          console.log(result);
          // 更新前端显示的点赞数
          let count = parseInt(likeCountEl.textContent) || 0;
          count++;
          likeCountEl.textContent = count;
        })
        .catch(error => {
          console.error('Error:', error);
          // 即使后端出错，也更新前端显示（用户体验考虑）
          let count = parseInt(likeCountEl.textContent) || 0;
          count++;
          likeCountEl.textContent = count;
        });
    },
    
    // 收藏帖子
    favoritePost(postId, event) {
      const favoriteCountEl = event.target.closest('.favorite-btn').querySelector('.favorite-count');
      
      // 使用新的收藏API
      fetch('/api/favorites', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ postId: postId })
      })
      .then(response => response.json())
      .then(data => {
        if (data.success) {
          // 更新前端显示的收藏数
          favoriteCountEl.textContent = data.favoriteCount || 0;
        } else {
          console.error('收藏失败:', data.message);
          // 显示错误消息给用户
          alert(data.message);
        }
      })
      .catch(error => {
        console.error('Error:', error);
        alert('收藏操作失败，请稍后重试');
      });
    },
    
    // 提交评论
    submitComment(postId, event) {
      const commentInput = event.target.closest('.comment-form').querySelector('.comment-input');
      const content = commentInput.value.trim();
      
      if (content) {
        // 这里需要实现提交评论的逻辑
        console.log('提交评论:', postId, content);
        commentInput.value = '';
      }
    }
  }
}
</script>

<style>
/* 全局样式 */
body {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
  background-image: url('/uploads/rem.gif');
  background-size: cover;
  background-attachment: fixed;
  background-position: center;
  color: white;
  min-height: 100vh;
}
</style>

<style scoped>

.header {
  background: rgba(135, 206, 250, 0.3);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(135, 206, 250, 0.3);
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
  color: white;
  padding: 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.nav-links {
  margin: 10px 0;
  text-align: center;
}

.nav-links a {
  color: white;
  text-decoration: none;
  margin: 0 10px;
  font-weight: bold;
  padding: 8px 16px;
  border-radius: 20px;
  background: rgba(135, 206, 250, 0.3);
  transition: all 0.3s ease;
}

.nav-links a:hover {
  background: rgba(135, 206, 250, 0.5);
  text-decoration: none;
  transform: translateY(-2px);
}

.nav-links a.active {
  background: rgba(135, 206, 250, 0.6);
}

.logo {
  font-size: 1.5rem;
  font-weight: bold;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.user-info {
  display: flex;
  gap: 1rem;
}

.auth-section {
  display: flex;
  gap: 1rem;
}

.auth-section input {
  padding: 0.5rem;
  border: 1px solid rgba(135, 206, 250, 0.3);
  border-radius: 8px;
  background: rgba(135, 206, 250, 0.3);
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
  color: white;
}

.auth-section input::placeholder {
  color: rgba(255, 255, 255, 0.7);
}

.auth-section button {
  padding: 0.5rem 1rem;
  background: rgba(135, 206, 250, 0.3);
  color: white;
  border: 1px solid rgba(135, 206, 250, 0.3);
  border-radius: 8px;
  cursor: pointer;
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
  transition: all 0.3s ease;
}

.auth-section button:hover {
  background: rgba(135, 206, 250, 0.7);
  transform: translateY(-2px);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.container {
  max-width: 800px;
  margin: 2rem auto;
  padding: 0 1rem;
}

.search-section {
  background: rgba(135, 206, 250, 0.3);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(135, 206, 250, 0.3);
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
  padding: 1rem;
  border-radius: 15px;
  margin-bottom: 2rem;
  display: flex;
  gap: 1rem;
}

.search-section input {
  flex: 1;
  padding: 0.5rem;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  box-sizing: border-box;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
}

.search-section select {
  padding: 0.5rem;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  box-sizing: border-box;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
}

/* 修复下拉菜单选项的显示问题 */
.search-section select option {
  background-color: #333;
  color: white;
}

.search-section input::placeholder {
  color: rgba(255, 255, 255, 0.7);
}

.post-form {
  background: rgba(135, 206, 250, 0.5);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(135, 206, 250, 0.5);
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
  padding: 1rem;
  border-radius: 15px;
  margin-bottom: 2rem;
}

.post-form h2 {
  margin-top: 0;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
}

.form-group input, .form-group textarea, .form-group select {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  box-sizing: border-box;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
  color: white;
}

.form-group input::placeholder, .form-group textarea::placeholder, .form-group select::placeholder {
  color: rgba(255, 255, 255, 0.7);
}

.form-group textarea {
  height: 100px;
  resize: vertical;
}

.btn {
  padding: 0.5rem 1rem;
  background: rgba(255, 107, 107, 0.8);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  cursor: pointer;
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
  transition: all 0.3s ease;
  font-weight: bold;
  letter-spacing: 0.5px;
}

.btn:hover {
  background: rgba(255, 107, 107, 1);
  transform: translateY(-2px);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.btn:active {
  transform: translateY(0);
}

.posts {
  display: grid;
  gap: 1rem;
}

.post {
  background: rgba(135, 206, 250, 0.3);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1.5px solid rgba(135, 206, 250, 0.3);
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
  padding: 1rem;
  border-radius: 15px;
  position: relative;
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1rem;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  cursor: pointer;
  border: 2px solid rgba(255, 255, 255, 0.5);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.user-details {
  display: flex;
  flex-direction: column;
}

.user-nickname {
  font-weight: bold;
  color: #64b5f6;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.post-category {
  font-size: 0.8rem;
  color: #FFD700;
  background: rgba(255, 215, 0, 0.2);
  padding: 2px 8px;
  border-radius: 10px;
  display: inline-block;
  margin: 2px 0;
}

.post-date {
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 0.7);
}

.post-actions {
  display: flex;
  gap: 10px;
}

.like-btn, .favorite-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  background: rgba(135, 206, 250, 0.5);
  color: white;
  border: 1px solid rgba(135, 206, 250, 0.5);
  border-radius: 20px;
  padding: 5px 10px;
  cursor: pointer;
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
  transition: all 0.3s ease;
}

.like-btn:hover, .favorite-btn:hover {
  background: rgba(135, 206, 250, 0.7);
  transform: translateY(-2px);
}

.heart-icon, .star-icon {
  font-size: 1rem;
}

.like-count, .favorite-count {
  font-size: 0.9rem;
}

.post-title {
  margin: 0 0 0.5rem 0;
  color: white;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.post-content {
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 0.5rem;
}

.post-images {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-gap: 10px;
  margin-bottom: 15px;
}

.post-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
  aspect-ratio: 1/1;
}

.gif-image {
  /* 确保 GIF 图片正确显示动画 */
  image-rendering: -webkit-optimize-contrast;
  image-rendering: crisp-edges;
}

/* 单张图片居中显示 */
.post-images.single-image {
  display: flex;
  justify-content: center;
}

.post-images.single-image .post-image {
  max-width: 80%;
  aspect-ratio: auto;
}

/* 两张图片并排显示 */
.post-images.two-images {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-gap: 10px;
}

/* 四张图片网格显示 */
.post-images.four-images {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  grid-gap: 10px;
}

.comments-section {
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid rgba(255, 255, 255, 0.3);
}

.comment {
  background: rgba(135, 206, 250, 0.3);
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
  border: 1px solid rgba(135, 206, 250, 0.3);
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
  padding: 0.5rem;
  margin-bottom: 0.5rem;
  border-radius: 8px;
}

.comment-header {
  display: flex;
  align-items: center;
  margin-bottom: 0.5rem;
}

.comment-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 0.5rem;
  cursor: pointer;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.comment-nickname {
  font-weight: bold;
  cursor: pointer;
  color: #64b5f6;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.comment-nickname:hover {
  text-decoration: underline;
}

.comment-form {
  margin-top: 1rem;
}

.comment-input {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  box-sizing: border-box;
  margin-bottom: 0.5rem;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
}

.comment-input::placeholder {
  color: rgba(255, 255, 255, 0.7);
}

.reply-form {
  margin-top: 10px;
  padding: 10px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
}

.reply-input {
  width: 100%;
  padding: 8px;
  border-radius: 5px;
  box-sizing: border-box;
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: white;
  margin-bottom: 10px;
}

.reply-button {
  padding: 8px 15px;
  font-size: 14px;
}

.reply-comment {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.1);
  padding: 0.5rem;
  margin: 0.5rem 0 0.5rem 1rem;
  border-radius: 8px;
}

.hidden {
  display: none;
}

#logoutButton {
  padding: 0.5rem 1rem;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  cursor: pointer;
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
  transition: all 0.3s ease;
}

#logoutButton:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.floating-button {
  position: fixed;
  bottom: 20px;
  right: 20px;
  width: 60px;
  height: 60px;
  background: rgba(255, 107, 107, 0.8);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  font-size: 2rem;
  cursor: pointer;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
}

.floating-button:hover {
  background: rgba(255, 107, 107, 1);
  transform: translateY(-2px);
  box-shadow: 0 6px 25px rgba(0, 0, 0, 0.4);
}

/* 分页控件样式 */
.pagination {
  margin-top: 1rem;
  text-align: center;
}

.pagination-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.pagination-controls button {
  padding: 0.5rem 1rem;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  cursor: pointer;
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
  transition: all 0.3s ease;
}

.pagination-controls button:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.pagination-controls .current-page {
  padding: 0.5rem 1rem;
  background: rgba(255, 107, 107, 0.8);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
  cursor: pointer;
}

.posts-pagination {
  margin-top: 2rem;
  margin-bottom: 2rem;
}
</style>