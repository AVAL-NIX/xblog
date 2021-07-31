import request from '@/util/request'

export function login (data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

export function random (data) {
  return request({
    url: '/admin/random',
    method: 'post',
    params: data
  })
}

export function topicInfo (data) {
  return request({
    url: '/admin/topic/info',
    method: 'post',
    params: data
  })
}
