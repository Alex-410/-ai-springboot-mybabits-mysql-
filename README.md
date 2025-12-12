# -ai-springboot-mybabits-mysql-
基于ai编写由springboot、mybabits、mysql的美食论坛系统，呵呵其实是期末作业要求上传github
# 美食论坛 (Food Forum)

一个基于Spring Boot + MySQL + MyBatis的美食分享论坛系统。

## 项目结构

```
food-forum/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/foodforum/
│   │   │       ├── controller/     # 控制器层
│   │   │       ├── entity/         # 实体类
│   │   │       ├── mapper/         # MyBatis映射接口
│   │   │       ├── service/        # 业务逻辑层
│   │   │       └── FoodForumApplication.java  # 启动类
│   │   └── resources/
│   │       ├── mapper/             # MyBatis映射文件
│   │       ├── static/             # 静态资源（CSS, JS, 图片）
│   │       ├── templates/          # 模板文件
│   │       └── application.yml     # 配置文件
├── database/
│   └── food_forum_schema.sql       # 数据库脚本
├── pom.xml                         # Maven配置文件
└── README.md
```

## 技术栈

- **后端**: Spring Boot 2.7.0, MyBatis
- **数据库**: MySQL
- **前端**: HTML, CSS, JavaScript
- **构建工具**: Maven

## 数据库配置

1. 确保MySQL服务正在运行
2. 创建数据库: `CREATE DATABASE pbl CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`
3. 执行 `database/food_forum_schema.sql` 脚本创建表结构和初始化数据

## 如何运行

### 方法一：使用Maven运行（推荐）

1. 克隆或下载项目代码
2. 确保已安装Java 8+和Maven
3. 修改 `src/main/resources/application.yml` 中的数据库连接配置
4. 在项目根目录下运行以下命令：

```bash
mvn clean install
mvn spring-boot:run
```

### 方法二：打包后运行

1. 打包项目：
```bash
mvn clean package
```

2. 运行生成的JAR文件：
```bash
java -jar target/food-forum-0.0.1-SNAPSHOT.jar
```

### 方法三：如果未安装Maven

如果您没有安装Maven或无法使用mvn命令，请直接使用Java运行已打包的JAR文件：

```bash
java -jar target/food-forum-0.0.1-SNAPSHOT.jar
```

注意：您需要确保系统中已安装Java 8或更高版本。

## 访问应用

启动应用后，访问地址: http://localhost:8080

## API接口

### 用户相关
- GET `/api/users` - 获取所有用户
- GET `/api/users/{id}` - 根据ID获取用户
- POST `/api/users` - 创建用户
- PUT `/api/users/{id}` - 更新用户
- DELETE `/api/users/{id}` - 删除用户

### 帖子相关
- GET `/api/posts` - 获取所有帖子
- GET `/api/posts/{id}` - 根据ID获取帖子
- GET `/api/posts/user/{userId}` - 根据用户ID获取帖子
- GET `/api/posts/category/{categoryId}` - 根据分类ID获取帖子
- POST `/api/posts` - 创建帖子
- PUT `/api/posts/{id}` - 更新帖子
- DELETE `/api/posts/{id}` - 删除帖子

### 收藏相关
- POST `/api/favorites` - 添加收藏
- DELETE `/api/favorites` - 取消收藏
- GET `/api/favorites/status` - 查询收藏状态
- GET `/api/favorites/user/{userId}` - 获取用户收藏列表

## 数据库表结构

1. **users** - 用户表
2. **roles** - 角色表
3. **user_roles** - 用户角色关联表
4. **categories** - 美食分类表
5. **posts** - 帖子表
6. **comments** - 评论表
7. **likes** - 点赞表
8. **favorites** - 收藏表

## 开发说明

1. 控制器层: 处理HTTP请求
2. 服务层: 实现业务逻辑
3. Mapper层: 数据库操作接口
4. 实体类: 与数据库表对应的Java类

## 注意事项

1. 项目使用了MyBatis注解方式编写SQL，如需使用XML配置方式，可在`resources/mapper`目录下创建对应的XML文件
2. 数据库连接信息需要根据实际环境进行配置
3. 项目默认端口为8080，可在`application.yml`中修改
4. 如果遇到中文乱码问题，请确保数据库使用utf8mb4字符集
