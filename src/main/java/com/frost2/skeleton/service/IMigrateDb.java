package com.frost2.skeleton.service;

import com.frost2.skeleton.common.bean.Result;

/**
 * @author 陈伟平
 * @date 2020-10-30 15:07:40
 */
public interface IMigrateDb {
    Result migrateDb(String cron,
                     String originalTable,
                     String migrateDay,
                     String timeColumn,
                     String backupTable,
                     String primaryKey);
}
