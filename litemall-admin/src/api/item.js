import request from '@/utils/request'

export function listItem(query) {
  return request({
    url: '/screen/item/selectListByTaskId',
    method: 'get',
    params: query
  })
}

export function createItem(data) {
  return request({
    url: '/screen/item/insertItem',
    method: 'post',
    data
  })
}

export function readItem(data) {
  return request({
    url: '/screen/item/selectItemById',
    method: 'get',
    data
  })
}

export function updateItem(data) {
  return request({
    url: '/screen/item/updateItemById',
    method: 'post',
    data
  })
}

export function deleteItem(taskId, itemId) {
  return request({
    url: '/screen/item/deleteById',
    method: 'post',
    params: {
      taskId: taskId,
      itemId: itemId
    }
  })
}
