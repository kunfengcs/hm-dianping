package com.ruoyi.dianping_admin.service;

import java.util.List;
import com.ruoyi.dianping_admin.domain.TbSeckillVoucher;

/**
 * 秒杀优惠券，与优惠券是一对一关系Service接口
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
public interface ITbSeckillVoucherService 
{
    /**
     * 查询秒杀优惠券，与优惠券是一对一关系
     * 
     * @param voucherId 秒杀优惠券，与优惠券是一对一关系主键
     * @return 秒杀优惠券，与优惠券是一对一关系
     */
    public TbSeckillVoucher selectTbSeckillVoucherByVoucherId(Long voucherId);

    /**
     * 查询秒杀优惠券，与优惠券是一对一关系列表
     * 
     * @param tbSeckillVoucher 秒杀优惠券，与优惠券是一对一关系
     * @return 秒杀优惠券，与优惠券是一对一关系集合
     */
    public List<TbSeckillVoucher> selectTbSeckillVoucherList(TbSeckillVoucher tbSeckillVoucher);

    /**
     * 新增秒杀优惠券，与优惠券是一对一关系
     * 
     * @param tbSeckillVoucher 秒杀优惠券，与优惠券是一对一关系
     * @return 结果
     */
    public int insertTbSeckillVoucher(TbSeckillVoucher tbSeckillVoucher);

    /**
     * 修改秒杀优惠券，与优惠券是一对一关系
     * 
     * @param tbSeckillVoucher 秒杀优惠券，与优惠券是一对一关系
     * @return 结果
     */
    public int updateTbSeckillVoucher(TbSeckillVoucher tbSeckillVoucher);

    /**
     * 批量删除秒杀优惠券，与优惠券是一对一关系
     * 
     * @param voucherIds 需要删除的秒杀优惠券，与优惠券是一对一关系主键集合
     * @return 结果
     */
    public int deleteTbSeckillVoucherByVoucherIds(Long[] voucherIds);

    /**
     * 删除秒杀优惠券，与优惠券是一对一关系信息
     * 
     * @param voucherId 秒杀优惠券，与优惠券是一对一关系主键
     * @return 结果
     */
    public int deleteTbSeckillVoucherByVoucherId(Long voucherId);
}
