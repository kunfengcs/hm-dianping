package com.hmdp.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.hmdp.dto.Result;
import com.hmdp.entity.SeckillVoucher;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisIdWorker;
import com.hmdp.utils.UserHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {

    @Resource
    private SeckillVoucherServiceImpl seckillVoucherService;

    @Resource
    private RedisIdWorker redisIdWorker;
    @Override
    @Transactional
    public Result seckillVoucher(Long voucherId) {
        //1,查询优惠劵
        SeckillVoucher voucher = seckillVoucherService.getById(voucherId);
        //2,判断秒杀是否开始
        if (voucher.getBeginTime().isAfter(LocalDateTime.now())){
            //尚未开始
            return Result.fail("秒杀尚未开始");
        }
        //3,判断秒杀是否已经结束
        if (voucher.getEndTime().isBefore(LocalDateTime.now())){
            //已经结束
            return Result.fail("秒杀已经结束");
        }
        //4,判断库存是否充足
        if (voucher.getStock() < 1) {
            //库存不足
            return Result.fail("库存不足");
        }
        //5.一人一单逻辑
        //5.1用户id
        Long userId = UserHolder.getUser().getId();
        Long count = lambdaQuery().eq(VoucherOrder::getUserId, userId)
                .eq(VoucherOrder::getVoucherId, voucherId).count();
        //5.1 判断是否存在
        if (count > 0) {
            // 用户已经购买过了
            return Result.fail("用户已经购买过一次！！");
        }
        //6, 扣减库存
        boolean success = seckillVoucherService.lambdaUpdate()
                .setSql("stock = stock -1")
                .eq(SeckillVoucher::getVoucherId,voucherId).update();
        if (!success) {
            //扣减库存
            return Result.fail("库存不足");
        }
        //6，创建订单
        VoucherOrder voucherOrder = new VoucherOrder();
        //6.1订单id
        long orderId = redisIdWorker.nextId("order");
        voucherOrder.setId(orderId);

        voucherOrder.setUserId(userId);
        //6.3代金劵id
        voucherOrder.setVoucherId(voucherId);
        save(voucherOrder);
        return Result.ok(orderId);
    }

}
