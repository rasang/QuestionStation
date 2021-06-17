<template>
	<el-container class="container">
		<!-- 导航栏 -->
		<el-header style="padding: 0;">
			<HeaderBar/>
		</el-header>
		<!-- 显示内容 -->
		<el-main id="main">
			<ViewQuestionDetails :questionId="questionId"/>
			<el-divider content-position="center">所有回答</el-divider>
			<Answer :questionId="questionId"/>
		</el-main>
		<!-- 添加评论按钮 -->
		<el-affix v-show="true" position="bottom" class="buttonAside" offset="100">
			<el-button @click="showEditor()" class="buttonSize" size="medium" type="primary" icon="el-icon-edit" circle></el-button>
		</el-affix>
	</el-container>	
	<!-- 编辑面板 -->
	<el-dialog title="发布回答" v-model="dialogVisible">
		<div id="editor"></div>
		<el-button @click="cancelEdit()" style="margin-top: 10px;">取消</el-button>
		<el-button @click="commitEdit()" type="primary">提交</el-button>
	</el-dialog>
</template>

<script>
import service from './http.js'
// import { ElMessage } from 'element-plus'
// import E from "wangeditor"
import HeaderBar from './components/HeaderBar.vue'
// import ViewQuestions from './components/ViewQuestions.vue'
// import MyZone from './components/MyZone.vue'
import ViewQuestionDetails from './components/ViewQuestionDetails.vue'
// import Comment from './components/Comment.vue'
import Answer from './components/Answer.vue'
import 'element-plus/lib/theme-chalk/index.css';
import {ref } from 'vue'
import E from "wangeditor"
import { ElMessage } from 'element-plus'
import qs from "qs"

function sleep (time) {
  return new Promise((resolve) => setTimeout(resolve, time));
}
// 获得get传参
function getQueryString(name) {
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var result = window.location.search.substr(1).match(reg);
	if(result!=null)return  unescape(result[2]); return null;
}


export default {
  name: 'Details',
	// 注册组件
  components: {
		HeaderBar,
		ViewQuestionDetails,
		// Comment,
		Answer
  },
	data(){
		// 数据
		return {
			questionId: null,
			input: ref(""),
			dialogVisible: false,
		}
	},
	// 保存get传参
	mounted(){
		this.questionId = getQueryString("id")
	}
	,methods:{
		// 显示编辑器
		showEditor(){
			// 通过localStorage简单判断登录状态
			if(localStorage.getItem("login") == "false" || localStorage.getItem("login") == null){
				ElMessage.error('未登录');
				return;
			}
			this.dialogVisible=true;
			// 创建编辑器示例
			sleep(500).then(()=>{
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
		// 提交编辑
		commitEdit(){
			if(localStorage.getItem("login") == "false" || localStorage.getItem("login") == null){
				this.dialogVisible=true;
				ElMessage.error('未登录');
				return;
			}
			// 以HTML格式提取内容
			var details = this.editor.txt.html();
			var answerData = {
				"questionId": this.questionId,
				"details": details
			}
			// 使用axios提交内容
			service.post("/user/answer", qs.stringify(answerData))
			.then((response)=>{
				// 通过响应的code判断结果
				if(response.data.code == 200){
					this.$notify({
						title: "Success!",
						message: response.data.msg,
						type: 'success'
						});
					this.dialogVisible = false;	
					// 重载页面
					sleep(1000).then(()=>{
						location.reload()
					});
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
	#main{
		padding-left: 20%;
		padding-right: 20%;
		background-color: #F6F6F6;
		min-height: calc(82vh);
	}
	
	#comment{
		width: 60%;
		height: 60px;
		background-color: #FFFFFF;
		border-radius: 14px;
		border-style: solid;
		border-width: 2px;
		border-color: #778899;
	}
	
	#input{
		margin: 8px;
		height: 44px;
		width: 100%;
		float: left;
	}
	
	.container{
		background-color: #F6F6F6;
	}
	
	.el-divider__text{
		background-color: #F6F6F6;
	}
	
	.buttonAside{
		text-align: right;
		margin-top: 10px;
		margin-right: 20px;
	}
	
	.buttonSize{
		min-width: 60px;
		min-height: 60px;
	}
</style>
