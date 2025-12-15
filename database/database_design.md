# 美食论坛数据库设计文档

## 1. 用户表 (users)
存储用户基本信息

```sql
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'User ID',
  `username` varchar(50) NOT NULL COMMENT 'Username',
  `email` varchar(100) NOT NULL COMMENT 'Email',
  `password` varchar(255) NOT NULL COMMENT 'Password',
  `nickname` varchar(100) DEFAULT NULL COMMENT 'Nickname',
  `avatar` varchar(255) DEFAULT NULL COMMENT 'Avatar URL',
  `gender` tinyint DEFAULT '0' COMMENT 'Gender: 0-unknown, 1-male, 2-female',
  `birthday` date DEFAULT NULL COMMENT 'Birthday',
  `signature` varchar(255) DEFAULT NULL COMMENT 'Signature',
  `phone` varchar(20) DEFAULT NULL COMMENT 'Phone',
  `status` tinyint DEFAULT '1' COMMENT 'Status: 0-disabled, 1-active',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created Time',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated Time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

## 2. 角色表 (roles)
定义系统角色

```sql
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'Role ID',
  `name` varchar(50) NOT NULL COMMENT 'Role Name',
  `description` varchar(255) DEFAULT NULL COMMENT 'Role Description',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created Time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

## 3. 用户角色关联表 (user_roles)
处理用户和角色的多对多关系

```sql
CREATE TABLE `user_roles` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
  `user_id` bigint NOT NULL COMMENT 'User ID',
  `role_id` int NOT NULL COMMENT 'Role ID',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Assigned Time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `user_roles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `user_roles_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

## 4. 美食分类表 (categories)
帖子分类信息

```sql
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'Category ID',
  `name` varchar(50) NOT NULL COMMENT 'Category Name',
  `description` varchar(255) DEFAULT NULL COMMENT 'Category Description',
  `sort_order` int DEFAULT '0' COMMENT 'Sort Order',
  `status` tinyint DEFAULT '1' COMMENT 'Status: 0-hidden, 1-visible',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created Time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

## 5. 帖子表 (posts)
存储用户发布的美食分享帖子

```sql
CREATE TABLE `posts` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Post ID',
  `user_id` bigint NOT NULL COMMENT 'Author User ID',
  `category_id` int NOT NULL COMMENT 'Category ID',
  `title` varchar(200) NOT NULL COMMENT 'Post Title',
  `content` text COMMENT 'Post Content',
  `images` text COMMENT 'Image URLs (JSON format)',
  `view_count` int DEFAULT '0' COMMENT 'View Count',
  `like_count` int DEFAULT '0' COMMENT 'Like Count',
  `comment_count` int DEFAULT '0' COMMENT 'Comment Count',
  `favorite_count` int DEFAULT '0' COMMENT 'Favorite Count',
  `is_top` tinyint DEFAULT '0' COMMENT 'Top Post: 0-no, 1-yes',
  `is_essence` tinyint DEFAULT '0' COMMENT 'Essence Post: 0-no, 1-yes',
  `status` tinyint DEFAULT '1' COMMENT 'Status: 0-draft, 1-published, 2-reviewing, 3-deleted',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created Time',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated Time',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `posts_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

## 6. 评论表 (comments)
存储用户对帖子的评论

```sql
CREATE TABLE `comments` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Comment ID',
  `post_id` bigint NOT NULL COMMENT 'Post ID',
  `user_id` bigint NOT NULL COMMENT 'Commenter User ID',
  `parent_id` bigint DEFAULT '0' COMMENT 'Parent Comment ID (0 for top-level)',
  `reply_user_id` bigint DEFAULT '0' COMMENT 'Replied User ID',
  `content` text NOT NULL COMMENT 'Comment Content',
  `like_count` int DEFAULT '0' COMMENT 'Like Count',
  `status` tinyint DEFAULT '1' COMMENT 'Status: 0-hidden, 1-visible',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created Time',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated Time',
  PRIMARY KEY (`id`),
  KEY `post_id` (`post_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON DELETE CASCADE,
  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

## 7. 点赞表 (likes)
记录用户点赞信息

```sql
CREATE TABLE `likes` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
  `user_id` bigint NOT NULL COMMENT 'User ID',
  `likeable_type` varchar(20) NOT NULL COMMENT 'Like Type: post/comment',
  `likeable_id` bigint NOT NULL COMMENT 'Liked Object ID',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Like Time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_like` (`user_id`,`likeable_type`,`likeable_id`),
  CONSTRAINT `likes_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

## 8. 收藏表 (favorites)
记录用户收藏的帖子

```sql
CREATE TABLE `favorites` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
  `user_id` bigint NOT NULL COMMENT 'User ID',
  `post_id` bigint NOT NULL COMMENT 'Post ID',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Favorite Time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_post` (`user_id`,`post_id`),
  KEY `post_id` (`post_id`),
  CONSTRAINT `favorites_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `favorites_ibfk_2` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

## 初始化数据

### 角色初始化
```sql
INSERT INTO roles VALUES
(1,'USER','Regular User','2025-12-02 09:40:55'),
(2,'ADMIN','Administrator','2025-12-02 09:40:55'),
(3,'MODERATOR','Moderator','2025-12-02 09:40:55');
```

### 分类初始化
```sql
INSERT INTO categories VALUES
(1,'家常菜谱','Daily home cooking recipes',0,1,'2025-12-02 09:40:55'),
(2,'川菜系列','Sichuan specialty dishes',0,1,'2025-12-02 09:40:55'),
(3,'粤菜系列','Cantonese specialty dishes',0,1,'2025-12-02 09:40:55'),
(4,'湘菜系列','Hunan specialty dishes',0,1,'2025-12-02 09:40:55'),
(5,'甜品系列','Various desserts',0,1,'2025-12-02 09:40:55'),
(6,'饮品系列','Drink making tutorials',0,1,'2025-12-02 09:40:55'),
(7,'烘焙系列','Baked goods and pastries',0,1,'2025-12-02 09:40:55'),
(8,'异国料理','International specialties',0,1,'2025-12-02 09:40:55');
```

### 管理员用户初始化
```sql
INSERT INTO users VALUES
(1,'admin','upda1111ed@example.com','1234','gold',NULL,NULL,NULL,NULL,NULL,1,'2025-12-02 09:40:55','2025-12-13 20:26:53');
```

### 用户角色关联初始化
```sql
INSERT INTO user_roles VALUES
(1,1,2,'2025-12-02 09:40:55');
```