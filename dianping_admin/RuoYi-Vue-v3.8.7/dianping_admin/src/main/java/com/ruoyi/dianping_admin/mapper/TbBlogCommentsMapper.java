package com.ruoyi.dianping_admin.mapper;

import java.util.List;
import com.ruoyi.dianping_admin.domain.TbBlogComments;

/**
 * 博客评论Mapper接口
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
public interface TbBlogCommentsMapper 
{
    /**
     * 查询博客评论     * 
     * @param id 博客评论主键
     * @return 博客评论     */
    public TbBlogComments selectTbBlogCommentsById(Long id);

    /**
     * 查询博客评论列表
     * 
     * @param tbBlogComments 博客评论     * @return 博客评论集合
     */
    public List<TbBlogComments> selectTbBlogCommentsList(TbBlogComments tbBlogComments);

    /**
     * 新增博客评论     * 
     * @param tbBlogComments 博客评论     * @return 结果
     */
    public int insertTbBlogComments(TbBlogComments tbBlogComments);

    /**
     * 修改博客评论     * 
     * @param tbBlogComments 博客评论     * @return 结果
     */
    public int updateTbBlogComments(TbBlogComments tbBlogComments);

    /**
     * 删除博客评论     * 
     * @param id 博客评论主键
     * @return 结果
     */
    public int deleteTbBlogCommentsById(Long id);

    /**
     * 批量删除博客评论     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbBlogCommentsByIds(Long[] ids);
}
