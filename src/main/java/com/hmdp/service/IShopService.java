package com.hmdp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmdp.dto.PageResult;
import com.hmdp.dto.RequestParams;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王坤峰
 * @since 2021-12-22
 */
public interface IShopService extends IService<Shop> {

    Result queryById(Long id);


    Result update(Shop shop);

    Result queryByType(Integer typeId, Integer current, Double x, Double y,String sortBy);

    PageResult search(RequestParams params);

    Result getSuggestion(String prefix);

}
