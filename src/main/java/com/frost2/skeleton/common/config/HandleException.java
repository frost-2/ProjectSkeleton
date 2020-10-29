package com.frost2.skeleton.common.config;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.frost2.skeleton.common.bean.Result;
import com.frost2.skeleton.common.customException.CustomException;
//import org.apache.ibatis.reflection.ReflectionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLSyntaxErrorException;

/**
 * 全局异常处理机制
 *
 * @author 陈伟平
 * @date 2020-7-6 09:28:59
 */
@RestControllerAdvice
public class HandleException {

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        return Result.failed();
    }

    //自定义异常
    @ExceptionHandler(CustomException.class)
    public Result handleException(CustomException e) {
        return Result.paramFormatError(e.getMsg());
    }

    //validator当没有通过校验是报org.springframework.validation.BindException
    @ExceptionHandler(BindException.class)
    public Result handleException(BindException e) {
        return Result.paramFormatError(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result handleException(MethodArgumentTypeMismatchException e) {
        return Result.paramFormatError("参数`" + e.getName() + "`类型转换错误:" + e.getCause());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleException(MethodArgumentNotValidException e) {
        return Result.paramFormatError(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    //请求方式不正确
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleException(HttpRequestMethodNotSupportedException e) {
        return Result.paramFormatError(e.getMessage());
    }

    //SQL 语法异常
    @ExceptionHandler(SQLSyntaxErrorException.class)
    public Result handleException(SQLSyntaxErrorException e) {
        return Result.failed(e.getMessage());
    }

//    @ExceptionHandler(ReflectionException.class)
//    public Result handleException(ReflectionException e) {
//        return Result.failed(e.getMessage());
//    }

    @ExceptionHandler(InvalidFormatException.class)
    public Result handleException(InvalidFormatException e) {
        return Result.failed(e.getOriginalMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleException(HttpMessageNotReadableException e) {
        return Result.paramFormatError(e.getCause().getMessage());
    }

}
