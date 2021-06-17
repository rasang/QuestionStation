<template>
	<div id="details_container">
		<el-avatar :src="this.userAvatar" shape="square" style="float: left;"></el-avatar>
		<!-- 用户名和自我介绍 -->
		<div id="name-intro-wrapper">
			<div style="margin-left: 10px; font-weight: 800;">{{this.userName}}</div>
			<div style="margin-left: 10px; color: #BBBBBB;">{{this.userIntro}}</div>
		</div>
		<div style="clear:both;"></div>
		<div class="image-width">
			<!-- 问题标题 -->
			<div id="title_container">{{questionTitle}}</div>
			<!-- 问题详情 -->
			<div v-html="questionDetails"></div>
		</div>
	</div>
</template>

<script>
import service from '../http.js';
export default{
	data(){
		return{
			questionTitle: null, // 标题
			questionDetails: null, // 详情
			lastAnswerDate: null, // 上一个回答的日期
			solved: null, // 问题是否已解决
			blocked: null, // 问题是否被拉黑
			userId: null, // 用户ID
			userName: null, // 用户名
			userAvatar: null, // 用户头像
			userIntro: null // 用户自我介绍
		}
	},
	props:{
		questionId: Number
	},
	watch:{
		questionId(newValue){
			// 请求数据
			service.get("/public/question/?id=" + newValue)
			.then(response=>{
				this.questionTitle = response.data.data.question.questionTitle;
				this.questionDetails = response.data.data.question.questionDetails;
				this.lastAnswerDate = response.data.data.question.lastAnswerDate;
				this.solved = response.data.data.question.solved;
				this.blocked = response.data.data.question.blocked;
				this.userId = response.data.data.userInfo.userId;
				this.userName = response.data.data.userInfo.userName;
				this.userAvatar = response.data.data.userInfo.userAvatar;
				this.userIntro = response.data.data.userInfo.userIntro;
			});
		}
	},
}
</script>

<style>
#details_container{
	background-color: #FFFFFF;
	padding: 20px;
	font-size: 150%;
	white-space: normal;
	word-break: break-all;
}

#title_container{
	font-size: 150%;
	background-color: #FFFFFF;
}
#details_container hr{
	width: 50%;
}

#user_info{
	height: 50px;
}

.image-width{
	width: 80%;
}
</style>
