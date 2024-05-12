import request from '@/utils/request'

// 查询商户信息列表
export function listShop(query) {
  return request({
    url: '/dianping_admin/shop/list',
    method: 'get',
    params: query
  })
}

// 查询商户信息详细
export function getShop(id) {
  return request({
    url: '/dianping_admin/shop/' + id,
    method: 'get'
  })
}

// 新增商户信息
export function addShop(data) {
  return request({
    url: '/dianping_admin/shop',
    method: 'post',
    data: data
  })
}

// 修改商户信息
export function updateShop(data) {
  return request({
    url: '/dianping_admin/shop',
    method: 'put',
    data: data
  })
}

// 删除商户信息
export function delShop(id) {
  return request({
    url: '/dianping_admin/shop/' + id,
    method: 'delete'
  })
}

// 商户总数
export function getShopCount() {
  return request({
    url: '/dianping_admin/shop/count',
    method: 'get'
  })
}
