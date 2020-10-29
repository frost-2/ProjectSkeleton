package com.frost2.skeleton.common.bean;

/**
 * @author 陈伟平
 * @date 2020-7-3 16:17:13
 */
public enum Code {
    SUCCESS(2000, "操作成功"),
    PARAM_FORMAT_ERROR(2004, "入参格式有误"),
    QUERY_NULL(2008, "查询数据为空"),
    FAILED(2009, "程序执行出错"),
    STOCK_NOT_ENOUGH(2010, "商品库存不足"),
    ADD_NULL(3004, "添加数据失败"),
    DELETE_NULL(4004, "删除数据不存在"),
    DELETE_FAIL(4005, "删除数据失败"),
    UPDATE_FAIL(5002, "数据更新失败"),
    UPDATE_NULL(5004, "更新数据不存在"),
    UPDATE_EXISTS(5005, "添加数据已存在"),
    CHECK_FAIL(6001, "接口检验失败"),
    IDEMPOTENT_FAIL(6002, "接口幂等性校验失败");

    private int code;
    private String message;

    private Code(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
