<template>
  <!--eslint-disable-next-line-->
	<div style="padding-left: 20%; padding-right: 20%;">
		<!-- vue的for循环展示问题列表 -->
		<li v-for="(i, index) in data" :key="index">
			<el-container id="container">
				<!-- 标题 -->
				<a style="text-decoration: none;" target="_blank" :href="'details.html?id='+i.question.questionId">
					<el-header id="title">{{i.question.questionTitle}}
					<el-tag v-show="i.question.blocked == 1" type="info" style="margin-left: 10px; float: right;">已屏蔽</el-tag>
					<el-tag v-show="i.question.solved == 1" type="success" style="margin-left: 10px; float: right;">已解决</el-tag>
					</el-header>
				</a>
				<!-- 详情和提问人名 -->
				<el-main id="author_details">
					<div class="question_details">{{i.userInfo.userName + ': ' + i.question.questionDetails}}</div>
				</el-main>
				<!-- 其他信息，日期等 -->
				<el-footer id="footer">
					<span>
						<a :href="'details.html?id='+i.question.questionId" target="_blank" style="text-decoration: none;">
							<img src="../assets/chat.png" class="chat_icon icon" align="center"/>
							<span style="color: #9F9F9F; margin-left: 5px;">查看详情</span>
						</a>
					</span>
					<span style="color: #BBBBBB; float: right;">{{new Date(i.question.lastAnswerDate).toLocaleString()}}</span>
				</el-footer>
			</el-container>
		</li>
	</div>
	<!-- 分页工具 -->
	<center>
		<el-pagination
			@current-change="loadQuestions"
			style="margin-top: 20px;"
			width="10%"
			background
			layout="prev, pager, next"
			:total="total">
		</el-pagination>
	</center>
</template>

<script>
	import service from '../http.js';
  export default {
    data () {
      return {
				currentPage: 0, // 当前页数
        data: [], // 问题
				total: 0 // 总的问题数量
      }
    },
    methods: {
			// 载入数据
			loadQuestions (page) {
				page = page - 1;
				// 根据用户是否登录判断要请求哪个接口，请求user接口可以显示自己被屏蔽的问题
				var urlPath = "/public";
				var login = (eval('(' + this.checkLogin() + ')'));
				if(login.code == 200){
					urlPath = "/user";
				}
				// 请求数据
				service.get(urlPath + "/questions?key=&count=10&page=" + page)
				.then((response)=>{
					this.data = response.data.data.questions;
					this.total = response.data.data.total;
				})
      },
			// 判断是否登录
			checkLogin(){
				let xmlhttp=new XMLHttpRequest();
				xmlhttp.open("GET","http://127.0.0.1:8080/user/checkLogin",false);
				xmlhttp.setRequestHeader("token", localStorage.getItem("token"));
				xmlhttp.send(null);
				return xmlhttp.responseText;
			}
    },
		// 在组件挂载后加载问题
		mounted() {
			this.loadQuestions(1);
		}
  }
</script>
<style>
li{
	list-style: none;
}
#container{
	background-color: #FFFFFF;
}
.list-item{
	padding-left: 14%;
	padding-right: 14%;
}
#container{
	border-style: solid;
	border-color: #BBBBBB;
	border-width: 1px;
}
#title{
	left: 162px;
	top: 102px;
	height: 40px !important;
	font-size: 28px;
	color: #000000;
	font-weight: 800;
	text-align: left;
	font-family: SourceHanSansSC-bold;
	margin-top: 5px;
	overflow:hidden; 
	text-overflow:ellipsis;
	display:-webkit-box; 
	-webkit-box-orient:vertical;
	-webkit-line-clamp:1; 
}
#author_details{
	left: 160px;
	top: 148px;
	color: #000000;
	font-size: 20px;
	font-weight: 300;
	text-align: left;
	font-family: SourceHanSansSC-regular;
	padding-top: 0px;
	padding-bottom: 10px;
}
#footer{
	height: 50px !important;
	text-align: left;
}
.chat_icon{
	height: 28px;
	width: 28px;
}
.favor_icon{
	height: 30px;
	width: 30px;
}
.icon{
	margin-left: 0px;
}
.question_details{
	width:100%;
	word-break:break-all;
	display:-webkit-box;/**对象作为伸缩盒子模型展示**/
	-webkit-box-orient:vertical;/**设置或检索伸缩盒子对象的子元素的排列方式**/
	-webkit-line-clamp:2;/**显示的行数**/
	overflow:hidden;/**隐藏超出的内容**/
}
</style>