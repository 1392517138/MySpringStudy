package top.p3wj.bean;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
/**
 * @author Aaron
 * @description
 * @date 2020/5/14 2:05 PM
 */
@Component
public class Dog implements ApplicationContextAware {
    ApplicationContext applicationContext;
//    可以获得IOC容器，这样在其他方法中就可以使用了。这个功能是ApplicationContextAwareProcessor做的
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

    }
    public Dog(){
        System.out.println("dog constructor...");
    }
    //对象创建并赋值后调用
    @PostConstruct
    public void init(){
        System.out.println("dog...@PostConstruct...");
    }
    //容器移除对象之前
    @PreDestroy
    public void destroy(){
        System.out.println("dog...@PreDestroy...");
    }
}
