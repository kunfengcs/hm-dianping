import request from '@/utils/request'

// 查询秒杀优惠券，与优惠券是一对一关系列表
export function listSeckill_voucher(query) {
  return request({
    url: '/dianping_admin/seckill_voucher/list',
    method: 'get',
    params: query
  })
}

// 查询秒杀优惠券，与优惠券是一对一关系详细
export function getSeckill_voucher(voucherId) {
  return request({
    url: '/dianping_admin/seckill_voucher/' + voucherId,
    method: 'get'
  })
}

// 新增秒杀优惠券，与优惠券是一对一关系
export function addSeckill_voucher(data) {
  return request({
    url: '/dianping_admin/seckill_voucher',
    method: 'post',
    data: data
  })
}

// 修改秒杀优惠券，与优惠券是一对一关系
export function updateSeckill_voucher(data) {
  return request({
    url: '/dianping_admin/seckill_voucher',
    method: 'put',
    data: data
  })
}

// 删除秒杀优惠券，与优惠券是一对一关系
export function delSeckill_voucher(voucherId) {
  return request({
    url: '/dianping_admin/seckill_voucher/' + voucherId,
    method: 'delete'
  })
}
