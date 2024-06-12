package com.hmdp.config;

import com.hmdp.interceptor.LoginInterceptor;
import com.hmdp.interceptor.RefreshTokenInterceptor;
import com.hmdp.interceptor.makeMerchantInterceptor;
import com.hmdp.service.IUserService;
import com.hmdp.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
@Slf4j
public class MvcConfig implements WebMvcConfigurer {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IUserService userService;

    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录拦截
        registry.addInterceptor(new LoginInterceptor()).
                excludePathPatterns(
                        "/shop/**",
                        "/voucher/**",
                        "/shop-type/**",
                        "/upload/**",
                        "/blog/hot",
                        "/user/code",
                        "/user/login",
                        "/map/**",
                        "/file/**"
                ).order(1);

        //token刷新的拦截器
        registry.addInterceptor(
                new RefreshTokenInterceptor(stringRedisTemplate,jwtTokenUtil))
                .addPathPatterns("/**").order(0);

        // TODO 添加新的拦截器，只拦截 /user/makeMerchant 接口
//        registry.addInterceptor(new makeMerchantInterceptor(stringRedisTemplate,userService,jwtTokenUtil))
//                .addPathPatterns("/user/makeMerchant")
//                .order(2);

    }
}
