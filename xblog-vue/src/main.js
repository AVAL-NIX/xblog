import Vue from 'vue'
import App from './App'
import router from './router'
import api from './api'
import '@/permission.js'
import Vant, {
  Toast
} from 'vant'
import 'vant/lib/index.css'

Vue.use(Vant)
Vue.use(api) // 引入API模块
Vue.config.productionTip = false
Vue.prototype.$message = Toast

let vue = new Vue({
  el: '#app',
  router,
  components: {App},
  template: '<App/>'
})

export default vue
