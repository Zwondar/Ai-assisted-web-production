<template>
    <div class="box1" id="abc">
        <div class="box">
            <!-- 竖着的导航栏 -->
            <div class="shu">

                <div class="s1" @click="goOne">
                    <h2>个人中心</h2>
                </div>


                <div class="s2">
                    <h2>资源中心</h2>
                </div>


                <div class="s3" @click="goThree">
                    <h2>AI问答</h2>
                </div>


                <div class="s4" @click="goFour">
                    <h2>网页制作</h2>
                </div>

            </div>
            <!-- 除侧边栏意外横着的部分 -->
            <div class="heng">
                <!-- 搜索框  -->
                <div class="sousuokuang">
                    <!-- 搜索框主体 -->
                    <div class="a">
                        <input type="text" class="b" v-model="sel.input">
                        <!-- 搜索按钮 -->
                        <button class="c" @click="getData">
                            <img src="@/assets/搜索框.png" style="width: 30px;">
                        </button>
                    </div>
                    <!-- 用户部分 -->
                    <div class="info">
                        <!-- 头像 -->
                        <img :src="imageUrl" width="50px" height="50px" class="tx" id="tx">
                        <!-- 用户名 -->
                        <strong id="yhm">{{realName}}</strong>
                    </div>
                </div>
                <!-- 窗口部分 -->
                <div class="chuangkou" id="chuangkou" :style="{ backgroundImage: `url(${imageUrl})` }">
                    <!-- 用户学校那一栏 -->
                    <div class="shuoming">
                        <!-- 用户 -->
                        <div class="yh">
                            <h2>用户</h2>
                            <ul id="yh">
                                <!-- 数据将在此处动态插入 -->
                                <li v-for="(item) in tableData" :key="item.filename">
                                    <img :src=item.spaceImg class="txx">
                                    {{ item.name }}
                                </li>
                            </ul>
                        </div>
                        <!-- 学校 -->
                        <div class="xx">
                            <h2>文件名</h2>
                            <ul id="xx">
                                <!-- 数据将在此处动态插入 -->
                                <li v-for="(item) in tableData" :key="item.filename">{{ item.filename }}</li>
                            </ul>
                        </div>
                        <!-- 学院 -->
                        <div class="xy">
                            <h2>更新时间</h2>
                            <ul id="xy">
                                <!-- 数据将在此处动态插入 -->
                                <li v-for="(item) in tableData" :key="item.filename">{{ item.upDataTime }}</li>
                            </ul>
                        </div>

                        <div class="gn">
                            <h2>功能</h2>
                            <ul id="gn">
                                <!-- 数据将在此处动态插入 -->

                                <li v-for="(item,index) in tableData" :key="item.filename">
                                    <div class="btndiv">
                                        <button class="ck" @click="view(index)">查看</button>
                                        <button class="xz" @click="download(index)">下载</button>
                                    </div>
                                </li>
                            </ul>
                        </div>

                        <div class="dz">
                            <h2>点赞</h2>
                            <ul id="dz">
                                <!-- 数据将在此处动态插入 -->
                                <li v-for="(item,index) in tableData" :key="item.filename">
                                    <div>
                                        <span class="dzz iconfont icon-shoucang" @click="dzChange(index)"></span>
                                    </div>
                                </li>
                            </ul>
                        </div>

                        <!-- 学科 -->
                        <div class="xk">
                            <h2>点赞数</h2>
                            <ul id="xk">
                                <!-- 数据将在此处动态插入 -->
                                <li v-for="(item) in tableData" :key="item.filename">{{ item.star }}</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    data() {
        return {
            tableData: [
            ],
            token: sessionStorage.getItem('token'),
            sel: {
                input: '',//输入框操作
                user_id: sessionStorage.getItem('UserID')
            },
            imageUrl: '',
            name : '',
            realName : ''
        };
    },
    methods: {

        goOne(){
            this.$router.push('/Log/MainTo');
            
        },
        goThree(){
            this.$router.push('/Log/AiViews');
        },
        goFour(){
            this.$router.push('/Log/MadeTo');
        },


        view(index){
         var url=this.tableData[index].url;
         this.$router.push({ name: 'view', query: { url: url } }); 
        },

    download(index){
        window.location.href = this.tableData[index].url;
    },

       dzChange(index){
        var params = {
        user_id : sessionStorage.getItem('UserID'),
        space_id : this.tableData[index].username,
        filename : this.tableData[index].filename
        };
 axios.post('http://localhost:8080/Log/toStar',params,{
    headers: {  
          'Token': this.token // 设置Token请求头 
        }
 }).then(
    response=>{
        if(response.data.code==0) {

            this.$notify({
          title: '警告',
          message: '您已经点过赞了',
          type: 'warning'
        });
        
        }else if(response.data.code==1) {
            var dzzElements = document.getElementsByClassName("dzz");
            // 对每个具有类名"dzz"的元素添加点击事件监听器
                dzzElements[index].style.color="yellow";
        this.$notify({
          title: 'success',
          message: '点赞成功',
          type: 'success'
        });
        }
    }
 ).catch(err=>{
    console.log(err);
 })
       },
        getData() {
            var jwt = sessionStorage.getItem("token");
            let headers = {
                Token: jwt // 将 'Authorization' 改为 'Token'
            };
            axios.post('http://localhost:8080/Log/getOpenData',this.sel, { headers }).then(
                response => {
                    this.tableData = response.data.data;

                }
            ).catch(err => {
                console.log(err)
            })
        }
    },
    mounted() {
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
            }),

            axios.post('http://localhost:8080/Log/putImage', this.sel, { headers }).then(
                response => {
                    this.imageUrl = response.data.data;
                }
            ).catch(err => {
                console.log(err)
            })


        axios.post('http://localhost:8080/Log/getOpenData', this.sel, { headers }).then(
            response => {
                this.tableData = response.data.data;
            
            }
        ).catch(err => {
            console.log(err)
        })
        axios.post('http://localhost:8080/Log/getName',this.sel,{
            headers: {  
          'Token': this.token // 设置Token请求头 
        }
        }).then(response=>{
        this.realName=response.data.data;
      })
    },
}
</script>

<style scoped>

@import '../点赞/iconfont.css';
@import url('https://fonts.font.im/css?family=Kirang+Haerang&display=swap');

#abc {
    margin: 0;
    padding: 0;
    background-image: linear-gradient(-225deg, #2CD8D5 0%, #C5C1FF 56%, #FFBAC3 100%);
}

.box1 {
    display: flex;
    width: 1650px;
    height: 885px;
    align-items: center;
    border-radius: 20px;
    justify-content: center;
    border-radius: 20px;
}

.box {

    display: flex;
    width: 1300px;
    height: 700px;
    border-radius: 20px;
    background-image: linear-gradient(90deg, #FFBAC3 0%, #C5C1FF 56%, #2CD8D5 100%);
}

/* 侧边导航栏 */
.s1,
.s2,
.s3,
.s4 {
    width: 110px;
    height: 170px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-right: #2CD8D5 solid 5px;
    color: #000;
}

.s1:hover,
.s2:hover,
.s3:hover,
.s4:hover {
    background-color: rgba(255, 255, 255, 0.6);
    transition: 0.1s;
    color: red;

}

.s1 {
    border-top-left-radius: 20px;
    border-bottom: #2CD8D5 solid 5px;
    cursor: pointer;
}

.s2 {
    border-bottom: #2CD8D5 solid 5px;
    cursor: pointer;
}

.s3 {
    border-bottom: #2CD8D5 solid 5px;
    cursor: pointer;
}

.s4 {
    border-bottom: #2CD8D5 solid 5px;
    border-bottom-left-radius: 20px;
    cursor: pointer;
    border-bottom: none;
    height: 175px;
}


h2 {
    font-size: 23px;
    height: 30px;
    font-family: kaiti;
}

/* 除侧边栏意外横着的部分 */
.heng {
    width: 1190px;
    height: 700px;
    border-top-right-radius: 20px;
    border-bottom-right-radius: 20px;
}

/* 用户学校那一栏 */
.daohang {
    display: flex;

}

/* 主体窗口 */
.chuangkou {
    background-color: #fff;
    width: 1186px;
    height: 646px;
    /* background-image: url(@/assets/1.png); */
    background-size: cover;
    background-position: center;
    overflow: auto;
}

/* 搜索框 */
.sousuokuang {
    height: 53px;
    /* background-color: #e94118; */
}

/* 搜索框主体 */
.a {
    position: absolute;
    top: 115px;
    left: 290px;
    transform: translate(0%, -50%);
    background-color: #d3a0e1;
    height: 40px;
    border-radius: 40px;
    padding: 10px;
}

/* 覆盖变长 */
.a:hover .b {
    width: 500px;
    padding: 0 6px;
}

/* 覆盖变色 */
.a:hover .c {
    background-color: rgb(227, 85, 206);
}

/* 输入框部分 */
.b {
    border: none;
    background: none;
    /* 取消选中搜索框时的外边框 */
    outline: none;
    width: 0;
    padding: 0;
    transition: .4s;
    line-height: 40px;
    font-size: 20px;
    color: wheat;
}

/* 搜索图片 */
.c {
    color: #e94118;
    float: right;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background-color: #d86ee9;
    display: flex;
    justify-content: center;
    align-items: center;
    transition: .4s;
    border-color: #d86ee9;
}

/* 头像上移 */
.info {
    transform: translateY(-30px);
}

/* 用户头像 */
.tx {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    border: 5px #fff solid;
    position: absolute;
    right: -20px;
    cursor: pointer;
}

/* 添加的头像 */
.txx {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    border: 5px #fff solid;
    /* margin-top: -10px; */
}

/* 用户名 */
.info strong {
    position: absolute;
    width: 300px;
    letter-spacing: 3px;
    top: 40px;
    right: -50px;
    font-size: 25px;
}

/* 添加的用户名 */
#yhmm {
    font-size: 30px;
    letter-spacing: 2px;
    display: block;
}

.dz span {
    font-size: 25px;
    color: rgba(255, 255, 255, 0.5);
}

/* li标签去点 */
ul {
    list-style-type: none;
}

.dz li {
    margin-top: 105.66px;
}
.dz ul li {
    width: 110px;
    height: 20px;
}

/* 头像li标签 */
.yh li {
    margin-top: 42.33px;
}

.yh ul li {
    width: 75.33px;
    height: 84px;
}

/* 学校li标签 */
.xx li {
    margin-top: 105.66px;
}

.xx ul li {
    width: 110px;
    height: 20px;
}

/* 学科li标签 */
.xk li {
    margin-top: 105.66px;
     font-family: 'Kirang Haerang', cursive;
      
}

.xk ul li {
    width: 110px;
    height: 20px;
}

/* 学院li标签 */
.xy li {
    margin-top: 105.66px;
}
.xy ul li {
    width: 110px;
    height: 20px;
}

/* 年级li标签 */
.nj li {
    margin-top: 105.66px;
}

.gn ul li {
    width: 110px;
    height: 16px;
}

/* 用户学校那一栏 */
.shuoming {
    display: flex;
    background-color: #C5C1FF;
    height: 30px;
}

/* 用户学校那一栏 ，拼音缩写*/
.xx,
.xy,
.xk,
.nj,
.dzs,
.tj,
.yh,
.dz,
.gn {
    width: 150px;
    margin-left: 10px;
}

/* 用户学校那一栏 ，拼音缩写*/
.xx h2,
.xy h2,
.xk h2,
.dz h2,
.yh h2,
.nj h2,
.dzs h2,
.gn h2,
.tj h2 {
    font-weight: 300;
    margin-top: 0;
}

.yh h2{
margin-left: 25px;
}

.xx h2 {
    margin-left: 20px;
}

.xy h2{
    margin-left: 40px;
}

.gn h2{
    margin-left: 75px;
}

.dz h2{
    margin-left: 25px;
}

.xk h2 {
    margin-left: 10px;
}
/* 添加的查看和下载按钮 */
.btndiv {
    display: flex;
    margin-top: 109.66px;
}

.btndiv button {
    width: 50px;
    margin-left: 10px;
}

.ck {
    background-image: linear-gradient(120deg, #84fab0 0%, #8fd3f4 100%);
    font-family: kaiti;
    color: #fff;
    border-radius: 5px;
    cursor: pointer;
    border: none;
}

.xz {
    font-family: kaiti;
    color: #fff;
    background-image: linear-gradient(120deg, #fccb90 0%, #d57eeb 100%);
    border: none;
    border-radius: 5px;
    cursor: pointer;
}

ul {
    margin-top: 0;
}

.yh {
    margin-left: 40px;
}

.xx {
    width: 150px;
    margin-left: 30px;
}

.tj button {
    margin-left: 20px;
    font-family: kaiti;
    font-size: 17px;
    color: #fff;
    display: block;
    width: 50px;
    height: 30px;
    background-image: linear-gradient(90deg, #FFBAC3 0%, #C5C1FF 56%, #2CD8D5 100%);
    border-radius: 5px;
    border: none;
    cursor: pointer;
}

.tj button:hover {
    background-color: rgba(255, 255, 255, 0.6);
}

.shuru {
    width: 500px;
    height: 600px;
    border-radius: 25px;
    border: none;
    /* background-color: rgb(220, 137, 243); */
    margin-left: 350px;
    position: relative;
    border-top: 1px solid rgba(255, 255, 255, 0.5);
    border-left: 1px solid rgba(255, 255, 255, 0.5);
    border-right: 1px solid rgba(255, 255, 255, 0.2);
    border-bottom: 1px solid rgba(255, 255, 255, 0.2);
    /* 模糊主体背景 */
    backdrop-filter: blur(30px);
    display: none;
}

.shuru label {
    display: block;
    font-size: 30px;
    font-family: kaiti;
    /* margin-top: 15px; */
    margin-left: 15px;
}

.shuru h2 {
    font-size: 38px;
    font-family: kaiti;
    margin-top: 0;
    margin-left: 170px;
    position: absolute;
    top: 35px;
}

.shuru input {
    width: 400px;
    height: 40px;
    border-radius: 10px;
    border: none;
    padding: 10px;
    font-size: 20px;
    background-color: rgba(255, 255, 255, 0.3);
    color: rgba(0, 0, 0, 0.9);
    border-color: rgba(255, 255, 255, 0.6);
}

.shuru form {
    display: block;
    position: absolute;
    top: 110px;
    left: 50px;
}

.shuru button {
    width: 120px;
    height: 35px;
    border: 1px solid rgba(66, 118, 6, 0.8);
    background-color: rgba(66, 118, 6, 0.5);
    cursor: pointer;
    color: rgba(255, 255, 255, 0.9);
    border-radius: 5px;
    margin: 5px;
    transition: 0.2s;
    margin-top: 30px;
    margin-left: 300px;

}

.shurutx {
    width: 500px;
    height: 350px;
    border-radius: 25px;
    border: none;
    /* background-color: rgb(220, 137, 243); */
    margin-left: 350px;
    position: relative;
    border-top: 1px solid rgba(255, 255, 255, 0.5);
    border-left: 1px solid rgba(255, 255, 255, 0.5);
    border-right: 1px solid rgba(255, 255, 255, 0.2);
    border-bottom: 1px solid rgba(255, 255, 255, 0.2);
    /* 模糊主体背景 */
    backdrop-filter: blur(30px);
    /* display: none; */
}

.shurutx label {
    display: block;
    font-size: 30px;
    font-family: kaiti;
    /* margin-top: 15px; */
    margin-left: 15px;
}

.shurutx h2 {
    font-size: 38px;
    font-family: kaiti;
    margin-top: 0;
    margin-left: 170px;
    position: absolute;
    top: 35px;
}

.shurutx input {
    width: 400px;
    height: 40px;
    border-radius: 10px;
    border: none;
    padding: 10px;
    font-size: 20px;
    background-color: rgba(255, 255, 255, 0.3);
    color: rgba(0, 0, 0, 0.9);
    border-color: rgba(255, 255, 255, 0.6);
}

.shurutx form {
    display: block;
    position: absolute;
    top: 110px;
    left: 50px;
}

.shurutx button {
    width: 120px;
    height: 35px;
    border: 1px solid rgba(66, 118, 6, 0.8);
    background-color: rgba(66, 118, 6, 0.5);
    cursor: pointer;
    color: rgba(255, 255, 255, 0.9);
    border-radius: 5px;
    margin: 5px;
    transition: 0.2s;
    margin-top: 30px;
    margin-left: 300px;

}
</style>