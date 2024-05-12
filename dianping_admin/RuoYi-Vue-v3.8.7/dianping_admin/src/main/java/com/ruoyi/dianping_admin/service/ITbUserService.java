package com.ruoyi.dianping_admin.service;

import java.util.List;
import com.ruoyi.dianping_admin.domain.TbUser;

/**
 * 用户Service接口
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
public interface ITbUserService 
{
    /**
     * 查询用户
     * 
     * @param id 用户主键
     * @return 用户
     */
    public TbUser selectTbUserById(Long id);

    /**
     * 查询用户列表
     * 
     * @param tbUser 用户
     * @return 用户集合
     */
    public List<TbUser> selectTbUserList(TbUser tbUser);

    /**
     * 新增用户
     * 
     * @param tbUser 用户
     * @return 结果
     */
    public int insertTbUser(TbUser tbUser);

    /**
     * 修改用户
     * 
     * @param tbUser 用户
     * @return 结果
     */
    public int updateTbUser(TbUser tbUser);

    /**
     * 批量删除用户
     * 
     * @param ids 需要删除的用户主键集合
     * @return 结果
     */
    public int deleteTbUserByIds(Long[] ids);

    /**
     * 删除用户信息
     * 
     * @param id 用户主键
     * @return 结果
     */
    public int deleteTbUserById(Long id);

    public long count();
}
