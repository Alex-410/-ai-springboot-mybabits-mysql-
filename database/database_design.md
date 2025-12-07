# 美食论坛数据库设计文档

## 1. 用户表 (users)
存储用户基本信息

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    nickname VARCHAR(100) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像URL',
    gender TINYINT DEFAULT 0 COMMENT '性别：0未知，1男，2女',
    birthday DATE COMMENT '生日',
    signature VARCHAR(255) COMMENT '个性签名',
    phone VARCHAR(20) COMMENT '手机号',
    status TINYINT DEFAULT 1 COMMENT '状态：0禁用，1正常',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);
```

## 2. 角色表 (roles)
定义系统角色

```sql
CREATE TABLE roles (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '角色名称',
    description VARCHAR(255) COMMENT '角色描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);
```

## 3. 用户角色关联表 (user_roles)
处理用户和角色的多对多关系

```sql
CREATE TABLE user_roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id INT NOT NULL COMMENT '角色ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '分配时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_role (user_id, role_id)
);
```

## 4. 美食分类表 (categories)
帖子分类信息

```sql
CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '分类名称',
    description VARCHAR(255) COMMENT '分类描述',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0隐藏，1显示',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);
```

## 5. 帖子表 (posts)
存储用户发布的美食分享帖子

```sql
CREATE TABLE posts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '帖子ID',
    user_id BIGINT NOT NULL COMMENT '发布用户ID',
    category_id INT NOT NULL COMMENT '分类ID',
    title VARCHAR(200) NOT NULL COMMENT '帖子标题',
    content TEXT COMMENT '帖子内容',
    images TEXT COMMENT '图片URL列表，JSON格式',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    comment_count INT DEFAULT 0 COMMENT '评论数',
    is_top TINYINT DEFAULT 0 COMMENT '是否置顶：0否，1是',
    is_essence TINYINT DEFAULT 0 COMMENT '是否精华：0否，1是',
    status TINYINT DEFAULT 1 COMMENT '状态：0草稿，1已发布，2审核中，3已删除',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
```

## 6. 评论表 (comments)
存储用户对帖子的评论

```sql
CREATE TABLE comments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    user_id BIGINT NOT NULL COMMENT '评论用户ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父评论ID，0表示一级评论',
    reply_user_id BIGINT DEFAULT 0 COMMENT '被回复用户ID',
    content TEXT NOT NULL COMMENT '评论内容',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    status TINYINT DEFAULT 1 COMMENT '状态：0隐藏，1显示',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## 7. 点赞表 (likes)
记录用户点赞信息

```sql
CREATE TABLE likes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    likeable_type VARCHAR(20) NOT NULL COMMENT '点赞类型：post/comment',
    likeable_id BIGINT NOT NULL COMMENT '点赞对象ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_like (user_id, likeable_type, likeable_id)
);
```

## 8. 收藏表 (favorites)
记录用户收藏的帖子

```sql
CREATE TABLE favorites (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_post (user_id, post_id)
);
```

## 初始化数据

### 角色初始化
```sql
INSERT INTO roles (name, description) VALUES 
('USER', '普通用户'),
('ADMIN', '管理员'),
('MODERATOR', '版主');
```

### 分类初始化
```sql
INSERT INTO categories (name, description) VALUES 
('家常菜', '日常家庭菜肴'),
('川菜', '四川特色菜系'),
('粤菜', '广东特色菜系'),
('湘菜', '湖南特色菜系'),
('甜品', '各类甜点 dessert'),
('饮品', '各类饮品制作'),
('烘焙', '烘焙糕点类'),
('异国料理', '各国特色美食');
```