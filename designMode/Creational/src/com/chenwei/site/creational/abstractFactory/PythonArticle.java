package com.chenwei.site.creational.abstractFactory;

/**
 * @author chenwei
 * @date 2019-01-12 09:26
 **/
public class PythonArticle extends Article {
    @Override
    public void produce() {
        System.out.println("Python手记");
    }
}
