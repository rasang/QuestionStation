import { createApp } from 'vue'
import Details from './Details.vue'
import ElementPlus from 'element-plus'
import axios from 'axios'
axios.defaults.withCredentials = true
axios.defaults.baseURL = "http://localhost:8080/"
const app = createApp(Details)
app.use(ElementPlus)
app.mount('#app')
