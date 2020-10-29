package com.frost2.skeleton.controller;

import com.frost2.skeleton.common.bean.Result;
import com.frost2.skeleton.service.IMigrateBuy;
import com.frost2.skeleton.service.IMigrateUsedList;
import com.frost2.skeleton.service.IMigrateWxPay;
import com.frost2.skeleton.service.ITimedTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 陈伟平
 * @date 2020-9-19 09:20:32
 */
@RestController
@Api(tags = "定时任务接口")
@RequestMapping(value = "timed")
public class TimedTaskController {

    @Autowired
    ITimedTaskService timedTaskServiceImpl;
    @Autowired
    IMigrateUsedList migrateUsedListImpl;
    @Autowired
    IMigrateBuy migrateBuyImpl;
    @Autowired
    IMigrateWxPay migrateWxPayImpl;

    @ApiOperation(value = "迁移历史订单数据", httpMethod = "POST")
    @PostMapping(value = "/migrate/usedList")
    public Result migrateUsedList(@RequestParam String jobName,
                                      @RequestParam String triggerName,
                                      @RequestParam String cron) {
        return migrateUsedListImpl.migrateUsedList(jobName, triggerName, cron);
    }

    @ApiOperation(value = "迁移充值订单数据", httpMethod = "POST")
    @PostMapping(value = "/migrate/buy")
    public Result migrateBuy(@RequestParam String jobName,
                                 @RequestParam String triggerName,
                                 @RequestParam String cron) {
        return migrateBuyImpl.migrateBuy(jobName, triggerName, cron);
    }

    @ApiOperation(value = "迁移预订单数据", httpMethod = "POST")
    @PostMapping(value = "/migrate/wxPay")
    public Result migrateWxPay(@RequestParam String jobName,
                                 @RequestParam String triggerName,
                                 @RequestParam String cron) {
        return migrateWxPayImpl.migrateWxPay(jobName, triggerName, cron);
    }

    @ApiOperation(value = "查询所有定时任务", httpMethod = "GET")
    @GetMapping(value = "/job")
    public Result queryAllTask() {
        return timedTaskServiceImpl.queryAllTask();
    }

    @ApiOperation(value = "查询定时任务", httpMethod = "GET")
    @GetMapping(value = "/job/{jobName}/{triggerName}")
    public Result queryTimedTask(@PathVariable String jobName, @PathVariable String triggerName) {
        return timedTaskServiceImpl.queryTimedTask(jobName, triggerName);
    }

    @ApiOperation(value = "查询最近5次执行时间", httpMethod = "GET")
    @GetMapping(value = "/job/{cron}")
    public Result queryAllTask(@PathVariable String cron) {
        return timedTaskServiceImpl.getRecentTriggerTime(cron);
    }

    @ApiOperation(value = "删除定时任务", httpMethod = "DELETE")
    @DeleteMapping(value = "/job/{jobName}/{triggerName}")
    public Result delTimedTask(@PathVariable String jobName, @PathVariable String triggerName) {
        return timedTaskServiceImpl.delTimedTask(jobName, triggerName);
    }

    @ApiOperation(value = "修改定时任务执行时间", httpMethod = "POST")
    @PostMapping(value = "/job/{jobName}/{triggerName}/{cron}")
    public Result rescheduleJob(@PathVariable String jobName, @PathVariable String triggerName, @PathVariable String cron) {
        return timedTaskServiceImpl.rescheduleJob(jobName, triggerName, cron);
    }


}
