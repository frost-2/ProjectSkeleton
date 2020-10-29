package com.frost2.skeleton.service;


import com.frost2.skeleton.common.bean.Result;

/**
 * @author 陈伟平
 * @date 2020-10-13 14:13:58
 */
public interface IMigrateBuy {
    Result migrateBuy(String jobName, String triggerName, String cron);
}
