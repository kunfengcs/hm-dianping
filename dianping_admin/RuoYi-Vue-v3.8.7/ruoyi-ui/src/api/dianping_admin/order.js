import request from '@/utils/request'

// 查询优惠券的订单列表
export function listOrder(query) {
  return request({
    url: '/dianping_admin/order/list',
    method: 'get',
    params: query
  })
}

// 查询优惠券的订单详细
export function getOrder(id) {
  return request({
    url: '/dianping_admin/order/' + id,
    method: 'get'
  })
}

// 新增优惠券的订单
export function addOrder(data) {
  return request({
    url: '/dianping_admin/order',
    method: 'post',
    data: data
  })
}

// 修改优惠券的订单
export function updateOrder(data) {
  return request({
    url: '/dianping_admin/order',
    method: 'put',
    data: data
  })
}

// 删除优惠券的订单
export function delOrder(id) {
  return request({
    url: '/dianping_admin/order/' + id,
    method: 'delete'
  })
}
