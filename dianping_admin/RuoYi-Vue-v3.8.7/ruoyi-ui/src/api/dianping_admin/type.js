import request from '@/utils/request'

// 查询商户类型列表
export function listType(query) {
  return request({
    url: '/dianping_admin/type/list',
    method: 'get',
    params: query
  })
}

// 查询商户类型详细
export function getType(id) {
  return request({
    url: '/dianping_admin/type/' + id,
    method: 'get'
  })
}

// 新增商户类型
export function addType(data) {
  return request({
    url: '/dianping_admin/type',
    method: 'post',
    data: data
  })
}

// 修改商户类型
export function updateType(data) {
  return request({
    url: '/dianping_admin/type',
    method: 'put',
    data: data
  })
}

// 删除商户类型
export function delType(id) {
  return request({
    url: '/dianping_admin/type/' + id,
    method: 'delete'
  })
}
