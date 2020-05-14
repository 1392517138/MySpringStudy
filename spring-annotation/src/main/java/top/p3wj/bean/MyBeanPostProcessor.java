package top.p3wj.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author Aaron
 * @description 后置处理器：初始化前后进行处理工作
 *              将后置处理器加入到容器中
 * @date 2020/5/14 2:15 PM
 */
//@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization"+"-->>"+beanName+"-->>"+bean);
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization"+"-->>"+beanName+"-->>"+bean);
        return null;
    }
}
