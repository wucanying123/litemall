import request from '@/utils/request'

export function listScreenDevice(query) {
  return request({
    url: '/screen/screenDevice/selectScreenDevicePage',
    method: 'get',
    params: query
  })
}

export function createScreenDevice(data) {
  return request({
    url: '/screen/screenDevice/insertScreenDevice',
    method: 'post',
    data
  })
}

export function readScreenDevice(data) {
  return request({
    url: '/screen/screenDevice/selectScreenDeviceById',
    method: 'get',
    data
  })
}

export function updateScreenDevice(data) {
  return request({
    url: '/screen/screenDevice/updateScreenDeviceById',
    method: 'post',
    data
  })
}

export function deleteScreenDevice(id) {
  return request({
    url: '/screen/screenDevice/deleteById',
    method: 'post',
    params: {
      id: id
    }
  })
}

export function selectOnlineDevice() {
  return request({
    url: '/screen/screenDevice/selectOnlineDevice',
    method: 'get'
  })
}
