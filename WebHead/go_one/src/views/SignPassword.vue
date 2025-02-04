<template>
 <div class="shell">
        <form>
            <h2>LOGIN</h2>
            <div class="form-item">
                <label for="username">Username</label>
                <div class="input-wrapper">
                    <input type="text" id="username" autocomplete="off" autocorrect="off" autocapitalize="off"
                        spellcheck="false" data-lpignore="true" v-model="user.username"/>
                </div>
            </div>
            <div class="form-item">
                <label for="password">Password</label>
                <div class="input-wrapper">
                    <input type="password" id="password" autocomplete="off" autocorrect="off" autocapitalize="off"
                        spellcheck="false" data-lpignore="true" v-model="user.password"/>
                    <button type="button" id="eyeball">
                        <div class="eye"></div>
                    </button>
                    <div id="beam"></div>
                </div>
            </div>
            <button type="button" id="submit" @click="GoBack">Sign in</button>
        </form>
    </div>
</template>

<script>
import axios from 'axios';
export default {
  data() {
    return {
    user :{
      username: '',
      password: ''

    }
    };
  },
  methods :{
   GoBack(){
   if((this.user.username==''||this.user.password==''))
   {
    this.$message({
          message: '账号或密码不能为空',
          type: 'warning'
        });
   }
  else {
   axios.post('http://localhost:8080/Sign',this.user).
        then(response=>{
           if(response.data.code==1) {
            this.$notify({
          title: '成功',
          message: '账号创建成功',
          type: 'success'
        });
           }else if(response.data.code==0){
            
            this.$notify({
          title: '警告',
          message: '该账号已存在',
          type: 'warning'
        });
        
           }
          }).catch(err=>{
          console.log(err);
          })
  }
   }
    
  },
  mounted(){
    const root = document.documentElement; // 获取根元素
        const eye = document.querySelector('#eyeball'); // 获取眼睛按钮元素
        const beam = document.querySelector('#beam'); // 获取光束元素
        const passwordInput = document.querySelector('#password'); // 获取密码输入框元素
        root.addEventListener('mousemove', (e) => {
            // 获取光束的位置和鼠标位置
            let rect = beam.getBoundingClientRect(); // 获取光束元素的位置信息
            
            let mouseX = rect.right + (rect.width / 2); // 光束的横坐标
            let mouseY = rect.top + (rect.height / 2); // 光束的纵坐标
            // 计算角度
            let rad = Math.atan2(mouseX - e.pageX, mouseY - e.pageY); // 计算鼠标和光束之间的角度
            let degrees = (rad * (20 / Math.PI) * -1) - 350; // 将弧度转换为角度
            // 设置根元素的样式变量
            root.style.setProperty('--beamDegrees', `${degrees}deg`); // 设置根元素的样式变量，控制光束的旋转角度
        });

        // 眼睛按钮点击事件监听器
        eye.addEventListener('click', e => {
            e.preventDefault(); // 阻止默认行为
            // 切换显示密码的状态
            document.body.classList.toggle('show-password'); // 切换body元素的类名，控制密码是否显示
            // 切换密码输入框的类型
            passwordInput.type = passwordInput.type === 'password' ? 'text' : 'password'; // 切换密码输入框的类型，控制密码是否显示
            // 聚焦到密码输入框
            passwordInput.focus(); // 将焦点聚焦到密码输入框
        });
  },
  
};

</script>

<style>
        /* 设置全局样式 */
        * {
            box-sizing: border-box;
            transition: .2s;
        }

        /* 设置根元素变量 */
        :root {
            --bgColor: white;
            /* 设置背景颜色变量为白色 */
            --inputColor: black;
            /* 设置输入框颜色变量为黑色 */
            --outlineColor: rgb(60, 115, 235);
            /* 设置输入框边框颜色变量为RGB(60, 115, 235) */
            --border: black;
        }

        /* 设置body样式 */
        body {
            display: flex;
            /* 设置body元素为flex布局 */
            justify-content: center;
            /* 水平居中对齐 */
            align-items: center;
            /* 垂直居中对齐 */
            height: 100vh;
            /* 设置body元素的高度为视口高度 */
            overflow: hidden;
            /* 隐藏溢出内容 */
            background: var(--bgColor);
            /* 设置背景颜色为变量--bgColor的值 */
        }

        /* 设置外层容器样式 */
        .shell {
            width: 100%;
            /* 设置外层容器的宽度为100% */
            height: 100vh;
            /* 设置外层容器的高度为视口高度 */
            display: flex;
            /* 设置外层容器为flex布局 */
            align-items: center;
            /* 垂直居中对齐 */
            justify-content: center;
            /* 水平居中对齐 */
            background-image: url(@/assets/1jpg.jpg);
            /* 设置背景图片为./img/1.png */
            background-size: cover;
            /* 背景图片等比例缩放铺满容器 */
        }

        /* 设置显示密码时的样式 */
        body.show-password {
            --bgColor: rgba(0, 0, 0, 0.9);
            /* 设置背景颜色变量为RGBA(0, 0, 0, 0.9) */
            --inputColor: white;
            /* 设置输入框颜色变量为白色 */
            --border: rgb(255, 255, 255);
        }


        /* 设置表单样式 */
        form {
            transform: translate3d(0, 0, 0);
            /* 3D变换，无位移 */
            padding: 50px;
            /* 设置内边距为10px */
            border: 20px solid var(--border);
            border-radius: 10px;
            box-shadow: 10px 10px 10px #00000065;
        }

        form>*+* {
            margin-top: 15px;
            /* 设置相邻元素之间的上边距为15px */
        }

        .form-item>*+* {
            margin-top: 0.5rem;
            /* 设置相邻元素之间的上边距为0.5rem */
        }

        /* 设置label, input, button样式 */
        h2,
        label,
        input,
        button {
            font-size: 2rem;
            /* 设置字体大小为2rem */
            color: var(--inputColor);
            /* 设置字体颜色为变量--inputColor的值 */
            font-family: '优设标题黑';
        }

        h2 {
            font-size: 4rem;
            margin: 0;
        }

        label:focus,
        input:focus,
        button:focus {
            outline-offset: 2px;
            /* 设置聚焦时的外边距为2px */
        }

        label::-moz-focus-inner,
        input::-moz-focus-inner,
        button::-moz-focus-inner {
            border: none;
            /* 去掉Firefox浏览器的聚焦时的内边框 */
        }

        /* 设置密码相关样式 */
        label[id=password],
        input[id=password],
        button[id=password] {
            color: black;
            /* 设置字体颜色为黑色 */
        }

        /* 设置按钮样式 */
        button {
            border: none;
            /* 去掉按钮的边框 */
        }

        [id=submit] {
            width: 100%;
            cursor: pointer;
            /* 设置鼠标样式为手型 */
            margin: 20px 0 0 2px;
            /* 设置外边距为20px 0 0 2px */
            padding: 0.75rem 1.25rem;
            /* 设置内边距为0.75rem 1.25rem */
            color: var(--bgColor);
            /* 设置字体颜色为变量--bgColor的值 */
            background-color: var(--inputColor);
            /* 设置背景颜色为变量--inputColor的值 */
            box-shadow: 4px 4px 0 rgba(30, 144, 255, 0.2);
            /* 设置阴影效果 */
        }

        [id=submit]:active {
            transform: translateY(1px);
            /* 设置点击时向下位移1px */
        }

        /* 设置输入框包裹器样式 */
        .input-wrapper {
            position: relative;
            /* 设置相对定位 */
        }

        /* 设置输入框样式 */
        input {
            padding: 0.75rem 4rem 0.75rem 0.75rem;
            /* 设置内边距为0.75rem 4rem 0.75rem 0.75rem */
            width: 100%;
            /* 设置宽度为100% */
            border: 2px solid transparent;
            /* 设置边为2px透明 */
            border-radius: 0;
            /* 设置边框圆角为0 */
            background-color: transparent;
            /* 设置背景颜色为透明 */
            box-shadow: inset 0 0 0 2px black, inset 6px 6px 0 rgba(30, 144, 255, 0.2), 3px 3px 0 rgba(30, 144, 255, 0.2);
            /* 设置阴影效果 */
            -webkit-appearance: none;
            /* 去掉Webkit浏览器的默认样式 */
        }

        input:focus {
            outline-offset: 1px;
            /* 设置聚焦时的外边距为1px */
        }

        /* 设置显示密码时的输入框样式 */
        .show-password input {
            box-shadow: inset 0 0 0 2px black;
            /* 设置阴影效果 */
            border: 2px dashed white;
            /* 设置边框为2px虚线白色 */
        }

        .show-password input:focus {
            outline: none;
            /* 去掉聚焦时的外边框 */
            border-color: rgb(255, 255, 145);
            /* 设置边框颜色为RGB(255, 255, 145) */
        }

        /* 设置眼睛按钮样式 */
        [id=eyeball] {
            --size: 1.25rem;
            /* 设置变量--size的值为1.25rem */
            display: flex;
            /* 设置为flex布局 */
            align-items: center;
            /* 垂直居中对齐 */
            justify-content: center;
            /* 水平居中对齐 */
            cursor: pointer;
            /* 设置鼠标样式为手型 */
            outline: none;
            /* 去掉聚焦时的外边框 */
            position: absolute;
            /* 设置绝对定位 */
            top: 50%;
            /* 设置顶部距离为50% */
            right: 0.75rem;
            /* 设置右侧距离为0.75rem */
            border: none;
            /* 去掉边框 */
            background-color: transparent;
            /* 设置背景颜色为透明 */
            transform: translateY(-50%);
            /* 设置向上位移50% */
        }

        [id=eyeball]:active {
            transform: translateY(calc(-50% + 1px));
            /* 设置点击时向上位移50% + 1px */
        }

        .eye {
            width: var(--size);
            /* 设置宽度为变量--size的值 */
            height: var(--size);
            /* 设置高度为变量--size的值 */
            border: 2px solid var(--inputColor);
            /* 设置边框为2px实线，颜色为变量--inputColor的值 */
            border-radius: calc(var(--size) / 1.5) 0;
            /* 设置边框圆角为变量--size的值除以1.5，0 */
            transform: rotate(45deg);
            /* 设置旋转45度 */
        }

        .eye:before,
        .eye:after {
            content: "";
            /* 清空内容 */
            position: absolute;
            /* 设置绝对定位 */
            top: 0;
            /* 设置顶部距离为0 */
            right: 0;
            /* 设置右侧距离为0 */
            bottom: 0;
            /* 设置底部距离为0 */
            left: 0;
            /* 设置左侧距离为0 */
            margin: auto;
            /* 设置自动外边距 */
            border-radius: 100%;
            /* 设置边框圆角为100% */
        }

        .eye:before {
            width: 35%;
            /* 设置宽度为35% */
            height: 35%;
            /* 设置高度为35% */
            background-color: var(--inputColor);
            /* 设置背景颜色为变量--inputColor的值 */
        }

        .eye:after {
            width: 65%;
            /* 设置宽度为65% */
            height: 65%;
            /* 设置高度为65% */
            border: 2px solid var(--inputColor);
            /* 设置边框为2px实线，颜色为变量--inputColor的值 */
            border-radius: 100%;
            /* 设置边框圆角为100% */
        }

        /* 设置光束样式 */
        [id=beam] {
            position: absolute;
            /* 设置绝对定位 */
            top: 50%;
            /* 设置顶部距离为50% */
            right: 1.75rem;
            /* 设置右侧距离为1.75rem */
            clip-path: polygon(100% 50%, 100% 50%, 0 0, 0 100%);
            /* 设置剪切路径为多边形 */
            width: 100vw;
            /* 设置宽度为100vw */
            height: 25vw;
            /* 设置高度为25vw */
            z-index: 1;
            /* 设置层级为1 */
            mix-blend-mode: multiply;
            /* 设置混合模式为multiply */
            transition: transform 200ms ease-out;
            /* 设置过渡效果为200ms的ease-out */
            transform-origin: 100% 50%;
            /* 设置变换原点为右侧50% */
            transform: translateY(-50%) rotate(var(--beamDegrees, 0));
            /* 设置向上位移50%并旋转--beamDegrees度 */
            pointer-events: none;
            /* 禁用指针事件 */
        }

        .show-password [id=beam] {
            background: rgb(255, 255, 145);
            /* 设置背景颜色为RGB(255, 255, 145) */
        }
    </style>