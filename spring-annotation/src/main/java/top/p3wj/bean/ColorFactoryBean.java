package top.p3wj.bean;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author Aaron
 * @description
 * @date 2020/5/14 12:10 AM
 */
//创建一个Spring定义的FactoryBean
public class ColorFactoryBean implements FactoryBean<Color> {
    /**
     * 返回一个Color对象，这个对象会添加到容器中
     */
    @Override
    public Color getObject() throws Exception {
        System.out.println("ColorFactoryBean....getObject....");
        return new Color();
    }
    @Override
    public Class<?> getObjectType() {
        return Color.class;
    }
    //是单例？true：这个bean是单实例,在容器中只会保存一份
    //false,多实例,每次都会创建一个新多bean
    @Override
    public boolean isSingleton() {
        return false;
    }
}
