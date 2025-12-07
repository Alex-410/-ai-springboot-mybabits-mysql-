const http = require('http');

// 直接通过ID更新用户信息（绕过登录）
const updateData = JSON.stringify({
    id: 1, // 假设要更新的用户ID为1
    nickname: 'UpdatedNickname',
    email: 'updated@example.com'
});

console.log('Sending update request with data:', updateData);

const updateOptions = {
    hostname: 'localhost',
    port: 8080,
    path: '/api/users/profile',
    method: 'PUT',
    headers: {
        'Content-Type': 'application/json',
        'Content-Length': Buffer.byteLength(updateData)
    }
};

console.log('Update options:', updateOptions);

// 发送更新请求
const updateReq = http.request(updateOptions, updateRes => {
    console.log(`Update Status Code: ${updateRes.statusCode}`);
    console.log('Update Response Headers:', updateRes.headers);
    
    let updateResponseBody = '';
    updateRes.on('data', chunk => {
        updateResponseBody += chunk;
    });
    
    updateRes.on('end', () => {
        console.log('Update Response:', updateResponseBody);
        
        // 解析更新响应
        try {
            const updateResponse = JSON.parse(updateResponseBody);
            if (updateResponse.success) {
                console.log('用户信息更新成功!');
                console.log('更新后的用户信息:', updateResponse.user);
            } else {
                console.log('用户信息更新失败:', updateResponse.message);
            }
        } catch (e) {
            console.error('解析响应失败:', e);
        }
    });
});

updateReq.on('error', error => {
    console.error('Update Request Error:', error);
});

updateReq.write(updateData);
updateReq.end();