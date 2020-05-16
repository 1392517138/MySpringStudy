//package top.p3wj;
//
//import org.junit.Test;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.springframework.core.env.ConfigurableEnvironment;
//import top.p3wj.bean.Person;
//import top.p3wj.conifg.MainConfig;
//import top.p3wj.conifg.MainConfig2;
//
//import java.util.Map;
//
///**
// * @author Aaron
// * @description
// * @date 2020/5/12 1:00 PM
// */
//
//public class IOCTest {
//    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
//    @Test
//    public void testImport() {
//        printBeans(applicationContext);
//    }
//
//
//    public void printBeans(AnnotationConfigApplicationContext applicationContext) {
////        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
////        for (String beanDefinitionName : beanDefinitionNames) {
////            System.out.println(beanDefinitionName);
////        }
//        //工厂获取的是调用getObject创建的对象
//        Object colorFactoryBean = applicationContext.getBean("colorFactoryBean");
//        Object colorFactoryBean1 = applicationContext.getBean("colorFactoryBean");
//        System.out.println(colorFactoryBean.getClass());
//        System.out.println(colorFactoryBean == colorFactoryBean1);
//        //若就想获取ColorFactoryBean，则前面加上&
//        Object colorFactoryBean2 = applicationContext.getBean("&colorFactoryBean");
//        System.out.println("----"+colorFactoryBean2.getClass());
//    }
//
//    @Test
//    public void test03() {
//        String[] beanNamesForType = applicationContext.getBeanNamesForType(Person.class);
//        //动态获取环境变量的值Mac OS X
//        ConfigurableEnvironment environment = applicationContext.getEnvironment();
////        String property = environment.getProperty("os.name"); //也可以获得Mac OS X，getProperty在PropertyResolver接口中
//        Map<String, Object> systemProperties = environment.getSystemProperties();//impl->ConfigurablePropertyResolver->ropertyResolver
//        System.out.println(systemProperties.get("os.name"));//获得操作系统的名字
////        for (String s : beanNamesForType) {
////            System.out.println(s);
////        }
////        Map<String, Person> beansOfType = applicationContext.getBeansOfType(Person.class);
////        System.out.println(beansOfType);
//    }
//
//
//    @Test
//    public void test02() {
//        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
//        //默认是单实例的，即无论多少次获取，获取到到都是MainConfig2中new的那一个对象
//        System.out.println("ioc容器创建完成!");
//        Object person = applicationContext.getBean("person");
////        Object person2 = applicationContext.getBean("person");
////        System.out.println(person == person2);
//    }
//
//    @Test
//    public void test01() {
//        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
//        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
//        for (String beanDefinitionName : beanDefinitionNames) {
//            System.out.println(beanDefinitionName);
//        }
//    }
//
//
//}
