package com.hmdp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmdp.entity.BlogComments;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王坤峰
 * @since 2021-12-22
 */
public interface IBlogCommentsService extends IService<BlogComments> {


    List<BlogComments> findCommentDetail(Long blogId);
}
