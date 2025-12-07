-- Food Forum Database Initialization Script

-- Use pbl database
USE pbl;

-- Drop existing tables (in dependency order)
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS favorites;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS categories;

-- 1. Users table
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'User ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT 'Username',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT 'Email',
    password VARCHAR(255) NOT NULL COMMENT 'Password',
    nickname VARCHAR(100) COMMENT 'Nickname',
    avatar VARCHAR(255) COMMENT 'Avatar URL',
    gender TINYINT DEFAULT 0 COMMENT 'Gender: 0-unknown, 1-male, 2-female',
    birthday DATE COMMENT 'Birthday',
    signature VARCHAR(255) COMMENT 'Signature',
    phone VARCHAR(20) COMMENT 'Phone',
    status TINYINT DEFAULT 1 COMMENT 'Status: 0-disabled, 1-active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Created Time',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated Time'
);

-- 2. Roles table
CREATE TABLE roles (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'Role ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT 'Role Name',
    description VARCHAR(255) COMMENT 'Role Description',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Created Time'
);

-- 3. User-Roles association table
CREATE TABLE user_roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'Primary Key ID',
    user_id BIGINT NOT NULL COMMENT 'User ID',
    role_id INT NOT NULL COMMENT 'Role ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Assigned Time',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_role (user_id, role_id)
);

-- 4. Categories table
CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'Category ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT 'Category Name',
    description VARCHAR(255) COMMENT 'Category Description',
    sort_order INT DEFAULT 0 COMMENT 'Sort Order',
    status TINYINT DEFAULT 1 COMMENT 'Status: 0-hidden, 1-visible',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Created Time'
);

-- 5. Posts table
CREATE TABLE posts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'Post ID',
    user_id BIGINT NOT NULL COMMENT 'Author User ID',
    category_id INT NOT NULL COMMENT 'Category ID',
    title VARCHAR(200) NOT NULL COMMENT 'Post Title',
    content TEXT COMMENT 'Post Content',
    images TEXT COMMENT 'Image URLs (JSON format)',
    view_count INT DEFAULT 0 COMMENT 'View Count',
    like_count INT DEFAULT 0 COMMENT 'Like Count',
    comment_count INT DEFAULT 0 COMMENT 'Comment Count',
    is_top TINYINT DEFAULT 0 COMMENT 'Top Post: 0-no, 1-yes',
    is_essence TINYINT DEFAULT 0 COMMENT 'Essence Post: 0-no, 1-yes',
    status TINYINT DEFAULT 1 COMMENT 'Status: 0-draft, 1-published, 2-reviewing, 3-deleted',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Created Time',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated Time',
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- 6. Comments table
CREATE TABLE comments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'Comment ID',
    post_id BIGINT NOT NULL COMMENT 'Post ID',
    user_id BIGINT NOT NULL COMMENT 'Commenter User ID',
    parent_id BIGINT DEFAULT 0 COMMENT 'Parent Comment ID (0 for top-level)',
    reply_user_id BIGINT DEFAULT 0 COMMENT 'Replied User ID',
    content TEXT NOT NULL COMMENT 'Comment Content',
    like_count INT DEFAULT 0 COMMENT 'Like Count',
    status TINYINT DEFAULT 1 COMMENT 'Status: 0-hidden, 1-visible',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Created Time',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated Time',
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 7. Likes table
CREATE TABLE likes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'Primary Key ID',
    user_id BIGINT NOT NULL COMMENT 'User ID',
    likeable_type VARCHAR(20) NOT NULL COMMENT 'Like Type: post/comment',
    likeable_id BIGINT NOT NULL COMMENT 'Liked Object ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Like Time',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_like (user_id, likeable_type, likeable_id)
);

-- 8. Favorites table
CREATE TABLE favorites (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'Primary Key ID',
    user_id BIGINT NOT NULL COMMENT 'User ID',
    post_id BIGINT NOT NULL COMMENT 'Post ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Favorite Time',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_post (user_id, post_id)
);

-- Initialize data

-- Insert roles
INSERT INTO roles (name, description) VALUES 
('USER', 'Regular User'),
('ADMIN', 'Administrator'),
('MODERATOR', 'Moderator');

-- Insert categories
INSERT INTO categories (name, description) VALUES 
('Home Cooking', 'Daily home cooking recipes'),
('Sichuan Cuisine', 'Sichuan specialty dishes'),
('Cantonese Cuisine', 'Cantonese specialty dishes'),
('Hunan Cuisine', 'Hunan specialty dishes'),
('Desserts', 'Various desserts'),
('Drinks', 'Drink making tutorials'),
('Baking', 'Baked goods and pastries'),
('International Cuisine', 'International specialties');

-- Create default admin user (username: admin, password: 1234)
-- Password encrypted with BCrypt: 1234 -> $2a$10$wHjp1RYzq9.IPguxIyBD5eUQbRzD/7/8HJ6f.vVnHrWlFyx6sLWxG
INSERT INTO users (username, email, password, nickname, avatar, gender, status) VALUES 
('admin', 'admin@example.com', '$2a$10$wHjp1RYzq9.IPguxIyBD5eUQbRzD/7/8HJ6f.vVnHrWlFyx6sLWxG', 'Admin', NULL, 0, 1);

-- Assign admin role to admin user
INSERT INTO user_roles (user_id, role_id) SELECT u.id, r.id FROM users u, roles r WHERE u.username='admin' AND r.name='ADMIN';