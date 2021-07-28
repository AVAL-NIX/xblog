import router from './router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {getToken} from '@/util/auth'
import {setLogin} from './util/auth'

NProgress.configure({showSpinner: false})

const whiteList = ['/login']

router.beforeEach(async (to, from, next) => {
  NProgress.start()

  document.title = to.meta.title

  const token = getToken()

  if (token) {
    if (to.path === '/login') {
      next({path: '/'})
      NProgress.done()
    } else {
      setLogin(() => {
        next(`/login?redirect=${to.path}`)
        NProgress.done()
      })
      next()
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
