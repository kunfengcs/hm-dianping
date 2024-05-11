import request from '@/utils/request'

// 查询博客评论列表
export function listComments(query) {
  return request({
    url: '/dianping_admin/comments/list',
    method: 'get',
    params: query
  })
}

// 查询博客评论详细
export function getComments(id) {
  return request({
    url: '/dianping_admin/comments/' + id,
    method: 'get'
  })
}

// 新增博客评论

export function addComments(data) {
  return request({
    url: '/dianping_admin/comments',
    method: 'post',
    data: data
  })
}

// 修改博客评论

export function updateComments(data) {
  return request({
    url: '/dianping_admin/comments',
    method: 'put',
    data: data
  })
}

// 删除博客评论

export function delComments(id) {
  return request({
    url: '/dianping_admin/comments/' + id,
    method: 'delete'
  })
}
