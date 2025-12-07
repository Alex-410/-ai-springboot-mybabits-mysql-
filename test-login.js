const http = require('http');

// 测试登录
const loginData = JSON.stringify({
  username: 'admin',
  password: '1234'
});

const loginOptions = {
  hostname: 'localhost',
  port: 8080,
  path: '/api/auth/login',
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Content-Length': Buffer.byteLength(loginData)
  }
};

const req = http.request(loginOptions, (res) => {
  let data = '';
  
  res.on('data', (chunk) => {
    data += chunk;
  });
  
  res.on('end', () => {
    console.log('Login Response Status:', res.statusCode);
    console.log('Login Response Headers:', res.headers);
    console.log('Login Response Body:', data);
    
    // 解析响应
    try {
      const responseData = JSON.parse(data);
      console.log('Parsed Response:', responseData);
    } catch (e) {
      console.error('Failed to parse JSON:', e);
    }
  });
});

req.on('error', (e) => {
  console.error('Login Request Error:', e.message);
});

req.write(loginData);
req.end();