package com.frost2.skeleton.common.bean;

/**
 * @author 陈伟平
 * @date 2020-5-6 15:27:58
 */
public class Constant {

    private Constant() {
    }

    //数据迁移边界
    public static int MIGRATE_USEDLIST = 90;
    public static int MIGRATE_BUY = 500;
    public static int MIGRATE_WXPAY = 30;

    //数据迁移任务名称、触发器名称
    public static final String JOB_MIGRATE_DB = "job_migrateDb";
    public static final String TRIGGER_MIGRATE_DB = "trigger_migrateDb";
    public static final String JOB_MIGRATE_WXPAY = "job_migrateWxPay";
    public static final String TRIGGER_MIGRATE_WXPAY = "trigger_migrateWxPay";

    //迁移数据表标识,用来判断迁移那种表
    public static final String WXPAY = "wxPay";

    //db.setting中数据源标志
    public static String GICL = "gicl";
    public static String PILES = "piles";
    public static String XJBAK = "xjBak";

    //盐值
    public static String salt = "X0J0K0C659A61DDBE6-8F797F01639A=E6A9689F4";
    //前台随机生成字符串
    public static final String nonceStr = "x-tit-noncestr";
    //sha1(nonceStr+salt)之后的密文
    public static final String vStr = "x-tit-vstr";
    //时间戳
    public static final String timeStamp = "x-tit-timeStamp";

}
