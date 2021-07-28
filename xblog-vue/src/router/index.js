import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/pages/front/Login'
import Topic from '@/pages/front/Topic'
import User from '@/pages/front/User'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      component: User,
      meta: {
        title: '用户',
        showNav: true
      }
    },
    {
      path: '/login',
      name: 'Login',
      component: Login,
      meta: {
        title: '登录',
        showNav: false
      }
    },
    {
      path: '/topic',
      name: 'topic',
      component: Topic,
      meta: {
        title: '刷题',
        showNav: true
      }
    }, {
      path: '/User',
      name: 'user',
      component: User,
      meta: {
        title: '用户',
        showNav: true
      }
    }

  ]
})
