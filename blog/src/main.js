import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'element-ui/lib/theme-chalk/display.css';

import store from './store'
import {
    get,
    post,
    upload,
    del,
    down,
    put
  } from '@/utils/api'

Vue.use(ElementUI)

Vue.config.productionTip = false

//API
Vue.prototype.$get = get
Vue.prototype.$post = post
Vue.prototype.$delete = del
Vue.prototype.$put = put
Vue.prototype.$upload = upload
Vue.prototype.$down = down
Vue.prototype.$resultCheck = (res , isTrue = false , isError = false) =>{
    return  new Promise((resolve, reject) =>{
        if(res.code < 1){
            if(isError){
                //this.message.error(res.msg)
            }
            resolve(res)
        }else{
            if(isTrue){
              //  this.Message.error(res.msg)
            }
            resolve(res)
        }
    })
}


new Vue({
  el: '#app',
  router,
  store,
  components: {
    App
  },
  template: '<App/>'
})
