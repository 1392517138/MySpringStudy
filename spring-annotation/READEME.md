### 01 spring-annotation



![image-20200511205219602](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200511205219602.png)

# I、组件注册

#### 一、导入spring-context

```java
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>4.3.12.RELEASE</version>
</dependency>
```

1. 如果创建beans.xml没有如下内容，则为没有添加spring支持

![image-20200511215300998](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200511215300998.png)

2. 则开启

![image-20200511215403953](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200511215403953.png)

3.

![image-20200511215507250](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200511215507250.png)

#### 二、实操。Configuration、Bean、ComponentScan(s)、TypeFilter

1. 创建一个Person类

![image-20200511215948782](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200511215948782.png)

2. 配置beans.xml 

   1. 给一个id方便从容器中获取

   2. 可以通过property作为一个属性的赋值

      ![image-20200511220207948](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200511220207948.png)

      **这是以前的一个配置文件**

3. 开始使用，写一个测试类

   1. 通过ClassPathXmlApplicationContext,表示类路径下的一个xml配置文件。**会返回IOC容器**
   2. 可通过getBean加上“id”进行获取。或是类型

   ![image-20200511220652858](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200511220652858.png)

4. 以前配置文件的方式被替换为了配置类

   1. 建立一个config.MainConfig

   ![image-20200511221104414](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200511221104414.png)

   2. 回到MainTest,通过AnnotationConfigApplicationContext注解式的config,它传入的就是这个配置类。相当于是穿配置类的位置。

![image-20200511221947155](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200511221947155.png)

		3.	通过getBeanDefinitionNames可获得Bean容器中组件的所有名称

![image-20200511222718056](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200511222718056.png)

4. 也可通过getBeanNamesForType

![image-20200511222836873](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200511222836873.png)

5. 通过上面的这个方法，我也可改变组键名称。要么改方法名，要么采用下面这种方式

![image-20200511223329264](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200511223329264.png)

6. 在实际开发中，包的扫描写得比较多

   1. ![image-20200511223832900](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200511223832900.png)
   2. 这是xml的写法（以前的方式）

   ```java
   <!--包扫描、只要标注了@Controller、@Service、@Repository、@Component，都会被自动扫描加入容器中-->
   <context:component-scan base-package="top.p3wj"></context:component-scan>
   ```

![image-20200512125701035](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512125701035.png)

​		3. 写在配置类中

![image-20200512125756335](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512125756335.png)

​		4. 效果演示

![image-20200512130735176](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512130735176.png)

​		发现其中mainConfig也是一个组件，是因为@Configuration也是一个@Component

![image-20200512131106829](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512131106829.png)

​		5. excludeFilters,过滤不扫描的内容。

![image-20200512131533196](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512131533196.png)

​	![image-20200512152116248](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512152116248.png)

![image-20200512152146253](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512152146253.png)

它是一个Filter()数组

![image-20200512152438561](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512152438561.png)

```java
//excludeFilters = Filter[] 指定扫描的时候按照规则排除哪些规则
//includeFilters = Filter[] 指定扫描的时候只需要包含哪些组件
//useDefaultFilters 默认为true,加载所有组件
```

![image-20200512152805361](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512152805361.png)

​		6.@ComponentScan

![image-20200512152959793](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512152959793.png)

​		在8几以上中才可以

![image-20200512153417456](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512153417456.png)

如果不是，就使用@ComponentScan，指定扫描策略

![image-20200512153632824](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512153632824.png)

![image-20200512153718572](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512153718572.png)

**FilterType.ASSIGNABLE_TYPE**			按照给定的类型

**FilterType.ASPECTJ**			使用ASPECTJ表达式（不太常用）

**FilterType.REGEX**				使用正则表达式

![image-20200512154654583](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512154654583.png)

实现TypeFilter

![image-20200512155505125](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512155505125.png)

![image-20200512155730049](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512155730049.png)

**top.p3wj中的每一个类都会进入进行匹配**

#### 三、Scope

![image-20200512160837963](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512160837963.png)

![image-20200512160917381](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512160917381.png)

​							**默认单实例**

![image-20200512174415394](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512174415394.png)

```java
* ConfigurableBeanFactory#SCOPE_PROTOTYPE  prototype   多实例
* ConfigurableBeanFactory#SCOPE_SINGLETON  singleton   单实例(默认值)
* org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST  request 同一次请求创建一个实例
* org.springframework.web.context.WebApplicationContext#SCOPE_SESSION  session 同一个session创建一个实例
```

**改为多实例后**

![image-20200512174717502](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512174717502.png)

这其实就相当于在xml文件中，Bean里加上scope

![image-20200512174811173](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512174811173.png)

**ioc容器启动会创建对象，放到ioc容器中，以后每次获取就是直接从容器(map.get())中拿**

**1.下面演示单实例**

![image-20200512175129601](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512175129601.png)

把test02中下面的注释掉

![image-20200512175221757](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512175221757.png)

**2.多实例情况**

![image-20200512180504798](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512180504798.png)

就不打印“给容器中添加Person.....”了

![image-20200512180656870](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512180656870.png)

ioc容器启动并不会去调用方法创建对象放在容器中。每次获取的时候才会调用方法创建对象。

#### 四、懒加载

- 单实例bean，默认在容器启动的时候创建对象
- 懒加载：容器启动不创建对象，第一次使用（获取）Bean创建对象，并初始化

![image-20200512181332557](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512181332557.png)

![image-20200512181358877](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512181358877.png)

**即在第一次获得的时候才加载**

若取消懒加载

![image-20200512181536119](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512181536119.png)

#### 五、按照条件给容器注入Bean

```java
@Conditional ,按照一定的条件进行判断，满足条件给容器中注册Bean
```

先前准备：

![image-20200512182257589](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512182257589.png)

![image-20200512182319410](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512182319410.png)

要求：

```java
* 如果是MacOs,给容器注册 jobs
* 如果是linux，给容器注册linus
```

![image-20200512183810347](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512183810347.png)

通过applicationContext拿到一个运行的环境

![image-20200512183326595](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512183326595.png)

![image-20200512183431866](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512183431866.png)

要传入一个Condition数组。@Conditional({})

![image-20200512183517813](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512183517813.png)

配置两个实现了Condition的类

![image-20200512191747085](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512191747085.png)

![image-20200512191753422](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512191753422.png)

设置一下参数![image-20200512192027729](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512192027729.png)

![image-20200512192110595](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512192110595.png)

```java
//boolean pserson = registry.containsBeanDefinition("pserson");//也可判断容器中是否包含一个Bean。也可给容器中注册Bean
```

可以做非常多的判断条件

![image-20200512202421896](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512202421896.png)

也可放在类上，含义即满足当前条件，这个类中配置的 所有bean注册才能生效

**注意**：

若有多个，则为按照顺序判断（猜测）已经设置-Dos.name=Linux

![image-20200512202810229](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512202810229.png)



### 六（一）、@Import导入

```java
/**
 * 给容器中注册组件：
 * 1) 包扫描+组件标注注解 (@Controller/@Service/@Repository/@Component)[自己写的]
 * 2) @Bean[导入的第三方包里面的组件],但是它比较麻烦（需要return等）
 * 3) @Import[快速给容器导入一个组件]
 */
```

1.新建一个color类![image-20200512201028328](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512201028328.png)

2.使用@Import![image-20200512201044794](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512201044794.png)

3.测试

![image-20200512202928632](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512202928632.png)

```java
//导入组件，id默认是组件的全类名,@Import(要导入到容器中到组件)，容器中就会自动注册这个组件，id默认是全类名
```

![image-20200512203021101](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512203021101.png)

可以导入多个，现写一个Red类

![image-20200512203140380](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200512203140380.png)

### 六（二）、@ImportSelector导入

```java
2)  ImportSelector：返回需要导入的组件的全数组
```

```java
public interface ImportSelector {

   /**
    * Select and return the names of which class(es) should be imported based on
    * the {@link AnnotationMetadata} of the importing @{@link Configuration} class.
    */
   String[] selectImports(AnnotationMetadata importingClassMetadata);

}
```

前提条件：

![image-20200513205236065](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200513205236065.png)

![image-20200513205309622](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200513205309622.png)

打上断点进行调试

![image-20200513205336350](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200513205336350.png)

结果：

![image-20200513205350130](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200513205350130.png)

如果返回null:---->return null;会报空指针，因为在拿类名的时候

![image-20200513205605821](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200513205605821.png)

所以不要返回null，可以返回一个空数组

```java
return new String[]{};
```

![image-20200513210013639](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200513210013639.png)

可获取到所以注解信息及类相关的 

因为是return的，所以也被导入了

![image-20200513211141399](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200513211141399.png)

### 六（三）、@ImportBeanDefination导入

```java
ImportBeanDefinitionRegistrar 手动注册Bean
```

**command+alt+b查看实现类**

![image-20200513213125884](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200513213125884.png)

#### 注意，import方式注入的名称为全类名

![image-20200513235948165](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200513235948165.png)

![image-20200514000256305](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514000256305.png)



### 七、@FactoryBean

![image-20200514000945452](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514000945452.png)

通过此方法把对象放到容器中

![image-20200514103628414](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514103628414.png)

![image-20200514103648956](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514103648956.png)

结果：

![image-20200514103750174](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514103750174.png)

```java
//工厂获取的是调用getObject创建的对象
```

```java
@Override
public boolean isSingleton() {
    return false;
}
```

isSingleton是false情况下是多实例，每一次获取都调用getObject

![image-20200514104048192](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514104048192.png)

2.

![image-20200514104418286](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514104418286.png)

Reason:

![image-20200514104829151](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514104829151.png)

```java
使用Spring提供的FactoryBean(工厂Bean)
*              1)  默认获取到到是工厂bean调用getObject创建的对象
*              2)  要获取工厂Bean本身，我们需要给id前面加一个&
*                  &colorFactoryBean
```

# II、生命周期

### 一、按照@Bean的方式指定

在以前，可以指定初始化和销毁方法

![image-20200514105507290](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514105507290.png)

![image-20200514114624653](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514114624653.png)

创建Car

```java
public class MainConfigOfLifeCycle {
    @Bean
    public Car car(){
        return new Car();
    }
}
```

![image-20200514114824116](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514114824116.png)

以上针对单实例对象

![image-20200514115444620](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514115444620.png)

通过调用close()关闭

![image-20200514115552183](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514115552183.png)

2.当改为多实例Bean当时候

![image-20200514120208416](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514120208416.png)

​	2.1 当获取的时候才会初始化

![image-20200514120255213](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514120255213.png)

​	2.2 容器关闭后不会进行销毁

![image-20200514120338201](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514120338201.png)

```java
* bean的生命周期：
*      bean创建---初始化---销毁的过程
* 容器管理bean的生命周期：
* 我们可以自定义初始化和销毁方法；容器在  bean进行到当前生命周期的时候调用我们自定义的初始化和销毁方式
*构造（对象创建）
*      单实例：在容器启动的时候创建对象
*      多实例：在每次获取的时候创建对象
*初始化：
*      对象创建完成，并赋值好，调用初始化方法。。。
*销毁：
*      单实例：容器关闭的时候
*      多实例：容器不会管理这个bean,容器不会调用销毁方法;需要手动调用
* 1)、指定初始化和销毁方法：
*          指定init-method和destroy-method方法
```

### 二、InitializingBean和DisposableBean

![image-20200514122845407](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514122845407.png)

![image-20200514122926120](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514122926120.png)

**在这里提出一个问题，@Bean不搭配@Configuration使用跟搭配有什么区别（还未解决）**



![image-20200514132432275](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514132432275.png)

![image-20200514132500329](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514132500329.png)

![image-20200514132603373](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514132603373.png)

**通过包扫描的方式进行注册，同时通过实现接口进行初始化和销毁**

```java
 2)、通过让Bean实现InitializingBean（定义初始化逻辑）
```

### 三、@PostConstruct和@PreDestroy

![image-20200514132924546](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514132924546.png)

![image-20200514140351680](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514140351680.png)

![image-20200514140414660](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514140414660.png)

这是java规范的注解，目前java8能用

![image-20200514141103750](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514141103750.png)

```java
* 3)、可以使用JSR250：
*          @PostConstruct：在bean创建完成并且属性赋值完成：来执行初始化方法
*          @PreDestroy:在容器销毁bean之前通知我们进行清理工作
```

### 四、BeanPostProcessor

![image-20200514141319899](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514141319899.png)

![image-20200514142626963](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514142626963.png)

先创建对象-》〉》〉初始化

![image-20200514142738648](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514142738648.png)

```java
* 4)、BeanPostProcessor【interface】,bean的后置处理器：
*          在bean初始化前后进行一些处理工作：
*          postProcessBeforeInitialization：在初始化之前工作
*          postProcessAfterInitialization：在初始化之后工作
```

### BeanPostProcessor原理

打断点debug一下

![image-20200514143005184](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514143005184.png)

1.查看调用方法栈，往上依次看

![image-20200514143549752](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514143549752.png)

![image-20200514143607149](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514143607149.png)

2.

![image-20200514143632531](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514143632531.png)

3.

![image-20200514144004605](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514144004605.png)

4.

![image-20200514144033011](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514144033011.png)

5.

![image-20200514144215598](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514144215598.png)

6.

![image-20200514144334348](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514144334348.png)

7.

![image-20200514144411012](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514144411012.png)

8.

![image-20200514144446397](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514144446397.png)

9.

![image-20200514144618656](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514144618656.png)

10.来看怎么创建的

![image-20200514144654708](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514144654708.png)

11.

![image-20200514144722127](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514144722127.png)

**12.创建好后准备初始化**

![image-20200514144918297](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514144918297.png)

![image-20200514145300044](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514145300044.png)

#### 13.原理体现的地方

![image-20200514145545659](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514145545659.png)

点进去看一下，里面的内容

![image-20200514145718974](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514145718974.png)

```java
applyBeanPostProcessorsAfterInitialization 就不看了，类似的
```

```java
*          遍历得到容器中所有的BeanPostProcessor：挨个执行beforeInitialization
*          一单返回null,跳出for循环，不会执行后面单BeanPostProcessor
*          populateBean(beanName, mbd, instanceWrapper); 给bean进行属性赋值的
*
*       initializeBean:
*      {
*          wrappedBean = this.applyBeanPostProcessorsBeforeInitialization(bean, beanName);
*          this.invokeInitMethods(beanName, wrappedBean, mbd);
*          wrappedBean = this.applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
*      }
```

### spring底层对BeanPostProcessor的使用

**我们来看一下ApplicationContextAwareProcessor**，实现的BeanPostProcessor

![image-20200514154132580](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514154132580.png)

![image-20200514154831407](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514154831407.png)

其实看看之前写的MyBeanPostProcessor,也是实现了它的方法

![image-20200514160401922](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514160401922.png)

ApplicationContextAwareProcessor是封装好了这些实现

**再来看下ApplicationContextAware**

1.

![image-20200514154526246](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514154526246.png)

2.

![image-20200514160625684](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514160625684.png)

如果是就调用下面的 **invokeAwareInterfaces(bean);**

点进去查看

![image-20200514160937349](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514160937349.png)

来debug看一下

准备如下：
![image-20200514161116770](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514161116770.png)

Debug:

![image-20200514171109848](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514171109848.png)

**并且会把ioc容器传进来，怎么传进来呢？接下来根据方法栈来看下之前调用的**

1.在这里调用 **postProcessBeforeInitialization**

![image-20200514171332661](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514171332661.png)

2.

![image-20200514171415726](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514171415726.png)

3.

![image-20200514171447377](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514171447377.png)

再来看看 **BeanValidationPostProcessor**

![image-20200514171639061](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514171639061.png)

该PostProcessor是用来做数据校验的，在web用的比较多

再来看看 **InitDestroyAnnotationBeanPostProcessor**

![image-20200514173814349](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514173814349.png)

为什么Dog中标注了这样的注解它就知道在哪执行呢？

![image-20200514173955545](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514173955545.png)

我们打一个断点来看一下

![image-20200514174035231](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514174035231.png)

**出了个问题，init不执行（以后解决）**发现的问题为：
**我还重写了个BeanPostProcessor的postProcessBeforeInitialization方法，@PostConstruct也是用**

![image-20200514203711376](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514203711376.png)

这里重写了：

![image-20200514203748694](/Users/piwenjing/Library/Application Support/typora-user-images/image-20200514203748694.png)



再来看看 **AutowiredAnnotationBeanPostProcessor**

@Autowired也是通过这个来注值的

```java
* spring底层对BeanPostProcessor的使用
*          bean赋值、注入其他组件,@Autowired,生命周期注解功能,@Async,等等
*          都是用BeanPostProcessor来完成的
```

