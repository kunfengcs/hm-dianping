package com.ruoyi.dianping_admin.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.dianping_admin.mapper.TbSeckillVoucherMapper;
import com.ruoyi.dianping_admin.domain.TbSeckillVoucher;
import com.ruoyi.dianping_admin.service.ITbSeckillVoucherService;

/**
 * 秒杀优惠券，与优惠券是一对一关系Service业务层处理
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@Service
public class TbSeckillVoucherServiceImpl implements ITbSeckillVoucherService 
{
    @Autowired
    private TbSeckillVoucherMapper tbSeckillVoucherMapper;

    /**
     * 查询秒杀优惠券，与优惠券是一对一关系
     * 
     * @param voucherId 秒杀优惠券，与优惠券是一对一关系主键
     * @return 秒杀优惠券，与优惠券是一对一关系
     */
    @Override
    public TbSeckillVoucher selectTbSeckillVoucherByVoucherId(Long voucherId)
    {
        return tbSeckillVoucherMapper.selectTbSeckillVoucherByVoucherId(voucherId);
    }

    /**
     * 查询秒杀优惠券，与优惠券是一对一关系列表
     * 
     * @param tbSeckillVoucher 秒杀优惠券，与优惠券是一对一关系
     * @return 秒杀优惠券，与优惠券是一对一关系
     */
    @Override
    public List<TbSeckillVoucher> selectTbSeckillVoucherList(TbSeckillVoucher tbSeckillVoucher)
    {
        return tbSeckillVoucherMapper.selectTbSeckillVoucherList(tbSeckillVoucher);
    }

    /**
     * 新增秒杀优惠券，与优惠券是一对一关系
     * 
     * @param tbSeckillVoucher 秒杀优惠券，与优惠券是一对一关系
     * @return 结果
     */
    @Override
    public int insertTbSeckillVoucher(TbSeckillVoucher tbSeckillVoucher)
    {
        tbSeckillVoucher.setCreateTime(DateUtils.getNowDate());
        return tbSeckillVoucherMapper.insertTbSeckillVoucher(tbSeckillVoucher);
    }

    /**
     * 修改秒杀优惠券，与优惠券是一对一关系
     * 
     * @param tbSeckillVoucher 秒杀优惠券，与优惠券是一对一关系
     * @return 结果
     */
    @Override
    public int updateTbSeckillVoucher(TbSeckillVoucher tbSeckillVoucher)
    {
        tbSeckillVoucher.setUpdateTime(DateUtils.getNowDate());
        return tbSeckillVoucherMapper.updateTbSeckillVoucher(tbSeckillVoucher);
    }

    /**
     * 批量删除秒杀优惠券，与优惠券是一对一关系
     * 
     * @param voucherIds 需要删除的秒杀优惠券，与优惠券是一对一关系主键
     * @return 结果
     */
    @Override
    public int deleteTbSeckillVoucherByVoucherIds(Long[] voucherIds)
    {
        return tbSeckillVoucherMapper.deleteTbSeckillVoucherByVoucherIds(voucherIds);
    }

    /**
     * 删除秒杀优惠券，与优惠券是一对一关系信息
     * 
     * @param voucherId 秒杀优惠券，与优惠券是一对一关系主键
     * @return 结果
     */
    @Override
    public int deleteTbSeckillVoucherByVoucherId(Long voucherId)
    {
        return tbSeckillVoucherMapper.deleteTbSeckillVoucherByVoucherId(voucherId);
    }
}
