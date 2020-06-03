# AOP 原理解析 跳转逻辑 代码演示

[TOC]

## 建议大家先看“三、总结”，有一个总体认识比较好

### 一、AOP功能测试

![image-20200529222027235](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200529222027235.png)

如果都放在了MathCalculator代码里，那就是一种耦合的方式

![image-20200529222119307](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200529222119307.png)

所以定义一个日志切面类(LogAspects)：切面类里面的方法需要动态感知MathCalculator.div运行到哪里，然后执行

![image-20200529223257175](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200529223257175.png)

```java
 通知方法：
* 前置通知(@Before)：logStart：在目标方法(div)运行之前运行
* 后置通知(@After)：logEnd：在目标方法(div)运行结束之后运行（无论方法正常结束还是异常结束）
* 返回通知(@AfterReturning)：logReturn：在目标方法(div)正常返回之后运行
* 异常通知(@AfterThrowing)：logException：在目标方法(div)出现异常以后运行
```

**先不用环绕通知,尝试前面4个**。注意不是Junit的@Before,@After

是org.aspectj.lang.annotation.After;

```java
@Before("top.p3wj.aop.MathCalculator.div")
```

如果想切MathCalculator的所有方法（且不区分参数，加".."），即**MathCalculator.\*(..)**

1.1

![image-20200529224223102](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200529224223102.png)

但是看起来非常繁琐，有公共的切入点，所以**可以提取出来**。定义一个方法

![image-20200529233816416](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200529233816416.png)

1.2 加入到容器中

![image-20200529225252909](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200529225252909.png)

1.3 告诉哪一个类是切面类

![image-20200529231426671](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200529231426671.png)

1.4 但是最后记住还要开启AspectJ。给配置类中加 @EnableAspectJAutoProxy 【开启基于注解的aop模式】

![image-20200529230229273](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200529230229273.png)

1.5 **测试一下是否成功**

![image-20200529234453223](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200529234453223.png)

发现并没有输出相应的东西，这是因为这是我们自己new的，只要容器中的组件才可以使用切面的功能

1.5.1 带异常

![image-20200529234643728](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200529234643728.png)

这下就有了

1.5.2 不带异常

![image-20200529234849476](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200529234849476.png)

2.1 那么怎么拿到运行时的信息呢？JoinPoint

2.1.1

JoinPoint.getSignature获得方法签名

![image-20200529235918427](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200529235918427.png)

获得方法名

![image-20200529235944269](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200529235944269.png)

参数列表

![image-20200530000140392](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200530000140392.png)

2.1.2

怎么获得返回值呢？returning属性

![image-20200530000332367](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200530000332367.png)

returning指定谁来封装这个返回值，比如我们用Object result来接受所有的返回值

![image-20200601091041227](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601091041227.png)

2.1.3 获得异常，通过throwing

也跟returning一样要指定

![image-20200601092640346](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601092640346.png)

不然会报红色，

![image-20200601092728808](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601092728808.png)

这个也一样，如果不指定，spring不知道这个exception要干什么

加上后用1/0看一下结果

![image-20200601093132330](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601093132330.png)

那么没有这个@AfterThrowing呢？

![image-20200601093242622](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601093242622.png)

我们发现是对这个异常没有进行我们的一个处理的，并没有输出那一句话。

2.1.4 JoinPoint位置

![image-20200601093603512](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601093603512.png)

我们发现，JoinPotin如果不放在第一个参数，spring是无法解析的

看一下异常：

```java
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'org.springframework.context.event.internalEventListenerProcessor': Initialization of bean failed; nested exception is java.lang.IllegalArgumentException: error at ::0 formal unbound in pointcut 
```

放在第一位就正常了

![image-20200601095034789](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601095034789.png)

![image-20200601095227532](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601095227532.png)

### 二、AOP原理

#### 2.1- @EnableAspectJAutoProxy

AOP就是从@EnableAspectJAutoProxy开始的，加了就有AOP,不加就没有

![image-20200601095551805](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601095551805.png)

我们点进去看一下

![image-20200601095644226](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601095644226.png)

发现有ImportBeanDefinitionRegistrar，这是之前学的可以给容器bean中自定义注册

![image-20200601095738427](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601095738427.png)

英文不好，我就用**Translation插件翻译了**，大家可以在plugins中搜索安装

可以自定义地来注册，给BeanDefinitionRegistry

我们回到以前看看以前怎么做的，回到MainConfig2

![image-20200601103527154](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601103527154.png)

ImportSelector

![image-20200601103554549](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601103554549.png)

ImportBeanDefinitionRegistrar

![image-20200601103627304](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601103627304.png)

![image-20200601103704464](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601103704464.png)

那么AspectJAutoProxyRegistrar注册了什么bean呢？我们打一个断点debug一下

![image-20200601104013160](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601104013160.png)

1.

![image-20200601104144543](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601104144543.png)

注册这个组件，如果需要的情况下

2.

![image-20200601104347510](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601104347510.png)

调用另一个方法，并传了AnnotationAwareAspectJAutoProxyCreator.class，我们step into进去瞧瞧

3.

![image-20200601104610923](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601104610923.png)

3.1 判断是否容器中包含org.springframework.aop.config.internalAutoProxyCreator

3.2 包含则取得这个org.springframework.aop.config.internalAutoProxyCreator

3.3判断这个名字是否等于

![image-20200601104811458](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601104811458.png)

对应的他想注册这个

![image-20200601104852093](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601104852093.png)

**AnnotationAwareAspectJAutoProxyCreator**

他这个是判断有了就：

![image-20200601105132380](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601105132380.png)

但是我们没有。。。第一次

所以进行了else

![image-20200601105150595](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601105150595.png)

3.4

定义一个bean

![image-20200601105307014](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601105307014.png)

3.5

![image-20200601105350821](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601105350821.png)

然后这个bean的名就叫org.springframework.aop.config.internalAutoProxyCreator

4.

4.1好了，这个beanDefinition就返回了，我们进行下一步

![image-20200601105642399](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601105642399.png)

把@EnableAspectJAutoProxy这个注解的信息拿来

4.2 拿来看这个proxyTargetClass，exposeProxy属性是否为true

![image-20200601105853715](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601105853715.png)

4.3 这个后来再说

如果为true，就做一些什么操作。

那么，重点就在于他给容器注册了一个**AnnotationAwareAspectJAutoProxyCreator**，把这个的功能研究出来了，那么AOP功能就出来了。以后也一样的，看见有@EnableXX的注解，就再去看看他给容器注册了什么组件，再去看这些组件的功能是什么

![image-20200601110455241](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601110455241.png)

5.那我们就来看一下**AnnotationAwareAspectJAutoProxyCreator**

5.1 它有很多的继承关系，大家可以放大看一下

![AnnotationAwareAspectJAutoProxyCreator](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/AnnotationAwareAspectJAutoProxyCreator.png)

![image-20200601110940446](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601110940446.png)

注意XXBeanPostProcessor，bean的后置处理器

BeanFactoryAware，能把工厂传进来的

![image-20200601111108286](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601111108286.png)

#### 2.2- AnnotationAwareAspectJAutoProxy

只要分析清楚作为后置处理器和BeanFactory做了哪些工作，整个aop的流畅我们就清楚了

因为是从AbstractAutoProxyCreator开始实现**SmartInstantiationAwareBeanPostProcessor**和**BeanFactoryAware**接口的

我们点进去看一下

1.

![image-20200601112854708](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601112854708.png)

我们发现是它进行setBeanFactory的，我们把断点打在这

![image-20200601113005975](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601113005975.png)

2.

只要是postProcessXX乱七八糟的，都是跟后置处理器有关的

![image-20200601113426892](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601113426892.png)

我们把所有跟后置处理器有关的逻辑都打上断点

直接返回，或返回空方法的我们就不管了

![image-20200601113228756](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601113228756.png)

打上断点：

```java
public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException
```

![image-20200601122232476](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601122232476.png)

```java
public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
```

![image-20200601122324655](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601122324655.png)

注意有的方法名字有点像，

![image-20200601122409855](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601122409855.png)

一个是postProcessBefore**Instantiation**，一个是postProcessBefore**Initialization**

一些不是重写的，自己定义的我们就不打断点了，例如

![image-20200601122602218](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601122602218.png)

3.

我们继续上一层：
![image-20200601122927278](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601122927278.png)

我们发现，在**AbstractAdvisorAutoProxyCreator**中，

![image-20200601123031587](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601123031587.png)

又把setBeanFactory重写了

![image-20200601123249073](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601123249073.png)

它会在**setBeanFactory**里边init一个BeanFactory

![image-20200601123332094](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601123332094.png)

我们再看看有没有跟后置处理器有关的，额。。。。没有。

4.我们再继续上面一层，

![image-20200601163600563](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601163600563.png)

前面是抽象的，这个是专门针对呀AspectJ的

我们发现，。。。。这个类不是跟beanpost这些有关的，我们把它跳过

5.我们继续上一层

**AnnotationAwareAspectJAutoProxyCreator**

![image-20200601164247986](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601164247986.png)

![image-20200601164054126](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601164054126.png)

我们发现，它重写了它父亲的父亲的initBeanFactory

相当于还得调用它，其他的就没有跟后置处理器有关的了。我们再给他打上断点

![image-20200601164341022](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601164341022.png)

再给他父类的setBeanFactory打上断点（它爷爷）

![image-20200601164503945](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601164503945.png)

#### 2.3- 注册AnnotationAwareAspectJAutoProxy

通过前面的分析打上了断点，我们再在这打上断点

![image-20200601164628529](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601164628529.png)

我们来启动debug一下

![image-20200601165006499](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601165006499.png)

启动，先来到了setBeanFactory

那么怎么来的呢？

![image-20200601165052253](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601165052253.png)

我们从这里开始看

1.创建IOC容器

![image-20200601165132511](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601165132511.png)

调用一个有参的构造器，就是下面这个，它分为三步

![image-20200601165246027](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601165246027.png)

a）首先无参构造器创建对象。

b）再把我们这个配置类注册进来

![image-20200601165436945](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601165436945.png)

c）调用refresh

refresh之前的文章有介绍，刷新容器的作用，即把容器中的bean全部创建出来

![image-20200601165708541](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601165708541.png)

2.这个bean注册拦截器逻辑是怎样的呢？（其实之前spring-annotation文章有讲过）

![image-20200601170057869](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601170057869.png)

3.再往前走

![image-20200601170217811](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601170217811.png)

我们来看看这个大方法是什么

![image-20200601170259348](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601170259348.png)

在beanFactory容器中拿到我们所有要创建的后置处理器。为什么我们已经定义了呢？因为我们那个test文件中传入了配置类，然后配置类里边有一个**@EnableAspectJAutoProxy**

![image-20200601170507034](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601170507034.png)

![image-20200601170523781](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601170523781.png)

之前说过这个注解会为我们容器注入一个**AnnotationAwareAspectJAutoProxyCreator**,包括容器中某人的一些后置处理器的定义。来到这一步的时候，只是有这么一些定义，还没有创建对象

4.我们继续，看看有哪些后置处理器

![image-20200601172126436](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601172126436.png)

发现有internalAutoProxyCreator

但当时注册的也是beanDefinetion，即当时注册的是bean的定义信息

bean的定义信息的类型就是AnnotationAwareAspectJAutoProxyCreator。这个之前有讲

![image-20200601204421466](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601204421466.png)

可发现他还给beanFactory中加了其他的一些BeanPostProcessor

4.

![image-20200601204759012](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601204759012.png)

![image-20200601204838447](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601204838447.png)

拿到这些所有的beanProcessor。它来看是不是这个PriorityOrdered下的接口

![image-20200601205040832](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601205040832.png)

它是有一个优先级排序，即这些beanPostProcessor首先这个接口来判断优先级，谁在前，谁在后

![image-20200601205216131](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601205216131.png)

5.排序

![image-20200601205451006](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601205451006.png)

![image-20200601205545331](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601205545331.png)

普通的就是没有实现优先级接口的

6.

**那么所谓的注册是什么呢？**

![image-20200601210452243](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601210452243.png)

我们现在要注册这个

它其实就是它，因为之前的bean定义

![image-20200601210558489](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601210558489.png)

我们来看一下它

![image-20200601210558490](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601210558490.png)

注意在左上方，是实现了Ordered接口的

![image-20200601210911491](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601210911491.png)

它拿到这个名字，然后从beanFactory中获取

就调到了这里

![image-20200601211100325](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601211100325.png)

又调用doGetBean

![image-20200601211227068](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601211227068.png)

这个**doGetBean**有点长，只截取了一部分

因为是第一次，容器中肯定没有

![image-20200601211605142](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601211605142.png)

![image-20200601212849824](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601212849824.png)

然后又回到了AbstractBeanFactory

![image-20200601211826775](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601211826775.png)

![image-20200601211857249](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601211857249.png)

现在就是来创建bean

7.那么我们就来看一下

![image-20200601213056575](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200601213056575.png)

如何创建名字叫它的**beanPostProcessor【AnnotationAwareAspectJAutoProxyCreator】**

8.

![image-20200602091327443](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602091327443.png)

9.

![image-20200602091357784](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602091357784.png)

![image-20200602091538538](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602091538538.png)

可以看到这个bean是有的。

这个bean的创建在上面，可以来看一下

![image-20200602091918049](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602091918049.png)

我们来看下创建的是什么

![image-20200602091953552](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602091953552.png)

![image-20200602092016208](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602092016208.png)

可以看见创建的是名为internalAutoProxyCreator的，类型为AnnotationAwareAspectJAutoProxyCreator的bean

![image-20200602092326769](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602092326769.png)

赋值是什么鬼？进去看一下

![image-20200602092434131](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602092434131.png)

就是bean的一些属性，name啊这些，下面我们依次点进去看一下。可能有点长。

![image-20200602093318908](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602093318908.png)

我们看见有一个BeanDefinitionHolder

![image-20200602093426622](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602093426622.png)

里面有一个BeanDefinition

![image-20200602093659428](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602093659428.png)

我们看见，就是这些属性

所以spring的对象创建是将Bean的定义信息存储到这个BeanDefinition相应的属性中，后面对Bean的操作就直接对BeanDefinition进行，例如拿到这个BeanDefinition后，可以根据里面的类名、构造函数、构造函数参数，使用反射进行对象initializeBean。

10.这时候创建好了，**该初始化了，这里要注意，我们的beanPostProcessor就是在初始化的前后进行工作的**

![image-20200602094241694](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602094241694.png)

这里面有个这个，其实在spring-annotation那篇博客中有涉及到

![image-20200602094438247](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602094438247.png)

看是不是这些Aware接口的，是就调用这些方法——Aware接口的方法回调

因为我们这个类型 的bean是BeanFactoryAware接口的。**怕你忘了，我再贴一下图**

![image-20200602094644217](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602094644217.png)

![image-20200602094719547](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602094719547.png)

所以进入了这个方法。我们倒回去，看看这个Aware的执行完了会是什么

![image-20200602095018184](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602095018184.png)

```java
applyBeanPostProcessorsBeforeInitialization
```

在初始化之前应用Bean后处理器

看一看。。。

![image-20200602095110943](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602095110943.png)

它是调用所有的后置处理器，调用他们的postProcessBeforeInitialization

所以后置处理器在Aware之后，后置处理器又在初始化之前

我们继续再来看这个3

![image-20200602095326410](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602095326410.png)

```
invokeInitMethods
```

这个英文简单，执行初始化方法。在哪自己@Bean的时候可以指定初始化方法。

我们还是进去看一下吧：

![image-20200602100020139](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602100020139.png)

执行完这个3后，又有一个4

![image-20200602100204786](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602100204786.png)

初始化后应用Bean后置处理器，再点进去

![image-20200602100316462](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602100316462.png)

也是跟之前的后置处理器一样，一个是Before,一个是After,在初始化的前后。

11.**我们还是回到我们的方法栈**

之前说它实现了BeanFactoryAware的

![image-20200602100532916](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602100532916.png)

12.

![image-20200602100600394](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602100600394.png)

**最终就来到了我们的AbstractAutoProxyCreator.setBeanFctory()**

即：

![image-20200602101049725](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602101049725.png)

13.我们又倒回去看看，setBeanFactory执行完后又是怎样的。step over来到下面这个initBeanFactory

![image-20200602101749138](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602101749138.png)

看，调到了**AnnotationAwareAspectJAutoProxyCreator**

也就是我们要给容器中创建的这个AspectJAutoProxyCreator的这个init方法，

它创建了两个东西，一个ReflectiveAspectJAdvisorFactory反射的AcpectJ工厂，BeanFactoryAspectJAdvisorsBuilderAdapter通知构建器的适配器。相当于把aspectJAdvisorFactory和beanFactory重新包装了一下

那么此时AnnotationAwareAspectJAutoProxyCreator就创建成功，并调用了它的initBeanFactory方法

**小结：**

![image-20200602110043679](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602110043679.png)

#### 2.4- AnnotationAwareAspectJAutoProxy执行时机

上面介绍了AnnotationAwareAspectJAutoProxyCreator就可以拦截到这些创建过程

我们来看一下它作为后置处理器接下来做了什么

来到这里

![image-20200602110706471](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602110706471.png)

```
postProcessBeforeInstantiation
```

看看BeanPostProcessor呢

![image-20200602110747386](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602110747386.png)

```java
postProcessBeforeInitialization
```

其实在前面有提到两者名字有一点像，那么他们的区别是什么呢？
往上看，我们实现的 后置处理器是

![image-20200602110939104](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602110939104.png)

![image-20200602110957139](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602110957139.png)

![image-20200602111024089](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602111024089.png)

注意一下。

那么为什么来到这呢？

![image-20200602111119631](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602111119631.png)

我们也来探究一下。回到之前的这个：

![image-20200602111316858](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602111316858.png)

是从之前的栈桢这里来的

然后是getBean->doGetBean,我们去看一下doGetBean

![image-20200602111748656](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602111748656.png)

这个sharedInstance得翻到上面

![image-20200602111853837](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602111853837.png)

spring是通过这个机制来保证单实例只被创建一次，所有被创建的bean都会被缓存起来。[这里建议大家看视频](https://www.bilibili.com/video/BV1oW41167AV?p=31)

![image-20200602112257063](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602112257063.png)

就到了创建bean的createBean,查看bean的一些定义信息

**好，下面是AOP的重点！！！，小伙子们打起精神**

1.

![image-20200602112641687](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602112641687.png)

希望后置处理器在此能返回一个代理对象：

如果能返回这个bean即！=null，它就直接返回。

如果不能，

![image-20200602112821983](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602112821983.png)

这里有一个doCreateBean,我们点进去

![image-20200602113013638](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602113013638.png)

前面内容有了，这里就不赘述了。和3.6流畅一样

我们现在是停到resolveBeforeInstantiation了，我们点进去看一下

后置处理器先尝试返回对象

![image-20200602113336903](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602113336903.png)

我们先看看**applyBeanPostProcessorsBeforeInstantiation**是干什么的，点进去

![image-20200602113540714](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602113540714.png)

如果说是**InstantiationAwareBeanPostProcessor**，就调用**postProcessBeforeInstantiation**方法

区别：BeanPostProcessor是在Bean对象创建完成初始化前后调用

InstantiationAwareBeanPostProcessor书在创建Bean实例之前先尝试用后置处理器返回对象的

不是以前学的**postProcessBeforeInitialization**

之前说了，我们的AnnotationAwareAspectJAutoProxyCreator就是InstantiationAwareBeanPostProcessor后置处理器

创建Bean,AnnotationAwareAspectJAutoProxyCreator会在任何bean创建之前先尝试返回bean的实例。它在所有bean创建之前会进行拦截，因为它是这个InstantiationAwareBeanPostProcessor后置处理器

![image-20200602143306027](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602143306027.png)

小结：

![image-20200602143713411](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602143713411.png)

#### 2.5- 创建AOP代理

我们来看看

**InstantiationAwareBeanPostProcessor**做了什么，我们来重新debug

```java
 AnnotationAwareAspectJAutoProxyCreator【InstantiationAwareBeanPostProcessor】
```

![image-20200602143846668](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602143846668.png)

现在是创建容器中第一个bean，这是一个循环创建的过程

再放行

![image-20200602144219610](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602144219610.png)

再放行，下面就一直方向了

![image-20200602144407667](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602144407667.png)

出了点问题，我直接截视频里的了。

![image-20200602145209906](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602145209906.png)

到了我们的计算器

![image-20200602145228503](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602145228503.png)

adviseBeans，已经增强的bean

![image-20200602145330724](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602145330724.png)

![image-20200602145352529](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602145352529.png)

等等。判断我们的calculator是否在adviseBeans里面

![image-20200602145647680](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602145647680.png)

判断是否是基础的类型，点进去看一下。

![image-20200602145723272](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602145723272.png)

即是不是Advice,PointCut,Advisor,AopInfrastructureBean这些接口

点进去看还会判断是否为切面

![image-20200602145939243](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602145939243.png)

![image-20200602145916600](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602145916600.png)

![image-20200602150013202](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602150013202.png)

判断是否有切面这个注解.即实现和注解都算作切面

![image-20200602150315932](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602150315932.png)

判断是否需要跳过，即不处理这个bean

![image-20200602150402489](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602150402489.png)

findCandidateAdvisors找到候选的增强器

现在有4个增强器（就是我们切面里面的通知方法）

![image-20200602150526309](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602150526309.png)

第一个：

![image-20200602150611155](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602150611155.png)

下一个

![image-20200602150630819](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602150630819.png)

下面就展示了，就是4个通知方法

每一个通知方法的增强器是InstantiationModelAwarePointcutAdvisor

![image-20200602154719051](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602154719051.png)

最终又调用父类的shouldSkip

接着就到了Initialization

![image-20200602155252213](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602155252213.png)

这里有一个

![image-20200602155404736](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602155404736.png)

**wrapIfNecessary**。包装，如果需要的情况下

![image-20200602155735187](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602155735187.png)

**这一节建议先自己看多看几遍源码，不然看视频也跟不走**

跟之前一样，也是判断是否为isInfrastructureClass等等

![image-20200602155956659](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602155956659.png)

findEligibleAdvisors找到可用的增强器

![image-20200602160156699](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602160156699.png)

来找到增强其可以应用到beanClass

找到能在当前bean使用的增强器（找哪些通知方法是需要切入当前bean方法的），怎么找呢？

![image-20200602160827479](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602160827479.png)

用这个AopUtils来找到这个findAdvisorsThatConApply

来看一下：

![image-20200602161056746](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602161056746.png)

先整一个能用的一个LinkedList<Advisor>

通过for循环判断每一个增强器是不是

![image-20200602161242503](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602161242503.png)

类型。

![image-20200602161315289](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602161315289.png)

下面还有一个for循环

canApply是用来判断是否能用，怎么叫能用呢？我们apply进去

![image-20200602161433194](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602161433194.png)

用切面表达式算一下每一个方法是否匹配

找到后还做了一个排序：

![image-20200602162801774](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602162801774.png)

即调用哪些通知方法都是有顺序的，继续就走到了这：

![image-20200602162835062](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602162835062.png)

然后就是这

![image-20200602162927679](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602162927679.png)

我们这些已经指定好的拦截器，我们增强器就是要拦截目标方法执行的

![image-20200602163127562](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602163127562.png)

把增强器拿到，然后把当前bean已经增强过了，放到缓存里面保存一下

即：如果该bean是需要增强的就会来到这一步

![image-20200602163327845](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602163327845.png)

![image-20200602163403986](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602163403986.png)

这是获取到的增强器

插入一句![image-20200602164512430](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602164512430.png)

直接贴的视频中的图是因为我打断点进入不了视频中的方法，原因在于我没有把它标示为spring项目，导致我的切面并没有真正的识别到方法

当你的方法旁边的提升不是

![image-20200602164650081](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602164650081.png)

而是

This advice advises no method就会出现这样的问题

解决方法：

打开Project Strusture

![image-20200602164905189](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602164905189.png)

添加就可以了

这个代理对象怎么创建呢？

![image-20200602173218614](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602173218614.png)

还是拿到这个增强器，保存到代理工程proxyFactory

![image-20200602173321010](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602173321010.png)

用我们这个代理工程![image-20200602192401258](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602192401258.png)来创建对象

来看看这个AOP怎么创建的

返回回来后

![image-20200602192526842](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602192526842.png)

![image-20200602173407429](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602173407429.png)

先来得到AOP代理的创建工厂，然后创建AOP(为这个this对象)

现在来创建AOP代理

会有两种形式的代理对象

![image-20200602192831361](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602192831361.png)

一种**JdkDynamicAopProxy**，一种是**ObjenesisCglibAopProxy**

这是spring自动决定

如果这个类是实现接口的能用jdk代理就用jdk代理。如果没有实现，例如我们的MathCalculator就用Cglib。我们也可以强制使用Cglib，这个后面再说



会为我们创建一个代理对象

![image-20200602193233863](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602193233863.png)

可以看见是使用Cglib增强的代理

相当于wrap的方法就用完了，给容器返回增强了代理的对象

组件想要执行方法，Cglib就会提枪调用那些我们保存了的通知

以后容器获取到的就是这个组件的代理对象，执行目标方法的时候，这个代理对象就会这个切面通知方法的流程

![image-20200602193703020](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602193703020.png)

![image-20200602193724811](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602193724811.png)

在bean的创建前后会做一些事情，会判断这个对象是否需要包装，即需要被切入（增强），就会创建代理对象，容器拿到的就是一个代理对象。对象执行方法就是这个代理对象在执行方法。

#### 2.6- 获得拦截器链-MethodInterceptor

来说说目标方法的执行

我们来打上断点进行debug

![image-20200602195042707](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602195042707.png)

![image-20200602195101713](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602195101713.png)

确定是Cglib增强过的

![image-20200602195203850](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602195203850.png)

里面还封装了5个增强器

![image-20200602195334387](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602195334387.png)

比如第一个增强方法他是这个AspectAfterThrowingAdvice

相当于容器中放的这个代理对象放了我们通知方法的信息，切入哪个方法 的信息

![image-20200602195539740](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602195539740.png)

确认MathCalculator类

来到CglibAopProxy.intercept

![image-20200602200811474](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602200811474.png)

拦截目标方法的执行（即让AOP代理先来拦截一下）

来看看他的逻辑。这里有一个chain

1.获取拦截器链

![image-20200602201117192](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602201117192.png)

根据这个对象获取拦截器链

![image-20200602201301273](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602201301273.png)

获取目标方法的拦截器链

![image-20200602201417113](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602201417113.png)

如果这个链是空的，就methodProxy.invoke。就是没有拦截器链，直接执行目标方法

如果有，它创建一个CglibMethodInvocation对象，然后调用它的proceed方法，

![image-20200602202041266](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602202041266.png)

把这些等等都传入进来

核心就来到了这个拦截器链该怎么获取，它是来做什么的。听起来像是在目标方法执行之前进行拦截的，但是在目标方法执行时我们是要执行通知方法的。所以我们猜测这个拦截器是来告诉通知方法怎么执行，再来执行目标方法。

![image-20200602202502819](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602202502819.png)

我们step into 进来

![image-20200602202745406](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602202745406.png)

这些都是缓存，缓存就是保存起来，方便下一次用。

它通过advisorChainFactory的getInterceptorsAndDynamicInterceptionAdvice来获取拦截器链

如果获取到了，就把这个cached返回。怎么获取的呢，我们step into 进去

![image-20200602212612436](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602212612436.png)

![image-20200602212644850](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602212644850.png)

![image-20200602212700159](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602212700159.png)

再返回

所以整个拦截器链被封装在list中

List<Object>interceptorList保存所有拦截器。注意，list创建的时候已经赋值好了长度

![image-20200602221856135](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200602221856135.png)

![image-20200603101709247](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603101709247.png)

![image-20200603101820800](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603101820800.png)

一个是我们默认（ExposeInvocationInterceptor）的，剩下的就是我们的通知方法，

我们继续往下走

![image-20200603103315435](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603103315435.png)

它拿到每一个advisor,判断如果是一个需要切面切入的增强器

![image-20200603105304968](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603105304968.png)

包装成一个interceptors

![image-20200603105413372](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603105413372.png)

再加入进来

![image-20200603105454246](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603105454246.png)

如果是其他类型，也一样的操作.又或者直接传进来，然后addAll

一句话，遍历所有的增强器，再将其转为interceptor

![image-20200603105645957](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603105645957.png)

主要是调用这句话

![image-20200603105734260](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603105734260.png)

那么为什么需要转呢

我们来看一下，第一个advisor是

![image-20200603110250477](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603110250477.png)

下来，它是这个PointcutAdvisor

![image-20200603123116272](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603123116272.png)

还有一些适配器，做一些转化再加进来

即如果是MethodInterceptor直接加入到集合中，如果不是（虽然这里没有不是的逻辑，待会我们可以debug走一下）就一个for循环，使用增强器的适配器转为MethodInterceptor加入到集合中

![image-20200603123521489](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603123521489.png)

我们来打一个断点，并放行

![image-20200603123735653](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603123735653.png)

这是一个AspectJAfterThrowingAdvice,我们来看一下这个

![image-20200603125832249](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603125832249.png)

发现它是这个MethodInterceptor

![image-20200603125936719](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603125936719.png)

然后这3个适配器，我们点击下一步，

![image-20200603130031081](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603130031081.png)

都没满足if的条件，即

![image-20200603130124289](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603130124289.png)

然后返回。

再来看看第二个：

![image-20200603130242908](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603130242908.png)

是这个AspectJAfterreturningAdvicem，它不是MethodInterceptor

![image-20200603130411713](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603130411713.png)

直接来到了for

![image-20200603130510308](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603130510308.png)

第一个是MethodBeforeAdviceAdapter，专门用来转前置通知的，它肯定是不支持我们这个afterReturning的，所以

![image-20200603130711233](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603130711233.png)

if就没有成功。

再来看第二个AfterReturningAdviceAdapter

![image-20200603130746035](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603130746035.png)

![image-20200603130829762](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603130829762.png)

进来了，它是支持的。我们进去这个AfterReturningAdviceAdapter，这个advisorAdapter看看它是怎么转的

![image-20200603131222692](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603131222692.png)

实际上就是把这个advice拿过来给他包装一个**AfterReturningAdviceInterceptor**

![image-20200603131344731](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603131344731.png)

即是这个MethodInterceptor就直接放，不是就给包装过来

第三个后置通知也是直接ok的。

![image-20200603131619405](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603131619405.png)

第四个是这个前置通知

![image-20200603131643995](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603131643995.png)

前置通知不是，用adapter转换

我们让它全部走完，现在全部转为了

![image-20200603131812000](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603131812000.png)

MethodInterceptor

最终回来，可以知道AfterReturningAdviceInterceptor跟MethodBeforeAdviceInterceptor是用adaptor转过来的

![image-20200603132207212](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603132207212.png)

转完过后，其实拦截器链就出来了，拦截器链里面就是每一个通知方法

![image-20200603132433834](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603132433834.png)

接下来往下走

![image-20200603134012772](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603134012772.png)

new Cglib，

![image-20200603134041904](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603134041904.png)

然后.proceed()来执行我们的拦截器

小结

![image-20200603134659132](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603134659132.png)

#### 2.7- 链式调用通知方法

下面来探究这个proceed方法

记录下拦截器链中有哪些内容

![image-20200603135222611](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603135222611.png)

如果拦截器链是空的就创建一个，我们就不看了，直接来到else

![image-20200603135400755](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603135400755.png)

![image-20200603135552029](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603135552029.png)

之前有讲,进去看一眼

![image-20200603140121853](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603140121853.png)

所以我们执行的是CglibMethodInvocaltion的proceed

可以看见，这里有一个索引

![image-20200603144824796](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603144824796.png)

**![image-20200603144901293](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603144901293.png)**

它默认是-1，这里做了一个判断即如果“-1 ==  this.interceptorsAndDynamicMethodMatchers.size() - 1”

这个size是什么呢？
![image-20200603145015273](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603145015273.png)

正好是这个拦截器。假如没有拦截器链，就相等了。

我们点进去看一下如果没有发生了什么？
![image-20200603145129137](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603145129137.png)

![image-20200603145211152](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603145211152.png)

其实就为反射创建。method.invoke()

currentInterceptorIndex记录当前拦截器的索引，如果当前是第四个拦截器了，当前索引为4。当前拦截器大小正好是5-1=4

它有两种情况：
1.如果没有拦截器，直接执行目标方法，或者执行拦截器的索引和数组大小-1大小一样（指定到了最后一个拦截器），一会儿来看这个过程。反正currentInterceptorIndex是用来记录索引

1.

![image-20200603155834512](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603155834512.png)

现在索引是-1，拦截器有5个

2.

![image-20200603155938280](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603155938280.png)

第0号就是这个**ExposeInvocationinterceptor**

3.拿到之后怎么做呢？step into

![image-20200603160418639](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603160418639.png)

它会调用invoke方法，然后把（this）传进来，这个this就是ExposeInvocationInterceptor对象。我们点进去：
4.

![image-20200603161019517](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603161019517.png)

它是先从invocation里面get,点进去看一下invocaiton

![image-20200603161208761](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603161208761.png)

它是一个ThreadLocal，TreadLocal就是同一条线程共享数据的，它把这个MethodInvocation共享。

第一次还没有共享，所以它把这个MethodInvocation先放进去

![image-20200603164515065](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603164515065.png)

放进去后，执行Cglib的proceed

为什么呢，因为是这么过来的，它是Cglib![image-20200603164644716](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603164644716.png)

![image-20200603164734893](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603164734893.png)

5.我们继续

![image-20200603164814683](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603164814683.png)

咦，同样的流程，只是索引从之前的-1变成了0

6.

它又自增一些，索引为1

![image-20200603164919022](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603164919022.png)

![image-20200603165027067](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603165027067.png)

**即**：

![image-20200603165115148](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603165115148.png)

它就是这么个执行的。我们接下来继续

7.

![image-20200603165217874](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603165217874.png)

可以看见它又调用这个invoke(this)，所有拦截器都会调用这个invoke(this)

进来，

![image-20200603165340735](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603165340735.png)

它又是调这个mi.proceed

mi就是我们的Cglib，调这个又是索引自增一次，又是拿下一个。所以流程是这么下来的

![image-20200603165523024](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603165523024.png)

现在是1了。

![image-20200603165545370](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603165545370.png)

自增一个变2

8.

再来看他，

![image-20200603165636935](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603165636935.png)

还是调invoke(this)，我们step into

![image-20200603165730600](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603165730600.png)

那就又是一样了。无限套娃，后面就不掩饰了

[也就是责任链模式，你可以点击查看还有其他模式举例](http://c.biancheng.net/view/1383.html)

通过链式的方式锁定到下一个拦截器

9.

我们来到**MethodBeforeAdviceInterceptor**

![image-20200603170415273](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603170415273.png)

它先调advice.beforem，调用前置通知的通知方法

![image-20200603170645520](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603170645520.png)

![image-20200603170712335](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603170712335.png)

这个interceptor总算做了一些事了

即这个**MethodBeforeAdviceInterceptor**在invoke的时候是先来调前置通知

它调完了才又进行之前的下一次

10.

现在拦截器的index跟5-1一样长了

我这里直接赋值4了

![image-20200603171446331](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603171446331.png)

11.来到了这里，invokeJoinpotint

![image-20200603171548852](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603171548852.png)

这个目标方法一执行完就return了

前置通知调完再调用目标方法

![image-20200603182943299](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603182943299.png)

然后这是一个递归因为，MethodBeforeAdviceInterceptor一调用完又返回到AspectAfterAdvice了

![image-20200603183417962](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603183417962.png)

不管是否有异常，都来执行通知方法，即我们的后置通知，因为这是我们后置通知的拦截器，有点像springMVC中的拦截器

如果方法没有抛异常就来到AfterReturningAdviceInterceptor。只不过我们的方法抛了异常，即1/0，

Returning也不做什么处理的，就抛给上层AspectAfterThrowingAdvice

![image-20200603183728249](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603183728249.png)

所以你看来到了AfterThrowingAdvice

![image-20200603183756602](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603183756602.png)

我们来看一下AfterReturningAdviceInterceptor

![image-20200603183903442](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603183903442.png)

只有你的proceed执行没有问题，它才会执行afterReturning

所以说我们的这个 **返回通知** 是在方法执行没有问题才执行

这么说：
现在在这里有1/0有错误

![image-20200603184212344](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603184212344.png)

就抛到了**AspectAfterThrowingAdvice**,且afterReturning没有执行

![image-20200603184322356](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603184322356.png)

Throwing这里拿到这个异常

![image-20200603184358754](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603184358754.png)

![image-20200603184427268](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603184427268.png)

![image-20200603184447259](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603184447259.png)

且如果有异常，这个异常就抛出去了。有人处理就处理，没有就抛给虚拟机

这就是我们的AOP流程：

1. 先来执行我们的前置通知
2. 执行我们的目标方法
3. 在执行后置通知
4. 如果有异常，执行异常通知
5. 如果没有异常，执行返回通知

![image-20200603184704703](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603184704703.png)

再看一下我们的代码

**MathCalculator**

![image-20200603184731945](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603184731945.png)

**LogAspects**

![image-20200603185037975](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603185037975.png)

**如果1/1**

![image-20200603185104068](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603185104068.png)

**如果1/0**

![image-20200603185135896](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603185135896.png)

可以看见有异常AfterReturning就没了

![image-20200603200555705](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603200555705.png)

来看这张图理解理解

![image-20200603200631349](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603200631349.png)

### 三、总结

这一节原理还是很多，建议大家还是多看看视频，跟着多debug。下面做一下总结：

我们来看在创建这个容器对象的时候

![image-20200603202944993](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603202944993.png)

会调用refresh()刷新容器

![image-20200603203026247](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603203026247.png)

之前讲过，这里只是回忆一下

![image-20200603203102325](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603203102325.png)

有一步是注册后置处理器。在这一步会创建后置处理器对象

后面一步就是初始化单实例bean

![image-20200603203220302](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603203220302.png)

如果要初始化剩下的单实例bean，那么我们的

![image-20200603203253263](https://cdn.jsdelivr.net/gh/1392517138/imgRepository@master/image-20200603203253263.png)

都在这了

组件创建完后就判断是否需要增强（即创建完后有一个postProcessAfterInitialization）：

return wrapIfNessary(bean,beanName,cacheKey)来判断是否需要包装（增强）。

是的话就将我们的通知方法包装成增强器（Advisor），给业务逻辑组件创建一个代理对象（Cglib）,**如果你有接口，它也可以创建jdk动态代理**

**而代理对象里面就有我们的增强器**。代理对象创建完后，我们的容器就创建完了。

执行目标方法其实就是代理对象执行目标方法。它是用CglibAopProxy.intercept()这个方法进行拦截。**拦截过程**：1.得到目标方法的拦截器链（也就是以前的增强器包装成拦截器MethodInterceptor）。2.利用拦截器的链式机制，依次进入每一个拦截器进行执行。

**3.执行效果我们就有两套：**

正常执行：前置通知-》目标方法-〉后置通知—》返回通知

出现一次：前置通知-》目标方法-〉后置通知—》异常通知

AOP的源码是spring里较重要的。大家多走几遍，来加深理解。以上只是为大家提供走源代码的一个思路并做下笔记帮助大家加深记忆。















