package com.ruoyi.dianping_admin.service;

import java.util.List;
import com.ruoyi.dianping_admin.domain.TbVoucher;

/**
 * 优惠券Service接口
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
public interface ITbVoucherService 
{
    /**
     * 查询优惠券
     * 
     * @param id 优惠券主键
     * @return 优惠券
     */
    public TbVoucher selectTbVoucherById(Long id);

    /**
     * 查询优惠券列表
     * 
     * @param tbVoucher 优惠券
     * @return 优惠券集合
     */
    public List<TbVoucher> selectTbVoucherList(TbVoucher tbVoucher);

    /**
     * 新增优惠券
     * 
     * @param tbVoucher 优惠券
     * @return 结果
     */
    public int insertTbVoucher(TbVoucher tbVoucher);

    /**
     * 修改优惠券
     * 
     * @param tbVoucher 优惠券
     * @return 结果
     */
    public int updateTbVoucher(TbVoucher tbVoucher);

    /**
     * 批量删除优惠券
     * 
     * @param ids 需要删除的优惠券主键集合
     * @return 结果
     */
    public int deleteTbVoucherByIds(Long[] ids);

    /**
     * 删除优惠券信息
     * 
     * @param id 优惠券主键
     * @return 结果
     */
    public int deleteTbVoucherById(Long id);
}
