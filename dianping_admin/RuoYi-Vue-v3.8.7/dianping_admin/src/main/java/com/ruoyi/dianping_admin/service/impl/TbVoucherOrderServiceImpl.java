package com.ruoyi.dianping_admin.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.dianping_admin.mapper.TbVoucherOrderMapper;
import com.ruoyi.dianping_admin.domain.TbVoucherOrder;
import com.ruoyi.dianping_admin.service.ITbVoucherOrderService;

/**
 * 优惠券的订单Service业务层处理
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@Service
public class TbVoucherOrderServiceImpl implements ITbVoucherOrderService 
{
    @Autowired
    private TbVoucherOrderMapper tbVoucherOrderMapper;

    /**
     * 查询优惠券的订单
     * 
     * @param id 优惠券的订单主键
     * @return 优惠券的订单
     */
    @Override
    public TbVoucherOrder selectTbVoucherOrderById(Long id)
    {
        return tbVoucherOrderMapper.selectTbVoucherOrderById(id);
    }

    /**
     * 查询优惠券的订单列表
     * 
     * @param tbVoucherOrder 优惠券的订单
     * @return 优惠券的订单
     */
    @Override
    public List<TbVoucherOrder> selectTbVoucherOrderList(TbVoucherOrder tbVoucherOrder)
    {
        return tbVoucherOrderMapper.selectTbVoucherOrderList(tbVoucherOrder);
    }

    /**
     * 新增优惠券的订单
     * 
     * @param tbVoucherOrder 优惠券的订单
     * @return 结果
     */
    @Override
    public int insertTbVoucherOrder(TbVoucherOrder tbVoucherOrder)
    {
        tbVoucherOrder.setCreateTime(DateUtils.getNowDate());
        return tbVoucherOrderMapper.insertTbVoucherOrder(tbVoucherOrder);
    }

    /**
     * 修改优惠券的订单
     * 
     * @param tbVoucherOrder 优惠券的订单
     * @return 结果
     */
    @Override
    public int updateTbVoucherOrder(TbVoucherOrder tbVoucherOrder)
    {
        tbVoucherOrder.setUpdateTime(DateUtils.getNowDate());
        return tbVoucherOrderMapper.updateTbVoucherOrder(tbVoucherOrder);
    }

    /**
     * 批量删除优惠券的订单
     * 
     * @param ids 需要删除的优惠券的订单主键
     * @return 结果
     */
    @Override
    public int deleteTbVoucherOrderByIds(Long[] ids)
    {
        return tbVoucherOrderMapper.deleteTbVoucherOrderByIds(ids);
    }

    /**
     * 删除优惠券的订单信息
     * 
     * @param id 优惠券的订单主键
     * @return 结果
     */
    @Override
    public int deleteTbVoucherOrderById(Long id)
    {
        return tbVoucherOrderMapper.deleteTbVoucherOrderById(id);
    }

    @Override
    public long count() {

        return tbVoucherOrderMapper.count();
    }
}
