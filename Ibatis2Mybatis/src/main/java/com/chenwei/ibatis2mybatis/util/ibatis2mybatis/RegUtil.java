package com.chenwei.ibatis2mybatis.util.ibatis2mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenwei
 * @date 2018/11/9 10:55 AM
 **/
public class RegUtil {


    /**
     * * 正则表达式匹配两个指定字符串中间的内容
     * * @param soap
     * * @return
     */
    public static List<String> getSubUtil(String soap, String rgex) {
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(rgex);
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            int i = 1;
            list.add(m.group(i));
            i++;
        }
        return list;
    }
}
