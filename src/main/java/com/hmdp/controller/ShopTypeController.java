package com.hmdp.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.service.IShopTypeService;
import io.netty.util.internal.StringUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 王坤峰
 * @since 2021-12-22
 */
@RestController
@RequestMapping("/shop-type")
public class ShopTypeController {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private IShopTypeService typeService;

    @GetMapping("list")
    public Result queryTypeList() {
        String key = "SHOP_TYPE_KEY";
        String shopType = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(shopType)){
            // 将 JSONArray 转为 List<ShopType>
            List<ShopType> typeList = JSONUtil.parseArray(shopType).toList(ShopType.class);
            return Result.ok(typeList);
        }

        List<ShopType> typeList = typeService
                .query().orderByAsc("sort").list();
        if (typeList == null){
            return Result.fail("商品类型空");
        }
        stringRedisTemplate.opsForValue().set(key
                ,JSONUtil.toJsonStr(typeList),30, TimeUnit.MINUTES);
        return Result.ok(typeList);
    }
}
