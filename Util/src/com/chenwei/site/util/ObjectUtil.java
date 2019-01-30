package com.chenwei.site.util;


import com.chenwei.site.model.ObjectType;
import org.springframework.util.ObjectUtils;

import java.util.Date;

/**
 * 对象工具类，通过此工具类可判断对象类型及非空等
 *
 * @author chenwei
 * @date 2019-01-07 18:34
 **/
public class ObjectUtil {

    /**
     * 获取对象类型
     * @param param
     * @return
     */
    public static String getObjectType(Object param) {
        String type = "";
        if (param instanceof Integer) {
            type = ObjectType.INT;
        } else if (param instanceof String) {
            type = ObjectType.STRING;
        } else if (param instanceof Double) {
            type = ObjectType.DOUBLE;
        } else if (param instanceof Float) {
            type = ObjectType.FLOAT;
        } else if (param instanceof Long) {
            type = ObjectType.LONG;
        } else if (param instanceof Boolean) {
            type = ObjectType.BOOLEAN;
        } else if (param instanceof Date) {
            type = ObjectType.DATE;
        } else {
            type = ObjectType.UNKNOWN;
        }
        return type;
    }
}
