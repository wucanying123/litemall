import request from '@/utils/request'

export function listSource(query) {
  return request({
    url: '/source/list',
    method: 'get',
    params: query
  })
}

export function listCatL1() {
  return request({
    url: '/source/l1',
    method: 'get'
  })
}

export function createSource(data) {
  return request({
    url: '/source/create',
    method: 'post',
    data
  })
}

export function readSource(data) {
  return request({
    url: '/source/read',
    method: 'get',
    data
  })
}

export function updateSource(data) {
  return request({
    url: '/source/update',
    method: 'post',
    data
  })
}

export function deleteSource(data) {
  return request({
    url: '/source/delete',
    method: 'post',
    data
  })
}
