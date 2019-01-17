package com.chenwei.site.creational.factorymethod;

/**
 * @author chenwei
 * @date 2019-01-11 16:06
 **/
public class PythonVideoFactory extends VideoFactory {
    @Override
    public Video getVideo() {
        return new PythonVideo();
    }
}
