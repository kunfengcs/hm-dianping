package com.hmdp.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.CacheClient;
import com.hmdp.utils.RedisData;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.*;

/**
 * <p>
 *  商户服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

//    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    @Resource
    private CacheClient cacheClient;

    @Override
    public Result queryById(Long id){
        // 解决缓存穿透
//        Shop shop = cacheClient
//                .queryWithPassThrough(CACHE_SHOP_KEY, id, Shop.class, this::getById, CACHE_SHOP_TTL, TimeUnit.MINUTES);

//         互斥锁解决缓存击穿
         Shop shop = cacheClient
                 .queryWithMutex(CACHE_SHOP_KEY, id, Shop.class, this::getById, CACHE_SHOP_TTL, TimeUnit.MINUTES);

        // 逻辑过期解决缓存击穿
//         Shop shop = cacheClient
//                 .queryWithLogicalExpire(CACHE_SHOP_KEY, id, Shop.class, this::getById, 20L, TimeUnit.SECONDS);

        if (shop == null) {
            return Result.fail("店铺不存在！");
        }
        // 7.返回
        return Result.ok(shop);
/*        String key = CACHE_SHOP_KEY + id;
        //1,从redis查询商品缓存
        String shopJSON = stringRedisTemplate.opsForValue().get(key);
        //2,判断是否存在
        if (StrUtil.isNotBlank(shopJSON)) {
            //3,存在直接返回
            Shop shop = JSONUtil.toBean(shopJSON, Shop.class);
            return Result.ok(shop);
        }
        //4,不存在，根据id查询数据库
        Shop shop = getById(id);
        //不存在，返回错误
        if(shop == null){
            stringRedisTemplate.opsForValue().set(key,"",2L,TimeUnit.MINUTES);
            return Result.fail("店铺不存在");
        }
        //6,存在，写入redis
        stringRedisTemplate.opsForValue()
                .set(key,JSONUtil.toJsonStr(shop),30L, TimeUnit.MINUTES);
        //7,返回
        return Result.ok(shop);*/
    }

    @Override
    @Transactional
    public Result update(Shop shop) {
        Long id = shop.getId();
        if (id == null) {
            return Result.fail("店铺id不能为空");
        }
        // 1.更新数据库
        updateById(shop);
        // 2.删除缓存
        stringRedisTemplate.delete(CACHE_SHOP_KEY + id);
        return Result.ok();
    }

/*    public Shop queryWithMutex(Long id) throws InterruptedException {
        String key = CACHE_SHOP_KEY + id;
        //1,从Redis中查询商铺缓存
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        //2,判断是否存在
        if (StrUtil.isNotBlank(shopJson)) {
            return JSONUtil.toBean(shopJson, Shop.class);
        }
        //3,存在返回
        //判断命中的值是否是空值
        if (shopJson != null) {
            return null;
        }
        //4，不存在，实现缓存重构
        //4.1获取互斥锁
        String lockkey = "lock:shop" + id;
        Shop shop = null;
        try {
            boolean isLock = tryLock(lockkey);
            //4.2判断是否获取成功
            if (!isLock) {
                //4.3 失败，则休眠重试
                Thread.sleep(50);
                return queryWithMutex(id);
            }
            //4,4 成功，根据id查询数据库
            shop = getById(id);
//        5，不存在，返回错误
            if (shop == null) {
                //                将空值写入redis
                stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
                //                返回错误信息
                return null;
            }
//        6，存在，写入Redis
            stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop), CACHE_SHOP_TTL, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
//        7，释放互斥锁
            unlock(lockkey);
        }
        return shop;
    }

    //缓存预热
    public void saveShop2Redis(Long id,Long expireSeconds){
        //1,查询店铺数据
        Shop shop = getById(id);
        //2，封装逻辑过期时间
        RedisData redisData = new RedisData();
        redisData.setData(shop);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(expireSeconds));
        //3,写入Redis
        stringRedisTemplate.opsForValue().set(CACHE_SHOP_KEY + id,JSONUtil.toJsonStr(redisData));
    }

    public Shop queryWithLogicalExpire(Long id){
        String key = CACHE_SHOP_KEY + id;
        //1,redis查询商铺缓存
        String json = stringRedisTemplate.opsForValue().get(key);
//        2，判断是否存在
        if (StrUtil.isBlank(json)) {
//        3，存在，直接返回
            return null;
        }
//        4，命中，需要先把json反序列化为对象
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        Shop shop = JSONUtil.toBean((JSONObject) redisData.getData(), Shop.class);
        LocalDateTime expireTime = redisData.getExpireTime();
//        5，判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())) {
//        5.1未过期，直接返回店铺信息
            return shop;
        }
//        5.2 已过期，需要缓存重建
//        6，缓存重建
//        6,1 获取互斥锁
        String lockkey = LOCK_SHOP_KEY + id;
        boolean isLock = tryLock(lockkey);
//        6,2判断是否获取锁成功
        if (isLock) {
            CACHE_REBUILD_EXECUTOR.submit(() ->{
                    //        成功 缓存重建
                    try {
                        this.saveShop2Redis(id, 20L);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        unlock(lockkey);
                    }
            });

        }
//        6,4 失败，返回过期的商铺信息
        return shop;
    }
    @Override
    @Transactional
    public Result update(Shop shop) {
        Long id = shop.getId();
        if (id == null) {
            return Result.fail("店铺id不能为空");
        }
        //1,更新数据库
        update(shop);
        //2，删除缓存
        stringRedisTemplate.delete(CACHE_SHOP_KEY + id);
        return Result.ok();
    }

    private boolean tryLock(String key){
*//*        IfAbsent() redis的setnx方法
        如果没有这个key，则插入成功，返回1，如果有这个key则插入失败，则返回0，*//*
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    private void unlock(String key){
        stringRedisTemplate.delete(key);
    }*/
}
