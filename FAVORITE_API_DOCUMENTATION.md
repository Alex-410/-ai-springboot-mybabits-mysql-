# 收藏功能API文档

## 概述
本文档详细描述了收藏功能模块的API接口，包括添加收藏、取消收藏、查询收藏状态和获取用户收藏列表等功能。

## 数据结构

### Favorite对象
| 字段名 | 类型 | 描述 |
|--------|------|------|
| id | Long | 收藏记录ID |
| userId | Long | 用户ID |
| postId | Long | 帖子ID |
| createdAt | Date | 收藏创建时间 |

## API接口

### 1. 添加收藏

#### 请求URL
`POST /api/favorites`

#### 请求头
```
Content-Type: application/json
```

#### 请求参数
```json
{
  "userId": 1,
  "postId": 1
}
```

#### 参数说明
| 参数名 | 必需 | 类型 | 说明 |
|--------|------|------|------|
| userId | 是 | Long | 用户ID |
| postId | 是 | Long | 帖子ID |

#### 返回结果
```json
{
  "success": true,
  "message": "收藏成功"
}
```

#### 错误情况
```json
{
  "success": false,
  "message": "收藏失败，可能已收藏或参数错误"
}
```

#### 示例请求
```bash
curl -X POST http://localhost:8080/api/favorites \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "postId": 1}'
```

### 2. 取消收藏

#### 请求URL
`DELETE /api/favorites`

#### 请求参数
通过查询参数传递：
- userId: 用户ID
- postId: 帖子ID

#### 返回结果
```json
{
  "success": true,
  "message": "取消收藏成功"
}
```

#### 错误情况
```json
{
  "success": false,
  "message": "取消收藏失败"
}
```

#### 示例请求
```bash
curl -X DELETE "http://localhost:8080/api/favorites?userId=1&postId=1"
```

### 3. 查询收藏状态

#### 请求URL
`GET /api/favorites/status`

#### 请求参数
通过查询参数传递：
- userId: 用户ID
- postId: 帖子ID

#### 返回结果
```json
{
  "success": true,
  "data": {
    "isFavorite": true
  }
}
```

#### 示例请求
```bash
curl -X GET "http://localhost:8080/api/favorites/status?userId=1&postId=1"
```

### 4. 获取用户收藏列表

#### 请求URL
`GET /api/favorites/user/{userId}`

#### 请求参数
路径参数：
- userId: 用户ID

查询参数：
- page: 页码（默认1）
- size: 每页大小（默认10）

#### 返回结果
```json
{
  "success": true,
  "data": {
    "favorites": [
      {
        "id": 1,
        "userId": 1,
        "postId": 1,
        "createdAt": "2023-05-01T12:00:00.000+00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10
  }
}
```

#### 示例请求
```bash
curl -X GET "http://localhost:8080/api/favorites/user/1?page=1&size=10"
```

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 500 | 服务器内部错误 |

## 使用示例

### 前端JavaScript示例

```javascript
// 添加收藏
async function addFavorite(userId, postId) {
  const response = await fetch('/api/favorites', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ userId, postId })
  });
  return await response.json();
}

// 取消收藏
async function removeFavorite(userId, postId) {
  const response = await fetch(`/api/favorites?userId=${userId}&postId=${postId}`, {
    method: 'DELETE'
  });
  return await response.json();
}

// 检查收藏状态
async function checkFavoriteStatus(userId, postId) {
  const response = await fetch(`/api/favorites/status?userId=${userId}&postId=${postId}`);
  return await response.json();
}

// 获取用户收藏列表
async function getUserFavorites(userId, page = 1, size = 10) {
  const response = await fetch(`/api/favorites/user/${userId}?page=${page}&size=${size}`);
  return await response.json();
}
```

## 注意事项

1. 所有API接口都返回统一的JSON格式响应
2. 时间格式遵循ISO 8601标准
3. 分页从第1页开始计算
4. 重复收藏会被拒绝
5. 用户只能操作自己的收藏记录