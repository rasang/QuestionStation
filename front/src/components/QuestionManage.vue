<template>
	<h1>管理已屏蔽问题</h1>
	<div class="input-wrapper">
		<el-input clearable class="input-name" v-model="blockQuestionInputName" placeholder="根据用户名搜索"></el-input>
		<el-button @click="QuestionBlockedChangePage(1)" class="search-button" type="primary">搜索</el-button>
	</div>
	<!-- 展示已屏蔽的问题 -->
	<el-table
		stripe
		:data="blockQuestionData"
		height="556px"
		border
		style="width: 100%">
		<el-table-column
			prop="question.questionId"
			label="问题ID"
			width="180">
		</el-table-column>
		<el-table-column
			prop="question.questionTitle"
			label="问题标题"
			width="180">
		</el-table-column>
		<el-table-column
			prop="question.questionDetails"
			label="问题详情">
		</el-table-column>
		<el-table-column
			prop="userInfo.userName"
			label="发布者">
		</el-table-column>
		<el-table-column
			fixed="right"
			label="操作"
			width="150">
			<template #default="scope">
				<!-- 取消屏蔽按钮 -->
				<el-button @click="handleCancelBlockQuestion(scope.row)" type="text" size="small">取消屏蔽</el-button>
			</template>
		</el-table-column>
	</el-table>
	<center>
		<!-- 分页 -->
		<el-pagination
			@current-change="QuestionBlockedChangePage"
			style="margin-top: 20px;"
			width="10%"
			background
			layout="prev, pager, next"
			:total="blockQuestionTotal">
		</el-pagination>
	</center>
	<el-divider></el-divider>
	<h1>管理未屏蔽问题</h1>
	<div class="input-wrapper">
		<el-input clearable class="input-name" v-model="questionInputName" placeholder="根据用户名搜索"></el-input>
		<el-button @click="QuestionChangePage(1)" class="search-button" type="primary">搜索</el-button>
	</div>
	<!-- 展示未屏蔽的问题 -->
	<el-table
		stripe
		:data="questionData"
		height="556px"
		border
		style="width: 100%">
		<el-table-column
			prop="question.questionId"
			label="问题ID"
			width="180">
		</el-table-column>
		<el-table-column
			prop="question.questionTitle"
			label="问题标题"
			width="180">
		</el-table-column>
		<el-table-column
			prop="question.questionDetails"
			label="问题详情">
		</el-table-column>
		<el-table-column
			prop="userInfo.userName"
			label="发布者">
		</el-table-column>
		<el-table-column
			fixed="right"
			label="操作"
			width="150">
			<template #default="scope">
				<!-- 屏蔽问题按钮 -->
				<el-button @click="handleBlockQuestion(scope.row)" type="text" size="small">屏蔽问题</el-button>
			</template>
		</el-table-column>
	</el-table>
	<center>
		<!-- 分页 -->
		<el-pagination
			@current-change="QuestionChangePage"
			style="margin-top: 20px;"
			width="10%"
			background
			layout="prev, pager, next"
			:total="questionTotal">
		</el-pagination>
	</center>
</template>

<script>
import { ref } from 'vue'
import service from '../http.js'
export default {
	data() {
		return {
			blockQuestionData: [], // 已屏蔽问题数据
			blockQuestionTotal: 0, // 已屏蔽问题总数
			blockQuestionCurrentPage: 0, // 已屏蔽问题当前页数
			blockQuestionInputName: ref(''), // 已屏蔽问题搜索关键字
			questionData: [],  // 未屏蔽问题数据
			questionTotal: 0, // 未屏蔽问题总数
			questionCurrentPage: 0,  // 未屏蔽问题当前页数
			questionInputName: ref('') // 未屏蔽问题搜索关键字
		};
	},
	methods:{
		// 请求已屏蔽问题数据
		QuestionBlockedChangePage(page){
			this.blockQuestionCurrentPage = page;
			page = page - 1;
			service.get("/admin/question/blocked?count=10&page=" + page + "&searchKey=" + this.blockQuestionInputName)
			.then(response=>{
				console.log(response);
				this.blockQuestionData = response.data.data.questionAndUserInfo;
				this.blockQuestionTotal = response.data.data.total;
			});
		},
		// 刷新数据
		freshQuestionBlockedData(){
			this.QuestionBlockedChangePage(this.blockQuestionCurrentPage);
		},
		// 处理屏蔽问题点击
		handleCancelBlockQuestion(row){
			// axios请求数据
			service.get("/admin/cancelBlock/questions?questionId=" + row.question.questionId)
			.then(response=>{
				this.$notify({
					title: "Success!",
					message: response.data.msg,
					type: "success"
				});
				this.freshQuestionBlockedData();
				this.freshQuestionData();
			})
			.catch(error=>{
				this.$notify.error({
					title: "Failed!",
					message: error.data.msg,
				});
			});
		},
		// 请求未屏蔽问题的数据
		QuestionChangePage(page){
			this.questionCurrentPage = page;
			page = page - 1;
			// axios请求数据
			service.get("/public/questions?count=10&page=" + page + "&key=" + this.questionInputName)
			.then(response=>{
				console.log(response);
				this.questionData = response.data.data.questions;
				this.questionTotal = response.data.data.total;
			});
		},
		// 刷新当前页数据
		freshQuestionData(){
			this.QuestionChangePage(this.questionCurrentPage);
			this.freshQuestionBlockedData();
		},
		// 处理屏蔽问题点击
		handleBlockQuestion(row){
			// axios请求数据
			service.get("/admin/block/questions?questionId=" + row.question.questionId)
			.then(response=>{
				this.$notify({
					title: "Success!",
					message: response.data.msg,
					type: "success"
				});
				this.freshQuestionBlockedData();
				this.freshQuestionData();
			})
			.catch(error=>{
				this.$notify.error({
					title: "Failed!",
					message: error.data.msg,
				});
			});
		}
	},
	// 挂载组件时加载数据
	mounted() {
		this.QuestionBlockedChangePage(1);
		this.QuestionChangePage(1);
	}
};
</script>

<style>
.cell{
	overflow:hidden;
	
	text-overflow:ellipsis;
	
	display:-webkit-box;
	
	-webkit-line-clamp:1;
	
	-webkit-box-orient:vertical;
}
</style>
