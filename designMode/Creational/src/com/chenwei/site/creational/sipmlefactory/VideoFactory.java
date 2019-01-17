package com.chenwei.site.creational.sipmlefactory;

/**
 * @author chenwei
 * @date 2019-01-11 15:16
 **/
public class VideoFactory {
    /*public Video getVideo(String type) {
        if ("java".equalsIgnoreCase(type)) {
            return new JavaVideo();
        } else if ("python".equalsIgnoreCase(type)) {
            return new PythonVideo();
        } else {
            return null;
        }
    }*/
    public Video getVideo(Class clazz) {
        Video video = null;
        try {
            video = (Video) Class.forName(clazz.getName()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return video;
    }
}
