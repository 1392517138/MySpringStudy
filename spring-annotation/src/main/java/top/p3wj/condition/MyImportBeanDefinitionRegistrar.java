package top.p3wj.condition;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import top.p3wj.bean.RainBow;

/**
 * @author Aaron
 * @description
 * @date 2020/5/13 9:15 PM
 */
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    /**
     *
     * importingClassMetadata: 当前类的注解信息
     * BeanDefinitionRegistry: BeanDefinition注册类
     *             把所有需要添加到容器中的bean;调用
     *             BeanDefinitionRegistry.registerBeanDefinition 手动注册
     *
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //判断容器中是否有红色跟蓝色、
        //注意，import方式注入的名称为全类名,以下方式不能注入
//        boolean red = registry.containsBeanDefinition("red");
        boolean red = registry.containsBeanDefinition("top.p3wj.bean.Red");
        boolean blue = registry.containsBeanDefinition("top.p3wj.bean.Blue");
        if (red && blue){
            //指定Bean定义信息，（Bean的类型，Bean...）
            RootBeanDefinition BeanDefinition = new RootBeanDefinition(RainBow.class);
            //注册一个Bean,指定Bean名
            registry.registerBeanDefinition("rainBow",BeanDefinition);
        }
    }
}
