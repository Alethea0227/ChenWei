package com.chenwei.site.principle.openclose;

/**
 * @author chenwei
 * @date 2019-01-11 11:56
 **/
public class Test {
    public static void main(String[] args) {
        JavaDiscountCourse discountCourse = new JavaDiscountCourse("50", "Test", 366d);
        System.out.println("课程名称：" + discountCourse.getName() + "课程ID：" + discountCourse.getId() + "课程原价：" + discountCourse.getDiscountPrice() + "课程打折价格：" + discountCourse.getPrice());
    }
}
