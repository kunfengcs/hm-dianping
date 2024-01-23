package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.CACHE_SHOP_KEY;

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

    @Override
    public Result queryById(Long id){
        String key = CACHE_SHOP_KEY + id;
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
            return Result.fail("店铺不存在");
        }
        //6,存在，写入redis
        stringRedisTemplate.opsForValue()
                .set(key,JSONUtil.toJsonStr(shop),30L, TimeUnit.MINUTES);
        //7,返回
        return Result.ok(shop);
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
}
