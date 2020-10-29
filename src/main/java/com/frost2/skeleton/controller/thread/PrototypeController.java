package com.frost2.skeleton.controller.thread;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 陈伟平
 * @date 2020-10-29 10:10:35
 */
@RestController
@Scope(value = "prototype")
@Api(tags = "线程安全的接口")
@RequestMapping(value = "safe")
public class PrototypeController {

    private static int var = 0;

    @ApiOperation(value = "prototype", httpMethod = "GET")
    @GetMapping(value = "/prototype")
    public String testVar() {
        System.out.println("var++ = " + var++);
        return String.valueOf(var);
    }

}
