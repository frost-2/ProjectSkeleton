package com.frost2.skeleton.service;

import com.frost2.skeleton.common.bean.Result;

/**
 * @author 陈伟平
 * @date 2020-9-19 09:34:54
 */
public interface ITimedTaskService {

    Result queryTimedTask(String jobName, String triggerName);

    Result delTimedTask(String jobName, String triggerName);

    Result queryAllTask();

    Result getRecentTriggerTime(String cron);

    Result rescheduleJob(String jobName, String triggerName, String cron);
}
