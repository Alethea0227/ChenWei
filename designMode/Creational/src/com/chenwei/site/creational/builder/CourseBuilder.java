package com.chenwei.site.creational.builder;

/**
 * @author chenwei
 * @date 2019-01-12 11:13
 **/
public class CourseBuilder extends AbstractCourseBuilder {
    private Course course = new Course();

    @Override
    Course build() {
        return course;
    }

    @Override
    void setCourseName(String name) {
        course.setCourseName(name);
    }

    @Override
    void setCourseTitle(String title) {
        course.setCourseTitle(title);
    }

    @Override
    void setCourseVideo(String videoName) {
        course.setCourseVideo(videoName);
    }
}
