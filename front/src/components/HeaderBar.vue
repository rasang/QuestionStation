
<template>
<el-menu :default-active="1" class="el-menu-demo" mode="horizontal" @select="handleSelect">
	<!-- logo -->
  <div id="logo">知道ZHIDAO</div>
	<!-- 首页 -->
  <el-menu-item id="index" index="1">首页</el-menu-item>
	<!-- 个人中心 -->
  <el-menu-item v-show="showMyZone" index="2">个人中心</el-menu-item>
	<!-- 登录注册登出按钮 -->
  <div id="login_register_button">
    <el-button @click="dialogFormVisible = true;model = 'login';" v-show="loginRegisterLogoutButtonState">登录</el-button>
    <el-button @click="dialogFormVisible = true;model = 'register'" v-show="loginRegisterLogoutButtonState" type="primary">注册</el-button>
    <el-button @click="logout();" v-show="!loginRegisterLogoutButtonState" type="primary">登出</el-button>
  </div>
</el-menu>
<div class="line"></div>
<!-- 登录对话框 -->
<el-dialog title="" v-model="dialogFormVisible" width="400px">
  <div id="login_logo">知道ZHIDAO</div>
  <el-form :model="form">
    <el-form-item label="用户名" :label-width="formLabelWidth">
      <el-input v-model="form.name" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item label="密 码" :label-width="formLabelWidth">
      <el-input v-model="form.password" autocomplete="off"></el-input>
    </el-form-item>
  </el-form>
  <template #footer>
    <span class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="onSubmit()">确 定</el-button>
    </span>
  </template>
</el-dialog>
</template>

<script>
	import service from '../http.js'
	import qs from "qs"
	import { ElMessage } from 'element-plus'
  export default {
    data() {
			// 数据
      return {
				loginRegisterLogoutButtonState: (localStorage.getItem("token")==null),
				model: "login",
        dialogFormVisible: false,
				// 表单
        form: {
          name: '',
          password: '',
        },
        formLabelWidth: '60px'
      };
    },
		props:{
			showMyZone: Boolean
		},
		methods:{
			// 提交登录表单
			onSubmit(){
				service.post("/public/" + this.model, qs.stringify({
					userName: this.form.name,
					password: this.form.password
				}))
				.then((response)=>{
					console.log(response);
					// 处理登录成功
					if(response.data.code == 200){
						this.$notify({
							title: "Success!",
							message: response.data.msg,
							type: "success"
						});
						// 切换按钮 并保存数据到localStorage
						this.dialogFormVisible = false;
						if(this.model == "login"){
							this.loginRegisterLogoutButtonState = false;
							localStorage.setItem("login", true);
							localStorage.setItem("token", response.data.data.token);
							localStorage.setItem("userId", response.data.data.userId);
						}
					}
					// 处理登录失败
					else{
						this.$notify.error({
							title: "Failed!",
							message: response.data.msg,
						});
					}
				})// 其他异常
				.catch((error)=>{
					error;
					this.$notify.error({
						title: "Failed!",
						message: "未知错误！"
					});
				})
			},
			// 处理切换首页和个人中心的点击事件
			handleSelect(key, path){
				console.log(key == 1);
				console.log(path);
				// 切换到首页
				if(key == 1){
					if(!this.showMyZone){
						location.href = "./index.html";
					}
					this.$parent.$parent.$parent.changeNavMenu("ViewQuestions");
				}
				// 切换到个人中心
				else{
					// 判断是否登录
					if(localStorage.getItem("token") == null || localStorage.getItem("token" == "")){
						ElMessage.error("未登录");
						document.getElementById("index").click();
					}
					else{
						this.$parent.$parent.$parent.changeNavMenu("MyZone");
					}
				}
			},
			// 登出
			logout(){
				localStorage.removeItem("token");
				location. reload();
			}
		},
		};
</script>

<style>
#logo{
    float: left;
    height: 60px;
    line-height: 60px;
    margin: 0;
    border-bottom: 2px solid transparent;
    color: #000000;
    padding: 0 20px;
    font-weight: 800;
	outline: none;
}
#login_register_button{
	float: right;
	height: 60px;
	margin-top: 10px;
	margin-right: 10px;
}
#login_logo{
    font-weight: 800;
    color: #000000;
	font-size: large;
	margin-bottom: 40px;
}
</style>
