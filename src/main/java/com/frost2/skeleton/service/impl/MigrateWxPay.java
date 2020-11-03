package com.frost2.skeleton.service.impl;

import cn.hutool.db.Db;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import com.frost2.skeleton.common.bean.Constant;
import com.frost2.skeleton.common.util.CollectionUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * @author 陈伟平
 * @date 2020-10-13 17:07:12
 */
public class MigrateWxPay implements Job {
    private final static Logger logger = LoggerFactory.getLogger(MigrateWxPay.class);

    private static String originalTable;   //原始数据表
    private static String migrateDay;      //数据迁移边界
    private static String timeColumn;      //时间字段,根据其判断将过去多久的数据进行迁移
    private static String backupTable;     //备份数据表
    private static String primaryKey;      //原始数据表主键

    public MigrateWxPay() {
    }

    public MigrateWxPay(String originalTable, String migrateDay, String timeColumn, String backupTable, String primaryKey) {
        MigrateWxPay.originalTable = originalTable;
        MigrateWxPay.migrateDay = migrateDay;
        MigrateWxPay.timeColumn = timeColumn;
        MigrateWxPay.backupTable = backupTable;
        MigrateWxPay.primaryKey = primaryKey;
    }

    @Override
    public void execute(JobExecutionContext context) {
        DbUtil.setCaseInsensitiveGlobal(false);

        /*
         * step1:查询出已支付和超过规定天数的预订单
         * */
        List<Entity> list = null;
        String sql = "select * from " + originalTable + " where PayStatus = 1 and date_add(" + timeColumn + ", interval " + migrateDay + " day) < now();";
        try {
            list = Db.use(Constant.GICL).query(sql);
        } catch (SQLException e) {
            logger.info("迁移表`" + originalTable + "`数据<< 查询语句执行失败:{}", e.getMessage());
        }

        if (CollectionUtil.isNotEmpty(list)) {
            /*
             * step2:现将数据插入xiaoJi_bak
             * */
            list.forEach(entity -> entity.setTableName(backupTable));
            int[] insert = new int[0];
            try {
                insert = Db.use(Constant.XJBAK).insert(list);
            } catch (SQLException e) {
                logger.info("迁移表`" + originalTable + "`数据<< 插入语句执行失败:{}", e.getMessage());
            }

            /*
             * step3:如果数据全部保存成功,则从原来的表中删除
             * */
            if (list.size() == com.frost2.skeleton.common.util.DbUtil.getEffectiveRow(insert)) {
                logger.info("迁移表`" + originalTable + "`数据<< 数量:{}", list.size());
                list.forEach(entity -> {
                    try {
                        Db.use(Constant.GICL).del(
                                Entity.create(originalTable).set(primaryKey, entity.get(primaryKey))
                        );
                    } catch (SQLException e) {
                        logger.info("迁移表`" + originalTable + "`数据<< 删除语句执行失败:{}", e.getMessage());
                        logger.info("迁移表`" + originalTable + "`数据<< 删除语句执行失败:{}", entity);
                    }
                });
            }

        } else {
            logger.info("迁移表`" + originalTable + "`数据<< 查询数据为空");
        }
    }
}
