package com.chenwei.site.creational.abstractFactory;

/**
 * @author chenwei
 * @date 2019-01-12 09:23
 **/
public class JavaCourseFactory implements CourseFactory {
    @Override
    public Video createVideo() {
        return new JavaVideo();
    }

    @Override
    public Article createArticle() {
        return new JavaArticle();
    }
}
