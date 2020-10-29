package com.frost2.skeleton.common.interceptor;

import com.frost2.skeleton.common.bean.Code;
import com.frost2.skeleton.common.bean.Constant;
import com.frost2.skeleton.common.customException.CustomException;
import com.frost2.skeleton.common.util.EncryptionUtils;
import com.frost2.skeleton.common.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 接口检验拦截器
 *
 * @author 陈伟平
 * @date 2020-04-14-上午 11:48
 */
@Component
public class CheckInterfaceInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String randomStr = request.getHeader(Constant.nonceStr);
        String vStr = request.getHeader(Constant.vStr);
        if (StringUtils.isBlank(randomStr) || StringUtils.isBlank(vStr)) {
            throw new CustomException(Code.PARAM_FORMAT_ERROR, "检验请求头为空");
        }
        if (EncryptionUtils.sha1(randomStr + Constant.salt).equalsIgnoreCase(vStr)) {
            return true;
        } else {
            throw new CustomException(Code.CHECK_FAIL);
        }
    }
}
