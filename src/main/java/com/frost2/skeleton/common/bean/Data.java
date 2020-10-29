package com.frost2.skeleton.common.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 后端接口返回数据
 *
 * @author 陈伟平
 * @date 2020-7-3 18:50:58
 */
@Getter
@Setter
public class Data<T> {

    private int row;
    private T date;

    public Data(int row, T date) {
        this.row = row;
        this.date = date;
    }
}
