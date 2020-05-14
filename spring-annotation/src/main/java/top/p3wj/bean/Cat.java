package top.p3wj.bean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author Aaron
 * @description
 * @date 2020/5/14 12:29 PM
 */
//@Component
public class Cat implements InitializingBean,DisposableBean {
    public Cat(){
        System.out.println("cat constructor...");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("cat...afterPropertiesSet...");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("cat...destroy...");
    }
}
