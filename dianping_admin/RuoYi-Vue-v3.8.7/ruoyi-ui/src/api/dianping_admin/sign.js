import request from '@/utils/request'

// 查询签到列表
export function listSign(query) {
  return request({
    url: '/dianping_admin/sign/list',
    method: 'get',
    params: query
  })
}

// 查询签到详细
export function getSign(id) {
  return request({
    url: '/dianping_admin/sign/' + id,
    method: 'get'
  })
}

// 新增签到
export function addSign(data) {
  return request({
    url: '/dianping_admin/sign',
    method: 'post',
    data: data
  })
}

// 修改签到
export function updateSign(data) {
  return request({
    url: '/dianping_admin/sign',
    method: 'put',
    data: data
  })
}

// 删除签到
export function delSign(id) {
  return request({
    url: '/dianping_admin/sign/' + id,
    method: 'delete'
  })
}
