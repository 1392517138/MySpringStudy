package top.p3wj.conifg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import top.p3wj.bean.Person;

/**
 * @author Aaron
 * @description
 * @date 2020/5/15 1:33 PM
 */
@Configuration
//使用@PropertySource读取外部配置文件中的k/v保存到运行的环境变量中
@PropertySource(value = {"classpath:/person.properties"})
public class MainConfigOfPropertyValues {
    @Bean
    public Person person(){
        return new Person();
    }
}
