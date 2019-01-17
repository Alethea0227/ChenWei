package com.chenwei.site.creational.factorymethod;

/**
 * @author chenwei
 * @date 2019-01-11 16:05
 **/
public class JavaVideoFactory extends VideoFactory {
    @Override
    public Video getVideo() {
        return new JavaVideo();
    }
}
