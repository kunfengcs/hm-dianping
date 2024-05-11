package com.ruoyi.dianping_admin.service;

import java.util.List;
import com.ruoyi.dianping_admin.domain.TbUserInfo;

/**
 * 用户信息Service接口
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
public interface ITbUserInfoService 
{
    /**
     * 查询用户信息
     * 
     * @param userId 用户信息主键
     * @return 用户信息
     */
    public TbUserInfo selectTbUserInfoByUserId(Long userId);

    /**
     * 查询用户信息列表
     * 
     * @param tbUserInfo 用户信息
     * @return 用户信息集合
     */
    public List<TbUserInfo> selectTbUserInfoList(TbUserInfo tbUserInfo);

    /**
     * 新增用户信息
     * 
     * @param tbUserInfo 用户信息
     * @return 结果
     */
    public int insertTbUserInfo(TbUserInfo tbUserInfo);

    /**
     * 修改用户信息
     * 
     * @param tbUserInfo 用户信息
     * @return 结果
     */
    public int updateTbUserInfo(TbUserInfo tbUserInfo);

    /**
     * 批量删除用户信息
     * 
     * @param userIds 需要删除的用户信息主键集合
     * @return 结果
     */
    public int deleteTbUserInfoByUserIds(Long[] userIds);

    /**
     * 删除用户信息信息
     * 
     * @param userId 用户信息主键
     * @return 结果
     */
    public int deleteTbUserInfoByUserId(Long userId);
}
