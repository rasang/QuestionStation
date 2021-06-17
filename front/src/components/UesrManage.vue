<template>
	<h1>管理用户注册申请</h1>
	<div class="input-wrapper">
		<el-input clearable class="input-name" v-model="applyingInputName" placeholder="根据用户名搜索"></el-input>
		<el-button @click="ApplyingChangePage(1)" class="search-button" type="primary">搜索</el-button>
	</div>
	<!-- 展示申请注册的用户 -->
	<el-table
		stripe
		:data="applyingData"
		height="617px"
		border
		style="width: 100%">
		<el-table-column
			prop="userId"
			label="用户ID"
			width="180">
		</el-table-column>
		<el-table-column
			prop="userName"
			label="用户名"
			width="180">
		</el-table-column>
		<el-table-column
			prop="uesrIntro"
			label="用户介绍">
		</el-table-column>
		<el-table-column
			prop="userTele"
			label="用户电话">
		</el-table-column>
		<el-table-column
			fixed="right"
			label="操作"
			width="150">
			<template #default="scope">
				<!-- 通过和拒绝申请按钮 -->
				<el-button @click="handlePassClick(scope.row)" type="text" size="small">通过申请</el-button>
				<el-button @click="handelRefuseClick(scope.row)" type="text" size="small">拒绝申请</el-button>
			</template>
		</el-table-column>
	</el-table>
	<center>
		<!-- 分页 -->
		<el-pagination
			@current-change="ApplyingChangePage"
			style="margin-top: 20px;"
			width="10%"
			background
			layout="prev, pager, next"
			:total="applyingTotal">
		</el-pagination>
	</center>
	<el-divider></el-divider>
	<!-- 管理用户  -->
	<h1>管理用户</h1>
	<div class="input-wrapper">
		<el-input clearable="" class="input-name" v-model="userInputName" placeholder="根据用户名搜索"></el-input>
		<el-button @click="userChangePage(1)" class="search-button" type="primary">搜索</el-button>
	</div>
	<!-- 展示用户 -->
	<el-table
		stripe
		:data="userData"
		height="617px"
		border
		style="width: 100%">
		<el-table-column
			prop="userId"
			label="用户ID"
			width="180">
		</el-table-column>
		<el-table-column
			prop="userName"
			label="用户名"
			width="180">
		</el-table-column>
		<el-table-column
			prop="uesrIntro"
			label="用户介绍">
		</el-table-column>
		<el-table-column
			prop="userTele"
			label="用户电话">
		</el-table-column>
		<el-table-column
			fixed="right"
			label="操作"
			width="150">
			<template #default="scope">
				<!-- 拉黑按钮 -->
				<el-button @click="handleBlock(scope.row)" type="text" size="small">拉黑用户</el-button>
			</template>
		</el-table-column>
	</el-table>
	<center>
		<!-- 分页 -->
		<el-pagination
			@current-change="userChangePage"
			style="margin-top: 20px;"
			width="10%"
			background
			layout="prev, pager, next"
			:total="userTotal">
		</el-pagination>
	</center>
	<h1>管理已拉黑用户</h1>
	<div class="input-wrapper">
		<el-input clearable="" class="input-name" v-model="blockedInputName" placeholder="根据用户名搜索"></el-input>
		<el-button @click="blockedChangePage(1)" class="search-button" type="primary">搜索</el-button>
	</div>
	<!-- 展示已拉黑用户 -->
	<el-table
		stripe
		:data="blockedData"
		height="617px"
		border
		style="width: 100%">
		<el-table-column
			prop="userId"
			label="用户ID"
			width="180">
		</el-table-column>
		<el-table-column
			prop="userName"
			label="用户名"
			width="180">
		</el-table-column>
		<el-table-column
			prop="uesrIntro"
			label="用户介绍">
		</el-table-column>
		<el-table-column
			prop="userTele"
			label="用户电话">
		</el-table-column>
		<el-table-column
			fixed="right"
			label="操作"
			width="150">
			<template #default="scope">
				<!-- 处理取消拉黑点击 -->
				<el-button @click="handleCancelBlock(scope.row)" type="text" size="small">取消拉黑</el-button>
			</template>
		</el-table-column>
	</el-table>
	<center>
		<!-- 分页 -->
		<el-pagination
			@current-change="blockedChangePage"
			style="margin-top: 20px;"
			width="10%"
			background
			layout="prev, pager, next"
			:total="userTotal">
		</el-pagination>
	</center>
</template>

<script>
import { ref } from 'vue'
import service from '../http.js'
export default {
	data() {
		return {
			applyingData: [], // 申请注册用户数据
			applyingTotal: 0, // 申请注册用户总数
			applyingCurrentPage: 0, // 申请注册用户页数
			applyingInputName: ref(''), // 申请注册用户搜索关键字
			userData: [],  // 用户数据
			userTotal: 0,  // 用户总数
			userCurrentPage: 0,  // 用户当前页
			userInputName: ref(''),  // 用户搜索关键字
			blockedData: [],  // 拉黑用户数据
			blockedTotal: 0,  // 拉黑用户总数
			blockedCurrentPage: 0,  // 拉黑用户当前页
			blockedInputName: ref('')  // 拉黑用户搜索关键字
		};
	},
	methods:{
		// 通过注册申请
		handlePassClick(row) {
			// axios请求后端
			service.get("/admin/user/applying/pass?userId=" + row.userId)
			.then(response=>{
				this.$notify({
					title: "Success!",
					message: response.data.msg,
					type: "success"
				});
				this.freshApplyingData();
			})
			.catch(error=>{
				this.$notify.error({
					title: "Failed!",
					message: error.data.msg,
				});
			});
		},
		// 拒绝注册申请
		handelRefuseClick(row){
			// axios请求后端
			service.get("/admin/user/applying/refuse?userId=" + row.userId)
			.then(response=>{
				this.$notify({
					title: "Success!",
					message: response.data.msg,
					type: "success"
				});
				this.freshApplyingData();
			})
			.catch(error=>{
				this.$notify.error({
					title: "Failed!",
					message: error.data.msg,
				});
			});
		},
		// 分页切换函数
		ApplyingChangePage(page){
			this.applyingCurrentPage = page;
			page = page - 1;
			// axios请求后端
			service.get("/admin/user/applying?count=10&page=" + page + "&userName=" + this.applyingInputName)
			.then(response=>{
				this.applyingData = response.data.data.users;
				this.applyingTotal = response.data.data.pageCount;
			});
		},
		// 刷新分页数据
		freshApplyingData(){
			this.ApplyingChangePage(this.applyingCurrentPage);
			this.userChangePage(1);
		},
		// 用户页面分页切换函数
		userChangePage(page){
			this.userCurrentPage = page;
			page = page - 1;
			// axios请求后端
			service.get("/admin/user?count=10&page=" + page + "&userName=" + this.userInputName)
			.then(response=>{
				this.userData = response.data.data.users;
				this.userTotal = response.data.data.pageCount;
			});
		},
		// 刷新当前页数据
		freshUserData(){
			this.userChangePage(this.userCurrentPage);
		},
		// 处理拉黑
		handleBlock(row){
			// axios请求后端
			service.get("/admin/block/user?userId=" + row.userId)
			.then(response=>{
				this.$notify({
					title: "Success!",
					message: response.data.msg,
					type: "success"
				});
				this.freshBlockedData();
			})
			.catch(error=>{
				this.$notify.error({
					title: "Failed!",
					message: error.data.msg,
				});
			});
		},
		// 已拉黑用户切换分页
		blockedChangePage(page){
			this.blockedCurrentPage = page;
			page = page - 1;
			// axios请求后端
			service.get("/admin/user/blocked?count=10&page=" + page + "&userName=" + this.userInputName)
			.then(response=>{
				this.blockedData = response.data.data.users;
				this.blockedTotal = response.data.data.pageCount;
			});
		},
		// 刷新当前页数据
		freshBlockedData(){
			this.blockedChangePage(this.userCurrentPage);
		},
		// 处理取消拉黑
		handleCancelBlock(row){
			// axios请求后端
			service.get("/admin/cancelBlock/user?userId=" + row.userId)
			.then(response=>{
				this.$notify({
					title: "Success!",
					message: response.data.msg,
					type: "success"
				});
				this.freshUserData();
				this.freshBlockedData();
			})
			.catch(error=>{
				this.$notify.error({
					title: "Failed!",
					message: error.data.msg,
				});
			});
		},
	},
	// 组件挂载后请求数据
	mounted() {
		this.ApplyingChangePage(1);
		this.userChangePage(1);
		this.blockedChangePage(1);
	}
};
</script>

<style>
.el-transfer-panel{
	width: 30% !important;
}

.commit_button{
	margin: 20px;
}
.el-table__row{
	height: 50px;
}
.input-name{
	width: 50%;
}
.search-button{
	margin-left: 20px;
}
.input-wrapper{
	margin: 15px;
}
</style>
