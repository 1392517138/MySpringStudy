package top.p3wj;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import top.p3wj.bean.Person;
import top.p3wj.conifg.MainConfigOfLifeCycle;
import top.p3wj.conifg.MainConfigOfPropertyValues;

/**
 * @author Aaron
 * @description
 * @date 2020/5/14 11:43 AM
 */
public class IOCTest_PropertyValue {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfPropertyValues.class);
    ApplicationContext applicationContext2 = new ClassPathXmlApplicationContext("beans.xml");

    @Test
    public void test03(){
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String property = environment.getProperty("person.nickName");
        System.out.println(property);
    }
    @Test
    public void test02(){
        System.out.println("=======");
        Person person = (Person)applicationContext2.getBean("person");
        System.out.println(person);
        applicationContext.close();
    }
    @Test
    public void test01(){
        printBeans(applicationContext);
        System.out.println("=======");
        Person person = (Person)applicationContext.getBean("person");
        System.out.println(person);
        applicationContext.close();
    }
    public void printBeans(ApplicationContext applicationContext) {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }
}
