package com.ruoyi.dianping_admin.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.dianping_admin.mapper.TbFollowMapper;
import com.ruoyi.dianping_admin.domain.TbFollow;
import com.ruoyi.dianping_admin.service.ITbFollowService;

/**
 * 用户关注Service业务层处理
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@Service
public class TbFollowServiceImpl implements ITbFollowService 
{
    @Autowired
    private TbFollowMapper tbFollowMapper;

    /**
     * 查询用户关注
     * 
     * @param id 用户关注主键
     * @return 用户关注
     */
    @Override
    public TbFollow selectTbFollowById(Long id)
    {
        return tbFollowMapper.selectTbFollowById(id);
    }

    /**
     * 查询用户关注列表
     * 
     * @param tbFollow 用户关注
     * @return 用户关注
     */
    @Override
    public List<TbFollow> selectTbFollowList(TbFollow tbFollow)
    {
        return tbFollowMapper.selectTbFollowList(tbFollow);
    }

    /**
     * 新增用户关注
     * 
     * @param tbFollow 用户关注
     * @return 结果
     */
    @Override
    public int insertTbFollow(TbFollow tbFollow)
    {
        tbFollow.setCreateTime(DateUtils.getNowDate());
        return tbFollowMapper.insertTbFollow(tbFollow);
    }

    /**
     * 修改用户关注
     * 
     * @param tbFollow 用户关注
     * @return 结果
     */
    @Override
    public int updateTbFollow(TbFollow tbFollow)
    {
        return tbFollowMapper.updateTbFollow(tbFollow);
    }

    /**
     * 批量删除用户关注
     * 
     * @param ids 需要删除的用户关注主键
     * @return 结果
     */
    @Override
    public int deleteTbFollowByIds(Long[] ids)
    {
        return tbFollowMapper.deleteTbFollowByIds(ids);
    }

    /**
     * 删除用户关注信息
     * 
     * @param id 用户关注主键
     * @return 结果
     */
    @Override
    public int deleteTbFollowById(Long id)
    {
        return tbFollowMapper.deleteTbFollowById(id);
    }
}
