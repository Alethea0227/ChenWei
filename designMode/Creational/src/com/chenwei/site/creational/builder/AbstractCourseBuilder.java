package com.chenwei.site.creational.builder;

/**
 * @author chenwei
 * @date 2019-01-12 10:46
 **/
public abstract class AbstractCourseBuilder {

    abstract Course build();

    abstract void setCourseName(String name);

    abstract void setCourseTitle(String title);

    abstract void setCourseVideo(String videoName);

}
