package com.frost2.skeleton.common.config;

import com.frost2.skeleton.common.interceptor.CORSInterceptor;
import com.frost2.skeleton.common.interceptor.CheckInterfaceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 陈伟平
 * @date 2019-11-07 4:12:00
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private CORSInterceptor corsInterceptor;
    @Autowired
    private CheckInterfaceInterceptor checkInterfaceInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //先添的拦截器，优先级高。
        registry.addInterceptor(corsInterceptor).addPathPatterns("/**");

        registry.addInterceptor(checkInterfaceInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**") //不拦截swagger-ui
                .excludePathPatterns("/statistics/**", "/", "/csrf", "/error");
    }

}
