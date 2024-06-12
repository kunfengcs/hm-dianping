package com.hmdp.controller;

import com.hmdp.dto.PageResult;
import com.hmdp.dto.RequestParams;
import com.hmdp.dto.Result;
import com.hmdp.service.IShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "地图模块")
@RestController
@RequestMapping("/map")
public class MapController {
    @Resource
    private IShopService shopService;

    @ApiOperation(value = "搜索店铺", notes = "根据关键字搜索店铺")
    @GetMapping("/shop")
    public PageResult search(@RequestBody RequestParams params) {
        return shopService.search(params);
    }

    @ApiOperation(value = "根据关键字搜索店铺", notes = "根据关键字搜索店铺")
    @GetMapping("suggestion")
    public Result getSuggestion(@RequestParam("key") String prefix){
        return shopService.getSuggestion(prefix);
    }
}
