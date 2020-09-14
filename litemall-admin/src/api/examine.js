import request from '@/utils/request'

export function listExamine(query) {
  return request({
    url: '/screen/examine/selectExaminePage',
    method: 'get',
    params: query
  })
}

export function createExamine(data) {
  return request({
    url: '/screen/examine/insertExamine',
    method: 'post',
    data
  })
}

export function readExamine(data) {
  return request({
    url: '/screen/examine/selectExamineById',
    method: 'get',
    data
  })
}

export function updateExamine(data) {
  return request({
    url: '/screen/examine/updateExamineById',
    method: 'post',
    data
  })
}

export function deleteExamine(id) {
  return request({
    url: '/screen/examine/deleteById',
    method: 'post',
    params: {
      id: id
    }
  })
}
