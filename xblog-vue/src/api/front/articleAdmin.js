import request from '@/util/request'

export function save (data) {
  return request({
    url: '/article-admin/save',
    method: 'post',
    data
  })
}
