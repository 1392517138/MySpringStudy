package top.p3wj.conifg;

import org.springframework.context.annotation.*;
import top.p3wj.bean.Color;
import top.p3wj.bean.ColorFactoryBean;
import top.p3wj.bean.Person;
import top.p3wj.bean.Red;
import top.p3wj.condition.LinuxCondition;
import top.p3wj.condition.MacOsCondition;
import top.p3wj.condition.MyImportBeanDefinitionRegistrar;
import top.p3wj.condition.MyImportSelector;

/**
 * @author Aaron
 * @description
 * @date 2020/5/12 4:03 PM
 */
@Configuration
//@Conditional({MacOsCondition.class})
@Import({Color.class,Red.class,MyImportSelector.class,MyImportBeanDefinitionRegistrar.class})
public class MainConfig2 {

    /**
     * ConfigurableBeanFactory#SCOPE_PROTOTYPE  prototype   多实例
     * ConfigurableBeanFactory#SCOPE_SINGLETON  singleton   单实例(默认值)
     * org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST  request 同一次请求创建一个实例
     * org.springframework.web.context.WebApplicationContext#SCOPE_SESSION  session 同一个session创建一个实例
     * @return
     */
    @Scope
    @Bean("person")
    @Lazy
    public Person person(){
        System.out.println("给容器中添加Person........");
        return new Person("张三",40);
    }

    /**
     * @Conditional ,按照一定的条件进行判断，满足条件给容器中注册Bean
     * 如果是MacOs,给容器注册 jobs
     * 如果是linux，给容器注册linus
     */
    @Conditional({MacOsCondition.class})
    @Bean("jobs")
    public Person person01(){
        return new Person("jobs",47);
    }

    @Conditional({LinuxCondition.class})
    @Bean("linux")
    public Person person02(){
        return new Person("linux",50);
    }

    /**
     * 给容器中注册组件：
     * 1) 包扫描+组件标注注解 (@Controller/@Service/@Repository/@Component)[自己写的]
     * 2) @Bean[导入的第三方包里面的组件],但是它比较麻烦（需要return等）
     * 3) @Import[快速给容器导入一个组件]导入组件
     *              1)  id默认是组件的全类名,@Import(要导入到容器中到组件)，容器中就会自动注册这个组件，id默认是全类名
     *              2)  ImportSelector：返回需要导入的组件的全数组
     *              3)  ImportBeanDefinitionRegistrar 手动注册Bean
     * 4) 使用Spring提供的FactoryBean(工厂Bean)
     *              1)  默认获取到到是工厂bean调用getObject创建的对象
     *              2)  要获取工厂Bean本身，我们需要给id前面加一个&
     *                  &colorFactoryBean
     */
    @Bean
    public ColorFactoryBean colorFactoryBean(){
        return new ColorFactoryBean();
    }
}
