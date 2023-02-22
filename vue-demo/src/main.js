import { createApp } from 'vue'
import App from './App.vue'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'
const app = createApp(App)

import axios from 'axios'
import VueAxios from 'vue-axios'
axios.defaults.baseURL = '/api'
app.use(VueAxios, axios)

import qs from 'qs';
app.config.globalProperties.qs = qs;

import router from './router/index'
app.use(router)

app.config.productionTip = false
app.use(Antd).mount('#app')
