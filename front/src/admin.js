import { createApp } from 'vue'
import Admin from './Admin.vue'
import ElementPlus from 'element-plus'
import axios from 'axios'

axios.defaults.withCredentials = true
axios.defaults.baseURL = "http://localhost:8080/"
const app = createApp(Admin)
app.use(ElementPlus)
app.mount('#admin')