<template>
	<div id="wrapper">
		<el-container id="container">
			<el-aside id="sizebar" width="250px">
				<!-- 头像栏 -->
				<el-upload
					id="avatar"
					class="avatar-uploader"
					:headers="getToken()"
					action="http://127.0.0.1:8080/user/upload"
					:show-file-list="false"
					:on-success="handleAvatarSuccess"
					:before-upload="beforeAvatarUpload"
				>
					<img v-if="userAvatar" :src="userAvatar" class="avatar">
					<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					<el-button style="margin-top: 10px;" type="primary">点击上传新头像</el-button>
				</el-upload>
			</el-aside>
			<!-- 个人信息栏-->
			<el-main id="info-main">
				<div style="margin-bottom: 50px;">
					<span v-show="editState" id="username">{{userName}}</span>
					<div v-show="!editState"><el-input style="font-size: 200%;" v-model="userName"></el-input></div>
				</div>
				<!-- 个人介绍 -->
				<el-row :gutter="20">
					<el-col :span="6"><h4>一句话介绍</h4></el-col>
					<el-col v-if="editState" :span="14"><h4 class="content">{{userIntro}}</h4></el-col>
					<div v-show="!editState"><el-input v-model="userIntro"></el-input></div>
				</el-row>
				<el-divider></el-divider>
				<!-- 电话 -->
				<el-row :gutter="20">
					<el-col :span="6"><h4>电话</h4></el-col>
					<el-col v-if="editState" :span="14"><h4 class="content">{{userTele}}</h4></el-col>
					<div v-show="!editState"><el-input v-model="userTele"></el-input></div>
				</el-row>
				<el-divider></el-divider>
				<!-- 所在地 -->
				<el-row :gutter="20">
					<el-col :span="6"><h4>所在地</h4></el-col>
					<el-col v-if="editState" :span="14"><h4 class="content">{{userSite}}</h4></el-col>
					<div v-show="!editState"><el-input v-model="userSite"></el-input></div>
				</el-row>
				<!-- 行业 -->
				<el-divider></el-divider>
				<el-row :gutter="20">
					<el-col :span="6"><h4>所在行业</h4></el-col>
					<el-col v-if="editState" :span="14"><h4 class="content">{{userIndustry}}</h4></el-col>
					<div v-show="!editState"><el-input v-model="userIndustry"></el-input></div>
				</el-row>
				<el-divider></el-divider>
				<!-- 启动编辑按钮 -->
				<div style="text-align: center;">
					<el-affix position="bottom" :offset="20">
						<el-button :type="editButtonType" @click="changeEditState()">{{editButtonText}}</el-button>
					</el-affix>
				</div>
				<!-- 我的提问和回答标签栏 -->
				<el-tabs v-model="activeName" style="margin-top: 20px;" type="card" @tab-click="handleClick">
					<!-- 我的提问 -->
						<el-tab-pane label="我的提问" name="first">
							<div v-for="(item, i) in questions" :key="i">
								<el-row :gutter="20">
									<el-col :span="20"><h3 style="margin-top: 10px; margin-bottom: 10px;">{{item.question.questionTitle}}</h3></el-col>
									<el-col :span="4">
										<div v-show="item.question.solved == 1">
											<center>已终结<i style="margin-left: 5px; color: #42B983;" class="el-icon-check"></i></center>
										</div>
										<div v-show="item.question.solved == 0">
											<center>
												<el-popconfirm
												@confirm="endQuestion(item.question.questionId)"
												title="确定要终结问题吗？" 
												confirm-button-text="是的" 
												cancel-button-text="算了">
													<template #reference>
														<el-button>终结问题</el-button>
													</template>
												</el-popconfirm>
											</center>
										</div>
									</el-col>
								</el-row>
								<p>{{item.question.questionDetails}}</p>
								<el-divider></el-divider>
							</div>
						</el-tab-pane>
						<!-- 我的回答 -->
						<el-tab-pane label="我的回答" name="second">
							<div v-for="(answer_item, j) in answers" :key="j">
								<div class="answer">{{answer_item.answer.answerDetails}}</div>
								<el-divider></el-divider>
							</div>
						</el-tab-pane>
					</el-tabs>
			</el-main>
		</el-container>
	</div>
</template>

<script>
function sleep (time) {
  return new Promise((resolve) => setTimeout(resolve, time));
}

import qs from "qs"
import service from '../http.js';
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
export default{
	data() {
		// 数据
		return{
			userId: null,
			userName: ref(""),
			userAvatar: ref(""),
			userTele: ref(""),
			userIntro: ref(""),
			userSite: ref(""),
			userIndustry: ref(""),
			editState: true, // 是否处于编辑状态的标志位
			editButtonType: "primary", //按钮样式
			editButtonText: "编辑", // 按钮文字
			activeName: 'first', //tab栏的起始激活位置，这里是我的提问
			questions: [], // 存储我的问题
			answers: [], // 存储我的回答
		}
	},
	mounted(){
		// 读取用户的个人信息
		service.get("/user/userInfo")
		.then(response=>{
			console.log(response);
			if(response.data.code == 200){
				this.userId = response.data.data.userId;
				this.userName = response.data.data.userName;
				this.userAvatar = response.data.data.userAvatar;
				this.userTele = response.data.data.userTele;
				this.userIntro = response.data.data.userIntro;
				this.userSite = response.data.data.userSite;
				this.userIndustry = response.data.data.userIndustry;
			}
		})
		.catch(error=>{
			ElMessage.error(error.data.msg);
		});
		// 请求我的问题数据
		service.get("/user/questions/my")
		.then(response=>{
			console.log(response);
			if(response.data.code == 200){
				this.questions = response.data.data;
			}
		})
		.catch(error=>{
			ElMessage.error(error.data.msg);
		});
		// 请求我的回答数据
		service.get("/user/answers/my")
		.then(response=>{
			console.log(response);
			if(response.data.code == 200){
				this.answers = response.data.data;
			}
		})
		.catch(error=>{
			ElMessage.error(error.data.msg);
		});
	},
	methods:{
		// 点击按钮变成可编辑状态
		changeEditState(){
			this.editState = !this.editState;
			// 根据按钮格式判断是要提交还是要进入编辑状态
			// 提交
			if(this.editButtonType == ""){
				this.editButtonType = "primary";
				this.editButtonText = "编辑";
				service.post("/user/userInfo", qs.stringify({
					userName: this.userName,
					userTele: this.userTele,
					userIntro: this.userIntro,
					userSite: this.userSite,
					userIndustry: this.userIndustry,
				}))
				.then(response=>{
					// 编辑成功
					if(response.data.code == 200){
						this.$notify({
							title: "Success!",
							message: "修改成功",
							type: "success"
						});
					}
					// 编辑失败
					else{
						this.$notify.error({
							title: "Failed!",
							message: "修改失败"
						});
					}
				});
			}
			// 进入编辑状态
			else{
				this.editButtonType = "";
				this.editButtonText = "完成";
			}
		},
		// 处理头像上传成功
		handleAvatarSuccess(res, file) {
			console.log(res);
			this.userAvatar = res.data.userAvatar;
			this.imageUrl = URL.createObjectURL(file.raw);
			this.$notify({
				title: "Success!",
				message: "头像上传成功",
				type: "success"
			});
		},
		// 在上传之前对文件的格式进行判断
		beforeAvatarUpload(file) {
			const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
			const isLt2M = file.size / 1024 / 1024 < 2;

			if (!isJPG) {
				this.$message.error('上传头像图片只能是 JPG或PNG 格式!');
			}
			if (!isLt2M) {
				this.$message.error('上传头像图片大小不能超过 2MB!');
			}
			return isJPG && isLt2M;
		},
		// 从localStorage获得token
		getToken(){
			return {"token" : localStorage.getItem("token")};
		},
		// 测试用
		handleClick(tab, event) {
			console.log(tab, event);
		},
		// 终结问题
		endQuestion(questionId){
			service.get("/user/questions/solved?questionId=" + questionId)
			.then(response=>{
				// 终结成功
				if(response.data.code == 200){
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
					this.$notify.error({
						title: "Failed!",
						message: response.data.msg,
					});
				}
			})
			.catch(()=>{
				this.$notify.error({
					title: "Failed!",
					message: "未知错误"
				});
			});
		}
	}
}
</script>

<style>
#wrapper{
	background-color: #FFFFFF;
	margin-left: 14%;
	margin-right: 14%;
	border-style: solid;
	border-color: #BBBBBB;
	border-width: 1px;
}

#info-main{
	background-color: #FFFFFF !important;
	text-align: left !important;
}

#avatar{
	margin-left: 25px;
	margin-right: 25px;
	margin-top: 25px;
}

#username{
	font-size: 350%;
	font-weight: 500;
}

.avatar{
		width: 200px;
		height: 200px;
		object-fit: cover;
}

.content{
	font-weight: 500;
}

.answer{
	overflow:hidden;
	text-overflow:ellipsis;
	display:-webkit-box; 
	-webkit-box-orient:vertical;
	-webkit-line-clamp:3; 
	margin: 20px;
}
</style>
