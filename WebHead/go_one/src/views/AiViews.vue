<template>
<div class="box1" id="abc" >

    <div id="loadings" class="loading-overlay" v-if="isLoading">
    </div>
        <div class="box">
            <div class="shu">
                <div class="s1" @click="goOne">
                    <h2>个人中心</h2>
                </div>
                <div class="s2" @click="goTwo">
                    <h2>资源中心</h2>
                </div>
                <div class="s3">
                    <h2>AI问答</h2>
                </div>
                <div class="s4" @click="goFour">
                    <h2>网页制作</h2>
                </div>
            </div>
            <div class="heng">
                <div class="daohang">
                    <div class="aiTDraw" id="aiTDraw" @click="autoImage">
                        <h3>AI绘图</h3>
                    </div>
                    <div class="aiRDraw" id="aiRDraw" @click="YourImage">
                        <h3>AI自绘像</h3>
                    </div>
                    <div class="aitalk" id="aitalk"  @click="ChoiceTalk">
                        <h3>AI对话</h3>
                    </div>
                </div>


                <!-- 对话框 -->
                <div class="chat-container" id="chat-container">
                    <!-- 加载中的容器 -->
                    <div id="loading" class="loading-container">
                        <!-- 转圈动画的元素 -->
                        <div class="spinner"></div>
                        <div class="text">
                            <h2>小I正在努力想答案</h2>
                        </div>
                    </div>
                </div>


                <!-- 图片上传框 -->
                <div class="uploadbox1" id="uploadbox1" v-show="upLoadImage">
                    <div class="uploadbox"> <!-- 图片框 -->
                        <img :src="imageUrl" v-show="showImage" id="pimg"> <!-- 准备加载的图片 -->
                        <div class="plussign">+</div> <!-- 加号 -->
                        <label for="fileinput">上传图片</label><!-- file input 的代替 -->
                    </div>
                    <!-- 提交的代替 -->
                    <div class="tj" @click="storage"><label>确认上传</label></div>
                        <!-- 上传图片的输入框 -->
                    <input type="file" id="fileinput" style="display: none;" @change="onFile">
                </div>

                <!-- 输入框 -->
                <div class="shell" id="shell">
                    <input type="text" placeholder="Search" id="shellinput" class="shellinput" v-model="styleIndex">

                    <!-- 确认按钮，加了提交监听函数 -->
                    <div class="tb" @click="ChangeAI">
                        <i class="fa fa-hand-o-right"></i>
                        <i class="fa fa-search"></i>
                    </div>
                </div>
                <!-- 图片选择框 -->

            </div>
        </div>
    </div>
    <!-- <script src="https://cdn.jsdelivr.net/npm/jquery/dist/jquery.min.js"></script> -->
    <!--  -->
</template>


<script>
import axios from 'axios';
export default {
    data() {
        return {
        imageUrl : '',
        upLoadImage : false,
        file : null,
        styleIndex : '',
        showImage : false,
        SeverYourselfImage : '',
        token : sessionStorage.getItem('token'),
        UserImage : null,
        sel: {
                user_id: sessionStorage.getItem('UserID')
            },
        choice : 1,
        name : '',
        frequency : 0,
        currentCode : 0,
        isLoading : false


        }
    },
    methods : {

        goOne(){
            this.$router.push('/Log/MainTo');
            window.location.reload(true);
        },
        goTwo(){
            this.$router.push('/Log/PublicTo');
        },
        goFour(){
            this.$router.push('/Log/MadeTo');
        },

        hideUpLoad(){
    this.upLoadImage=false;
    this.file=null;
    this.showImage=false;
        },
    autoImage(){
        this.choice=1;
        this.hideUpLoad();
        var shellinput = document.getElementById('shellinput');
            shellinput.placeholder = "描述一下图片吧";
            this.addaiMessage('小I','可以给小I一点提示吗');
            this.imageUrl='';
    },
    YourImage (){
     this.choice=2;
     this.showUpLoad();
     var shellinput = document.getElementById('shellinput');
            shellinput.placeholder = "请选择你喜欢的样式哦";
            this.addaiMessage('小I','0 复古漫画,1 3D童话,2 二次元,3 小清新,4 未来科技,5 国画古风,6 将军百战,7 炫彩卡通,8 清雅国风,9 喜迎新年');
            this.addaiMessage('小I','请输入对应的数字哟');
    },

    ChoiceTalk(){
     this.choice=3;
     this.hideUpLoad();
     var shellinput = document.getElementById('shellinput');
            shellinput.placeholder = "";
            this.addaiMessage('小I','可以开始和小I进行对话了哟');
            this.imageUrl='';
    },
        showUpLoad(){
    this.upLoadImage=true;
        },
        onFile(e){
            this.file = e.target.files[0];
            var reader=new FileReader;
            if(this.file!=null) {
                this.showImage=true;
                reader.onload = (e) => {
        // 将读取到的图片数据设置为 imageUrl
        this.imageUrl = e.target.result; // 这里就是图片的 Base64 URL
      };

      // 开始读取文件为DataURL（Base64编码）
      reader.readAsDataURL(this.file);
                
            }
        },

        storage(){
            if(this.file!=null) {
                this.$notify({
          title: 'success',
          message: '图片上传成功',
          type: 'success'
        });
            }else {
                this.$notify({
          title: '警告',
          message: '请先选中图片',
          type: 'warning'
        });
            }
        },
        showLoading() {
            var loadingContainer = document.getElementById('loading');
            loadingContainer.style.display = 'flex'; // 显示加载容器
            this.isLoading=true;
        },

        // 隐藏加载动画函数，也是直接调用
        hideLoading() {
            var loadingContainer = document.getElementById('loading');
            loadingContainer.style.display = 'none'; // 隐藏加载容器
            this.isLoading=false;
        },

        sendaimessage() {
            var aimessage = this.contents;//换成服务器传回来ai文本信息？
            if (aimessage) {
                this.addaiMessage('小I', aimessage);
            }
        },
        addaiMessage(sender, content) {
            var chatContainer = document.getElementById('chat-container');
            // 创建消息div
            var newMessageDiv = document.createElement('div');
            newMessageDiv.className = 'chat-message';

            // 发送者div,名字
            var senderDiv = document.createElement('div');
            senderDiv.style.width = '25px';
            senderDiv.innerText = sender + ': ';
            newMessageDiv.appendChild(senderDiv);

            // 聊天内容框的左侧三角形
            var sjdiv = document.createElement('div');
            sjdiv.className = 'sjdiv';
            newMessageDiv.appendChild(sjdiv);

            // 聊天内容框
            var messageDiv = document.createElement('div');
            messageDiv.className = 'messageDiv';
            // 加入聊天内容
            messageDiv.innerText = content;
            newMessageDiv.appendChild(messageDiv);

            // 头像div
            var avatarImg = document.createElement('img');
            avatarImg.className = 'avatar';
            // 可以从服务器调取用户头像
            avatarImg.src ='https://web-backs.oss-cn-hangzhou.aliyuncs.com/AI%E8%83%8C%E6%99%AF.png'; // 这里为ai的头像路径
            // 把头像放在最前面
            newMessageDiv.insertBefore(avatarImg, senderDiv);
            // 塞入聊天窗口
            chatContainer.appendChild(newMessageDiv);
        },


        sendimgMessage() {
            var imgmessage = this.SeverYourselfImage;//换成服务器传回来图片地址？
            if (imgmessage) {
                this.addimgMessage('小I', imgmessage);
            }
        },
        // 创建AI返回的图片
        addimgMessage(sender, url) {
            var chatContainer = document.getElementById('chat-container');
            // 创建消息div
            var newMessageDiv = document.createElement('div');
            newMessageDiv.className = 'chat-message';

            // 发送者div,名字
            var senderDiv = document.createElement('div');
            // senderDiv.className = 'senderdiv';
            senderDiv.style.width = '25px';
            senderDiv.innerText = sender + ': ';
            newMessageDiv.appendChild(senderDiv);

            // 聊天内容框的左侧三角形
            var sjdiv = document.createElement('div');
            sjdiv.className = 'sjdiv';
            newMessageDiv.appendChild(sjdiv);

            // 聊天内容框
            var messageDiv = document.createElement('div');
            messageDiv.className = 'messageDiv';
            // 加入聊天内容
            var imgMessage = document.createElement('img');
            imgMessage.className = 'imgMessage';
            imgMessage.src = url;
            messageDiv.appendChild(imgMessage);
            newMessageDiv.appendChild(messageDiv);

            // 头像div
            var avatarImg = document.createElement('img');
            avatarImg.className = 'avatar';
            avatarImg.src = 'https://web-backs.oss-cn-hangzhou.aliyuncs.com/AI%E8%83%8C%E6%99%AF.png'; //AI的头像路径
            // 把头像放在最前面
            newMessageDiv.insertBefore(avatarImg, senderDiv);
            // 塞入聊天窗口
            chatContainer.appendChild(newMessageDiv);
        },

        addMessage(sender, content) {
            var chatContainer = document.getElementById('chat-container');
            // 创建消息div
            var newMessageDiv = document.createElement('div');
            newMessageDiv.className = 'chat-message';

            // 发送者div,名字
            var senderDiv = document.createElement('div');
            // senderDiv.style.width = '25px';
            senderDiv.innerText = sender + ': ';
            newMessageDiv.appendChild(senderDiv);

            // 聊天内容框的左侧三角形
            var sjdiv = document.createElement('div');
            sjdiv.className = 'sjdiv';
            newMessageDiv.appendChild(sjdiv);

            // 聊天内容框
            var messageDiv = document.createElement('div');
            messageDiv.className = 'messageDiv';
            // 加入聊天内容
            messageDiv.innerText = content;
            newMessageDiv.appendChild(messageDiv);

            // 头像div
            var avatarImg = document.createElement('img');
            avatarImg.className = 'avatar';
            // 可以从服务器调取用户头像
            avatarImg.src = this.UserImage; // 这里替换为用户的头像路径？
            // 把头像放在最前面
            newMessageDiv.insertBefore(avatarImg, senderDiv);
            // 塞入聊天窗口
            chatContainer.appendChild(newMessageDiv);
        },
         // 创建ai的消息

        ChangeAI(){
           
        if(this.choice==1) {
            
         this.PaintImages();
         
        }else if(this.choice==2){
           
            this.upLoadImages();
        }else {
           
        this.TalkAi();
        }
        },

        PaintImages(){
        var formData =new FormData();
        formData.append('message',this.styleIndex);
        if(this.styleIndex==''){
            this.$notify({
          title: '警告',
          message: '请先输入描述信息,方便小I进行绘图哦',
          type: 'warning'
        });
        }else {
            this.showLoading();
            this.addMessage(this.name,this.styleIndex);
        axios.post('http://localhost:8080/Log/ai/TDraw',formData,{
            headers: {  
          'Token': this.token // 设置Token请求头 
        }
        }).then(
            response=>{
                this.SeverYourselfImage = response.data.data;
                this.sendimgMessage();
                this.hideLoading();
                this.styleIndex='';
            }
        )
        }
        },

        TalkAi(){
    
    if(this.styleIndex==''){
        this.$notify({
          title: '警告',
          message: '请输入对话哦',
          type: 'warning'
        });
    }else {
        if(this.frequency==0) {
            var formData=new FormData();
    formData.append('message',this.styleIndex);
    formData.append('code',1);
    this.addMessage(this.name,this.styleIndex);
    this.showLoading();
        axios.post('http://localhost:8080/Log/ai/talk/open',formData,{
            headers: {  
          'Token': this.token // 设置Token请求头 
        }
        }).then(response=>{
     this.contents=response.data.data[1];
     this.currentCode=response.data.data[0];
     this.sendaimessage();
     this.hideLoading();
     this.frequency=1;
     alert(this.currentCode)
    })

        }else {

if(this.styleIndex=='0') {
this.TalkAiOver();
}else {
    var form=new FormData();
    form.append('message',this.styleIndex);
    form.append('code',1);
    form.append('currentCode',this.currentCode);
    this.addMessage(this.name,this.styleIndex);
    this.showLoading();
    axios.post('http://localhost:8080/Log/ai/talk/link',form,{
        headers: {  
          'Token': this.token // 设置Token请求头 
        }
    }).then(response=>{
        this.contents=response.data.data;
        this.sendaimessage();
        this.hideLoading();
    })
        }
        }
        

    }

        },

        TalkAiOver(){
            alert(this.currentCode)
            var formData =new FormData();
            formData.append('code',0);
            formData.append('currentCode',this.currentCode);
            axios.post('http://localhost:8080/Log/ai/talk/close',formData,{
                headers: {  
          'Token': this.token // 设置Token请求头 
        }
            }).then(response=>{
        this.contents=response.data.data;
        this.contents='谢谢你和小I进行对话哦';
        this.sendaimessage();
        this.hideLoading();
        this.frequency=0;
    })
        },

        upLoadImages(){
            if(this.styleIndex=='') {
                this.$notify({
          title: '警告',
          message: '请选择您喜欢的样式',
          type: 'warning'
        });
            }else {
            const formData = new FormData();
            formData.append('file',this.file);
            formData.append('styleIndex',this.styleIndex);
            this.showLoading();
            axios.post('http://localhost:8080/Log/ai/RDraw',formData,{
                headers: {  
          'Content-Type': 'multipart/form-data' ,
          'Token': this.token // 设置Token请求头 
        }
            }).then(response=>{
               if(response.data.code==1) {
                this.SeverYourselfImage =response.data.data;
                this.sendimgMessage();
                this.showImage=false;
                this.imageUrl='';
                this.hideLoading();
               }
            }).catch(err=>{
                console.log(err);
            })
        }
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
                    this.UserImage = response.data.data;
                }
            ).catch(err => {
                console.log(err)
            }),
            axios.post('http://localhost:8080/Log/getName',this.sel,{
                headers: {  
          'Token': this.token // 设置Token请求头 
        }
            }).then(response=>{
        this.name=response.data.data;
      })

    },
    beforeDestroy () {
        var formData =new FormData();
            formData.append('code',0);
            formData.append('currentCode',this.currentCode);
        axios.post('http://localhost:8080/Log/ai/talk/close',formData,{
            headers: {  
          'Token': this.token // 设置Token请求头 
        }
        });
    }
}

</script>

<style>

@import url('https://cdn.staticfile.org/font-awesome/4.7.0/css/font-awesome.css');

.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.5); /* 可调整透明度 */
  z-index: 999; /* 设置高 z-index 使其覆盖其他元素 */
  display: flex;
  justify-content: center;
  align-items: center;
}

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



.s1,
.s2,
.s3,
.s4 {
    cursor: pointer;
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
    border-bottom: none;
    height: 175px;
    cursor: pointer;
}

h2 {
    font-size: 23px;
    height: 30px;
    font-family: kaiti;
}

h3 {
    font-size: 23px;
    font-family: kaiti;
    display: block;
}

.aiRDraw,
.aiTDraw,
.aitalk {
    width: 395.333333333333333333333333333333333333333333333333333333333333333333333333333333333333px;
    height: 50px;
    align-items: center;
    justify-content: center;
    display: flex;
    cursor: pointer;
}

.aiRDraw:hover,
.aiTDraw:hover,
.aitalk:hover {
    background-color: #fff;
    transition: 0.1s;
    color: red;
}

.aiRDraw {
    border-right: #2CD8D5 solid 5px;
}

.aiTDraw {
    border-right: #aea9fb solid 5px;
}

.aitalk {
    border-top-right-radius: 20px;
}

.heng {
    width: 1190px;
    height: 700px;
    border-top-right-radius: 20px;
    border-bottom-right-radius: 20px;
}

.daohang {
    display: flex;
    height: 50px;
}


.chat-container {
    background-color: #fff;
    width: 1186px;
    height: 586px;
    overflow: auto;
    background-image: url(@/assets/2.png);
    background-position: center;
    background-size: cover;
}

.shell {
    position: relative;
    width: 1146px;
    padding: 10px;
    padding-left: 30px;
    background-color: #8cf6bc;
    border-bottom-right-radius: 20px;
}

.shell input {
    outline: none;
    width: 1000px;
    height: 40.5px;
    color: #fff;
    font: 50 30px '优设标题黑';
    border: 0;
    background-color: transparent;
}

.shell input::placeholder {
    color: #8f36367e;
}

.shell div {
    cursor: pointer;
    display: flex;
    font-size: 30px;

    position: absolute;
    right: 60px;
    top: 10px;
    color: #fff;
    width: 50px;
    height: 30px;
}

.shell div .fa {
    margin: 5px 14px;
    transition: .3s;
}

.shell div .fa-search {
    transform: translateX(-80px);
    opacity: 1;
}

.shell div .fa-hand-o-right {
    transform: translateX(-50px);
    opacity: 0;
}

.shell div:hover .fa-search {
    transform: translateX(0);
    opacity: 0;
}

.shell div:hover .fa-hand-o-right {
    transform: translateX(55px);
    opacity: 1;
}

.shell div::before {
    content: 'GO!';
    position: absolute;
    display: block;
    font-size: 30px;
    background-color: #8cf6bc;
    padding: 5px 15px;
    top: 25px;
    right: -25px;
    border-radius: 5px;
    transition: .3s;
    opacity: 0;
    animation: box 1s infinite ease;
}

.shell div:hover::before {
    top: -70px;
    opacity: 1;
}

@keyframes box {
    0% {
        transform: rotate(0deg);
    }

    33% {
        transform: rotate(8deg);
    }

    66% {
        transform: rotate(-8deg);
    }

    100% {
        transform: rotate(0deg);
    }
}

.chat-message {
    width: 785px;
    margin-bottom: 10px;
    margin-top: 10px;
    margin-left: 30px;
    display: flex;
    align-items: center;
}

.avatar {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    border: #C5C1FF 5px solid;
    margin-right: 10px;
}

.messageDiv {
    background-color: rgba(128, 128, 128, 0.2);
    padding: 10px;
    border-radius: 5px;
}

.sjdiv {
    width: 0;
    height: 0;
    /*让中心为零，便于让边框形成三角形*/
    border: 11px solid transparent;
    /*让其他三条边框透明，以便于另一条边框形成三角形*/
    border-right: 11px solid rgba(128, 128, 128, 0.2);
    /*想要哪条边框当三角形就写哪条*/

}

/* 加载中的容器样式 */
.loading-container {
    position: fixed;
    /* 固定定位，覆盖整个聊天窗口 */
    top: 142px;
    left: 290px;
    width: 1186px;
    height: 586px;
    background-color: rgba(255, 255, 255, 0.7);
    /* 半透明背景 */
    display: flex;
    justify-content: center;
    /* 水平居中 */
    align-items: center;
    /* 垂直居中 */
    z-index: 9999;
    /* 确保它在最顶层 */
    display: none;
}

.text {
    position: fixed;
    top: 458px;
    left: 799px;
}

.text h2 {
    font-size: 20px;
    font-weight: 400;
    color: #aea9fb;
}

/* 转圈动画的样式 */
.spinner {
    border: 4px solid #f3f3f3;
    /* 外围边框 */
    border-top: 4px solid #3498db;
    /* 顶部边框颜色 */
    border-radius: 50%;
    /* 圆形 */
    width: 50px;
    /* 宽度 */
    height: 50px;
    /* 高度 */
    animation: spin 1s linear infinite;
    /* 动画名称，持续时间，速度曲线，循环 */
}

/* 关键帧动画 */
@keyframes spin {
    0% {
        transform: rotate(0deg);
    }

    /* 初始状态 */
    100% {
        transform: rotate(360deg);
    }

    /* 结束状态 */
}

.imgMessage {
    width: 300px;

}


.uploadbox {
    width: 300px;
    height: 300px;
    border: 1px dashed #C5C1FF;
    position: fixed;
    z-index: 999;
    top: 300px;
    right: 263px;

}

.tj {
    width: 80px;
    height: 35px;
    border: 1px solid #C5C1FF;
    border-radius: 15px;
    background-color: #C5C1FF;
    position: fixed;
    z-index: 999;
    top: 615px;
    right: 263px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.plussign {
    font-size: 35px;
    color: #C5C1FF;
    margin-top: 104px;
    margin-left: 136px;

}

.tj label {
    cursor: pointer;
    color: #fff;
    /* font-size: 50px; */
}

.uploadbox label {
    font-size: 20px;
    color: #C5C1FF;
    margin-left: 110px;
    cursor: pointer;
}

#pimg {
    width: 300px;
    height: 300px;
    position: absolute;
    z-index: 1000;
}


</style>