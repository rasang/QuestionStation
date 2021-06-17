<template>
	<el-container v-show="show" style="height: calc(98vh); border: 1px solid #eee">
		<el-aside width="200px" style="background-color: rgb(238, 241, 246)">
			<el-menu
				style="height: 100%;"
				default-active="1"
				class="el-menu-vertical-demo"
				@select="handleSelect">
				<div id="logo">知道ZHIDAO</div>
				<el-menu-item index="1">
					<i class="el-icon-user"></i>
					<template #title>用户管理</template>
				</el-menu-item>
				<el-menu-item index="2">
					<i class="el-icon-question"></i>
					<template #title>问题管理</template>
				</el-menu-item>
				<el-menu-item index="3">
					<i class="el-icon-chat-round"></i>
					<template #title>回答管理</template>
				</el-menu-item>
				<el-menu-item index="4">
					<i class="el-icon-setting"></i>
					<template #title>设置</template>
				</el-menu-item>
			</el-menu>
		</el-aside>
	
		<el-main>
			<component :is="componentName"></component>
		</el-main>
	</el-container>
</template>

<script>
import service from './http.js'
import 'element-plus/lib/theme-chalk/index.css';
import UserManage from './components/UesrManage.vue'
import QuestionManage from './components/QuestionManage.vue'
import AnswerManage from './components/AnswerManage.vue'
export default {
  name: 'Admin',
	// 注册组件
  components: {
		UserManage,
		QuestionManage,
		AnswerManage
  },
	data(){
		// 数据
		return {
			componentName: "UserManage",
			show: false
		}
	},
	methods:{
		// 处理点击侧边栏的事件,切换组件
		handleSelect(index){
			// 用户管理
			if(index == 1){
				this.componentName = "UserManage";
			}
			// 问题管理
			else if(index == 2){
				this.componentName = "QuestionManage";
			}
			// 回答管理
			else if(index == 3){
				this.componentName = "AnswerManage";
			}
		}
	},
	
	mounted() {
		// 测试用户是否登录,然后显示内容
		service.get("/admin/checkLogin")
		.then(response=>{
			if(response.data.code == 200){
				this.show = true;
			}
			else{
				location.href = "index.html";
			}
		})
		.catch(error=>{
			console.log(error);
			location.href = "index.html";
		});
	}
}
</script>

<style>
#logo{
	height: 60px;
	line-height: 60px;
	margin: 0;
	border-bottom: 2px solid transparent;
	color: #000000;
	padding: 0 20px;
	font-weight: 800;
	font-size: 140%;
	outline: none;
}
</style>
