package com.frost2.skeleton.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 陈伟平
 * @date 2019-11-11 7:41:00
 */
@Component
public class CORSInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

//        response.setHeader("Access-Control-Allow-Origin", "https://localhost:8080");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,X-Requested-With,x-tit-vstr,x-tit-noncestr,x-tit-timestamp");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Expose-Headers", "LoginStatus");

        // 预检请求返回200
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(200);
            return false;
        }
        return true;
    }

}
