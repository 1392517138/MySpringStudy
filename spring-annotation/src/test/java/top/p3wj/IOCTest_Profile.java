package top.p3wj;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import top.p3wj.bean.Boss;
import top.p3wj.bean.Car;
import top.p3wj.bean.Color;
import top.p3wj.conifg.MainConfigOfAutowired;
import top.p3wj.conifg.MainConfigOfProfile;

import javax.sql.DataSource;

/**
 * @author Aaron
 * @description
 * @date 2020/5/14 11:43 AM
 */
public class IOCTest_Profile {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfProfile.class);

    //1.使用命令行动态参数
    @Test
    public void test02() {
        //1.创建一个applicationContext
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        //2.设置需要激活的环境
        applicationContext.getEnvironment().setActiveProfiles("test", "dev");
        //3.注册主配置类
        applicationContext.register(MainConfigOfProfile.class);
        applicationContext.refresh();
        printBeanNames(applicationContext);
    }

    private void printBeanNames(AnnotationConfigApplicationContext applicationContext) {
        String[] beanNamesForType = applicationContext.getBeanNamesForType(DataSource.class);
        for (String s : beanNamesForType) {
            System.out.println(s);
        }
    }

    //1.使用命令行动态参数
    @Test
    public void test01() {
        printBeanNames(applicationContext);
    }


}
