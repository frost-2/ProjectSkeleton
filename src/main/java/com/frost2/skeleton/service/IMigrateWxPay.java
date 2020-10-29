package com.frost2.skeleton.service;

import com.frost2.skeleton.common.bean.Result;

/**
 * @author 陈伟平
 * @date 2020-10-13 17:43:25
 */
public interface IMigrateWxPay {
    Result migrateWxPay(String jobName, String triggerName, String cron);
}
