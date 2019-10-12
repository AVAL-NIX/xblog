import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/components/front/Index'
import ArticleIndex from '@/components/front/article/ArticleIndex'

Vue.use(Router)

export default new Router({
  routes: [{
      name: 'Index',
      path: '/',
      component: Index
    },
    {
        name:'ArticleIndex',
      path: '/article/:id',
      component: ArticleIndex
    }
  ]
})
