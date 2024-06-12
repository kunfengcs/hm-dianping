package com.hmdp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmdp.dto.Result;
import com.hmdp.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王坤峰
 * @since 2021-12-24
 */
public interface IUserInfoService extends IService<UserInfo> {

    Result updateInfo(UserInfo user);

    Result getUserInfo();

}
