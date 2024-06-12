package com.hmdp.mapper;

import com.hmdp.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 王坤峰
 * @since 2021-12-22
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("select password from tb_user where phone = #{phone}")
    String getPassword(String phone);
}
