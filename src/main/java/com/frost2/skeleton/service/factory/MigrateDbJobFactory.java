package com.frost2.skeleton.service.factory;

import cn.hutool.core.lang.Singleton;
import com.frost2.skeleton.common.bean.Code;
import com.frost2.skeleton.common.customException.CustomException;
import com.frost2.skeleton.common.util.StringUtils;
import com.frost2.skeleton.service.impl.MigrateDb;

import java.util.HashMap;

/**
 * 工厂类生成{@link MigrateDb},通过build模式设置MigrateDB类需要的参数,
 * 其中所有的参数都是必须的.
 *
 * @author 陈伟平
 * @date 2020-10-30 14:28:09
 */
public class MigrateDbJobFactory {

    private static volatile MigrateDb migrateDb;
    private String originalTable;   //原始数据表
    private String migrateDay;      //数据迁移边界
    private String timeColumn;      //时间字段,根据其判断将过去多久的数据进行迁移
    private String backupTable;     //备份数据表
    private String primaryKey;      //原始数据表主键

    public MigrateDb build() {
        checkParam();
        if (migrateDb == null) {
            synchronized (MigrateDbJobFactory.class) {
                if (migrateDb == null) {
                    migrateDb = new MigrateDb(originalTable, migrateDay, timeColumn, backupTable, primaryKey);
                }
            }
        }
        HashMap<String, String> map = MigrateDb.map;
        map.put(originalTable, originalTable + "-" + migrateDay + "-" + timeColumn + "-" + backupTable + "-" + primaryKey);
        return migrateDb;
    }

    public MigrateDbJobFactory setOriginalTable(String originalTable) {
        this.originalTable = originalTable;
        return this;
    }

    public MigrateDbJobFactory setMigrateDay(String migrateDay) {
        this.migrateDay = migrateDay;
        return this;
    }

    public MigrateDbJobFactory setTimeColumn(String timeColumn) {
        this.timeColumn = timeColumn;
        return this;
    }

    public MigrateDbJobFactory setBackupTable(String backupTable) {
        this.backupTable = backupTable;
        return this;
    }

    public MigrateDbJobFactory setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
        return this;
    }

    private void checkParam() {
        if (StringUtils.isBlank(originalTable)) {
            throw new CustomException(Code.PARAM_FORMAT_ERROR, "必须调用setOriginalTable设置参数:originalTable");
        }
        if (StringUtils.isBlank(migrateDay)) {
            throw new CustomException(Code.PARAM_FORMAT_ERROR, "必须调用setMigrateDay设置参数:migrateDay");
        }
        if (StringUtils.isBlank(timeColumn)) {
            throw new CustomException(Code.PARAM_FORMAT_ERROR, "必须调用setTimeColumn设置参数:timeColumn");
        }
        if (StringUtils.isBlank(backupTable)) {
            throw new CustomException(Code.PARAM_FORMAT_ERROR, "必须调用setBackupTable设置参数:backupTable");
        }
        if (StringUtils.isBlank(primaryKey)) {
            throw new CustomException(Code.PARAM_FORMAT_ERROR, "必须调用setPrimaryKey设置参数:primaryKey");
        }
    }

}
