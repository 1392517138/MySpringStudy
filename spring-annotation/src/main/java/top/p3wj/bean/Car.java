package top.p3wj.bean;

import org.springframework.stereotype.Component;

/**
 * @author Aaron
 * @description
 * @date 2020/5/14 11:02 AM
 */

public class Car {
    public Car(){
        System.out.println("car constructor...");
    }

    public void init(){
        System.out.println("car...init...");
    }

    public void destroy(){
        System.out.println("car...destroy...");
    }
}
