/**
 * 这里觉得引用不方便，直接挂在VUE的原型上面了 ，
 * 目录按照后台
 *  模块
 *    -- cotroller
 *          -- 方法
 *
 * 使用方法 ： this.$api.track.save().then().catch()

 * @author zhengxin
 * @date 2020/11/26
 */
// 导入所有接口
import api from './api'

const install = Vue => {
  if (install.installed) { return }
  install.installed = true

  Object.defineProperties(Vue.prototype, {
    // 注意，此处挂载在 Vue 原型的 $api 对象上
    $api: {
      get () {
        return api
      }
    }
  })
}

export default install
