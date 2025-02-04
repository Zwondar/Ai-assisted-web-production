<template>
    <div class="box1" id="abc">
        <div class="box">
            <!-- 竖着的导航栏 -->
            <div class="shu">

                <div class="s1">
                    <h2 >个人中心</h2>
                </div>


                <div class="s2" @click="goTwo">
                    <h2 >资源中心</h2>
                </div>


                <div class="s3" @click="goThree">
                    <h2  >AI问答</h2>
                </div>


                <div class="s4" @click="goFour">
                    <h2 >网页制作</h2>
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
                        <img :src="imageUrl" width="50px" height="50px" class="tx" id="tx" @click="tx">
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
                                    <img :src="imageUrl" class="txx">
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
                                        <button class="fx" @click="share(index)">分享</button>
                                        <button class="sc" @click="deletes(index)">删除</button>
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


                           <!-- 添加 -->
                           <div class="tj" id="tj">
                            <button @click="tj"><span>添加</span></button>
                        </div>
                  
                    </div>

                      
                    <!-- 信息输入部分 -->
                    <div class="shuru" id="shuru" v-show="shuRu">
                        <h2>输入信息</h2>
                        <form>
                            <label>文件名</label>
                            <input type="text" class="xuex" id="xuex" v-model="filename">
                            <label>文件</label>
                            <label for="xuey" class="aslabel">上传html文件</label>
                            <input type="file" class="xuey" id="xuey" accept=".html" @change="onFileChange">
                            <button type="button" id="queren" @click="upload">确认</button>
                        </form>

                    </div>
                    <div class="shuru" id="shurutx" v-show="shuRuTx">
                        <h2>更改信息</h2>
                        <form>
                            <label for="dizhi" class="aslabel">上传头像</label>
                            <input type="file" class="dizhi" id="dizhi" accept="image/*" @change="fileTXs">
                            <label>用户名</label>
                            <input type="text" class ="xuex" v-model="name">
                            <button type="button" id="querentx" @click="getUserImage">确认</button>
                        </form>

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
            filename : '',
            file :null,
            shuRu : false,
            shuRuTx : false,
            name : '',
            realName : ''
        };
    },
    methods: {
        goTwo(){
            this.$router.push('/Log/PublicTo');
        },
        goThree(){
            this.$router.push('/Log/AiViews');
        },
        goFour(){
            this.$router.push('/Log/MadeTo');
        },

        tj(){
           this.shuRu=true;
        },

        tx(){
        this.shuRuTx=true;
        },
        fileTXs(e){
            this.file = e.target.files[0];  
    },
        getUserImage(){
            if(this.name==''||this.name.length>=10) 
            {
                this.$notify({
          title: '警告',
          message: '用户名不符合规则',
          type: 'warning'
        });
            }else {

        const formData = new FormData();    
      formData.append('name' ,this.name);
      formData.append('file',this.file);
      formData.append('user_id',this.sel.user_id);
    axios.post('http://localhost:8080/Log/Standing',formData,{
        headers: {  
          'Content-Type': 'multipart/form-data' ,
          'Token': this.token // 设置Token请求头 
        }
    }).then(response=>{
        if(response.data.code==1){
            this.imageUrl=response.data.data;
            this.realName=this.name;
            this.$notify({
          title: "修改成功",
          message: '您的用户名和头像已经修改成功啦',
          type: 'success'
        });
        
        this.shuRuTx=false;
        
        }
    })
}
        },

        onFileChange(e) {  
      this.file = e.target.files[0];  
    },
        upload(){
            if(this.filename=='' ||this.filename.length>=10){
                this.$notify({
          title: '警告',
          message: '文件名不符合规则',
          type: 'warning'
        });
            }else {
            const formData = new FormData();  
      formData.append('filename', this.filename);  
      formData.append('file', this.file);
      formData.append('user_id',this.sel.user_id);
    axios.post('http://localhost:8080/Log/UserUpLoad',formData,{
        headers: {  
          'Content-Type': 'multipart/form-data' ,
          'Token': this.token // 设置Token请求头 
        }
    }).then(response=>{
        if(response.data.code==1){
            this.$notify({
          title: response.data.data,
          message: '您的作品已经上传成功啦',
          type: 'success'
        });
        
        this.shuRu=false;
        
        }
    })
}
        },

        share(index){
    const param = {
        id :this.tableData[index].id
    }

        axios.post('http://localhost:8080/Log/share',param,{
            headers: {  
          'Token': this.token // 设置Token请求头 
        }
        }).then(response=>{
            this.$notify({
          title: response.data.data,
          message: '您的作品已经分享成功啦',
          type: 'success'
        });

        })
        },

        deletes(index) {
        const param = {
            user_id : this.tableData[index].username,
            filename : this.tableData[index].filename
        }

axios.post('http://localhost:8080/Log/delete',param, {
    headers: {  
          'Token': this.token // 设置Token请求头 
        }
}).then(response=>{
    this.$notify({
          title: response.data.data,
          message: '您的作品已经成功删除了',
          type: 'success'
        });

})

        },

        view(index){
         var url=this.tableData[index].url;
         this.$router.push({ name: 'view', query: { url: url } }); 
        },

    download(index){
        window.location.href = this.tableData[index].url;
    },
        getData() {
            var jwt = sessionStorage.getItem("token");
            let headers = {
                Token: jwt // 将 'Authorization' 改为 'Token'
            };
            axios.post('http://localhost:8080/Log/getUserData', this.sel, { headers }).then(
                response => {
                    this.tableData = response.data.data;

                }
            ).catch(err => {
                console.log(err)
            })
        }
    },
    mounted() {

    var a=document.getElementById('tj');
    if(a.getBoundingClientRect().height!=30) window.location.reload(true);

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


        axios.post('http://localhost:8080/Log/getUserData', this.sel, { headers }).then(
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

.dizhi {
    display: none;
}

.aslabel {
            font-family: kaiti;
            font-size: 20px;
            display: inline-block;
            width: 390px;
            height: 50px;
            line-height: 30px;
            text-align: center;
            border: 1px solid #ccc;
            border-radius: 5px;
            margin-right: 10px;
            cursor: pointer;
            color: #fff;
            background-image: linear-gradient(-225deg, #C5C1FF 0%, #FFBAC3 100%);
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
}

.s2 {
    border-bottom: #2CD8D5 solid 5px;
}

.s3 {
    border-bottom: #2CD8D5 solid 5px;
}

.s4 {
    border-bottom: #2CD8D5 solid 5px;
    border-bottom-left-radius: 20px;
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
    /* background-image: url(1.png); */
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

/* 头像li标签 */
.yh li {
    margin-top: 42.33px;
  
}
/* 学校li标签 */
.xx li {
    margin-top: 105.66px;
    width: 150px;
}

/* 学科li标签 */
.xk li {
    margin-left: 100px;
    margin-top: 105.66px;
    font-family: 'Kirang Haerang', cursive;
}

/* 学院li标签 */
.xy li {
    margin-top:105.66px;
}

/* 年级li标签 */
.nj li {
    margin-top: 105.66px;
}
.gn ul li {
    height: 32.33px;
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
    width: 200px;
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
    margin-left: 40px;
}

.xx h2{
    margin-left: 20px;
}

.xy h2{
    width: 135.33px;
    margin-left: 40px;
}

.gn h2 {
    width: 50px;
    margin-left: 100px;
}

.xk h2{
    width: 100px;
    margin-left: 100px;
}

.tj{
    margin-left: 100px;
}

/* 添加的查看和下载按钮 */
.btndiv {
    display: flex;
    margin-top: 93.66px;
    align-items: center;
    
}

.btndiv button {
    width: 50px;
    margin-left: 10px;
}

.ck,.xz,.fx,.sc {
    font-family: kaiti;
    color: #fff;
    border-radius: 5px;
    cursor: pointer;
    border: none;
    margin-top: 2px;
}
.ck{
    background-image: linear-gradient(120deg, #84fab0 0%, #8fd3f4 100%);
}
.xz {
    background-image: linear-gradient(120deg, #fccb90 0%, #d57eeb 100%);
}
.fx{
    background-image: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.sc{
    background-image: linear-gradient(to right, #f78ca0 0%, #f9748f 19%, #fd868c 60%, #fe9a8b 100%);
}
ul {
    margin-top: 0;
}

.yh {
    margin-left: 30px;
}

.xx {
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
    /* display: none; */
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
.shurutx input{
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

.xuey {
    display: none;
}
</style>