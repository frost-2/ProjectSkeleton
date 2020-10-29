package com.frost2.skeleton.service;

import com.frost2.skeleton.common.bean.Result;

/**
 * @author 陈伟平
 * @date 2020-9-19 15:34:56
 */
public interface IMigrateUsedList {

    Result migrateUsedList(String jobName, String triggerName, String cron);
}
