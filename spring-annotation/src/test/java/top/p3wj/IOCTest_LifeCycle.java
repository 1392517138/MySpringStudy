package top.p3wj;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import top.p3wj.bean.Car;
import top.p3wj.bean.Dog;
import top.p3wj.conifg.MainConfigOfLifeCycle;

/**
 * @author Aaron
 * @description
 * @date 2020/5/14 11:43 AM
 */
public class IOCTest_LifeCycle {
    @Test
    public void test01(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfLifeCycle.class);
        System.out.println("容器创建完成....");
//        Dog dog = (Dog)applicationContext.getBean("dog");
        applicationContext.close();
    }
}
