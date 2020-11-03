package com.frost2.skeleton.service.factory;

import com.frost2.skeleton.common.bean.Code;
import com.frost2.skeleton.common.bean.Constant;
import com.frost2.skeleton.common.customException.CustomException;
import com.frost2.skeleton.common.util.StringUtils;
import com.frost2.skeleton.service.impl.MigrateDb;
import com.frost2.skeleton.service.impl.MigrateWxPay;
import org.quartz.Job;

import java.util.HashMap;

/**
 * 工厂类生成{@link MigrateDb},通过build模式设置MigrateDB类需要的参数,
 * 其中所有的参数都是必须的.
 *
 * @author 陈伟平
 * @date 2020-10-30 14:28:09
 */
public class MigrateDbJobFactory {

    private static volatile MigrateDb migrateDb;    //单例
    private String originalTable;   //原始数据表
    private String migrateDay;      //数据迁移边界
    private String timeColumn;      //时间字段,根据其判断将过去多久的数据进行迁移
    private String backupTable;     //备份数据表
    private String primaryKey;      //原始数据表主键

    public Class<? extends Job> build() {

        checkParam();

        switch (originalTable) {
            case Constant.WXPAY:
                return getMigrateWxPay();
            default:
                return getMigrateDb();
        }
    }

    /**
     * 获取{@link MigrateWxPay}实例
     *
     * @return MigrateWxPay.class
     */
    private Class<? extends Job> getMigrateWxPay() {
        MigrateWxPay migrateWxPay = new MigrateWxPay(originalTable, migrateDay, timeColumn, backupTable, primaryKey);
        return migrateWxPay.getClass();
    }

    /**
     * 获取{@link MigrateDb}实例
     *
     * @return MigrateDb.class
     */
    private Class<? extends Job> getMigrateDb() {
        if (migrateDb == null) {
            synchronized (MigrateDbJobFactory.class) {
                if (migrateDb == null) {
                    migrateDb = new MigrateDb(originalTable, migrateDay, timeColumn, backupTable, primaryKey);
                }
            }
        }
        HashMap<String, String> map = MigrateDb.map;
        map.put(originalTable, originalTable + "-" + migrateDay + "-" + timeColumn + "-" + backupTable + "-" + primaryKey);
        return migrateDb.getClass();
    }

    /**
     * 校验参数是否为空,是则抛出异常
     */
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

    /**
     * 设置原始数据表表名
     *
     * @param originalTable
     * @return this
     */
    public MigrateDbJobFactory setOriginalTable(String originalTable) {
        this.originalTable = originalTable;
        return this;
    }

    /**
     * 设置数据迁移边界
     *
     * @param migrateDay
     * @return this
     */
    public MigrateDbJobFactory setMigrateDay(String migrateDay) {
        this.migrateDay = migrateDay;
        return this;
    }

    /**
     * 设置时间字段,根据其判断将过去多久的数据进行迁移
     *
     * @param timeColumn
     * @return this
     */
    public MigrateDbJobFactory setTimeColumn(String timeColumn) {
        this.timeColumn = timeColumn;
        return this;
    }

    /**
     * 设置备份数据表表名
     *
     * @param backupTable
     * @return this
     */
    public MigrateDbJobFactory setBackupTable(String backupTable) {
        this.backupTable = backupTable;
        return this;
    }

    /**
     * 设置原始数据表主键
     *
     * @param primaryKey
     * @return this
     */
    public MigrateDbJobFactory setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
        return this;
    }

}
