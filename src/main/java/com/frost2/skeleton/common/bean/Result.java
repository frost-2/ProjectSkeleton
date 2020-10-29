package com.frost2.skeleton.common.bean;

import com.frost2.skeleton.common.customException.CustomException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 前后端对接规范
 *
 * @author 陈伟平
 * @date 2020-7-3 16:09:12
 */
@ToString
@Getter
@Setter
public class Result<T> {
    private long code;
    private String msg;
    private Data<T> result;


    public Result(Code code, Data<T> data) {
        this.code = code.getCode();
        this.msg = code.getMessage();
        this.result = data;
    }

    public Result(Code code) {
        this.code = code.getCode();
        this.msg = code.getMessage();
        this.result = null;
    }

    public Result(long code, String message, Data<T> data) {
        this.code = code;
        this.msg = message;
        this.result = data;
    }

    /**
     * 成功返回结果
     *
     * @param <T> 返回数据类型
     * @return Result
     */
    public static <T> Result<T> succ() {
        return new Result<>(Code.SUCCESS, null);
    }

    /**
     * 查询成功返回结果
     *
     * @param row  返回数据行数
     * @param data 返回数据集
     * @param <T>  返回数据类型
     * @return Result
     */
    public static <T> Result<T> succ(int row, T data) {
        return new Result<>(Code.SUCCESS, new Data<>(row, data));
    }

    /**
     * 查询成功返回结果
     *
     * @param row  返回数据行数
     * @param data 返回数据集
     * @param <T>  返回数据类型
     * @return Result
     */
    public static <T> Result<T> query_succ(int row, T data) {
        if (null == data || 0 == row) {
            return queryNull();
        } else {
            return new Result<>(Code.SUCCESS, new Data<>(row, data));
        }
    }

    /**
     * 添加成功返回结果
     *
     * @param row 返回数据行数
     * @param <T> 返回数据类型
     * @return Result
     */
    public static <T> Result<T> add_succ(int row) {
        if (0 == row) {
            return failed();
        } else {
            return succ();
        }
    }

    /**
     * 更新成功返回结果
     *
     * @param row 返回数据行数
     * @param <T> 返回数据类型
     * @return Result
     */
    public static <T> Result<T> update_succ(int row) {
        if (0 == row) {
            return failed(Code.UPDATE_NULL);
        } else {
            return succ();
        }
    }

    /**
     * 更新成功返回结果
     *
     * @param flag 是否更新成功
     * @param <T>  返回数据类型
     * @return Result
     */
    public static <T> Result<T> update_succ(boolean flag) {
        if (flag) {
            return succ();
        }
        return failed(Code.UPDATE_FAIL);
    }

    /**
     * 删除成功返回结果
     *
     * @param affectedRow 受影响数据行数
     * @param expectedRow 期望的数据行数
     * @param <T>         返回数据类型
     * @return Result
     */
    public static <T> Result<T> del_succ(int affectedRow, int expectedRow) {
        if (0 == affectedRow) {
            return failed(Code.DELETE_NULL);
        } else if (affectedRow != expectedRow) {
            //抛出异常:需要回滚数据
            throw new CustomException(Code.FAILED, "删除数据行数与期待不同");
        } else {
            return succ();
        }
    }

    /**
     * 删除成功返回结果
     *
     * @param flag 是否删除成功
     * @param <T>  返回数据类型
     * @return Result
     */
    public static <T> Result<T> del_succ(boolean flag) {
        if (flag) {
            return succ();
        }
        return failed(Code.UPDATE_FAIL);
    }

    /**
     * 失败返回结果
     *
     * @param <T> 返回数据类型
     * @return Result
     */
    public static <T> Result<T> failed() {
        return failed(Code.FAILED);
    }

    /**
     * 失败返回结果
     *
     * @param code Code状态码
     * @param <T>  返回数据类型
     * @return Result
     */
    public static <T> Result<T> failed(Code code) {
        return failed(code, code.getMessage());
    }

    /**
     * 失败返回结果
     *
     * @param message 错误消息
     * @param <T>     返回数据类型
     * @return Result
     */
    public static <T> Result<T> failed(String message) {
        return failed(Code.FAILED, message);
    }

    /**
     * 失败返回结果:参数message覆盖了code中的message
     *
     * @param code    Code状态码
     * @param message 错误消息
     * @param <T>     返回数据类型
     * @return Result
     */
    public static <T> Result<T> failed(Code code, String message) {
        return new Result<>(code.getCode(), message, null);
    }

    /**
     * 参数校验失败
     *
     * @param message 错误信息
     * @param <T>     返回数据类型
     * @return Result
     */
    public static <T> Result<T> paramFormatError(String message) {
        return new Result<>(Code.PARAM_FORMAT_ERROR.getCode(), message, null);
    }

    /**
     * 失败返回结果
     *
     * @param <T> 返回数据类型
     * @return Result
     */
    public static <T> Result<T> queryNull() {
        return failed(Code.QUERY_NULL);
    }

}
