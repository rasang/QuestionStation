<template>
	<el-container>
		<!-- 导航栏 -->
		<el-header style="padding: 0 !important;">
			<HeaderBar showMyZone="true"/>
		</el-header>
		<!-- 显示主题 -->
		<el-main id="main">
			<component :is="componentName"></component>
			<!-- 返回顶部按钮 -->
			<el-backtop>
				<el-button class="buttonSize" size="medium" icon="el-icon-top" circle></el-button>
			</el-backtop>
			<!-- 发布问题按钮 -->
			<div v-show="editorButton" class="buttonAside">
				<el-button @click="showEditor()" class="buttonSize" size="medium" type="primary" icon="el-icon-edit" circle></el-button>
			</div>
		</el-main>
	</el-container>
	<!-- 编辑器 -->
	<el-dialog title="发布问题" v-model="dialogVisible">
		<el-input v-model="title" placeholder="标题"></el-input>
		<div id="editor"></div>
		<el-button @click="cancelEdit()" style="margin-top: 10px;">取消</el-button>
		<el-button @click="commitEdit()" type="primary">提交</el-button>
	</el-dialog>
</template>

<script>
import service from './http.js'
import { ElMessage } from 'element-plus'
import E from "wangeditor"
import HeaderBar from './components/HeaderBar.vue'
import ViewQuestions from './components/ViewQuestions.vue'
import MyZone from './components/MyZone.vue'
import 'element-plus/lib/theme-chalk/index.css';
import {ref } from 'vue'
import qs from "qs"

function sleep (time) {
  return new Promise((resolve) => setTimeout(resolve, time));
}

export default {
  name: 'App',
	// 注册组件
  components: {
    HeaderBar,
		ViewQuestions,
		MyZone,
  },
	data(){
		// 变量
		return {
			componentName: "ViewQuestions",
			dialogVisible: false,
			title: ref(''),
			editor: null,
			editorButton: true
		}
	},
	methods:{
		// 更改组件
		changeNavMenu(_componentName){
			if(_componentName === "ViewQuestions"){
				this.editorButton = true;
			}
			else{
				this.editorButton = false;
			}
			this.componentName = _componentName;
		},
		// 显示编辑器
		showEditor(){
			if(localStorage.getItem("login") == "false" || localStorage.getItem("login") == null){
				ElMessage.error('未登录');
				return;
			}
			this.dialogVisible=true;
			sleep(500).then(()=>{
				// 创建示例
				this.editor = new E("#editor");
				this.editor.create();
				this.editor.txt.html('');
			});
		},
		// 取消编辑
		cancelEdit(){
			this.$confirm("取消编辑将失去内容，是否继续？","提示",{
				confirmButtonText: "确定",
				cancelButtonText: "取消",
				type: "warning"
			}).then(()=>{
				// 确认取消
				this.dialogVisible = false;
			});
		},
		// 提交问题
		commitEdit(){
			// 通过login简单判断用户是否登录
			if(localStorage.getItem("login") == "false" || localStorage.getItem("login") == null){
				this.dialogVisible=true;
				ElMessage.error('未登录');
				return;
			}
			// 以HTML格式提取出问题内容
			var details = this.editor.txt.html();
			var questionData = {
				"title": this.title,
				"details": details
			}
			// 通过axios发送请求
			service.post("/user/questions", qs.stringify(questionData))
			.then((response)=>{
				if(response.data.code == 200){
					this.$notify({
						title: "Success!",
						message: response.data.msg,
						type: 'success'
						});
					this.dialogVisible = false;
				}
				else{
					this.$notify.error({
						title: "Failed!",
						message: response.data.msg,
						});
				}
			});
		}
	}
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
#main{
	background-color: #F6F6F6 !important;
	min-height: calc(90vh);
}
.buttonAside{
	position: fixed;
	text-align: left;
	margin-top: 10px;
	bottom: 25px;
}
.buttonSize{
	min-width: 60px;
	min-height: 60px;
}
#editor{
	text-align: left;;
}
</style>
