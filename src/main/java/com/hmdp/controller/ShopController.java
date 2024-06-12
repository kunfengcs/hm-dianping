package com.hmdp.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.service.IShopService;
import com.hmdp.utils.CacheClient;
import com.hmdp.utils.SystemConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

import static com.hmdp.utils.RedisConstants.CACHE_SHOP_KEY;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 王坤峰
 * @since 2021-12-22
 */
@Api(tags = "商铺模块")
@RestController
@RequestMapping("/shop")
public class ShopController {

    @Resource
    private IShopService shopService;



    /**
     * 根据id查询商铺信息
     * @param id 商铺id
     * @return 商铺详情数据
     */
    @ApiOperation(value = "根据id查询商铺信息", notes = "根据id查询商铺信息")
    @GetMapping("/{id}")
    public Result queryShopById(@PathVariable("id") Long id) {
        return shopService.queryById(id);
    }

    /**
     * 新增商铺信息
     * @param shop 商铺数据
     * @return 商铺id
     */
    @ApiOperation(value = "新增商铺信息", notes = "新增商铺信息")
    @PostMapping
    public Result saveShop(@RequestBody Shop shop) {
        // 写入数据库
        shopService.save(shop);
        // 返回店铺id
        return Result.ok(shop.getId());
    }

    /**
     * 更新商铺信息
     * @param shop 商铺数据
     * @return 无
     */
    @ApiOperation(value = "更新商铺信息", notes = "更新商铺信息")
    @PutMapping
    public Result updateShop(@RequestBody Shop shop) {
        // 写入数据库
        return shopService.update(shop);
//        return Result.ok();
    }

    /**
     * 根据商铺类型分页查询商铺信息
     * @param typeId 商铺类型
     * @param current 页码
     * @return 商铺列表
     */
    @ApiOperation(value = "根据商铺类型分页查询商铺信息", notes = "根据商铺类型分页查询商铺信息")
    @GetMapping("/of/type")
    public Result queryShopByType(
            @RequestParam("typeId") Integer typeId,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "x", required = false) Double x,
            @RequestParam(value = "y", required = false) Double y,
            @RequestParam(value = "sortBy", required = false) String sortBy
    ) {
//        // 根据类型分页查询
//        Page<Shop> page = shopService.query()
//                .eq("type_id", typeId)
//                .page(new Page<>(current, SystemConstants.DEFAULT_PAGE_SIZE));
//        // 返回数据
//        return Result.ok(page.getRecords());
        return shopService.queryByType(typeId, current, x, y,sortBy);
    }

    /**
     * 根据商铺名称关键字分页查询商铺信息
     * @param name 商铺名称关键字
     * @param current 页码
     * @return 商铺列表
     */
    @ApiOperation(value = "根据商铺名称关键字分页查询商铺信息", notes = "根据商铺名称关键字分页查询商铺信息")
    @GetMapping("/of/name")
    public Result queryShopByName(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "current", defaultValue = "1") Integer current
    ) {
        // 根据类型分页查询
        Page<Shop> page = shopService.query()
                .like(StrUtil.isNotBlank(name), "name", name)
                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 返回数据
        return Result.ok(page.getRecords());
    }

    @ApiOperation(value = "获取所有商铺信息", notes = "获取所有商铺信息")
    @PostMapping("/list")
    public Result getShopList(){
        List<Shop> list = shopService.list();
        return Result.ok(list);
    }
    @ApiOperation(value = "根据name查询商铺信息", notes = "根据name查询商铺信息")
    @GetMapping("/check/{name}")
    public Result getByName(@PathVariable("name")String name){
        if (name != null && !name.isEmpty()) {
            // 根据name查询店铺
            Shop shop = shopService.query().eq("name", name).one();
            return Result.ok(shop);
        } else {
            // 根据id查询店铺
            return Result.ok(shopService.getById(name));
        }
    }
}
