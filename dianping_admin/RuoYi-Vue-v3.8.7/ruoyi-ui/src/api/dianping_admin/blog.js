import request from '@/utils/request'

// 查询用户博客列表
export function listBlog(query) {
  return request({
    url: '/dianping_admin/blog/list',
    method: 'get',
    params: query
  })
}

// 查询用户博客详细
export function getBlog(id) {
  return request({
    url: '/dianping_admin/blog/' + id,
    method: 'get'
  })
}

// 新增用户博客

export function addBlog(data) {
  return request({
    url: '/dianping_admin/blog',
    method: 'post',
    data: data
  })
}

// 修改用户博客

export function updateBlog(data) {
  return request({
    url: '/dianping_admin/blog',
    method: 'put',
    data: data
  })
}

// 删除用户博客

export function delBlog(id) {
  return request({
    url: '/dianping_admin/blog/' + id,
    method: 'delete'
  })
}
