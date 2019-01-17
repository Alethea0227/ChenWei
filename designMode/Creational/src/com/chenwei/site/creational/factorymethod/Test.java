package com.chenwei.site.creational.factorymethod;

/**
 * @author chenwei
 * @date 2019-01-11 15:16
 **/
public class Test {
    public static void main(String[] args) {
        VideoFactory videoFactory = new PythonVideoFactory();
        Video video = videoFactory.getVideo();
        video.produce();
    }
}
