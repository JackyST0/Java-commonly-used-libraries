package com.example.testspringboot.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2025/1/9 17:18
 */
public class ContextUtil {
    public ContextUtil() {
    }

    /**
     * 获取当前的HttpServletRequest对象
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs == null ? null : attrs.getRequest();
    }

    /**
     * 获取当前的HttpServletResponse对象
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attrs = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        return attrs == null ? null : attrs.getResponse();
    }

    /**
     * 获取当前的HttpSession对象
     */
    public static HttpSession getSession() {
        ServletRequestAttributes attrs = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        return attrs == null ? null : attrs.getRequest().getSession(false);
    }
}
