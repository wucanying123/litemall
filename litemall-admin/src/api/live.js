import request from '@/utils/request'

export function listLive(query) {
  return request({
    url: '/screen/live/selectLivePage',
    method: 'get',
    params: query
  })
}

export function createLive(data) {
  return request({
    url: '/screen/live/insertLive',
    method: 'post',
    data
  })
}

export function readLive(data) {
  return request({
    url: '/screen/live/selectLiveById',
    method: 'get',
    data
  })
}

export function updateLive(data) {
  return request({
    url: '/screen/live/updateLiveById',
    method: 'post',
    data
  })
}

export function deleteLive(id) {
  return request({
    url: '/screen/live/deleteById',
    method: 'post',
    params: {
      id: id
    }
  })
}
