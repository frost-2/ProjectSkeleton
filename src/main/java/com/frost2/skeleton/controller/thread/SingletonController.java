package com.frost2.skeleton.controller.thread;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 没有添加@scope注解时，该Controller默认是单例的。
 *
 * @author 陈伟平
 * @date 2020-10-29 10:10:35
 */
@RestController
@Api(tags = "单例的Controller")
@RequestMapping(value = "/thread")
public class SingletonController {

    private int var = 0;

    /**
     * SingletonController单例情况下,每次请求都是访问同一个var变量
     *
     * @return var++
     */
    @ApiOperation(value = "singleton", httpMethod = "GET")
    @GetMapping(value = "/singleton")
    public String testVar() {
        System.out.println("var++ = " + var++);
        return String.valueOf(var);
    }

}
