package com.ruoyi.dianping_admin.mapper;

import java.util.List;
import com.ruoyi.dianping_admin.domain.TbBlog;

/**
 * 用户博客Mapper接口
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
public interface TbBlogMapper 
{
    /**
     * 查询用户博客
     * 
     * @param id 用户博客主键
     * @return 用户博客
     */
    public TbBlog selectTbBlogById(Long id);

    /**
     * 查询用户博客列表
     * 
     * @param tbBlog 用户博客
     * @return 用户博客集合
     */
    public List<TbBlog> selectTbBlogList(TbBlog tbBlog);

    /**
     * 新增用户博客
     * 
     * @param tbBlog 用户博客
     * @return 结果
     */
    public int insertTbBlog(TbBlog tbBlog);

    /**
     * 修改用户博客
     * 
     * @param tbBlog 用户博客
     * @return 结果
     */
    public int updateTbBlog(TbBlog tbBlog);

    /**
     * 删除用户博客
     * 
     * @param id 用户博客主键
     * @return 结果
     */
    public int deleteTbBlogById(Long id);

    /**
     * 批量删除用户博客
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbBlogByIds(Long[] ids);

    /**
     * 博客总数量
     * @return 博客总数量
     */
    public long count();
}
