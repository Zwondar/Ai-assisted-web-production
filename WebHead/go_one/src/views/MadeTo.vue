<template>
    <div>
        <iframe src="/Weeb.html" frameborder="0" width="100%" height="800px"></iframe>
    </div>
</template>
<script>
import axios from 'axios'
export default {
    data() {
        return {
        };
    },
    mounted(){
        var jwt = sessionStorage.getItem("token");
        let headers = {
            Token: jwt // 将 'Authorization' 改为 'Token'
        };
        axios.post('http://localhost:8080/Log/verify', {}, { headers })//向该url发送请求
            .then()
            .catch(err => {
                console.log(err);

                this.$notify.error({
                    title: '错误',
                    message: '您的权限不够'
                });

                this.$router.push('/');
            })
    }
 
}

</script>

<style>
</style>