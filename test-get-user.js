const http = require('http');

// 获取指定ID的用户信息
const userId = 1; // 我们之前更新的用户ID

const options = {
    hostname: 'localhost',
    port: 8080,
    path: `/api/users/${userId}`,
    method: 'GET',
    headers: {
        'Content-Type': 'application/json'
    }
};

console.log(`Sending request to get user with ID ${userId}...`);

const req = http.request(options, res => {
    console.log(`Status Code: ${res.statusCode}`);
    
    let responseBody = '';
    res.on('data', chunk => {
        responseBody += chunk;
    });
    
    res.on('end', () => {
        console.log('Response:', responseBody);
        
        try {
            const user = JSON.parse(responseBody);
            console.log('User details:');
            console.log('- ID:', user.id);
            console.log('- Username:', user.username);
            console.log('- Nickname:', user.nickname);
            console.log('- Email:', user.email);
        } catch (e) {
            console.error('Failed to parse response:', e);
        }
    });
});

req.on('error', error => {
    console.error('Request Error:', error);
});

req.end();