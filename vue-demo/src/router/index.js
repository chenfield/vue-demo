import {createRouter, createWebHistory} from 'vue-router'
//import { createApp } from 'vue'
//import App from './App.vue'
import home from '../components/ComHome.vue'
import table from '../components/ComTable.vue'
import upload from '../components/ComUpload.vue'
//const Vue = createApp({})

//Vue.use(VueRouter)

const routes = [
    {
        path:'/',
        component: home
    },    
    {
        path:'/home',
        component: home
    },    
    {
        path:'/table',
        component: table
    },    
    {
        path:'/upload',
        component: upload
    }
]

const router = new createRouter({
    routes,
    history: createWebHistory()
})

export default router