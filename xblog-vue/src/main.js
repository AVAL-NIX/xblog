import Vue from 'vue'
import App from './App'
import router from './router'
import api from './api'
import '@/permission.js'
import Vant from 'vant'
import 'vant/lib/index.css'

Vue.use(Vant)
Vue.use(api) // 引入API模块
Vue.config.productionTip = false

let vue = new Vue({
  el: '#app',
  router,
  components: {App},
  template: '<App/>'
})

export default vue
