package top.p3wj;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import top.p3wj.bean.Boss;
import top.p3wj.bean.Car;
import top.p3wj.bean.Color;
import top.p3wj.bean.Person;
import top.p3wj.conifg.MainConfigOfAutowired;
import top.p3wj.conifg.MainConfigOfPropertyValues;
import top.p3wj.dao.BookDao;
import top.p3wj.service.BookService;

/**
 * @author Aaron
 * @description
 * @date 2020/5/14 11:43 AM
 */
public class IOCTest_Autowired {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfAutowired.class);

    
    @Test
    public void test01(){
        Boss bean = applicationContext.getBean(Boss.class);
        Car bean1 = applicationContext.getBean(Car.class);
        Color bean2 = applicationContext.getBean(Color.class);
        System.out.println(bean);
        System.out.println(bean1);
        System.out.println(bean2);
//        BookService bean = applicationContext.getBean(BookService.class);
//        System.out.println(bean);
//        BookDao bean1 = applicationContext.getBean(BookDao.class);
//        System.out.println(bean1);
//        BookDao bean2 = (BookDao)applicationContext.getBean("bookDao");
//        BookDao bean3 = (BookDao)applicationContext.getBean("bookDao2");
//        System.out.println(bean2);
//        System.out.println(bean3);

    }
    public void printBeans(ApplicationContext applicationContext) {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }
}
