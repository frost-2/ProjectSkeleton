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

    @Override
    public void execute(JobExecutionContext context) {
        DbUtil.setCaseInsensitiveGlobal(false);

        /*
         * step1:查询出已支付和超过规定天数的预订单
         * */
        List<Entity> list = null;
        String sql = "select * from wxPay where PayStatus = 1 and date_add(Date, interval " + Constant.MIGRATE_WXPAY + " day) < now();";
        try {
            list = Db.use(Constant.GICL).query(sql);
        } catch (SQLException e) {
            logger.info("迁移预订单<< 查询语句执行失败:{}", e.getMessage());
        }

        if (CollectionUtil.isNotEmpty(list)) {
            /*
             * step2:现将数据插入xiaoJi_bak
             * */
            list.forEach(entity -> entity.setTableName("wxPay_pastDue"));
            int[] insert = new int[0];
            try {
                insert = Db.use(Constant.XJBAK).insert(list);
            } catch (SQLException e) {
                logger.info("迁移预订单<< 插入语句执行失败:{}", e.getMessage());
            }

            /*
             * step3:如果数据全部保存成功,则总原来的表中删除
             * */
            if (list.size() == com.frost2.skeleton.common.util.DbUtil.getEffectiveRow(insert)) {
                logger.info("迁移充值订单<< 数量:{}", list.size());
                list.forEach(entity -> {
                    try {
                        Db.use(Constant.GICL).del(
                                Entity.create("wxPay").set("WxPayID", entity.get("WxPayID"))
                        );
                    } catch (SQLException e) {
                        logger.info("迁移预订单<< 删除语句执行失败:{}", e.getMessage());
                        logger.info("迁移预订单<< 删除语句执行失败:{}", entity);
                    }
                });
            }

        } else {
            logger.info("迁移充值订单<< 查询数据为空");
        }
    }
}
