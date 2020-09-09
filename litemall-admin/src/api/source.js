import request from '@/utils/request'

export function listSource(query) {
  return request({
    url: '/screen/source/selectSourcePage',
    method: 'get',
    params: query
  })
}

export function createSource(data) {
  return request({
    url: '/screen/source/insertSource',
    method: 'post',
    data
  })
}

export function readSource(data) {
  return request({
    url: '/screen/source/selectSourceById',
    method: 'get',
    data
  })
}

export function updateSource(data) {
  return request({
    url: '/screen/source/updateSourceById',
    method: 'post',
    data
  })
}

export function deleteSource(id) {
  return request({
    url: '/screen/source/deleteById',
    method: 'post',
    params: {
      id: id
    }
  })
}
