import request from '@/utils/request'

export function listPlaySource(query) {
  return request({
    url: '/screen/playSource/selectPlaySourcePage',
    method: 'get',
    params: query
  })
}

export function createPlaySource(data) {
  return request({
    url: '/screen/playSource/insertPlaySource',
    method: 'post',
    data
  })
}

export function readPlaySource(data) {
  return request({
    url: '/screen/playSource/selectPlaySourceById',
    method: 'get',
    data
  })
}

export function updatePlaySource(data) {
  return request({
    url: '/screen/playSource/updatePlaySourceById',
    method: 'post',
    data
  })
}

export function deletePlaySource(id) {
  return request({
    url: '/screen/playSource/deleteById',
    method: 'post',
    params: {
      id: id
    }
  })
}
