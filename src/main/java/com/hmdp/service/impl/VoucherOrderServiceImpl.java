package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.SeckillVoucher;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisIdWorker;
import com.hmdp.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王坤峰
 * @since 2021-12-22
 */
@Slf4j
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {

    @Resource
    private SeckillVoucherServiceImpl seckillVoucherService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisIdWorker redisIdWorker;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 解锁Lua脚本，默认RedisScript实例，用于原子性的解锁操作
     */
    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    //静态块，加载并设置解释Lua脚本
    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));//设置Lua脚本路径
        SECKILL_SCRIPT.setResultType(Long.class);//设置返回结果类型为Long
    }


    //异步处理线程池  -----不应该用单线程，出于简单
    //private static final ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();

    // 使用固定大小的线程池，可以根据实际需求调整线程数量
    private static final ExecutorService SECKILL_ORDER_EXECUTOR = new ThreadPoolExecutor(
            4, 8, // 核心线程数和最大线程数
            10, TimeUnit.SECONDS, // 空闲线程存活时间
            new LinkedBlockingQueue<>(1024), // 阻塞队列大小
            new ThreadPoolExecutor.CallerRunsPolicy() // 当线程池饱和时，提交任务的线程直接执行该任务
    );

    IVoucherOrderService proxy ;


    @PostConstruct
    private void init(){
        SECKILL_ORDER_EXECUTOR.submit(this::processOrdersFromStream);
    }

    private static final String QUEUE_NAME = "stream.orders";
    //redis stream 队列
    private void processOrdersFromStream() {
        while (true) {
            try {
                //1,获取消息队列中的订单 XREADFROUP GROUP g1 c1 COUNT 1 BLOCK 2000 STREAMS stream.orders >  // 参数>表示未被组内消费的起始消息
                List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
                        Consumer.from("g1", "c1"),
                        StreamReadOptions.empty().count(1).block(Duration.ofSeconds(2)),
                        StreamOffset.create(QUEUE_NAME, ReadOffset.lastConsumed()));
                //2,判断消息获取是否成功
                if(list == null || list.isEmpty()){
                    //如果获取失败，说明没有消息，继续下一次循环
                    continue;
                }
                //3,解析消息中的订单消息
                MapRecord<String, Object, Object> record = list.get(0);
                Map<Object, Object> values = record.getValue();
                VoucherOrder voucherOrder = BeanUtil.fillBeanWithMap(values, new VoucherOrder(), true);
                //4,如果获取成功，可以下单
                handleVoucherOrder(voucherOrder);
                //5,ACK 确认 SACK stream.orders g1 id
                stringRedisTemplate.opsForStream().acknowledge(QUEUE_NAME,"g1",record.getId());
            } catch (Exception e) {
                log.error("处理订单异常",e);
                try {
                    //1,获取pending-list中的订单信息 XREADGROUP GROUP g1 c1 COUNT 1 STREAMS stream.orders >
                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
                            Consumer.from("g1", "c1"),
                            StreamReadOptions.empty().count(1),
                            StreamOffset.create(QUEUE_NAME, ReadOffset.lastConsumed())
                    );
                    //2,判断消息获取是否成功
                    if(list == null || list.isEmpty()){
                        //如果获取失败，说明pending-list 没有异常消息，结束循环
                        break;
                    }
                    //3,解析消息中的订单消息
                    MapRecord<String, Object, Object> record = list.get(1);
                    Map<Object, Object> values = record.getValue();
                    VoucherOrder voucherOrder = BeanUtil.fillBeanWithMap(values, new VoucherOrder(), true);
                    //4,如果获取成功，可以下单
                    handleVoucherOrder(voucherOrder);
                    //5,ACK 确认 SACK stream.orders g1 id
                    stringRedisTemplate.opsForStream().acknowledge(QUEUE_NAME,"g1",record.getId());
                } catch (Exception ex) {
                    log.error("处理pending-list订单异常",e);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException exc) {
                        exc.printStackTrace();
                    }
                }
            }
        }
    }

/*    //阻塞队列
    private BlockingQueue<VoucherOrder> orderTasks = new ArrayBlockingQueue<>(1024 * 1024);
    Thread thread = new Thread(() -> {
        while (true) {
            try {
                //1,获取队列中的订单
                VoucherOrder voucherOrder = orderTasks.take();
                //2,创建订单
                handleVoucherOrder(voucherOrder);
            } catch (Exception e) {
                log.error("处理订单异常",e);
            }
        }
    });*/

    /**
     * 处理创建订单
     * @param voucherOrder 订单对象
     */
    private void handleVoucherOrder(VoucherOrder voucherOrder){

//        Long userId = UserHolder.getUser().getId();  //线程池不是一个线程
        //1,获取用户
        Long userId = voucherOrder.getUserId();
        //2,创建锁对象
        RLock lock = redissonClient.getLock("lock:order:" + userId);
        //获取锁对象
        boolean isLock = lock.tryLock();
        //加锁失败
        if (!isLock) {
            log.error("不允许重复下单");
            return;
        }
        try {
            //获取代理对象（事务）
           proxy.createVoucherOrder(voucherOrder);
        } finally {
            //释放锁
            lock.unlock();
        }
    }

    /**
     * 参与秒杀活动，尝试购买指定优惠券
     *
     * @param voucherId 待购买的优惠券ID
     * @return 秒杀结果，包含成功状态及可能的订单ID或错误信息
     */
    @Override
    public Result seckillVoucher(Long voucherId) {

        //用户ID
        Long userId = UserHolder.getUser().getId();
        //订单ID
        long orderId = redisIdWorker.nextId("order");
        // 1.执行lua脚本
        Long result = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                voucherId.toString(), userId.toString(), String.valueOf(orderId)
        );
        int res = result.intValue();
        //2.判断结果是否为0
        if(res != 0){
            // 2.1 不为0，代表没有购买资格
            log.error(res == 1 ? "库存不足" : "不能重复下单");
            return Result.fail(res == 1 ? "库存不足" : "不能重复下单");
        }

        //2.2 为0 ， 有购买资格
/*        VoucherOrder voucherOrder = new VoucherOrder();
        //2.3 订单ID
        voucherOrder.setId(orderId);
        voucherOrder.setUserId(userId);
        voucherOrder.setVoucherId(voucherId);

         // 保存到阻塞队列
        orderTasks.add(voucherOrder);*/
        //3.获取代理对象
        proxy = (IVoucherOrderService) AopContext.currentProxy();
        //3.返回订单id
        return Result.ok(orderId);
    }


/*
    @Override
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
        Long userId = UserHolder.getUser().getId();
        //创建锁对象
//        SimpleRedisLock lock = new SimpleRedisLock("order:" + userId, stringRedisTemplate);
        RLock lock = redissonClient.getLock("lock:order:" + userId);
        //获取锁对象
        boolean isLock = lock.tryLock();
        //加锁失败
        if (!isLock) {
            return Result.fail("不允许重复下单");
        }
        try {
            //获取代理对象（事务）
            IVoucherOrderService proxy = (IVoucherOrderService) AopContext.currentProxy();
            return proxy.createVoucherOrder(voucherId);
        } finally {
            //释放锁
            lock.unlock();
        }

        //为什么用intern(),因为每次userId都是一个新的对象，string不一样，
        //intern()去字符串常量池中返回和这个字符串一样的字符串地址
        synchronized (userId.toString().intern()){
            //获取代理对象（事务）
            *//**//*AopContext.currentProxy() 是Spring AOP提供的一个方法，
            它的作用是在当前被代理的方法内部获取当前代理对象本身。
            因为有时在业务逻辑中，可能需要调用其他未添加切面的方法，
            而直接使用 this 关键字会访问到目标业务对象而非代理对象，
            这时就需要用到 currentProxy() 方法来获取完整的代理对象，
            从而确保后续调用的方法同样能享受到AOP代理带来的增强效果。*//**//*
            IVoucherOrderService proxy = (IVoucherOrderService) AopContext.currentProxy();
            return proxy.createVoucherOrder(voucherId);
        }
        *//*
    }

    */

    /**
     * 在事务中创建订单，确保库存扣减与订单生成的原子性
     *
     * @param voucherOrder 待购买的优惠券ID
     */
    @Transactional
    public void createVoucherOrder(VoucherOrder voucherOrder) {
        //5.一人一单逻辑
        //5.1用户id
        Long userId = voucherOrder.getUserId();
        Long count = lambdaQuery().eq(VoucherOrder::getUserId, userId)
                .eq(VoucherOrder::getVoucherId, voucherOrder.getVoucherId()).count();
        //5.1 判断是否存在
        if (count > 0) {
            // 用户已经购买过了
           log.error("用户已经购买过一次！！");
        }
        //6, 扣减库存
        boolean success = seckillVoucherService.lambdaUpdate()
                .setSql("stock = stock - 1") // set stock = stock - 1
                .eq(SeckillVoucher::getVoucherId, voucherOrder.getVoucherId())
                .gt(SeckillVoucher::getStock, 0) // where id = ? and stock > 0    gt() gt（大于 >）
                .update();
        if (!success) {
            //扣减库存
            log.error("库存不足");
        }
        //6.3代金劵id
        save(voucherOrder);
    }
}
