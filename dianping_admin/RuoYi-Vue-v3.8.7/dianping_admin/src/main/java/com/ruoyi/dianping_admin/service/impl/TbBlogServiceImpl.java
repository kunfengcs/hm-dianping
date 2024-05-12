package com.ruoyi.dianping_admin.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.dianping_admin.mapper.TbBlogMapper;
import com.ruoyi.dianping_admin.domain.TbBlog;
import com.ruoyi.dianping_admin.service.ITbBlogService;

/**
 * 用户博客Service业务层处理
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@Service
public class TbBlogServiceImpl implements ITbBlogService 
{
    @Autowired
    private TbBlogMapper tbBlogMapper;

    /**
     * 查询用户博客
     * 
     * @param id 用户博客主键
     * @return 用户博客
     */
    @Override
    public TbBlog selectTbBlogById(Long id)
    {
        return tbBlogMapper.selectTbBlogById(id);
    }

    /**
     * 查询用户博客列表
     * 
     * @param tbBlog 用户博客
     * @return 用户博客
     */
    @Override
    public List<TbBlog> selectTbBlogList(TbBlog tbBlog)
    {
        return tbBlogMapper.selectTbBlogList(tbBlog);
    }

    /**
     * 新增用户博客
     * 
     * @param tbBlog 用户博客
     * @return 结果
     */
    @Override
    public int insertTbBlog(TbBlog tbBlog)
    {
        tbBlog.setCreateTime(DateUtils.getNowDate());
        return tbBlogMapper.insertTbBlog(tbBlog);
    }

    /**
     * 修改用户博客
     * 
     * @param tbBlog 用户博客
     * @return 结果
     */
    @Override
    public int updateTbBlog(TbBlog tbBlog)
    {
        tbBlog.setUpdateTime(DateUtils.getNowDate());
        return tbBlogMapper.updateTbBlog(tbBlog);
    }

    /**
     * 批量删除用户博客
     * 
     * @param ids 需要删除的用户博客主键
     * @return 结果
     */
    @Override
    public int deleteTbBlogByIds(Long[] ids)
    {
        return tbBlogMapper.deleteTbBlogByIds(ids);
    }

    /**
     * 删除用户博客信息
     * 
     * @param id 用户博客主键
     * @return 结果
     */
    @Override
    public int deleteTbBlogById(Long id)
    {
        return tbBlogMapper.deleteTbBlogById(id);
    }

    @Override
    public long count() {
        return tbBlogMapper.count();
    }
}
