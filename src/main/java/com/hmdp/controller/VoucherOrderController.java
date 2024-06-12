package com.hmdp.controller;


import com.hmdp.dto.Result;
import com.hmdp.service.IVoucherOrderService;
import com.hmdp.service.impl.VoucherOrderServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
/**
 * <p>
 * VoucherOrderController 类是用于处理与优惠券订单相关的 HTTP 请求的 RESTful 控制器。
 * 它通过注入的 {@link VoucherOrderServiceImpl} 实现了对优惠券秒杀功能的调用。
 * </p>
 *
 * @author 王坤峰
 * @since 2021-12-22
 */
@Api(tags = "优惠券订单模块")
@RestController
@RequestMapping("/voucher-order")
public class VoucherOrderController {

    /**
     * <p>
     * voucherOrderService 字段是一个 {@link VoucherOrderServiceImpl} 类型的对象，
     * 通过 Spring 的 {@code @Resource} 注解进行依赖注入。
     * 它是控制器与服务层之间的桥梁，用于执行具体的优惠券秒杀逻辑。
     * </p>
     */
    @Resource
    private VoucherOrderServiceImpl voucherOrderService;

    /**
     * <p>
     * seckillVoucher 方法是一个 HTTP POST 请求处理器，其 URL 路径为 "/voucher-order/seckill/{id}"。
     * 它接收路径参数 {@code id}，表示待秒杀的优惠券编号（类型为 Long）。
     * 方法调用 {@link VoucherOrderServiceImpl#seckillVoucher(Long)} 执行秒杀操作，
     * 并将结果（封装在 {@link Result} 对象中）作为响应返回给客户端。
     * </p>
     *
     * @param voucherId 待秒杀的优惠券编号
     * @return 秒杀结果封装在 {@link Result} 对象中，包含成功状态及可能的订单ID或错误信息
     */
    @ApiOperation(value = "秒杀优惠券", notes = "秒杀优惠券")
    @PostMapping("seckill/{id}")
    public Result seckillVoucher(@PathVariable("id") Long voucherId) {
        return voucherOrderService.seckillVoucher(voucherId);
    }
}
