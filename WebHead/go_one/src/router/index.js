import Vue from 'vue'
import VueRouter from 'vue-router'
import LoginOn from '../views/LoginOn.vue'
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'LoginOn',
    component: LoginOn
  },
  {
    path: '/Log/On',
    name: 'MainTo',
    component: () => import('../views/MainTo.vue')
  },
  {
    path: '/Sign',
    name: 'sign',
    component: () => import('../views/SignPassword.vue')
  },
  {
    path :'/Log/PublicTo',
    name : 'public',
    component :()=>import('../views/PublicTo.vue')
  },
  {
    path: '/Log/On/view',
    name: 'view',
    component: () => import('../views/ViewText.vue')
  },
  {
    path :'/Log/MainTo',
    name : 'main',
    component :()=>import('../views/MainTo.vue')
  },
  {
    path :'/Log/AiViews',
    name : 'AiViews',
    component :()=>import('../views/AiViews.vue')
  }, 
  {
    path :'/Log/MadeTo',
    name : 'MadeTo',
    component :()=>import('../views/MadeTo.vue')
  }
  
]

const router = new VueRouter({
  routes
});

export default router
