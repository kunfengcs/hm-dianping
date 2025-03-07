package com.hmdp.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.entity.UserInfo;
import com.hmdp.service.IUserInfoService;
import com.hmdp.service.IUserService;
import com.hmdp.utils.UserHolder;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 王坤峰
 * @since 2021-12-22
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = "用户模块")
public class UserController {

    @Resource
    private IUserService userService;

    @Resource
    private IUserInfoService userInfoService;

    /**
     * 发送手机验证码
     */
    @ApiOperation(value = "发送手机验证码", notes = "发送手机验证码并保存验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String"),
    })
    @PostMapping("code")
    public Result sendCode(@RequestParam("phone") String phone, HttpSession session) {
        // TODO 发送短信验证码并保存验证码
        return userService.sendCode(phone,session);
    }

    /**
     * 登录功能
     * @param loginForm 登录参数，包含手机号、验证码；或者手机号、密码
     */
    @ApiOperation(value = "登录功能", notes = "实现登录功能")
    @ApiResponses({
            @ApiResponse(code = 200, message = "登录成功"),
    })
    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginForm, HttpSession session) {
        // TODO 实现登录功能
        return userService.login(loginForm,session);
    }

    /**
     * 登出功能
     * @return 无
     */
    @ApiOperation(value = "登出功能", notes = "实现登出功能")
    @ApiResponses({
            @ApiResponse(code = 200, message = "登出成功"),
    })
    @PostMapping("/logout")
    public Result logout(){
        // TODO 实现登出功能
        return userService.logout();
    }

    @ApiOperation(value = "获取当前登录用户信息", notes = "获取当前登录的用户并返回")
    @GetMapping("/me")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
    })
    public Result me(){
        // 获取当前登录的用户并返回
        UserDTO user = UserHolder.getUser();
        log.info("返回当前登录用户"+user);
        return Result.ok(user);
    }

    @ApiOperation(value = "根据用户ID获取用户信息", notes = "根据用户ID获取用户信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/info/{id}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
    })
    public Result info(@PathVariable("id") Long userId) {
        // 查询详情
        UserInfo info = userInfoService.getById(userId);
        if (info == null) {
            // 没有详情，应该是第一次查看详情
            return Result.ok();
        }
        info.setCreateTime(null);
        info.setUpdateTime(null);
        // 返回
        return Result.ok(info);
    }
    // TODO 加一个更新为商户接口 ，通过手机号，把is_shop 字段改为1
    // 谁可以访问这个接口，想法：可以再加一个拦截器 只有特定的用户可以访问

    /**
     * 更新用户为商户接口
     * @param requestBody 手机号
     * @return 返回更新结果
     */
    @ApiOperation(value = "更新用户为商户", notes = "通过手机号更新用户为商户")
    @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String")
    @PostMapping("/makeMerchant")
    public Result makeMerchant(@RequestBody Map<String, String> requestBody) {
        String phone = requestBody.get("phone");
        // 根据手机号查找用户
        User user = userService.getOne(new QueryWrapper<User>().eq("phone", phone));
        if (user == null) {
            // 如果用户不存在，返回错误提示
            return Result.fail("用户不存在");
        }
        // 将用户设为商户
        user.setIsShop(1);
        userService.updateById(user);
        log.info("用户"+ phone +"更新为商户");
        return Result.ok("用户已更新为商户");
    }

    /**
     * 根据ID查询用户
     * @param userId 用户ID
     * @return 用户dto
     */
    @ApiOperation(value = "根据ID查询用户", notes = "根据ID查询用户")
    @GetMapping("/{id}")
    public Result queryById(@PathVariable("id") Long userId) {
        // 查询详情
        User user = userService.getById(userId);
        if (user == null) {
            return Result.ok();
        }
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        return Result.ok(userDTO);
    }

    /**
     * 签到功能
     * @return
     */
    @ApiOperation(value = "签到功能", notes = "实现签到功能")
    @PostMapping("/sign")
    public Result sign(){
        return userService.sign();
    }

    /**
     * 签到统计
     *
     * @return 连续签到天数
     */
    @GetMapping("/sign/count")
    public Result signCount() {
        return userService.signCount();
    }
    /**
     * 获取详细信息和更新
     */
    @ApiOperation(value = "获取详细信息和更新", notes = "获取详细信息和更新")
    @PutMapping("/info")
    public Result updateInfo(@RequestBody UserInfo user){
        return userInfoService.updateInfo(user);
    }

    /**
     * 获取详细信息
     */
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @GetMapping("/getInfo")
    public Result getUserInfo(){
        return userInfoService.getUserInfo();
    }

    /**
     * 改变昵称
     */
    @ApiOperation(value = "改变昵称", notes = "改变昵称")
    @PutMapping("/changeName")
    public Result updateInfo(@RequestParam("nickName") String nickName ){
        return userService.changeName(nickName);
    }

    /**
     * 获取用户
     */
    @ApiOperation(value = "获取用户", notes = "获取用户")
    @GetMapping("/getUser")
    public Result getUser(@RequestParam("id")String id){
        User user = userService.getById(id);
        return Result.ok(user);
    }

    /**
     * 修改密码
     */
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @PutMapping("/changePassword")
    public Result changePassword(@RequestParam("password")String password,@RequestParam("newPassword")String newPassword){
        User user = userService.getById(UserHolder.getUser().getId());
        if (user.getPassword().equals(password)){
            user.setPassword(newPassword);
            userService.updateById(user);
            return Result.ok("修改成功");
        }
        return Result.fail("修改失败");
    }
}
