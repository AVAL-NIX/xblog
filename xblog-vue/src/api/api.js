/*
 * 接口统一集成模块
 */
var api = {}

const requireComponent = require.context(
  // 其组件目录的相对路径
  './',
  // 是否查询其子目录
  true,
  // 匹配基础组件文件名的正则表达式,
  /[A-Za-z]\w+\.(js|vue)$/
)

requireComponent.keys().forEach(fileName => {
  // 获取组件配置
  const componentConfig = requireComponent(fileName)

  fileName = fileName.substring(fileName.lastIndexOf('/') + 1, fileName.lastIndexOf('.'))
  api[fileName] = componentConfig.default || componentConfig
})

// 默认全部导出
export default api
