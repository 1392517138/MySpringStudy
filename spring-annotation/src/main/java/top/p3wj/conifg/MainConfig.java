package top.p3wj.conifg;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import top.p3wj.bean.Person;
import top.p3wj.service.BookService;

/**
 * @author Aaron
 * @description 配置类等同于配置文件
 * @date 2020/5/11 10:07 PM
 */
//配置类==配置文件
@Configuration //告诉spring这是一个配置类

@ComponentScans(
        value = {
                @ComponentScan(value = "top.p3wj", includeFilters = {
//                        @ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class,Service.class}),
//                        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = BookService.class),
                        @ComponentScan.Filter(type = FilterType.CUSTOM,classes = MyTypeFilter.class)
                },useDefaultFilters = false)//指定要扫描的包
        }
)


//excludeFilters = Filter[] 指定扫描的时候按照规则排除哪些规则
//includeFilters = Filter[] 指定扫描的时候只需要包含哪些组件
public class MainConfig {
    //给容器中注册一个Bean；类型为返回值类型；id默认是方法名作为id
    @Bean("person02")
    public Person person() {
        return new Person("lisi", 20);
    }
}
