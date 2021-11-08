import router from '@/router'

const TOKEN = 'login-token'
const USERINFO = 'user-info'
let login = null

export function getToken () {
  return localStorage.getItem(TOKEN)
}

export function setToken (token) {
  return localStorage.setItem(TOKEN, token)
}

export function removeToken () {
  return localStorage.removeItem(TOKEN)
}

export function getUserinfo () {
  const userinfo = localStorage.getItem(USERINFO)
  if (userinfo) {
    return JSON.parse(userinfo)
  }
  console.error('auth no userinfo', userinfo)
  return {}
}

export function setUserinfo (userinfo) {
  localStorage.setItem(USERINFO, JSON.stringify(userinfo || {}))
}

export function doLogin () {
  if (login) {
    login()
  }
}

export function setLogin (l) {
  login = l
}

export function logout () {
  removeToken()
  console.log('退出成功！')
  router.push('/')
}

export default TOKEN
