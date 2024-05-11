package com.ruoyi.dianping_admin.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.dianping_admin.mapper.TbUserInfoMapper;
import com.ruoyi.dianping_admin.domain.TbUserInfo;
import com.ruoyi.dianping_admin.service.ITbUserInfoService;

/**
 * 用户信息Service业务层处理
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@Service
public class TbUserInfoServiceImpl implements ITbUserInfoService 
{
    @Autowired
    private TbUserInfoMapper tbUserInfoMapper;

    /**
     * 查询用户信息
     * 
     * @param userId 用户信息主键
     * @return 用户信息
     */
    @Override
    public TbUserInfo selectTbUserInfoByUserId(Long userId)
    {
        return tbUserInfoMapper.selectTbUserInfoByUserId(userId);
    }

    /**
     * 查询用户信息列表
     * 
     * @param tbUserInfo 用户信息
     * @return 用户信息
     */
    @Override
    public List<TbUserInfo> selectTbUserInfoList(TbUserInfo tbUserInfo)
    {
        return tbUserInfoMapper.selectTbUserInfoList(tbUserInfo);
    }

    /**
     * 新增用户信息
     * 
     * @param tbUserInfo 用户信息
     * @return 结果
     */
    @Override
    public int insertTbUserInfo(TbUserInfo tbUserInfo)
    {
        tbUserInfo.setCreateTime(DateUtils.getNowDate());
        return tbUserInfoMapper.insertTbUserInfo(tbUserInfo);
    }

    /**
     * 修改用户信息
     * 
     * @param tbUserInfo 用户信息
     * @return 结果
     */
    @Override
    public int updateTbUserInfo(TbUserInfo tbUserInfo)
    {
        tbUserInfo.setUpdateTime(DateUtils.getNowDate());
        return tbUserInfoMapper.updateTbUserInfo(tbUserInfo);
    }

    /**
     * 批量删除用户信息
     * 
     * @param userIds 需要删除的用户信息主键
     * @return 结果
     */
    @Override
    public int deleteTbUserInfoByUserIds(Long[] userIds)
    {
        return tbUserInfoMapper.deleteTbUserInfoByUserIds(userIds);
    }

    /**
     * 删除用户信息信息
     * 
     * @param userId 用户信息主键
     * @return 结果
     */
    @Override
    public int deleteTbUserInfoByUserId(Long userId)
    {
        return tbUserInfoMapper.deleteTbUserInfoByUserId(userId);
    }
}
