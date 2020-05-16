package top.p3wj;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
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
        BookService bean = applicationContext.getBean(BookService.class);
        System.out.println(bean);
        BookDao bean1 = applicationContext.getBean(BookDao.class);
        System.out.println(bean1);
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
