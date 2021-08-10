let prodUrl = {
  baseApi: ''
}

// 开发环境
if (process.env.NODE_ENV === 'development') {
  prodUrl = {
    baseApi: 'https://avalon-zheng.xin/api2'
  }
} else {
  prodUrl = {
    baseApi: 'https://avalon-zheng.xin/api2'
  }
}

export default {
  baseUrl: prodUrl
}
