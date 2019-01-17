package com.chenwei.site.creational.abstractFactory;

/**
 * @author chenwei
 * @date 2019-01-12 09:27
 **/
public class PythonCourseFactory implements CourseFactory {
    @Override
    public Video createVideo() {
        return new PythonVideo();
    }

    @Override
    public Article createArticle() {
        return new PythonArticle();
    }
}
