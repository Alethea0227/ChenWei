package com.chenwei.site.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * JPA分页工具类，获取Pageable对象等
 *
 * @author chenwei
 * @date 2019-01-07 15:29
 **/
public class PageUtil {
    public static Pageable create(int pageNum, int pageSize) {
        return PageRequest.of(pageNum, pageSize);
    }

    public static Pageable create(int pageNum, int pageSize, Sort.Direction sort) {
        return PageRequest.of(pageNum, pageSize, sort);
    }

    public static Pageable create(int pageNum, int pageSize, Sort.Direction sort, String... sortProperties) {
        return PageRequest.of(pageNum, pageSize, sort, sortProperties);
    }
}
