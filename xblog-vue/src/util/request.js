import axios from 'axios'
import TOKEN, {doLogin, getToken, removeToken, setToken} from './auth'
import config from '@/config'
import {Toast} from 'vant'

const service = axios.create({
  baseURL: config.baseUrl.baseApi, // url = base url + request url
  withCredentials: true, // send cookies when cross-domain requests
  timeout: 60 * 1000 // request timeout
})

service.interceptors.request.use(
  (config) => {
    config.headers[TOKEN] = getToken()
    return config
  },
  (error) => {
    console.error(error) // for debug
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  (response) => {
    const result = response.data
    if (response.headers[TOKEN]) {
      setToken(response.headers[TOKEN])
    }

    switch (response.status) {
      case 404:
        console.error('该接口不存在 -> ' + response.request.url)
        console.error(response.status)
        Toast('您访问的资源不存在！')
        break
      case 403:
        console.error('token失效，进入登录页 -> ' + response.request.url)
        removeToken()
        doLogin()
        return // 这里直接返回不执行错误回调了
      case 500:
        Toast('服务器请求失败！')
        break
      case 502:
        Toast('服务器暂时无法访问！')
        break
      case 503:
        Toast('服务器正在维护！')
        break
      case 504:
        Toast('请求超时请重试！')
        break
      default: {

      }
    }
    if (result && result.code && result.code !== 200) {
      if (result.msg) {
        Toast(result.msg)
      }
      return Promise.reject(result)
    } else {
      return Promise.resolve(result)
    }
  }
)

export default service
