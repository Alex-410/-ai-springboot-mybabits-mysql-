  # 美食论坛项目说明

## 项目介绍
这是一个基于Spring Boot + MySQL + Vue 3的美食论坛项目，提供了帖子发布、评论、点赞、收藏等功能。项目支持纯HTML静态访问和Vue开发模式两种启动方式。

## 技术栈

### 后端
- Spring Boot 2.7.0
- MySQL
- MyBatis
- Spring Security

### 前端
- Vue 3
- Vue Router 4
- Vite 5
- 纯HTML/CSS/JavaScript

## 项目结构

```
├── src/
│   ├── main/
│   │   ├── java/          # 后端Java代码
│   │   ├── resources/     # 后端资源文件
│   │   │   ├── static/    # 前端静态资源
│   │   │   │   ├── src/   # Vue源代码
│   │   │   │   ├── index.html    # 主页面
│   │   │   │   └── other.html    # 其他页面
│   │   │   └── application.yml   # 后端配置文件
├── uploads/               # 上传文件存储目录
├── package.json           # 前端依赖配置
├── vite.config.js         # Vite配置文件
└── pom.xml                # Maven配置文件
```

## 环境准备

### 1. 安装Java和Maven
- JDK 11+ 版本
- Maven 3.6+ 版本

### 2. 安装Node.js和npm
- Node.js 16+ 版本
- npm 8+ 版本

### 3. 配置数据库
- 安装MySQL数据库
- 创建数据库：`food_forum`
- 执行SQL脚本：`database/food_forum_schema.sql` 初始化表结构

## 启动方式

### 方式一：纯HTML静态访问（推荐用于快速预览）

1. **启动后端服务**
   ```bash
   # 在项目根目录下执行
   mvn spring-boot:run
   ```

2. **访问静态页面**
   打开浏览器，访问以下地址：
   ```
   http://localhost:8080/index.html
   ```

3. **其他页面访问**
   ```
   http://localhost:8080/login.html       # 登录页面
   http://localhost:8080/register.html    # 注册页面
   http://localhost:8080/post.html        # 发布帖子页面
   http://localhost:8080/profile.html     # 个人资料页面
   http://localhost:8080/favorites.html   # 我的收藏页面
   http://localhost:8080/comments.html    # 评论管理页面
   ```

### 方式二：Vue开发模式启动（推荐用于开发）

1. **启动后端服务**
   ```bash
   # 在项目根目录下执行
   mvn spring-boot:run
   ```

2. **安装前端依赖**
   ```bash
   # 在项目根目录下执行
   npm install
   ```

3. **启动Vue开发服务器**
   ```bash
   # 在项目根目录下执行
   npm run dev
   ```

4. **访问Vue应用**
   打开浏览器，访问以下地址：
   ```
   http://localhost:3000
   ```

### 方式三：生产构建

1. **构建前端资源**
   ```bash
   # 在项目根目录下执行
   npm run build
   ```

2. **启动后端服务**
   ```bash
   # 在项目根目录下执行
   mvn spring-boot:run
   ```

3. **访问生产环境**
   打开浏览器，访问以下地址：
   ```
   http://localhost:8080
   ```

## 主要功能

1. **用户管理**
   - 注册
   - 登录
   - 个人资料管理

2. **帖子管理**
   - 发布帖子
   - 查看帖子列表
   - 搜索帖子
   - 分类浏览

3. **互动功能**
   - 点赞帖子
   - 收藏帖子
   - 评论功能
   - 回复评论

4. **其他功能**
   - 分页显示
   - 图片上传
   - 背景图显示

## API说明

### 主要API端点

1. **认证相关**
   - `GET /api/auth/status` - 检查登录状态
   - `POST /api/auth/login` - 用户登录
   - `POST /api/auth/logout` - 用户注销

2. **帖子相关**
   - `GET /api/posts/page-with-user` - 获取帖子列表（带用户信息）
   - `POST /api/posts` - 发布帖子
   - `GET /api/posts/{id}` - 获取帖子详情
   - `POST /api/posts/{id}/like` - 点赞帖子

3. **收藏相关**
   - `POST /api/favorites` - 收藏帖子
   - `GET /api/favorites/user/{userId}` - 获取用户收藏列表

4. **分类相关**
   - `GET /api/categories` - 获取所有分类

5. **文件上传**
   - `POST /api/upload/image` - 上传图片

## 注意事项

1. **数据库配置**
   - 默认数据库用户名：`root`
   - 默认数据库密码：`123456`
   - 如需修改，请到 `src/main/resources/application.yml` 中配置

2. **端口配置**
   - 后端默认端口：`8080`
   - Vue开发服务器默认端口：`3000`
   - 如需修改，请到对应的配置文件中调整

3. **静态资源**
   - 上传文件默认存储在 `uploads/` 目录下
   - 背景图路径：`uploads/rem.gif`
   - 默认头像路径：`uploads/avatars/default.png`

4. **跨域配置**
   - 后端已配置CORS，允许所有来源访问
   - Vue开发服务器已配置代理，将 `/api` 和 `/uploads` 请求转发到后端

5. **开发建议**
   - 开发阶段建议使用Vue开发模式启动，便于热更新和调试
   - 生产环境建议使用生产构建方式，性能更好
   - 数据库操作建议使用事务管理

## 常见问题

1. **Q：为什么图片无法显示？**
   A：请检查：
   - 后端服务是否正常运行
   - `uploads/` 目录是否存在
   - 图片路径是否正确
   - Vite配置中是否添加了 `/uploads` 代理

2. **Q：为什么后端内容无法渲染？**
   A：请检查：
   - 后端服务是否正常运行
   - API请求是否返回正确数据
   - 前端代码中API请求路径是否正确
   - 浏览器控制台是否有报错信息

3. **Q：为什么登录后无法访问某些页面？**
   A：请检查：
   - 用户权限是否正确
   - Spring Security配置是否正确
   - 前端路由守卫是否正确实现

## 联系方式

如有问题或建议，欢迎提出Issue或Pull Request。

---

**项目更新日志：**
- 2025-12-02：项目初始化
- 2025-12-07：添加帖子发布功能
- 2025-12-12：添加点赞和收藏功能
- 2025-12-14：修复图片显示问题，添加README文档
