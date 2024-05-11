import request from '@/utils/request'

// 查询用户信息列表
export function listInfo(query) {
  return request({
    url: '/dianping_admin/info/list',
    method: 'get',
    params: query
  })
}

// 查询用户信息详细
export function getInfo(userId) {
  return request({
    url: '/dianping_admin/info/' + userId,
    method: 'get'
  })
}

// 新增用户信息
export function addInfo(data) {
  return request({
    url: '/dianping_admin/info',
    method: 'post',
    data: data
  })
}

// 修改用户信息
export function updateInfo(data) {
  return request({
    url: '/dianping_admin/info',
    method: 'put',
    data: data
  })
}

// 删除用户信息
export function delInfo(userId) {
  return request({
    url: '/dianping_admin/info/' + userId,
    method: 'delete'
  })
}
