package com.frost2.skeleton.service.impl;

import com.frost2.skeleton.common.bean.Code;
import com.frost2.skeleton.common.bean.Constant;
import com.frost2.skeleton.common.bean.Result;
import com.frost2.skeleton.common.util.QuartzUtil;
import com.frost2.skeleton.service.*;
import com.frost2.skeleton.service.factory.MigrateDbJobFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author 陈伟平
 * @date 2020-9-19 09:24:07
 */
@Service
public class TimedTaskServiceImpl implements ITimedTaskService, IMigrateUsedList, IMigrateBuy, IMigrateWxPay, IMigrateDb {

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

    /**
     * 上面三个迁移数据操作，其实大同小异，将他们对应的定时任务类抽象出来形成{@link MigrateDb},将不同的地方通过参数传递到MigrateDb,
     * 每次创建定时任务时，只需要设置参数，生成对应的MigrateDb实例即可。
     * <p>
     * 此处采用工厂模式+build模式，生成MigrateDb实例。这里其实工厂模式体现的不明显，因为只有一个类MigrateDb，
     * 如果后续的迁移逻辑变复杂，他的sql不像MigrateDb中那么简单时，就会有类似于MigrateDb的其他类，此时{@link MigrateDbJobFactory}中的build方法将会明显的体现出工厂模式。
     * <p>
     *
     * @problem 这种方式存在很大的问题，每次创建的任务都包括之前所有的任务，并且重复执行，因为保存任务信息的map的static的，还需要解决。
     * @update 2020-11-2 解决问题problem
     * @update 2020-11-2 增加数据迁移实例,修改工厂类
     *
     * @param cron          cron表达式
     * @param originalTable 原始数据表
     * @param migrateDay    数据迁移边界
     * @param timeColumn    时间字段,根据其判断将过去多久的数据进行迁移
     * @param backupTable   备份数据表
     * @param primaryKey    原始数据表主键
     * @see MigrateDb 数据迁移
     * @see MigrateDbJobFactory 数据迁移工厂类
     */
    @Override
    public Result migrateDb(String cron, String originalTable, String migrateDay, String timeColumn, String backupTable, String primaryKey) {
        if (!QuartzUtil.checkCronExpression(cron)) {
            return Result.failed(Code.PARAM_FORMAT_ERROR, "cron解析错误");
        }
        String description = "将表[" + originalTable + "]" + migrateDay + "天以前的数据迁移到[" + backupTable + "]";

        MigrateDb migrateDb = new MigrateDbJobFactory()
                .setOriginalTable(originalTable)
                .setBackupTable(backupTable)
                .setMigrateDay(migrateDay)
                .setTimeColumn(timeColumn)
                .setPrimaryKey(primaryKey)
                .build();
        //对注释中@problem的解决办法
        if (MigrateDb.map.size() == 1) {
            boolean isStart = QuartzUtil.addJob(Constant.JOB_NAME, Constant.TRIGGER_NAME, description, migrateDb.getClass(), cron);
            if (isStart) {
                return Result.succ();
            }
            return Result.failed("迁移预订单定时任务启动失败");
        } else {
            return Result.succ();
        }
    }

}
