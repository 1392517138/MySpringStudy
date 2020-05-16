package top.p3wj.conifg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import top.p3wj.dao.BookDao;

/**
 * @author Aaron
 * @description 自动装配：
 *      Spring利用以来注入(DI)，完成对IOC容器中中各个组件对依赖关系赋值
 *      1）@Autowired：自动注入
 *          1）、默认优先按照类型去找容器中的组件applicationContext.getBean(BookDao.class)找到就赋值
 *          2）、如果找到多个相同类型的组件，再将属性的名称作为组件的id去容器中查找
 *                                      applicationContext.getBean("bookDao")
 *          BookService{
 *              @Autowired
 *              BookDao bookDao;
 *          }
 *          3）、@Qualifier("bookDao"):使用@Qualifier指定需要装配的组件id，而不是使用属性名
 *          4) 、自动装配默认一定要将属性赋值好，没有就会报错 可以使用@Autowired(required=false)
 *          5) 、@Primary：让spring进行自动装配的时候，默认使用首选的bean
 * @date 2020/5/15 2:34 PM
 */
@Configuration
@ComponentScan({"top.p3wj.service","top.p3wj.dao","top.p3wj.controller"})
public class MainConfigOfAutowired {

    @Primary //有可能BookDao类型的在容器中有多个，但每次自动装配首选装配它
    @Bean("bookDao2")
    public BookDao bookDao(){
        BookDao bookDao = new BookDao();
        bookDao.setLabel("2");
        return bookDao;
    }
}
