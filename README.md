  # 美食论坛项目说明

## 项目介绍
这是一个基于Spring Boot + MySQL + Vue 3的美食论坛项目，提供了帖子发布、评论、点赞、收藏等功能。项目支持纯HTML静态访问和Vue开发模式两种启动方式。

### AI协助开发
本项目在开发过程中使用了AI助手进行协助，包括但不限于：
- 代码结构设计和优化
- 前端Vue应用转换和组件开发
- 后端API设计和实现
- 数据库设计和优化
- 权限分离设计
- 项目文档撰写和更新
- 技术栈选择和配置建议

AI协助开发显著提高了开发效率，确保了代码质量和项目的完整性。

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
│   │   │   └── com/example/foodforum/
│   │   │       ├── controller/     # 后端API控制器（API编写位置）
│   │   │       ├── service/         # 业务逻辑层
│   │   │       ├── mapper/          # MyBatis映射层
│   │   │       └── entity/          # 实体类
│   │   ├── resources/     # 后端资源文件
│   │   │   ├── static/    # 前端静态资源
│   │   │   │   ├── src/   # Vue源代码
│   │   │   │   │   ├── views/       # Vue页面组件
│   │   │   │   │   ├── router.js    # Vue路由配置
│   │   │   │   │   └── main.js      # Vue入口文件
│   │   │   │   ├── index.html       # 主页面
│   │   │   │   └── other.html       # 其他页面
│   │   │   └── application.yml      # 后端配置文件
├── uploads/               # 上传文件存储目录
├── package.json           # 前端依赖配置
├── vite.config.js         # Vite配置文件（前端代理配置）
└── pom.xml                # Maven配置文件
```

## 前后端分离实现

### 1. 前后端分离架构

本项目采用了前后端分离的架构设计，具体实现如下：

#### 后端设计
- **技术栈**：Spring Boot 2.7.0 + MyBatis + MySQL
- **核心职责**：提供RESTful API接口，处理业务逻辑和数据持久化
- **部署方式**：独立部署，作为API服务器运行

#### 前端设计
- **技术栈**：Vue 3 + Vue Router 4 + Vite 5
- **核心职责**：实现用户界面，通过API调用与后端交互
- **部署方式**：
  - 开发模式：独立运行在Vite开发服务器上
  - 生产模式：构建后作为静态资源部署在后端服务器或独立的静态资源服务器上

### 2. 后端API接口说明

#### API基础地址
- **开发环境**：
  - 后端服务地址：`http://localhost:8080`
  - API基础路径：`http://localhost:8080/api`
- **生产环境**：
  - 根据实际部署情况，API地址为服务器域名或IP + `/api` 路径

#### API编写位置
- **文件路径**：`src/main/java/com/example/foodforum/controller/`
- **主要控制器**：
  - `AuthController.java` - 认证相关API（登录、注册、登出等）
  - `PostController.java` - 帖子相关API（发布、查询、点赞等）
  - `CommentController.java` - 评论相关API（发布、查询、删除等）
  - `FavoriteController.java` - 收藏相关API（收藏、取消收藏等）
  - `CategoryController.java` - 分类相关API（查询分类等）
  - `AdminController.java` - 管理员登录API
  - `AdminManagementController.java` - 管理员管理功能API

#### API命名规范
- 采用RESTful API设计风格
- 使用HTTP方法区分操作类型：
  - `GET` - 获取资源
  - `POST` - 创建资源
  - `PUT` - 更新资源
  - `DELETE` - 删除资源
- URL使用小写字母，单词之间用连字符（-）分隔
- 资源名称使用复数形式

### 3. 前端API调用方式

#### 代理配置
- **配置文件**：`vite.config.js`
- **代理规则**：
  ```javascript
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
  ```

#### 调用示例
- **在Vue组件中调用API**：
  ```javascript
  // 获取帖子列表
  fetch('/api/posts/page-with-user')
    .then(response => response.json())
    .then(data => {
      // 处理返回数据
    })
    .catch(error => {
      // 处理错误
    });
  
  // 登录
  fetch('/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(loginForm),
    credentials: 'same-origin'
  })
  .then(response => response.json())
  .then(data => {
    // 处理登录结果
  });
  ```

### 4. 跨域处理

#### 后端跨域配置
- **配置文件**：`src/main/java/com/example/foodforum/config/CorsConfig.java`
- **实现方式**：使用Spring Boot的CORS配置，允许所有来源访问

#### 前端代理处理
- **开发环境**：通过Vite代理配置，将API请求转发到后端服务器
- **生产环境**：
  - 方式一：前端静态资源部署在后端服务器上，通过相对路径调用API
  - 方式二：使用Nginx等反向代理服务器，统一处理前端和后端请求

### 5. 前后端数据交互格式

- **数据格式**：JSON
- **响应格式**：
  ```json
  {
    "success": true,        // 操作是否成功
    "message": "操作成功",   // 响应消息
    "data": {}             // 响应数据（可选）
  }
  ```

### 6. 前后端分离的优势

1. **开发效率提升**：前后端开发可以并行进行，互不影响
2. **技术栈灵活**：前后端可以使用各自最适合的技术栈
3. **代码维护性好**：前后端代码分离，职责明确，便于维护和扩展
4. **部署灵活**：可以独立部署，便于水平扩展
5. **用户体验优化**：前端可以实现更流畅的交互效果

### 7. 启动和访问方式

#### 开发模式
1. **启动后端服务**：
   ```bash
   mvn spring-boot:run
   ```
   - 后端服务地址：`http://localhost:8080`
   - API访问地址：`http://localhost:8080/api/...`

2. **启动前端开发服务器**：
   ```bash
   npm run dev
   ```
   - 前端访问地址：`http://localhost:3000`
   - 前端通过代理访问后端API：`http://localhost:3000/api/...`

#### 生产模式
1. **构建前端资源**：
   ```bash
   npm run build
   ```

2. **启动后端服务**：
   ```bash
   mvn spring-boot:run
   ```

3. **访问应用**：
   - 访问地址：`http://localhost:8080`
   - 前端静态资源由后端服务器提供
   - API访问地址：`http://localhost:8080/api/...`

## 环境准备

### 1. 安装Java和Maven
- JDK 11+ 版本
- Maven 3.6+ 版本

### 2. 安装Node.js和npm
- Node.js 16+ 版本
- npm 8+ 版本

### 3. 配置数据库
- 安装MySQL数据库
- 创建数据库：`pbl`
- 执行SQL脚本：`database/pbl.sql` 初始化表结构

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

## 数据库设计说明

### 1. 数据库概述

本项目使用MySQL数据库，设计了8张核心表，实现了完整的CRUD（创建、读取、更新、删除）操作，支持美食论坛的所有功能。数据库设计遵循了关系型数据库的最佳实践，包括合理的表结构设计、主键外键约束、索引优化等。

### 2. 数据库文件

- **主要数据库文件**：`database/pbl.sql`（包含完整的表结构和初始数据）
- **数据库设计文档**：`database/database_design.md`（详细的数据库设计说明）

### 3. 数据库导入方法

#### 方法一：使用MySQL命令行

```bash
# 登录MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE pbl CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入SQL文件
USE pbl;
SOURCE /path/to/project/database/pbl.sql;
```

#### 方法二：使用phpMyAdmin或Navicat

1. 登录phpMyAdmin或Navicat
2. 创建数据库 `pbl`，字符集选择 `utf8mb4`，排序规则选择 `utf8mb4_unicode_ci`
3. 导入 `database/pbl.sql` 文件

### 4. 数据库配置修改

**重要提示**：请根据您自己的数据库实际情况修改以下配置！

如需修改数据库连接配置，请到 `src/main/resources/application.yml` 文件中修改：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/pbl?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&connectionCollation=utf8mb4_unicode_ci
    username: root          # 数据库用户名（根据您的实际情况修改）
    password: 926953llk     # 数据库密码（根据您的实际情况修改）
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      connection-init-sql: SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci
```

**需要修改的配置项**：
- `url` 中的 `3306`：数据库端口（默认3306，如有修改请调整）
- `url` 中的 `pbl`：数据库名（根据您创建的数据库名修改）
- `username`：数据库用户名（默认root，如有修改请调整）
- `password`：数据库密码（根据您的实际密码修改）

**示例**：如果您的数据库名是 `food_forum`，用户名是 `admin`，密码是 `123456`，则配置应改为：
```yaml
url: jdbc:mysql://localhost:3306/food_forum?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&connectionCollation=utf8mb4_unicode_ci
username: admin
password: 123456
```

### 5. 核心数据库表设计

#### 5.1 用户相关表

##### users - 用户表
- **功能**：存储系统所有用户的基本信息
- **核心字段**：id, username, email, password, nickname, avatar, status
- **CRUD实现**：
  - 创建：用户注册时插入新用户
  - 读取：登录验证、获取用户信息
  - 更新：修改个人资料、头像
  - 删除：管理员删除用户

##### roles - 角色表
- **功能**：定义系统中的角色类型
- **核心字段**：id, name, description
- **CRUD实现**：
  - 创建：系统初始化时插入角色
  - 读取：获取角色列表
  - 更新：修改角色信息
  - 删除：删除角色

##### user_roles - 用户角色关联表
- **功能**：建立用户与角色的多对多关系
- **核心字段**：id, user_id, role_id
- **CRUD实现**：
  - 创建：为用户分配角色
  - 读取：获取用户角色、角色用户列表
  - 更新：修改用户角色
  - 删除：取消用户角色

#### 5.2 内容相关表

##### categories - 美食分类表
- **功能**：定义帖子的分类
- **核心字段**：id, name, description, status
- **CRUD实现**：
  - 创建：管理员创建新分类
  - 读取：获取分类列表、按分类查询帖子
  - 更新：修改分类信息
  - 删除：删除分类

##### posts - 帖子表
- **功能**：存储用户发布的美食分享帖子
- **核心字段**：id, user_id, category_id, title, content, images, view_count, like_count, comment_count, status
- **CRUD实现**：
  - 创建：用户发布新帖子
  - 读取：获取帖子列表、帖子详情
  - 更新：修改帖子内容
  - 删除：删除帖子

##### comments - 评论表
- **功能**：存储用户对帖子的评论和回复
- **核心字段**：id, post_id, user_id, parent_id, reply_user_id, content, status
- **CRUD实现**：
  - 创建：用户发布评论
  - 读取：获取帖子评论列表
  - 更新：修改评论内容
  - 删除：删除评论

#### 5.3 互动相关表

##### likes - 点赞表
- **功能**：记录用户对帖子和评论的点赞信息
- **核心字段**：id, user_id, likeable_type, likeable_id
- **CRUD实现**：
  - 创建：用户点赞
  - 读取：获取点赞数量、检查是否点赞
  - 删除：取消点赞

##### favorites - 收藏表
- **功能**：记录用户收藏的帖子
- **核心字段**：id, user_id, post_id
- **CRUD实现**：
  - 创建：用户收藏帖子
  - 读取：获取用户收藏列表、检查是否收藏
  - 删除：取消收藏

### 6. 数据库关系设计

- **用户与角色**：多对多关系，通过user_roles表关联
- **用户与帖子**：一对多关系，一个用户可以发布多个帖子
- **帖子与分类**：多对一关系，一个帖子属于一个分类，一个分类包含多个帖子
- **帖子与评论**：一对多关系，一个帖子可以有多个评论
- **评论与评论**：自关联关系，支持评论回复
- **用户与评论**：一对多关系，一个用户可以发布多个评论
- **用户与点赞**：一对多关系，一个用户可以点赞多个内容
- **用户与收藏**：一对多关系，一个用户可以收藏多个帖子

### 7. 数据库设计特点

1. **完整性约束**：
   - 所有表都有主键约束
   - 外键约束确保数据一致性
   - 唯一约束防止重复数据

2. **性能优化**：
   - 合理的索引设计（如帖子表的user_id和category_id索引）
   - 分表设计（将点赞、收藏等高频操作单独建表）

3. **可扩展性**：
   - 模块化设计，便于添加新功能
   - 角色权限系统支持灵活的权限管理

4. **数据安全性**：
   - 用户密码加密存储
   - 状态字段控制数据可见性

### 8. 数据库接口

后端提供了完整的RESTful API接口，支持对所有表的CRUD操作：

#### 用户相关
- `GET /api/auth/status` - 检查登录状态
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/upload-avatar` - 上传头像

#### 帖子相关
- `GET /api/posts/page-with-user` - 分页获取帖子列表（带用户信息）
- `POST /api/posts` - 发布帖子
- `GET /api/posts/{id}` - 获取帖子详情
- `PUT /api/posts/{id}` - 更新帖子
- `DELETE /api/posts/{id}` - 删除帖子
- `POST /api/posts/{id}/like` - 点赞帖子

#### 评论相关
- `GET /api/comments/post/{postId}` - 获取帖子的评论列表
- `POST /api/comments` - 发布评论
- `PUT /api/comments/{id}` - 更新评论
- `DELETE /api/comments/{id}` - 删除评论

#### 收藏相关
- `POST /api/favorites` - 收藏帖子
- `DELETE /api/favorites/{id}` - 取消收藏
- `GET /api/favorites/user/{userId}` - 获取用户收藏列表
- `GET /api/favorites/user/{userId}/post/{postId}` - 检查用户是否收藏了帖子

#### 分类相关
- `GET /api/categories` - 获取所有分类
- `POST /api/categories` - 创建分类
- `PUT /api/categories/{id}` - 更新分类
- `DELETE /api/categories/{id}` - 删除分类

### 9. 初始数据

数据库SQL文件中包含以下初始数据：

- 3个角色：`USER`（普通用户）、`ADMIN`（管理员）、`MODERATOR`（版主）
- 8个美食分类：家常菜谱、川菜系列、粤菜系列、湘菜系列、甜品系列、饮品系列、烘焙系列、异国料理
- 4个初始用户：
  - 管理员：用户名 `admin`，密码 `1234`
  - 普通用户：用户名 `123`，密码 `123`
  - 测试用户：用户名 `lvlinkun`，密码 `926953llk`
  - 测试用户：用户名 `admin1234`，密码 `123456`
- 多条测试帖子、评论、收藏数据

### 10. 数据库使用建议

1. **数据备份**：定期备份数据库，防止数据丢失
2. **索引优化**：根据实际查询需求优化索引
3. **性能监控**：监控数据库性能，及时优化慢查询
4. **安全加固**：
   - 使用强密码策略
   - 限制数据库用户权限
   - 定期更新数据库版本
5. **数据迁移**：进行数据迁移时，确保数据完整性和一致性

## 登录注册逻辑说明

### 1. 前端登录注册文件

#### Vue登录组件
- **文件路径**：`src/main/resources/static/src/views/LoginView.vue`
- **功能**：实现用户登录和注册表单，处理表单提交和API调用
- **主要功能**：
  - 登录表单：收集用户名和密码，调用登录API
  - 注册表单：收集用户注册信息，进行表单验证，调用注册API
  - 表单切换：在登录和注册表单之间切换
  - 消息提示：显示登录/注册成功或失败的消息

#### 前端路由配置
- **文件路径**：`src/main/resources/static/src/router.js`
- **功能**：配置登录页面路由，将 `/login`、`/login.html` 和 `/register.html` 映射到登录组件

#### 静态登录页面
- **文件路径**：`src/main/resources/static/login.html` 和 `src/main/resources/static/register.html`
- **功能**：纯HTML版本的登录和注册页面，用于静态访问方式

### 2. 后端登录注册文件

#### 认证控制器
- **文件路径**：`src/main/java/com/example/foodforum/controller/AuthController.java`
- **功能**：处理所有认证相关的API请求
- **主要接口**：
  - `POST /api/auth/register` - 用户注册
  - `POST /api/auth/login` - 用户登录
  - `POST /api/auth/logout` - 用户登出
  - `GET /api/auth/status` - 检查登录状态
  - `POST /api/auth/upload-avatar` - 上传用户头像

#### 用户服务
- **文件路径**：`src/main/java/com/example/foodforum/service/UserService.java`
- **功能**：提供用户相关的业务逻辑，包括用户注册、登录验证等

#### 用户实体类
- **文件路径**：`src/main/java/com/example/foodforum/entity/User.java`
- **功能**：定义用户数据模型，包含用户名、密码、邮箱等字段

### 3. 登录注册流程

#### 注册流程
1. 用户在前端填写注册表单（用户名、邮箱、密码、昵称等）
2. 前端进行表单验证（如密码一致性检查）
3. 前端发送POST请求到 `/api/auth/register`
4. 后端AuthController接收请求，调用UserService进行用户名重复性检查
5. UserService将新用户信息插入到数据库
6. 后端返回注册结果给前端
7. 前端显示注册成功或失败的消息

#### 登录流程
1. 用户在前端填写登录表单（用户名、密码）
2. 前端发送POST请求到 `/api/auth/login`
3. 后端AuthController接收请求，调用UserService根据用户名查找用户
4. 后端验证密码是否正确
5. 验证成功后，将用户信息保存到Session中
6. 后端返回登录结果和用户信息给前端
7. 前端显示登录成功或失败的消息
8. 登录成功后，前端跳转到首页

#### 登录状态检查
1. 前端页面加载时，发送GET请求到 `/api/auth/status`
2. 后端检查Session中是否存在用户信息
3. 后端返回登录状态和用户信息给前端
4. 前端根据返回结果更新页面显示（如显示/隐藏登录按钮、显示用户头像等）

## 权限分离说明

### 1. 角色定义

项目中实现了权限分离，主要分为两种角色：

#### 普通用户（USER）
- **默认角色**：所有新注册用户自动获得此角色
- **登录方式**：通过普通登录表单登录
- **具体操作权限**：
  
  **浏览功能**：
  - 浏览所有帖子列表
  - 按分类浏览帖子
  - 搜索帖子
  - 查看帖子详情
  - 查看帖子评论
  - 查看分类列表
  
  **发布功能**：
  - 发布新帖子
  - 上传帖子图片
  - 发布评论
  - 回复评论
  
  **互动功能**：
  - 点赞帖子
  - 收藏帖子
  - 查看自己的收藏列表
  - 取消点赞
  - 取消收藏
  
  **个人管理**：
  - 修改个人资料（昵称、性别、生日、个性签名等）
  - 上传和修改头像
  - 修改密码
  - 查看自己的帖子和评论

#### 管理员（ADMIN）
- **特殊角色**：拥有系统最高权限
- **登录方式**：通过专门的管理员登录入口登录
- **管理员账号说明**：
  - 数据库初始管理员：用户名 `admin`，密码 `1234`（具有ADMIN角色）
  - 系统硬编码管理员：用户名 `root`，密码 `926953llk`（用于直接管理员登录）
- **具体操作权限**：
  
  **所有普通用户的权限**：
  - 拥有普通用户的所有操作权限
  
  **用户管理**：
  - 查看所有用户列表
  - 查看单个用户详情
  - 禁用/启用用户账号
  - 删除用户账号
  - 修改用户角色
  
  **帖子管理**：
  - 查看所有帖子（包括草稿和审核中的帖子）
  - 审核帖子（通过/拒绝）
  - 置顶/取消置顶帖子
  - 设为精华/取消精华帖子
  - 修改帖子内容
  - 删除帖子
  - 查看帖子统计数据
  
  **评论管理**：
  - 查看所有评论
  - 审核评论（通过/拒绝）
  - 修改评论内容
  - 删除评论
  
  **分类管理**：
  - 查看所有分类
  - 创建新分类
  - 修改分类信息
  - 删除分类
  - 调整分类顺序
  
  **系统管理**：
  - 管理上传文件
  - 查看系统日志
  - 管理系统配置
  - 管理角色和权限

### 2. 权限实现机制

#### 后端权限检查
- **文件路径**：`src/main/java/com/example/foodforum/controller/AdminController.java` 和 `src/main/java/com/example/foodforum/controller/AdminManagementController.java`
- **实现方式**：
  - 管理员登录后，在Session中设置 `isAdmin` 标志
  - 所有管理员API接口都通过 `checkAdminPermission()` 方法检查权限
  - 非管理员访问管理员API会返回 "未授权访问" 错误

#### 前端权限控制
- **实现方式**：
  - 前端根据登录状态和用户角色显示不同的菜单和功能
  - 管理员可以访问专门的管理页面
  - 普通用户无法访问管理员功能

### 3. 管理员功能

#### 管理员登录
- **API**：`POST /api/admin/login`
- **功能**：验证管理员账号密码，设置管理员Session

#### 管理员状态检查
- **API**：`GET /api/admin/status`
- **功能**：检查管理员是否已登录

#### 用户管理
- **API**：`GET /api/admin/manage/users`、`PUT /api/admin/manage/users/{userId}/status`、`DELETE /api/admin/manage/users/{userId}`
- **功能**：查看所有用户、修改用户状态、删除用户

#### 帖子管理
- **API**：`GET /api/admin/manage/posts`、`PUT /api/admin/manage/posts/{postId}/status`、`DELETE /api/admin/manage/posts/{postId}`
- **功能**：查看所有帖子、修改帖子状态、删除帖子

#### 评论管理
- **API**：`GET /api/admin/manage/comments`、`PUT /api/admin/manage/comments/{commentId}/status`、`DELETE /api/admin/manage/comments/{commentId}`
- **功能**：查看所有评论、修改评论状态、删除评论

#### 分类管理
- **API**：`GET /api/admin/manage/categories`、`POST /api/admin/manage/categories`、`PUT /api/admin/manage/categories/{categoryId}`、`DELETE /api/admin/manage/categories/{categoryId}`
- **功能**：查看所有分类、创建分类、修改分类、删除分类

### 4. 权限分离的优势

1. **安全性**：防止普通用户误操作或恶意操作系统
2. **职责明确**：不同角色有不同的职责和权限
3. **可扩展性**：便于后续添加更多角色和权限
4. **管理效率**：管理员可以集中管理系统资源

### 5. 注意事项

- 管理员账号密码是系统默认配置，建议在生产环境中修改
- 管理员操作具有高风险，建议谨慎使用
- 所有管理员操作都应记录日志，便于审计

## 注意事项

1. **端口配置**
   - 后端默认端口：`8080`
   - Vue开发服务器默认端口：`3000`
   - 如需修改，请到对应的配置文件中调整

2. **静态资源**
   - 上传文件默认存储在 `uploads/` 目录下
   - 背景图路径：`uploads/rem.gif`
   - 默认头像路径：`uploads/avatars/default.png`

3. **跨域配置**
   - 后端已配置CORS，允许所有来源访问
   - Vue开发服务器已配置代理，将 `/api` 和 `/uploads` 请求转发到后端

4. **开发建议**
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
