let prodUrl = {
  baseApi: ''
}

// 开发环境
if (process.env.NODE_ENV === 'development') {
  prodUrl = {
    baseApi: 'http://localhost:8888'
  }
} else {
  prodUrl = {
    // baseApi: 'http://localhost:8888'
  }
}

export default {
  baseUrl: prodUrl
}
