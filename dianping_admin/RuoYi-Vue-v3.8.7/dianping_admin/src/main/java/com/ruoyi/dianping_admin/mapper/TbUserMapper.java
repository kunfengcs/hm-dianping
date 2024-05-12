package com.ruoyi.dianping_admin.mapper;

import java.util.List;
import com.ruoyi.dianping_admin.domain.TbUser;

/**
 * 用户Mapper接口
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
public interface TbUserMapper 
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
     * 删除用户
     * 
     * @param id 用户主键
     * @return 结果
     */
    public int deleteTbUserById(Long id);

    /**
     * 批量删除用户
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbUserByIds(Long[] ids);

    public long count();
}
