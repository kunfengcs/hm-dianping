package com.hmdp.interceptor;

import com.hmdp.dto.UserDTO;
import com.hmdp.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录拦截
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       //1,判断是否需要拦截(TheadLocal中是否有用户）
        if (UserHolder.getUser() == null) {
            //没有，需要拦截，设置状态码
            log.info("拦截请求请求:"+ request.getRequestURI());
            response.setStatus(401);
            //拦截
            return false;
        }
        //用用户，则放行
        return true;

        /*//1,获取session
        HttpSession session = request.getSession();
        //2,获取session中的用户
        Object user = session.getAttribute("user");
        //3，判断用户是否存在
        if (user == null){
            //4,不存在拦截，返回401(无权限)状态码
            response.setStatus(401);
        }
        //5，存在，保存用户信息到ThreadLocal
        UserHolder.saveUser((UserDTO) user);*/
        //6,放行
//        return true;
    }
}

