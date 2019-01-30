package com.chenwei.site.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author chenwei
 * @date 2018-12-14 16:08
 **/
@Component
@Slf4j
public class SessionUtil {

    public static HttpSession getSession() {
        try {
            return getRequest().getSession();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HttpServletRequest getRequest() {
        try {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return servletRequestAttributes.getRequest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HttpServletResponse getResponse() {
        try {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return servletRequestAttributes.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setSessionValue(String key, Object value) {
        try {
            getSession().setAttribute(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getSessionValue(String key) {
        try {
            return getSession().getAttribute(key);
        } catch (Exception e) {
            return null;
        }
    }

    public static void invalidate() {
        try {
            getSession().invalidate();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("session注销失败");
        }
    }
}
