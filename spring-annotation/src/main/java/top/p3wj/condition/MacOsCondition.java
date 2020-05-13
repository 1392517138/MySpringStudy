package top.p3wj.condition;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author Aaron
 * @description
 * @date 2020/5/12 6:36 PM
 */
public class MacOsCondition implements Condition {
    /**
     * ConditionContext:判断条件能使用的上下午文环境
     * AnnotatedTypeMetadata:注释信息
     * @return
     */
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //1.能获取到ioc使用的beanFactory
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        //2.获取类加载器
        ClassLoader classLoader = context.getClassLoader();
        //3.获取到环境变量
        Environment environment = context.getEnvironment();
        //4.获取bean定义的注册类。所有的bean都是在这里注册
        BeanDefinitionRegistry registry = context.getRegistry();
        //boolean pserson = registry.containsBeanDefinition("pserson");//也可判断容器中是否包含一个Bean
        System.out.println(classLoader);
        String property = environment.getProperty("os.name");
        if (property.contains("Mac")){
            return true;
        }
        return false;
    }
}
