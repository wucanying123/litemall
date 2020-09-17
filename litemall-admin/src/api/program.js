import request from '@/utils/request'

export function listProgram(query) {
  return request({
    url: '/screen/program/selectProgramPage',
    method: 'get',
    params: query
  })
}

export function createProgram(data) {
  return request({
    url: '/screen/program/insertProgram',
    method: 'post',
    data
  })
}

export function readProgram(data) {
  return request({
    url: '/screen/program/selectProgramById',
    method: 'get',
    data
  })
}

export function updateProgram(data) {
  return request({
    url: '/screen/program/updateProgramById',
    method: 'post',
    data
  })
}

export function deleteProgram(id) {
  return request({
    url: '/screen/program/deleteById',
    method: 'post',
    params: {
      id: id
    }
  })
}
