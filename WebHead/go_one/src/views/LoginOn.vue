<template>
    <div id="login-page" :style="{ 'background-image': `url(${backgroundImage})` }">
      <div class="login-container">
        <h1 class="set-width">Login</h1>
        <input type="text" placeholder="Username" v-model="loginData.username" class="larger-input" />
        <br>
        <br>
        <input type="password" placeholder="Password" v-model="loginData.password" class="larger-input" />
        <br>
        <br>
        <button @click="handleLogin" class="larger-button" >Login</button>
       <span>- - - - - - - - - - - </span>
        <button @click="handleSign" class="larger-button">Sign</button>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios'
  export default {
    data() {
      return {
        backgroundImage: require('@/assets/White.gif'), 
        loginData: { // 将要发送的数据封装到loginData对象中
        username: '',
        password: '',
        con : false
      }
      };
    },
    methods: {
      handleLogin() {
      axios.post('http://localhost:8080/Detection',this.loginData)//向该url发送请求
      .then(response => {
        
        if (response.data.code==1) {
      // 获取用户信息
      // 存储JWT令牌到sessionStorage中，与用户ID关联
      sessionStorage.setItem(`token`, response.data.data);
      sessionStorage.setItem(`UserID`,response.data.msg);

      this.$notify({
          title: '登录成功',
          message: '欢迎进入系统',
          type: 'success'
        });

      this.$router.push('/Log/MainTo');
    }
    else 
    {
      this.$message.error(response.data.msg);
      this.loginData.username="";
      this.loginData.password=""
    }
  
      })
      .catch(err => {
        alert(err.data.msg)
      })
  },
      handleSign () {
        this.$message('您正在注册账户中');
        this.$router.push('/Sign');
        }
      },
      mounted () {
        var a=document.getElementById('login-page');
        if(a.getBoundingClientRect().width==400) window.location.reload(true);
      }
    };
  </script>
  
  <style scoped>
  
  .set-width{
     margin-left: 110px
  }
  
  .larger-input {
  width: 300px; /* 可根据需要调整宽度 */
  height: 28px; /* 可根据需要调整高度 */
  padding: 12px;
  font-size: 16px;
  }
  
  /* 新增一个.larger-button类来增大按钮大小 */
  .larger-button {
  width: 100px; /* 调整按钮宽度 */
  height: 30px; /* 调整按钮高度 */
  font-size: 20px; /* 调整字体大小 */
  padding: 0px; /* 调整内边距 */
  }
  #login-page {
    height: 100vh;
    background-position: center;
    background-repeat: no-repeat;
    background-size: cover;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
    .login-container {  
     /* padding: 2rem;  */
      /* background-color:red; 添加半透明白色背景以确保文本可读   */
       /* border-radius: 2500px;    */
      /* box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);   */
      width: 400px;  
      height: 500px;  
    }  

    
  </style>