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
 * @date 2020-10-13-下午 2:15
 */
public class MigrateBuy implements Job {
    private final static Logger logger = LoggerFactory.getLogger(MigrateBuy.class);

    @Override
    public void execute(JobExecutionContext context) {
        List<Entity> list;
        try {
            DbUtil.setCaseInsensitiveGlobal(false);

            String sql = "select * from buy where date_add(Date, interval " + Constant.MIGRATE_BUY + " day) < now()";
            list = Db.use(Constant.GICL).query(sql);

            if (CollectionUtil.isNotEmpty(list)) {
                list.forEach(entity -> entity.setTableName("buy_pastDue"));
                int[] insert = Db.use(Constant.XJBAK).insert(list);

                if (list.size() == com.frost2.skeleton.common.util.DbUtil.getEffectiveRow(insert)) {
                    logger.info("迁移充值订单<< 数量:{}", list.size());
                    list.forEach(entity -> {
                        try {
                            Db.use(Constant.GICL).del(
                                    Entity.create("buy").set("buyID", entity.get("buyID"))
                            );
                        } catch (SQLException e) {
                            logger.info("迁移充值订单<< 删除数据失败:{}", e.getMessage());
                            logger.info("迁移充值订单<< 删除数据失败:{}", entity);
                        }
                    });
                }
            } else {
                logger.info("迁移充值订单<< 查询数据为空");
            }
        } catch (SQLException e) {
            logger.info("迁移充值订单<< sql语句执行失败: {}", e.getMessage());
        }
    }
}
