package com.frost2.skeleton.common.util;

/**
 * @author 陈伟平
 * @date 2020-10-13 14:28:58
 */
public class DbUtil {

    public static int getEffectiveRow(int[] insert) {
        int totalRow = 0;
        for (int row : insert) {
            totalRow += row;
        }
        return totalRow;
    }
}
