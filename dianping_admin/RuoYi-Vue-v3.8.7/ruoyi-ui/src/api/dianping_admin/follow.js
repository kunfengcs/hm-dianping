import request from '@/utils/request'

// 查询用户关注列表
export function listFollow(query) {
  return request({
    url: '/dianping_admin/follow/list',
    method: 'get',
    params: query
  })
}

// 查询用户关注详细
export function getFollow(id) {
  return request({
    url: '/dianping_admin/follow/' + id,
    method: 'get'
  })
}

// 新增用户关注
export function addFollow(data) {
  return request({
    url: '/dianping_admin/follow',
    method: 'post',
    data: data
  })
}

// 修改用户关注
export function updateFollow(data) {
  return request({
    url: '/dianping_admin/follow',
    method: 'put',
    data: data
  })
}

// 删除用户关注
export function delFollow(id) {
  return request({
    url: '/dianping_admin/follow/' + id,
    method: 'delete'
  })
}
