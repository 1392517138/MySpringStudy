package top.p3wj.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Aaron
 * @description
 * @date 2020/5/17 8:10 PM
 */
//默认加载ioc容器中的组件，容器启动会调用无参构造器创建对象，再进行初始化赋值等操作
@Component
public class Boss {
    private Car car;

    @Override
    public String toString() {
        return "Boss{" +
                "car=" + car +
                '}';
    }

    public Car getCar() {
        return car;
    }

    //    @Autowired
    //标注方法，Spring容器创建当前对象，就会调用方法，完成赋值
    //方法使用的参数，自定义类型的值从ioc容器中获取
    public void setCar(Car car) {
        this.car = car;
    }

    //构造器要用的组件、都是从容器中获取
//    @Autowired
    public Boss(/*@Autowired*/ Car car) {
        System.out.println("这是Boss等有参构造器");
        this.car = car;
    }
}
