import request from '@/utils/request'

export function listTask(query) {
  return request({
    url: '/screen/task/selectTaskPage',
    method: 'get',
    params: query
  })
}

export function createTask(data) {
  return request({
    url: '/screen/task/insertTask',
    method: 'post',
    data
  })
}

export function insertQuickTask(programName, programId) {
  return request({
    url: '/screen/task/insertQuickTask',
    method: 'post',
    params: {
      programName: programName,
      programId: programId
    }
  })
}

export function readTask(data) {
  return request({
    url: '/screen/task/selectTaskById',
    method: 'get',
    data
  })
}

export function updateTask(data) {
  return request({
    url: '/screen/task/updateTaskById',
    method: 'post',
    data
  })
}

export function updateTaskTotalById(data) {
  return request({
    url: '/screen/task/updateTaskTotalById',
    method: 'post',
    data
  })
}

export function deleteTask(id) {
  return request({
    url: '/screen/task/deleteById',
    method: 'post',
    params: {
      id: id
    }
  })
}

export function playTask(data) {
  return request({
    url: '/screen/task/playTask',
    method: 'post',
    data
  })
}
