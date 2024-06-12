package com.hmdp.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.hmdp.utils.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王坤峰
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IShopTypeService typeService;
    @Override
    public Result queryByList() {
//        List<ShopType> typeList = typeService
//                .query().orderByAsc("sort").list();
       //1.从缓存中查询商铺类型缓存
        List<String> shopTypeJson = stringRedisTemplate.opsForList().range(RedisConstants.CACHE_TYPE_KEY, 0, -1);
        //2.判断是否存在缓存
        if (!CollectionUtil.isEmpty(shopTypeJson)){
            //不为空则将json转为bean集合
            List<ShopType> shopTypeList = shopTypeJson.stream().map(item -> JSONUtil.toBean(item, ShopType.class))
                    .sorted(Comparator.comparingInt(ShopType::getSort))
                    .collect(Collectors.toList());
            //返回缓存数据
            return Result.ok(shopTypeList);
        }
        //如果不存在缓存数据
//        LambdaQueryWrapper<ShopType> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.orderByAsc(ShopType::getSort);
//        List<ShopType> shopTypes = this.list(queryWrapper);
        List<ShopType> shopTypes = typeService
                .query().orderByAsc("sort").list();
        //判断数据库中是否有数据
        if (CollectionUtil.isEmpty(shopTypes)){
            return Result.fail("商铺分类信息为空");
        }
        //存在则写入缓存
        List<String> shopTypeCache = shopTypes.stream().sorted(Comparator.comparingInt(ShopType::getSort))
                .map(JSONUtil::toJsonStr)
                .collect(Collectors.toList());
        //此处只能使用右插入，保证顺序
        stringRedisTemplate.opsForList().rightPushAll(RedisConstants.CACHE_TYPE_KEY,shopTypeCache);
        stringRedisTemplate.expire(RedisConstants.CACHE_TYPE_KEY,RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);
        //返回数据
        return Result.ok(shopTypes);
    }
}
