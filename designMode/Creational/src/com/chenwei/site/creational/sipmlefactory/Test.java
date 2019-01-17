package com.chenwei.site.creational.sipmlefactory;

/**
 * @author chenwei
 * @date 2019-01-11 15:16
 **/
public class Test {
    public static void main(String[] args) {
        VideoFactory videoFactory = new VideoFactory();
        Video video = videoFactory.getVideo(JavaVideo.class);
        if (video == null) {
            return;
        }
        video.produce();
    }
}
