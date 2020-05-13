package top.p3wj.bean;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author Aaron
 * @description
 * @date 2020/5/14 12:10 AM
 */
//创建一个Spring定义的FactoryBean
public class ColorFactoryBean implements FactoryBean<Color> {
    @Override
    public Color getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
