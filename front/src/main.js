import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import axios from 'axios'
// import VueRouter from 'vue-router'

axios.defaults.withCredentials = true;
axios.defaults.baseURL = "http://localhost:8080/"
const app = createApp(App)
app.use(ElementPlus)
// app.use(VueRouter)
app.mount('#app')
