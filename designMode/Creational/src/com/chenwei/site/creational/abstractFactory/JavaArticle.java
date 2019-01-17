package com.chenwei.site.creational.abstractFactory;

/**
 * @author chenwei
 * @date 2019-01-12 09:24
 **/
public class JavaArticle extends Article {
    @Override
    public void produce() {
        System.out.println("Java手记");
    }
}
