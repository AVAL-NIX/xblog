import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/pages/front/Login'
import Topic from '@/pages/front/Topic'
import User from '@/pages/front/User'
import TopicList from '@/pages/front/topic/TopicList'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      component: Topic,
      meta: {
        title: '刷题',
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
      path: '/topic/list',
      name: 'topic',
      component: TopicList,
      meta: {
        title: '刷题列表',
        showNav: false
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
