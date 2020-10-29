package com.frost2.skeleton.service.impl;

import com.frost2.skeleton.common.bean.Code;
import com.frost2.skeleton.common.bean.Constant;
import com.frost2.skeleton.common.bean.Result;
import com.frost2.skeleton.common.util.QuartzUtil;
import com.frost2.skeleton.service.IMigrateBuy;
import com.frost2.skeleton.service.IMigrateUsedList;
import com.frost2.skeleton.service.IMigrateWxPay;
import com.frost2.skeleton.service.ITimedTaskService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author 陈伟平
 * @date 2020-9-19 09:24:07
 */
@Service
public class TimedTaskServiceImpl implements ITimedTaskService, IMigrateUsedList, IMigrateBuy, IMigrateWxPay {

    /**
     * 查询正在运行的定时任务
     *
     * @param jobName     任务名称
     * @param triggerName 触发器名
     * @return JobDetail
     */
    @Override
    public Result queryTimedTask(String jobName, String triggerName) {
        HashMap<String, String> jobTask = QuartzUtil.getJob(jobName, triggerName);
        return Result.query_succ(jobTask.size(), jobTask);
    }

    /**
     * @param jobName     任务名称
     * @param triggerName 触发器名
     * @return 是否删除成功
     */
    @Override
    public Result delTimedTask(String jobName, String triggerName) {
        boolean isDelJob = QuartzUtil.removeJob(jobName, triggerName);
        return Result.del_succ(isDelJob);
    }

    /**
     * @param jobName     任务名称
     * @param triggerName 触发器名
     * @return 是否修改成功
     */
    @Override
    public Result rescheduleJob(String jobName, String triggerName, String cron) {
        if (!QuartzUtil.checkCronExpression(cron)) {
            return Result.failed(Code.PARAM_FORMAT_ERROR, "cron解析错误");
        }
        boolean isModifyJob = QuartzUtil.rescheduleJob(jobName, triggerName, cron);
        return Result.update_succ(isModifyJob);
    }

    /**
     * 查询所有正在运行的定时任务
     *
     * @return JobDetail
     */
    @Override
    public Result queryAllTask() {
        List<HashMap<String, String>> list = QuartzUtil.getJobs();
        return Result.query_succ(list.size(), list);
    }

    /**
     * @param cron cron表达式
     * @return 最近5次的执行时间
     */
    @Override
    public Result getRecentTriggerTime(String cron) {
        if (!QuartzUtil.checkCronExpression(cron)) {
            return Result.failed(Code.PARAM_FORMAT_ERROR, "该Cron不支持");
        }
        List<String> list = QuartzUtil.getRecentTriggerTime(cron);
        return Result.query_succ(list.size(), list);
    }

    /**
     * 定时将历史订单的数据迁移到备份数据库
     *
     * @param jobName     任务名称
     * @param triggerName 触发器名称
     * @param cron        cron表达式
     */
    @Override
    public Result migrateUsedList(String jobName, String triggerName, String cron) {
        if (!QuartzUtil.checkCronExpression(cron)) {
            return Result.failed(Code.PARAM_FORMAT_ERROR, "cron解析错误");
        }
        String description = "将" + Constant.MIGRATE_USEDLIST + "天以前的历史订单迁移到xiaoJi_bak";
        boolean isStart = QuartzUtil.addJob(jobName, triggerName, description, MigrateUsedList.class, cron);
        if (isStart) {
            return Result.succ();
        }
        return Result.failed("迁移充值订单定时任务启动失败");
    }

    /**
     * 定时将充值订单的数据迁移到备份数据库
     *
     * @param jobName     任务名称
     * @param triggerName 触发器名称
     * @param cron        cron表达式
     */
    @Override
    public Result migrateBuy(String jobName, String triggerName, String cron) {
        if (!QuartzUtil.checkCronExpression(cron)) {
            return Result.failed(Code.PARAM_FORMAT_ERROR, "cron解析错误");
        }
        String description = "将" + Constant.MIGRATE_BUY + "天以前的充值订单迁移到xiaoJi_bak";
        boolean isStart = QuartzUtil.addJob(jobName, triggerName, description, MigrateBuy.class, cron);
        if (isStart) {
            return Result.succ();
        }
        return Result.failed("迁移充值订单定时任务启动失败");
    }

    /**
     * 定时将预订单的数据迁移到备份数据库
     *
     * @param jobName     任务名称
     * @param triggerName 触发器名称
     * @param cron        cron表达式
     */
    @Override
    public Result migrateWxPay(String jobName, String triggerName, String cron) {
        if (!QuartzUtil.checkCronExpression(cron)) {
            return Result.failed(Code.PARAM_FORMAT_ERROR, "cron解析错误");
        }
        String description = "将已支付订单以及" + Constant.MIGRATE_WXPAY + "天以前的预订单迁移到xiaoJi_bak";
        boolean isStart = QuartzUtil.addJob(jobName, triggerName, description, MigrateWxPay.class, cron);
        if (isStart) {
            return Result.succ();
        }
        return Result.failed("迁移预订单定时任务启动失败");
    }
}
