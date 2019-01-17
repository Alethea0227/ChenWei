package com.chenwei.site.principle.openclose;

/**
 * @author chenwei
 * @date 2019-01-11 11:56
 **/
public class JavaDiscountCourse extends JavaCourse {

    public JavaDiscountCourse(String id, String name, Double price) {
        super(id, name, price);
    }

    public Double getDiscountPrice() {
        return super.getPrice() * 0.8;
    }
}
