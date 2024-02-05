package com.hmdp;

import cn.hutool.core.util.NumberUtil;
import com.hmdp.entity.Shop;
import com.hmdp.service.IShopService;
import com.hmdp.service.impl.ShopServiceImpl;
import com.hmdp.utils.CacheClient;
import com.hmdp.utils.RedisIdWorker;
import io.netty.util.internal.StringUtil;
import net.sf.jsqlparser.expression.StringValue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.CACHE_SHOP_KEY;
import static com.hmdp.utils.RedisConstants.SECKILL_STOCK_KEY;

@SpringBootTest
class HmDianPingApplicationTests {

    @Resource
    private ShopServiceImpl shopService;
    @Resource
    private CacheClient cacheClient;

    @Resource
    private RedisIdWorker redisIdWorker;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private ExecutorService es = Executors.newFixedThreadPool(500);


    @Test
    void setSeckillToRedis(){
        stringRedisTemplate.opsForValue().set(SECKILL_STOCK_KEY + 1,
                String.valueOf(10));
    }
    @Test
    void testRedisIdWorker() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(300);

        Runnable task = () -> {
            for (int i = 0; i < 100; i++) {
                long id = redisIdWorker.nextId("order");
                System.out.println("id=" + id);
            }
            latch.countDown();
        };
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 300; i++) {
            es.submit(task);
        }
        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("tiem= "+ (end - begin));
    }
/*    @Test
    void testRedisIdWorker(){
        long order1 = redisIdWorker.nextId("order");
        System.out.println(Long.toBinaryString(order1));
        long order2 = redisIdWorker.nextId("order");
        System.out.println(Long.toBinaryString(order2));
    }*/
    @Test
    void testSaveShop() throws InterruptedException {
        for (int i = 1; i <= 14; i++) {
//            Long id = Long.valueOf(i);
            Shop shop = shopService.getById(i);
            cacheClient.setWithLogicalExpire(CACHE_SHOP_KEY + i, shop, 10L, TimeUnit.SECONDS);
        }
    }
//    @Test
//    void testSaveShop() throws InterruptedException {
//        Shop shop = shopService.getById(1L);
//        cacheClient.setWithLogicalExpire(CACHE_SHOP_KEY + 1L, shop, 10L, TimeUnit.SECONDS);
//    }


    @Test
    void getNewTime(){
        System.out.println(LocalDateTime.now());
    }
}

