package com.ruoyi.dianping_admin.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.dianping_admin.mapper.TbBlogCommentsMapper;
import com.ruoyi.dianping_admin.domain.TbBlogComments;
import com.ruoyi.dianping_admin.service.ITbBlogCommentsService;

/**
 * 博客评论Service业务层处理
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@Service
public class TbBlogCommentsServiceImpl implements ITbBlogCommentsService 
{
    @Autowired
    private TbBlogCommentsMapper tbBlogCommentsMapper;

    /**
     * 查询博客评论     * 
     * @param id 博客评论主键
     * @return 博客评论     */
    @Override
    public TbBlogComments selectTbBlogCommentsById(Long id)
    {
        return tbBlogCommentsMapper.selectTbBlogCommentsById(id);
    }

    /**
     * 查询博客评论列表
     * 
     * @param tbBlogComments 博客评论     * @return 博客评论
     */
    @Override
    public List<TbBlogComments> selectTbBlogCommentsList(TbBlogComments tbBlogComments)
    {
        return tbBlogCommentsMapper.selectTbBlogCommentsList(tbBlogComments);
    }

    /**
     * 新增博客评论
     * 
     * @param tbBlogComments 博客评论
     * @return 结果
     */
    @Override
    public int insertTbBlogComments(TbBlogComments tbBlogComments)
    {
        tbBlogComments.setCreateTime(DateUtils.getNowDate());
        return tbBlogCommentsMapper.insertTbBlogComments(tbBlogComments);
    }

    /**
     * 修改博客评论
     * 
     * @param tbBlogComments 博客评论
     * @return 结果
     */
    @Override
    public int updateTbBlogComments(TbBlogComments tbBlogComments)
    {
        tbBlogComments.setUpdateTime(DateUtils.getNowDate());
        return tbBlogCommentsMapper.updateTbBlogComments(tbBlogComments);
    }

    /**
     * 批量删除博客评论
     * 
     * @param ids 需要删除的博客评论主键
     * @return 结果
     */
    @Override
    public int deleteTbBlogCommentsByIds(Long[] ids)
    {
        return tbBlogCommentsMapper.deleteTbBlogCommentsByIds(ids);
    }

    /**
     * 删除博客评论信息
     * 
     * @param id 博客评论主键
     * @return 结果
     */
    @Override
    public int deleteTbBlogCommentsById(Long id)
    {
        return tbBlogCommentsMapper.deleteTbBlogCommentsById(id);
    }
}
