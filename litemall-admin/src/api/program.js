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

export function readProgram(query) {
  return request({
    url: '/screen/program/readProgram',
    method: 'get',
    params: query
  })
}

export function updateSimpleProgramById(data) {
  return request({
    url: '/screen/program/updateSimpleProgramById',
    method: 'post',
    data
  })
}

export function updateComplexProgramById(data) {
  return request({
    url: '/screen/program/updateComplexProgramById',
    method: 'post',
    data
  })
} export function updateSeniorProgramById(data) {
  return request({
    url: '/screen/program/updateSeniorProgramById',
    method: 'post',
    data
  })
}
