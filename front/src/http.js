import axios from 'axios'   //引入 axios
import { ElMessage } from 'element-plus'
//import qs from 'qs'; // 引入qs模块，用来序列化post类型的数据，某些请求会用得到
//import { ElMessage } from 'element-plus'   //引入 element-ui 的 Message 模块，用于信息提示
// create an axios instance   创建axios实例
axios.defaults.withCredentials = true;
const service = axios.create({
  baseURL: "http://localhost:8080/", // api 的 base_url
  timeout: 5000, // request timeout  设置请求超时时间
  responseType: "json",
  withCredentials: true, // 是否允许带cookie这些
  headers: {
    "Content-Type": "application/x-www-form-urlencoded"
  }
})


// axios请求数据拦截器,用于刷新token
service.interceptors.request.use(request=>{
	var token = localStorage.getItem("token");
	if(token!=null && token!= ""){
		request.headers["token"] = token;
	}
		request.headers["Cookie"] = token;
	return request;
});

// 此函数用来防止同一时间弹出过多提示框, 可视为自定义的提示框
function timeControlMessage(msg){
	var lastMessageTime = localStorage.getItem("lastMessageTime");
	var now = Date.now();
	console.log("last:" + lastMessageTime);
	console.log(Date.now());
	if(lastMessageTime == null || now - lastMessageTime > 3000){
		localStorage.setItem("lastMessageTime", Date.now());
		ElMessage.error(msg);
	}
}

// http响应拦截器,主要判断返回数据中的响应码,判断是否重载
service.interceptors.response.use(response=>{
	console.log(response.data);
	// 检查登录状态
	if(response.data.code == 302){
		localStorage.setItem("login", "false");
		localStorage.removeItem("token");
		timeControlMessage("登录过期，1s后跳转到主页");
		setTimeout(()=>{
			location.reload();
		}, 1000);
	}
	return response;
});


export default service