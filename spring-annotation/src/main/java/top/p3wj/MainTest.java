package top.p3wj;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import top.p3wj.bean.Person;
import top.p3wj.conifg.MainConfig;

/**
 * @author Aaron
 * @description
 * @date 2020/5/11 10:02 PM
 */
public class MainTest {
    public static void main(String[] args) {
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
//        //可通过"id"获取
//        Person person = (Person)applicationContext.getBean("person");
//        System.out.println(person);
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        //可通过类型来获取
//        Person person1 = (Person)applicationContext.getBean(Person.class);
//        Person person2 = (Person) applicationContext.getBean("person");
//        System.out.println(person1);
//        System.out.println(person2);

//        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
//        for (String beanDefinitionName : beanDefinitionNames) {
//            System.out.println(beanDefinitionName);
//        }
        String[] beanNamesForType = applicationContext.getBeanNamesForType(Person.class);
        for (String s : beanNamesForType) {
            System.out.println(s);
        }

    }
}
