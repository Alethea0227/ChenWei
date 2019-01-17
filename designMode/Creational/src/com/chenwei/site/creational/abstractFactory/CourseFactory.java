package com.chenwei.site.creational.abstractFactory;

/**
 * @author chenwei
 * @date 2019-01-12 09:22
 **/
public interface CourseFactory {
    Video createVideo();

    Article createArticle();
}
