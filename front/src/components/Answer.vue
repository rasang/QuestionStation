<template>
	<!-- for循环展示回答 -->
	<div class="answer-wrapper" v-show="answerData != null" v-for="(item, i) in answerData" :key="i">
		<div style="height: 40px;">
			<!-- 头像 -->
			<el-avatar :src="item.userInfo.userAvatar" shape="square" style="float: left;"></el-avatar>
			<!-- 用户名和自我介绍 -->
			<div id="name-intro-wrapper">
				<div style="margin-left: 10px; font-weight: 800;">{{item.userInfo.userName}}</div>
				<div style="margin-left: 10px; color: #BBBBBB;">{{item.userInfo.userIntro}}</div>
			</div>
			<!-- 是否被屏蔽 -->
			<el-tag v-show="item.answer.blocked == 1" type="info" style="float: right; margin-left: 10px;">已屏蔽</el-tag>
			<div v-show="checkUser(item.userInfo.userId)" style="float: right;">
				<!-- 更多按钮 -->
				<el-dropdown>
					<span class="el-dropdown-link">
						<i style="float: right;" class="el-icon-more"></i>
					</span>
					<template #dropdown>
						<el-dropdown-menu>
							<!-- 删除评论选项 -->
							<el-dropdown-item @click="deleteAnswer(item.answer.answerId)">删除评论</el-dropdown-item>
							<!-- 修改评论选项 -->
							<el-dropdown-item @click="updateAnswer(item.answer.answerId, item.answer.answerDetails)">修改评论</el-dropdown-item>
						</el-dropdown-menu>
					</template>
				</el-dropdown>
			</div>
		</div>
		<!-- 回答详情 -->
		<div style="margin-top: 20px;" class="content">
			<div v-html="item.answer.answerDetails" style="text-align: left;"></div>
		</div>
		<!-- 回答日期 -->
		<div style="color: #BBBBBB; float: right; display: flex;">{{new Date(item.answer.answerDate).toLocaleString()}}</div>
	</div>
	<!-- 编辑框 -->
	<el-dialog title="编辑回答" v-model="dialogVisible">
		<div id="editor"></div>
		<el-button @click="cancelEdit()" style="margin-top: 10px;">取消</el-button>
		<el-button @click="commitEdit()" type="primary">提交</el-button>
	</el-dialog>
</template>

<script>

import E from "wangeditor"
import { ElMessage } from 'element-plus'
import service from '../http.js'
import qs from "qs"
function sleep (time) {
  return new Promise((resolve) => setTimeout(resolve, time));
}

export default{
	data(){
		return{
			answerData: null,  //存储回答数据
			dialogVisible: false, // 编辑框是否可见
			answerId: null  // 回答ID
		}
	},
	props:{
		// 问题ID
		questionId: Number
	},
	watch:{
		// 当questionId发送变化执行这个方法，请求回答数据
		questionId(newValue){
			service.get("/public/answers?questionId=" + newValue)
			.then(response=>{
				console.log(response);
				this.answerData = response.data.data;
			});
		}
	},
	methods:{
		// 用于比较评论的用户ID是否是当前用户
		checkUser(userId){
			if(localStorage.getItem("userId") == userId){
				return true;
			}
			return false;
		},
		// 删除回答
		deleteAnswer(answerId){
			// 请求数据
			service.get("/user/answer/delete?answerId=" + answerId)
			.then(response=>{
				// 请求成功
				if(response.data.code == 200){
					this.$notify({
						title: "Success!",
						message: response.data.msg,
						type: "success"
					});
					// 重新加载
					location.reload();
				}
				// 请求失败
				else{
					this.$notify.error({
						title: "Failed!",
						message: response.data.msg,
					});
				}
			})
		},
		// 更新回答
		updateAnswer(answerId, answerDetails){
			// 判断是否登录
			if(localStorage.getItem("login") == "false" || localStorage.getItem("login") == null){
				ElMessage.error('未登录');
				return;
			}
			this.dialogVisible=true; // 设置编辑框可见
			this.answerId = answerId;
			// 加载编辑组件
			sleep(500).then(()=>{
				this.editor = new E("#editor");
				this.editor.create();
				this.editor.txt.html(answerDetails);
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
			// 判断是否登录
			if(localStorage.getItem("login") == "false" || localStorage.getItem("login") == null){
				this.dialogVisible=true;
				ElMessage.error('未登录');
				return;
			}
			// 组合请求数据
			var details = this.editor.txt.html();
			var answerData = {
				"answerId": this.answerId,
				"details": details
			}
			// 发送数据
			service.post("/user/answer/update", qs.stringify(answerData))
			.then((response)=>{
				// 成功
				if(response.data.code == 200){
					// 弹窗
					this.$notify({
						title: "Success!",
						message: response.data.msg,
						type: 'success'
						});
					this.dialogVisible = false;	
					sleep(1000).then(()=>{
						location.reload()
					});
				}
				// 失败
				else{
					// 弹窗
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
.answer-wrapper{
	background-color: #FFFFFF;
	padding: 20px;
	margin: 20px;
	padding-bottom: 30px;
}
#name-intro-wrapper{
	margin-left: 8px; 
	float: left;
}
#comment-wrapper{
	width: 100%;
	height: 50px;
}

.content img{
	width: 80%;
}

.content a{
	color: #000000;
}

.content a:hover{
	color: cornflowerblue;
}

.content input {
	pointer-events: none;
}

.content li {
	list-style-type:none;
}

.content table {
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
}
.content table td,
.content table th {
  border-bottom: 1px solid #ccc;
  border-right: 1px solid #ccc;
  padding: 3px 5px;
}
.content table th {
  border-bottom: 2px solid #ccc;
  text-align: center;
	background-color: #BBBBBB;
}

blockquote {
  display: block;
  border-left: 8px solid #d0e5f2;
  padding: 5px 10px;
  margin: 10px 0;
  line-height: 1.4;
  font-size: 100%;
  background-color: #f1f1f1;
}
</style>
