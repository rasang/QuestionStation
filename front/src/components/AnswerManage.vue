<template>
	<!-- 已屏蔽回答列表 -->
	<h1>管理已屏蔽回答</h1>
	<div class="input-wrapper">
		<!-- 搜索关键字输入框 -->
		<el-input clearable class="input-name" v-model="blockAnswerInputName" placeholder="根据用户名搜索"></el-input>
		<!-- 搜索按钮 -->
		<el-button @click="AnswerBlockedChangePage(1)" class="search-button" type="primary">搜索</el-button>
	</div>
	<!-- 展示结果 -->
	<el-table
		stripe
		:data="blockAnswerData"
		height="556px"
		border
		style="width: 100%">
		<!-- 回答id -->
		<el-table-column
			prop="answer.answerId"
			label="回答ID"
			width="180">
		</el-table-column>
		<!-- 详情 -->
		<el-table-column
			prop="answer.answerDetails"
			label="回答详情">
		</el-table-column>
		<!-- 回答发布者 -->
		<el-table-column
			prop="userInfo.userName"
			label="发布者">
		</el-table-column>
		<!-- 对回答进行操作 -->
		<el-table-column
			fixed="right"
			label="操作"
			width="150">
			<template #default="scope">
				<!-- 取消屏蔽按钮 -->
				<el-button @click="handleCancelBlockAnswer(scope.row)" type="text" size="small">取消屏蔽</el-button>
			</template>
		</el-table-column>
	</el-table>
	<center>
		<!-- 分页 -->
		<el-pagination
			@current-change="AnswerBlockedChangePage"
			style="margin-top: 20px;"
			width="10%"
			background
			layout="prev, pager, next"
			:total="blockAnswerTotal">
		</el-pagination>
	</center>
	<el-divider></el-divider>
	<!-- 未屏蔽回答列表 -->
	<h1>管理未屏蔽回答</h1>
	<div class="input-wrapper">
		<!-- 搜索关键字输入框 -->
		<el-input clearable class="input-name" v-model="answerInputName" placeholder="根据用户名搜索"></el-input>
		<!-- 搜索按钮框 -->
		<el-button @click="AnswerChangePage(1)" class="search-button" type="primary">搜索</el-button>
	</div>
	<!-- 数据展示 -->
	<el-table
		stripe
		:data="answerData"
		height="556px"
		border
		style="width: 100%">
		<!-- 回答ID -->
		<el-table-column
			prop="answer.answerId"
			label="回答ID"
			width="180">
		</el-table-column>
		<!-- 回答详情 -->
		<el-table-column
			prop="answer.answerDetails"
			label="回答详情">
		</el-table-column>
		<!-- 回答发布者 -->
		<el-table-column
			prop="userInfo.userName"
			label="发布者">
		</el-table-column>
		<!-- 对该回答进行操作 -->
		<el-table-column
			fixed="right"
			label="操作"
			width="150">
			<template #default="scope">
				<!-- 屏蔽按钮 -->
				<el-button @click="handleBlockAnswer(scope.row)" type="text" size="small">屏蔽回答</el-button>
			</template>
		</el-table-column>
	</el-table>
	<center>
		<!-- 分页 -->
		<el-pagination
			@current-change="AnswerChangePage"
			style="margin-top: 20px;"
			width="10%"
			background
			layout="prev, pager, next"
			:total="answerTotal">
		</el-pagination>
	</center>
</template>

<script>
import { ref } from 'vue'
import service from '../http.js'
export default {
	data() {
		return {
			blockAnswerData: [],  // 已屏蔽的回答数据
			blockAnswerTotal: 0,  //已屏蔽的回答数量
			blockAnswerCurrentPage: 0,  // 已屏蔽回答当前页
			blockAnswerInputName: ref(''),  // 搜索关键字
			answerData: [], // 未屏蔽回答数据
			answerTotal: 0, // 未屏蔽回答总数
			answerCurrentPage: 0, // 未屏蔽回答当前页
			answerInputName: ref('') // 搜索关键字
		};
	},
	methods:{
		// 请求已屏蔽回答的数据
		AnswerBlockedChangePage(page){
			this.blockAnswerCurrentPage = page;
			page = page - 1;
			service.get("/admin/answers?count=10&blocked=1&page=" + page + "&key=" + this.blockAnswerInputName)
			.then(response=>{
				console.log(response);
				this.blockAnswerData = response.data.data.answers;
				this.blockAnswerTotal = response.data.data.total;
			});
		},
		// 刷新数据
		freshAnswerBlockedData(){
			this.AnswerBlockedChangePage(this.blockAnswerCurrentPage);
		},
		// 处理取消屏蔽回答
		handleCancelBlockAnswer(row){
			// axios请求
			service.get("/admin/cancelBlock/answer?answerId=" + row.answer.answerId)
			.then(response=>{
				// 对返回结果弹窗显示
				this.$notify({
					title: "Success!",
					message: response.data.msg,
					type: "success"
				});
				this.freshAnswerBlockedData();
				this.freshAnswerData();
			})
			.catch(error=>{
				this.$notify.error({
					title: "Failed!",
					message: error.data.msg,
				});
			});
		},
		// 请求未屏蔽的回答数据
		AnswerChangePage(page){
			this.answerCurrentPage = page;
			page = page - 1;
			// axios请求数据
			service.get("/admin/answers?count=10&blocked=0&page=" + page + "&key=" + this.blockAnswerInputName)
			.then(response=>{
				console.log(response);
				this.answerData = response.data.data.answers;
				this.answerTotal = response.data.data.total;
			});
		},
		// 刷新数据
		freshAnswerData(){
			this.AnswerChangePage(this.answerCurrentPage);
			this.freshAnswerBlockedData();
		},
		// 处理屏蔽回答
		handleBlockAnswer(row){
			// axios发送数据
			service.get("/admin/block/answer?answerId=" + row.answer.answerId)
			.then(response=>{
				// 对返回结果进行提示
				this.$notify({
					title: "Success!",
					message: response.data.msg,
					type: "success"
				});
				this.freshAnswerBlockedData();
				this.freshAnswerData();
			})
			.catch(error=>{
				this.$notify.error({
					title: "Failed!",
					message: error.data.msg,
				});
			});
		}
	},
	// 在组件挂载时请求数据
	mounted() {
		this.AnswerBlockedChangePage(1);
		this.AnswerChangePage(1);
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
