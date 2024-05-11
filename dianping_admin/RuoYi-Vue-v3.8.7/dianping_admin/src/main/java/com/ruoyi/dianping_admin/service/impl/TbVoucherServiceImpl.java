package com.ruoyi.dianping_admin.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.dianping_admin.mapper.TbVoucherMapper;
import com.ruoyi.dianping_admin.domain.TbVoucher;
import com.ruoyi.dianping_admin.service.ITbVoucherService;

/**
 * 优惠券Service业务层处理
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@Service
public class TbVoucherServiceImpl implements ITbVoucherService 
{
    @Autowired
    private TbVoucherMapper tbVoucherMapper;

    /**
     * 查询优惠券
     * 
     * @param id 优惠券主键
     * @return 优惠券
     */
    @Override
    public TbVoucher selectTbVoucherById(Long id)
    {
        return tbVoucherMapper.selectTbVoucherById(id);
    }

    /**
     * 查询优惠券列表
     * 
     * @param tbVoucher 优惠券
     * @return 优惠券
     */
    @Override
    public List<TbVoucher> selectTbVoucherList(TbVoucher tbVoucher)
    {
        return tbVoucherMapper.selectTbVoucherList(tbVoucher);
    }

    /**
     * 新增优惠券
     * 
     * @param tbVoucher 优惠券
     * @return 结果
     */
    @Override
    public int insertTbVoucher(TbVoucher tbVoucher)
    {
        tbVoucher.setCreateTime(DateUtils.getNowDate());
        return tbVoucherMapper.insertTbVoucher(tbVoucher);
    }

    /**
     * 修改优惠券
     * 
     * @param tbVoucher 优惠券
     * @return 结果
     */
    @Override
    public int updateTbVoucher(TbVoucher tbVoucher)
    {
        tbVoucher.setUpdateTime(DateUtils.getNowDate());
        return tbVoucherMapper.updateTbVoucher(tbVoucher);
    }

    /**
     * 批量删除优惠券
     * 
     * @param ids 需要删除的优惠券主键
     * @return 结果
     */
    @Override
    public int deleteTbVoucherByIds(Long[] ids)
    {
        return tbVoucherMapper.deleteTbVoucherByIds(ids);
    }

    /**
     * 删除优惠券信息
     * 
     * @param id 优惠券主键
     * @return 结果
     */
    @Override
    public int deleteTbVoucherById(Long id)
    {
        return tbVoucherMapper.deleteTbVoucherById(id);
    }
}
