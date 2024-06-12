package com.hmdp.controller;


import com.hmdp.dto.Result;
import com.hmdp.service.IFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 王坤峰
 * @since 2021-12-22
 */
@Api(tags = "关注模块")
@RestController
@RequestMapping("/follow")
public class FollowController {

    @Resource
    private IFollowService followService;

    /**
     * 关注
     * @param followUserId 关注的用户ID
     * @param isFollow 是否关注
     * @return
     */
    @ApiOperation(value = "关注")
    @PutMapping("/{id}/{isFollow}")
    public Result follow(@PathVariable("id") Long followUserId, @PathVariable Boolean isFollow) {
        return followService.follow(followUserId,isFollow);
    }

    /**
     * 是否关注
     * @param followUserId 关注的 用户
     * @return 是否关注 关注 1
     */
    @ApiOperation(value = "是否关注")
    @GetMapping("/or/not/{id}")
    public Result isFollow(@PathVariable("id") Long followUserId){
        return followService.isFollow(followUserId);
    }

    @ApiOperation(value = "共同关注")
    @GetMapping("/common/{id}")
    public Result followCommons(@PathVariable("id") Long id){
        return followService.followCommons(id);
    }
    @ApiOperation(value = "我的关注")
    @GetMapping("/mine/{id}")
    public Result followMine(@PathVariable("id") Long id){
        return followService.followMine(id);
    }

    @ApiOperation(value = "我的粉丝")
    @GetMapping("/yours/{id}")
    public Result followYours(@PathVariable("id") Long id){
        return followService.followYours(id);
    }
}
