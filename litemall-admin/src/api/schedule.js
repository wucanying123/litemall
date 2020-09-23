import request from '@/utils/request'

export function listSchedule(query) {
  return request({
    url: '/screen/schedule/selectListByItemId',
    method: 'get',
    params: query
  })
}

export function createSchedule(data, itemId) {
  return request({
    url: '/screen/schedule/insertSchedule',
    method: 'post',
    data,
    params: {
      itemId: itemId
    }
  })
}

export function readSchedule(data) {
  return request({
    url: '/screen/schedule/selectScheduleById',
    method: 'get',
    data
  })
}

export function updateSchedule(data) {
  return request({
    url: '/screen/schedule/updateScheduleById',
    method: 'post',
    data
  })
}

export function deleteSchedule(id, itemId) {
  return request({
    url: '/screen/schedule/deleteById',
    method: 'post',
    params: {
      id: id,
      itemId: itemId
    }
  })
}

export function playSchedule(id, cardId) {
  return request({
    url: '/screen/schedule/playSchedule',
    method: 'post',
    params: {
      id: id,
      cardId: cardId
    }
  })
}
