package com.frost2.skeleton.service.impl;

import cn.hutool.db.Db;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import com.frost2.skeleton.common.bean.Constant;
import com.frost2.skeleton.common.util.CollectionUtil;
import com.frost2.skeleton.service.factory.MigrateDbJobFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * 数据迁移任务类，通过工厂模式生成Class，传入quartz
 *
 * @author 陈伟平
 * @date 2020-10-30 14:10:36
 * @see MigrateDbJobFactory 工厂模式生成MigrateDb实例
 */
public class MigrateDb implements Job {
    private final static Logger logger = LoggerFactory.getLogger(MigrateDb.class);

    public static HashMap<String, String> map = new HashMap<>();

    //quartz创建人物需要调用无参构造
    public MigrateDb() {
    }

    /**
     * @param originalTable 原始数据表
     * @param migrateDay    数据迁移边界
     * @param timeColumn    时间字段,根据其判断将过去多久的数据进行迁移
     * @param backupTable   备份数据表
     * @param primaryKey    原始数据表主键
     */
    public MigrateDb(String originalTable, String migrateDay, String timeColumn, String backupTable, String primaryKey) {
        MigrateDb.map.put(originalTable, originalTable + "-" + migrateDay + "-" + timeColumn + "-" + backupTable + "-" + primaryKey);
    }

    @Override
    public void execute(JobExecutionContext context) {
        map.forEach((key, value) -> {
            String[] split = value.split("-");
            migrateData(split[0], split[1], split[2], split[3], split[4]);
        });
    }

    /**
     * 将originalTable表migrateDay天前的数据迁移到backupTable
     *
     * @param originalTable 原始数据表
     * @param migrateDay    数据迁移边界
     * @param timeColumn    时间字段,根据其判断将过去多久的数据进行迁移
     * @param backupTable   备份数据表
     * @param primaryKey    原始数据表主键
     */
    private void migrateData(String originalTable, String migrateDay, String timeColumn, String backupTable, String primaryKey) {
        List<Entity> list;
        try {
            DbUtil.setCaseInsensitiveGlobal(false);

            String sql = "select * from " + originalTable + " where date_add(" + timeColumn + ", interval " + migrateDay + " day) < now()";
            list = Db.use(Constant.GICL).query(sql);

            if (CollectionUtil.isNotEmpty(list)) {
                list.forEach(entity -> entity.setTableName(backupTable));
                int[] insert = Db.use(Constant.XJBAK).insert(list);

                if (list.size() == com.frost2.skeleton.common.util.DbUtil.getEffectiveRow(insert)) {
                    logger.info("迁移表`" + originalTable + "`数据<< 数量:{}", list.size());
                    list.forEach(entity -> {
                        try {
                            Db.use(Constant.GICL).del(
                                    Entity.create(originalTable).set(primaryKey, entity.get(primaryKey))
                            );
                        } catch (SQLException e) {
                            logger.info("迁移表`" + originalTable + "`数据<< 删除数据失败:{}", e.getMessage());
                            logger.info("迁移表`" + originalTable + "`数据<< 删除数据失败:{}", entity);
                        }
                    });
                }
            } else {
                logger.info("迁移表`" + originalTable + "`数据<< 查询数据为空");
            }
        } catch (SQLException e) {
            logger.info("迁移表`" + originalTable + "`数据<< sql语句执行失败: {}", e.getMessage());
        }
    }
}
