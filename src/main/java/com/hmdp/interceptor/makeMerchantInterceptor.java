package com.hmdp.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.service.IUserService;
import com.hmdp.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

import static com.hmdp.utils.RedisConstants.LOGIN_USER_KEY;

/**
 *  TODO 添加新的拦截器，只拦截 /user/makeMerchant 接口
 *  只有 特定用户可以访问
 */
@Slf4j
public class makeMerchantInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    private IUserService userService;

    private JwtTokenUtil jwtTokenUtil;


    public makeMerchantInterceptor(StringRedisTemplate stringRedisTemplate, IUserService userService,JwtTokenUtil jwtTokenUtil) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截到请求");
        //1,获取请求头中的token
        String token = request.getHeader("authorization");
//        System.err.println("token:"+token);
        if (StrUtil.isBlank(token)) {
            return true;
        }
        /*//2,基于TOKEN获取Redis 中的用户
        String key = LOGIN_USER_KEY + token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
        //3，判断用户是否存在
        if (userMap.isEmpty()) {
            return true;
        }
        //5，将查询到的hash数据转为UserDTO
        UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);*/


        UserDTO userDTO = jwtTokenUtil.getValueFormClaims(token, "user", UserDTO.class);

        Long id = userDTO.getId();
        User user = userService.getById(id);
        System.err.println(user);
        if (Objects.equals(user.getPhone(), "15035567595")) return true;
        else{
            log.info("拦截阻止请求");
            response.setStatus(403);
            return false;
        }
    }
}
