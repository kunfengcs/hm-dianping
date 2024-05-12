package com.ruoyi.dianping_admin.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.dianping_admin.mapper.TbUserMapper;
import com.ruoyi.dianping_admin.domain.TbUser;
import com.ruoyi.dianping_admin.service.ITbUserService;

/**
 * 用户Service业务层处理
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@Service
public class TbUserServiceImpl implements ITbUserService 
{
    @Autowired
    private TbUserMapper tbUserMapper;

    /**
     * 查询用户
     * 
     * @param id 用户主键
     * @return 用户
     */
    @Override
    public TbUser selectTbUserById(Long id)
    {
        return tbUserMapper.selectTbUserById(id);
    }

    /**
     * 查询用户列表
     * 
     * @param tbUser 用户
     * @return 用户
     */
    @Override
    public List<TbUser> selectTbUserList(TbUser tbUser)
    {
        return tbUserMapper.selectTbUserList(tbUser);
    }

    /**
     * 新增用户
     * 
     * @param tbUser 用户
     * @return 结果
     */
    @Override
    public int insertTbUser(TbUser tbUser)
    {
        tbUser.setCreateTime(DateUtils.getNowDate());
        return tbUserMapper.insertTbUser(tbUser);
    }

    /**
     * 修改用户
     * 
     * @param tbUser 用户
     * @return 结果
     */
    @Override
    public int updateTbUser(TbUser tbUser)
    {
        tbUser.setUpdateTime(DateUtils.getNowDate());
        return tbUserMapper.updateTbUser(tbUser);
    }

    /**
     * 批量删除用户
     * 
     * @param ids 需要删除的用户主键
     * @return 结果
     */
    @Override
    public int deleteTbUserByIds(Long[] ids)
    {
        return tbUserMapper.deleteTbUserByIds(ids);
    }

    /**
     * 删除用户信息
     * 
     * @param id 用户主键
     * @return 结果
     */
    @Override
    public int deleteTbUserById(Long id)
    {
        return tbUserMapper.deleteTbUserById(id);
    }

    @Override
    public long count() {
        return tbUserMapper.count();
    }
}
