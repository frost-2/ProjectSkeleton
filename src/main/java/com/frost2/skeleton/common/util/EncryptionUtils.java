package com.frost2.skeleton.common.util;

import com.frost2.skeleton.common.bean.Code;
import com.frost2.skeleton.common.customException.CustomException;
import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 *
 * @author 陈伟平
 * @date 2020-4-10 15:50:56
 */
public class EncryptionUtils {

    /**
     * MD5加密
     *
     * @param param 待加密的字符串
     * @return String  加密后的字符串
     */
    public String md5(String param) {
        if (StringUtils.isBlank(param)) {
            throw new CustomException(Code.FAILED, "MD5加密参数为空");
        }
        try {
            //1.确定计算方式
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //2.加密字符串
            BASE64Encoder base64Encoder = new BASE64Encoder();
            return base64Encoder.encode(md5.digest(param.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException(Code.FAILED, "MD5加密出现异常");
        }
    }

    /**
     * SHA1加密
     *
     * @param param 加密字符串
     * @return 返回加密字符串
     */
    public static String sha1(String param) {
        if (StringUtils.isBlank(param)) {
            throw new CustomException(Code.FAILED, "SHA1加密参数为空");
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(param.getBytes(StandardCharsets.UTF_8));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            throw new CustomException(Code.FAILED, "SHA1加密出现异常");
        }
    }

}
