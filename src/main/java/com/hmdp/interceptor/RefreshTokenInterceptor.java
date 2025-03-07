package com.hmdp.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.UserDTO;
import com.hmdp.utils.JwtTokenUtil;
import com.hmdp.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.LOGIN_USER_KEY;
import static com.hmdp.utils.RedisConstants.LOGIN_USER_TTL;

/**
 * 刷新Token拦截器
 * 保存用户信息
 */
@Slf4j
public class RefreshTokenInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    private JwtTokenUtil jwtTokenUtil;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate,JwtTokenUtil jwtTokenUtil) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.jwtTokenUtil = jwtTokenUtil;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("接受到请求:"+ request.getRequestURI());
        //1,获取请求头中的token
        String token = request.getHeader("authorization");
        System.err.println("token:"+token);
        if (StrUtil.isBlank(token)) {
            return true;
        }
/*        //2,基于TOKEN获取Redis 中的用户
        String key = LOGIN_USER_KEY + token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
        //3，判断用户是否存在
        if (userMap.isEmpty()) {
            return true;
        }
        //5，将查询到的hash数据转为UserDTO
        UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);*/

        //从jwt中获取用户

        LinkedHashMap linkedHashMapUser = jwtTokenUtil.getValueFormClaims(token, "user", LinkedHashMap.class);
        UserDTO userDTO = BeanUtil.fillBeanWithMap(linkedHashMapUser, new UserDTO(), false);
        if (userDTO == null){
            log.info("token无效,未获取到用户");
            return true;
        }
        //6,存在，保存用户信息到 ThreadLocal
        log.info("当前登录用户："+userDTO);
        UserHolder.saveUser(userDTO);
/*        //7,刷新token 有效期
        stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);*/

        //刷新jwt TODO 未实现
        String newtoken = jwtTokenUtil.refreshToken(token);
        //8，放行
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //移除用户
        UserHolder.removeUser();
    }


}
