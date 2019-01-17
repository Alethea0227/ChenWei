package com.chenwei.site.creational.builder;

/**
 * @author chenwei
 * @date 2019-01-12 10:47
 **/
public class Course {
    private String courseName;
    private String courseTitle;
    private String courseVideo;

    public String getCourseName() {
        return courseName;
    }

    public Course setCourseName(String courseName) {
        this.courseName = courseName;
        return this;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public Course setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
        return this;
    }

    public String getCourseVideo() {
        return courseVideo;
    }

    public Course setCourseVideo(String courseVideo) {
        this.courseVideo = courseVideo;
        return this;
    }
}
