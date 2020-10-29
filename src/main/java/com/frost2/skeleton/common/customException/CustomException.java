package com.frost2.skeleton.common.customException;

import com.frost2.skeleton.common.bean.Code;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 陈伟平
 * @date 2020-7-3 16:40:52
 */
@ToString
@Getter
@Setter
public class CustomException extends RuntimeException {

    public CustomException(Code failed) {
        this.code = failed.getCode();
        this.msg = failed.getMessage();
    }

    public CustomException(Code code, String msg) {
        this.code = code.getCode();
        this.msg = msg;
    }

    private int code;
    private String msg;

}
