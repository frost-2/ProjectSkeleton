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
@Api(tags = "PrototypeController")
@RequestMapping(value = "safe")
public class PrototypeController {

    private int var = 0;
    private static int staticVar = 0;

    /**
     * 当Controller的作用域(scope)设置为prototype时：
     * 1：虽然每次都会创建一个实例，所以var++结果始终是1
     * 2：虽然每次都会创建一个实例，但是今天变量是保存在方法区中的，他是线程共享的，所以staticVar结果一直累加
     */
    @ApiOperation(value = "prototype", httpMethod = "GET")
    @GetMapping(value = "/prototype")
    public String testVar() {
        System.out.println("var++ = " + var++);
        System.out.println("staticVar++ = " + staticVar++);
        return this.hashCode() + ":" + var + "-" + staticVar;
    }

}
