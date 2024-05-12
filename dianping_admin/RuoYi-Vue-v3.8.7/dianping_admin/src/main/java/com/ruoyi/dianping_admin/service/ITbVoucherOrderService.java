package com.ruoyi.dianping_admin.service;

import java.util.List;
import com.ruoyi.dianping_admin.domain.TbVoucherOrder;

/**
 * 优惠券的订单Service接口
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
public interface ITbVoucherOrderService 
{
    /**
     * 查询优惠券的订单
     * 
     * @param id 优惠券的订单主键
     * @return 优惠券的订单
     */
    public TbVoucherOrder selectTbVoucherOrderById(Long id);

    /**
     * 查询优惠券的订单列表
     * 
     * @param tbVoucherOrder 优惠券的订单
     * @return 优惠券的订单集合
     */
    public List<TbVoucherOrder> selectTbVoucherOrderList(TbVoucherOrder tbVoucherOrder);

    /**
     * 新增优惠券的订单
     * 
     * @param tbVoucherOrder 优惠券的订单
     * @return 结果
     */
    public int insertTbVoucherOrder(TbVoucherOrder tbVoucherOrder);

    /**
     * 修改优惠券的订单
     * 
     * @param tbVoucherOrder 优惠券的订单
     * @return 结果
     */
    public int updateTbVoucherOrder(TbVoucherOrder tbVoucherOrder);

    /**
     * 批量删除优惠券的订单
     * 
     * @param ids 需要删除的优惠券的订单主键集合
     * @return 结果
     */
    public int deleteTbVoucherOrderByIds(Long[] ids);

    /**
     * 删除优惠券的订单信息
     * 
     * @param id 优惠券的订单主键
     * @return 结果
     */
    public int deleteTbVoucherOrderById(Long id);

    public long count();
}
