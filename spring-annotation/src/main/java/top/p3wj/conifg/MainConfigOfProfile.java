package top.p3wj.conifg;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringValueResolver;
import top.p3wj.bean.Yellow;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * @author Aaron
 * @description Profile:
 * Spring为我们提供的可以根据当前环境，动态地激活和切换一些列组件的功能:
 * 开发环境、测试环境、生产环境：
 * 数据源：（/A）（/B）（/C）
 * @Profile 指定组件在哪个环境下才能被注册到容器中。不指定任何环境下都能注册这个组件
 * 1）、加了环境表示的bean,只有这个环境被激活的时候才能被注册到容器中，默认是default环境
 * 2) 、写在配置类上，只有是指定的环境的时候，整个配置类里面的所有配置才能开始生效
 * 3）、没有标注环境标识的bean,任何环境下都会加载
 * @date 2020/5/18 8:37 PM
 */
@Profile("prod")
@Configuration
@PropertySource("classpath:/dbconfig.properties")
public class MainConfigOfProfile implements EmbeddedValueResolverAware {

    @Value("${db.user}")
    private String user;

    private StringValueResolver stringValueResolver;

    private String driverClass;

    @Profile("test")
    @Bean
    public Yellow yellow() {
        return new Yellow();
    }

    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        //拿到这个值解析器
        this.stringValueResolver = resolver;
    }

    @Profile("default")
    @Bean("dataSource")
    public DataSource dataSource(@Value("${db.password}") String pwd) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhostL:3306/test");
        driverClass = stringValueResolver.resolveStringValue("${db.driverClass}");
        dataSource.setDriverClass(driverClass);
        return dataSource;
    }

    @Profile("test")
    @Bean("dataSourceTest")
    public DataSource dataSourceTest(@Value("${db.password}") String pwd) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhostL:3306/smarthouse");
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        return dataSource;
    }

    @Profile("dev")
    @Bean("dataSourceDev")
    public DataSource dataSourceDev(@Value("${db.password}") String pwd) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhostL:3306/kaoyan");
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        return dataSource;
    }

    @Profile("prod")
    @Bean("dataSourceProd")
    public DataSource dataSourceProd(@Value("${db.password}") String pwd) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhostL:3306/graduate");
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        return dataSource;
    }

}
