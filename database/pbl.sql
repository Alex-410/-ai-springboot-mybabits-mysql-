/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' wudi666*/;
DROP TABLE IF EXISTS categories;
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'Category ID',
  `name` varchar(50) NOT NULL COMMENT 'Category Name',
  `description` varchar(255) DEFAULT NULL COMMENT 'Category Description',
  `sort_order` int DEFAULT '0' COMMENT 'Sort Order',
  `status` tinyint DEFAULT '1' COMMENT 'Status: 0-hidden, 1-visible',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created Time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES categories WRITE;
INSERT INTO categories VALUES
 (1,'家常菜谱','Daily home cooking recipes',0,1,'2025-12-02 09:40:55'),
 (2,'川菜系列','Sichuan specialty dishes',0,1,'2025-12-02 09:40:55'),
 (3,'粤菜系列','Cantonese specialty dishes',0,1,'2025-12-02 09:40:55'),
 (4,'湘菜系列','Hunan specialty dishes',0,1,'2025-12-02 09:40:55'),
 (5,'甜品系列','Various desserts',0,1,'2025-12-02 09:40:55'),
 (6,'饮品系列','Drink making tutorials',0,1,'2025-12-02 09:40:55'),
 (7,'烘焙系列','Baked goods and pastries',0,1,'2025-12-02 09:40:55'),
 (8,'异国料理','International specialties',0,1,'2025-12-02 09:40:55');
UNLOCK TABLES;

DROP TABLE IF EXISTS comments;
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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES comments WRITE;
INSERT INTO comments VALUES
 (2,3,2,NULL,0,'666',0,1,'2025-12-03 11:02:44','2025-12-03 11:02:44'),
 (3,4,3,NULL,0,'666',0,1,'2025-12-03 11:15:25','2025-12-03 11:15:25'),
 (5,8,1,NULL,0,'123131',0,1,'2025-12-04 22:13:30','2025-12-04 22:13:30'),
 (6,4,1,NULL,0,'11111',0,0,'2025-12-04 22:13:36','2025-12-13 19:59:35'),
 (7,8,1,NULL,0,'666',0,0,'2025-12-04 22:13:59','2025-12-13 19:59:33'),
 (9,4,1,NULL,0,'21除非',0,0,'2025-12-04 23:44:38','2025-12-13 19:54:23'),
 (16,18,1,NULL,0,'111你记仇',0,0,'2025-12-13 19:54:51','2025-12-13 19:54:59');
UNLOCK TABLES;

DROP TABLE IF EXISTS favorites;
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES favorites WRITE;
INSERT INTO favorites VALUES
 (4,3,18,'2025-12-13 02:56:36'),
 (5,3,17,'2025-12-13 02:56:40'),
 (7,1,18,'2025-12-13 19:11:06'),
 (8,1,17,'2025-12-13 19:17:39'),
 (9,1,15,'2025-12-13 19:31:35'),
 (10,1,16,'2025-12-13 19:31:39');
UNLOCK TABLES;

DROP TABLE IF EXISTS likes;
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

DROP TABLE IF EXISTS posts;
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
  `is_top` tinyint DEFAULT '0' COMMENT 'Top Post: 0-no, 1-yes',
  `is_essence` tinyint DEFAULT '0' COMMENT 'Essence Post: 0-no, 1-yes',
  `status` tinyint DEFAULT '1' COMMENT 'Status: 0-draft, 1-published, 2-reviewing, 3-deleted',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created Time',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated Time',
  `favorite_count` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `posts_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES posts WRITE;
INSERT INTO posts VALUES
 (1,1,1,'1','1',NULL,0,0,0,0,0,1,'2025-12-02 20:36:23','2025-12-02 20:36:23',0),
 (3,2,1,'什么美食好吃？','狼牙土豆yyds\n',NULL,0,0,0,0,0,1,'2025-12-03 11:02:38','2025-12-03 11:02:38',0),
 (4,3,1,'土豆yyds','土豆天下第一好吃',NULL,0,0,0,0,0,1,'2025-12-03 11:15:17','2025-12-03 11:15:17',0),
 (7,1,2,'1234','1234',NULL,0,10,0,0,0,1,'2025-12-04 21:57:10','2025-12-05 00:20:40',8),
 (8,1,1,'woaini','zld',NULL,0,0,0,0,0,1,'2025-12-04 21:58:06','2025-12-04 21:58:06',0),
 (10,1,1,'我和你','4444',NULL,0,0,0,0,0,1,'2025-12-06 21:27:59','2025-12-06 21:27:59',0),
 (11,1,1,'111','11111111111',NULL,0,0,0,0,0,1,'2025-12-07 12:57:49','2025-12-07 12:57:49',0),
 (12,1,1,'我问问','322才',NULL,0,3,0,0,0,1,'2025-12-07 13:02:14','2025-12-07 13:02:20',0),
 (13,1,1,'一拖股份查收下啊','会不会GV人防测东西维萨',NULL,0,0,0,0,0,1,'2025-12-07 13:04:45','2025-12-07 13:04:45',0),
 (14,1,1,'谢谢','谢谢',NULL,0,0,0,0,0,1,'2025-12-07 13:13:34','2025-12-07 13:13:34',0),
 (15,1,3,'让我','法大学','[\"/uploads/compressed_post_85fb10f8-d3f1-42c3-a9a6-5e61766df0d3.gif\"]',0,0,0,0,0,1,'2025-12-07 13:29:49','2025-12-07 13:29:49',0),
 (16,1,1,'很过分的事','广泛的撒','[\"/uploads/compressed_post_4e77bf6c-b059-4eb1-a5d3-83848ff55b22.gif\"]',0,4,0,0,0,1,'2025-12-07 13:55:38','2025-12-12 14:53:55',3),
 (17,1,1,'巨化股份21','建行股份的',NULL,0,5,0,0,0,NULL,'2025-12-07 13:56:05','2025-12-13 22:30:39',0),
 (18,1,6,'测试','哈哈哈11',NULL,0,5,0,0,0,NULL,'2025-12-09 20:36:47','2025-12-13 22:30:32',0);
UNLOCK TABLES;

DROP TABLE IF EXISTS roles;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'Role ID',
  `name` varchar(50) NOT NULL COMMENT 'Role Name',
  `description` varchar(255) DEFAULT NULL COMMENT 'Role Description',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created Time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES roles WRITE;
INSERT INTO roles VALUES
 (1,'USER','Regular User','2025-12-02 09:40:55'),
 (2,'ADMIN','Administrator','2025-12-02 09:40:55'),
 (3,'MODERATOR','Moderator','2025-12-02 09:40:55');
UNLOCK TABLES;

DROP TABLE IF EXISTS user_roles;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES user_roles WRITE;
INSERT INTO user_roles VALUES
 (1,1,2,'2025-12-02 09:40:55');
UNLOCK TABLES;

DROP TABLE IF EXISTS users;
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES users WRITE;
INSERT INTO users VALUES
 (1,'admin','upda1111ed@example.com','1234','gold',NULL,NULL,NULL,NULL,NULL,1,'2025-12-02 09:40:55','2025-12-13 20:26:53'),
 (2,'123','123@qq.com','123','123',NULL,NULL,NULL,NULL,NULL,1,'2025-12-02 19:08:31','2025-12-13 20:32:47'),
 (3,'lvlinkun','2274786068@qq.com','926953llk','Alex',NULL,NULL,NULL,NULL,NULL,1,'2025-12-03 11:13:59','2025-12-13 22:21:23'),
 (6,'admin1234','1234@qq.com','123456','1234',NULL,NULL,NULL,NULL,NULL,1,'2025-12-13 23:51:52','2025-12-13 23:51:52');
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
