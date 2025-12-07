const http = require('http');

// 先登录获取sessionId
const loginData = JSON.stringify({
  username: 'admin',
  password: '1234'
});

console.log('Sending login request...');
console.log('Login data:', loginData);

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

console.log('Login options:', loginOptions);

const loginReq = http.request(loginOptions, (res) => {
  console.log('Login response status code:', res.statusCode);
  console.log('Login response headers:', res.headers);
  
  // 获取sessionId
  const cookies = res.headers['set-cookie'];
  let sessionId = '';
  if (cookies && cookies.length > 0) {
    sessionId = cookies[0].split(';')[0];
    console.log('Session ID:', sessionId);
  } else {
    console.log('No cookies found in login response');
  }
  
  let data = '';
  res.on('data', (chunk) => {
    data += chunk;
  });
  
  res.on('end', () => {
    console.log('Login Response Body:', data);
    
    // 解析登录响应
    let loginResponse;
    try {
      loginResponse = JSON.parse(data);
    } catch (e) {
      console.error('Failed to parse login response:', e);
      return;
    }
    
    // 检查登录是否成功
    if (!loginResponse.success) {
      console.log('Login failed');
      return;
    }
    
    // 现在测试更新用户信息
    const updateData = JSON.stringify({
      nickname: 'Updated Admin',
      email: 'updated-admin@example.com'
    });
    
    console.log('Sending update request...');
    console.log('Update data:', updateData);
    
    const updateOptions = {
      hostname: 'localhost',
      port: 8080,
      path: '/api/users/profile',
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Content-Length': Buffer.byteLength(updateData),
        'Cookie': sessionId  // 移除了不必要的单引号
      }
    };
    
    console.log('Update options:', updateOptions);
    
    const updateReq = http.request(updateOptions, (res) => {
      console.log('Update response status code:', res.statusCode);
      console.log('Update response headers:', res.headers);
      
      let updateResult = '';
      res.on('data', (chunk) => {
        updateResult += chunk;
      });
      
      res.on('end', () => {
        console.log('Update Profile Response Status:', res.statusCode);
        console.log('Update Profile Response Headers:', res.headers);
        console.log('Update Profile Response Body:', updateResult);
        
        // 如果更新成功，尝试获取更新后的用户信息
        if (res.statusCode === 200) {
          const profileOptions = {
            hostname: 'localhost',
            port: 8080,
            path: '/api/users/profile',
            method: 'GET',
            headers: {
              'Cookie': sessionId
            }
          };
          
          const profileReq = http.request(profileOptions, (res) => {
            let profileResult = '';
            res.on('data', (chunk) => {
              profileResult += chunk;
            });
            
            res.on('end', () => {
              console.log('Get Profile Response:', profileResult);
            });
          });
          
          profileReq.on('error', (e) => {
            console.error('Get Profile Request Error:', e.message);
          });
          
          profileReq.end();
        }
      });
    });
    
    updateReq.on('error', (e) => {
      console.error('Update Request Error:', e.message);
    });
    
    updateReq.write(updateData);
    updateReq.end();
  });
});

loginReq.on('error', (e) => {
  console.error('Login Request Error:', e.message);
});

loginReq.write(loginData);
loginReq.end();