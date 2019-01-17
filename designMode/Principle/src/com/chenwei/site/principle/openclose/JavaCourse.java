package com.chenwei.site.principle.openclose;

/**
 * @author chenwei
 * @date 2019-01-11 11:55
 **/
public class JavaCourse implements Course {
    private String id;
    private String name;
    private Double price;

    public JavaCourse(String id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Double getPrice() {
        return this.price;
    }
}
