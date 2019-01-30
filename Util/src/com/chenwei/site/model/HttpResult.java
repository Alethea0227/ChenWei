package com.chenwei.site.model;

import lombok.Data;

/**
 * @author chenwei
 * @date 2019-01-28 18:17
 **/
@Data
public class HttpResult {
    private int statusCode;
    private String resultStr;

    public HttpResult(int statusCode, String resultStr) {
        this.statusCode = statusCode;
        this.resultStr = resultStr;
    }
}
