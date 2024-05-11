package com.ruoyi.dianping_admin.mapper;

import java.util.List;
import com.ruoyi.dianping_admin.domain.TbFollow;

/**
 * 用户关注Mapper接口
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
public interface TbFollowMapper 
{
    /**
     * 查询用户关注
     * 
     * @param id 用户关注主键
     * @return 用户关注
     */
    public TbFollow selectTbFollowById(Long id);

    /**
     * 查询用户关注列表
     * 
     * @param tbFollow 用户关注
     * @return 用户关注集合
     */
    public List<TbFollow> selectTbFollowList(TbFollow tbFollow);

    /**
     * 新增用户关注
     * 
     * @param tbFollow 用户关注
     * @return 结果
     */
    public int insertTbFollow(TbFollow tbFollow);

    /**
     * 修改用户关注
     * 
     * @param tbFollow 用户关注
     * @return 结果
     */
    public int updateTbFollow(TbFollow tbFollow);

    /**
     * 删除用户关注
     * 
     * @param id 用户关注主键
     * @return 结果
     */
    public int deleteTbFollowById(Long id);

    /**
     * 批量删除用户关注
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbFollowByIds(Long[] ids);
}
