package com.chenwei.site.creational.abstractFactory;

/**
 * @author chenwei
 * @date 2019-01-12 09:25
 **/
public class Test {
    public static void main(String[] args) {
        CourseFactory courseFactory = new JavaCourseFactory();
        Article article = courseFactory.createArticle();
        Video video = courseFactory.createVideo();
        article.produce();
        video.produce();
    }
}
