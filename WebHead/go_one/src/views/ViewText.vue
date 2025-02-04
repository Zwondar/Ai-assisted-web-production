<template>
    <div class="parent-container">
    <div v-html="htmlContainer" class="parent-container"></div>
  </div>
</template>

<script>
import axios from 'axios';
export default {
    data() {
        return {
        url :this.$route.query.url,
        htmlContainer : ''
      }
    },
    mounted() {
axios.get(this.url).then(response=>{
  this.htmlContainer = response.data;
})

       var jwt=sessionStorage.getItem("token");
        let headers = {
    Token: jwt 
  };
        axios.post('http://localhost:8080/Log/verify',{},{headers})//向该url发送请求
      .then()
      .catch(err => {
        console.log(err);
        this.$router.push('/');
      })
    }
}
</script>

<style>

.parent-container {  
  width: 100vw; /* 或者使用百分比、视口单位等 */  
  height: 100vh; /* 或者使用其他合适的值，比如 min-height */  
  /* 其他样式 */  
}  
</style>