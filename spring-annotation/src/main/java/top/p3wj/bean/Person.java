package top.p3wj.bean;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Aaron
 * @description
 * @date 2020/5/11 9:55 PM
 */
public class Person {
    /**
     * 使用@Value赋值
     * 1、基本树枝
     * 2、可以写SpEL,#{}
     * 3、可以写${},去除配置文件中的值【properties】（在运行环境变量的值）
     */
    @Value("张三")
    private String name;
    @Value("#{20-2}")
    private Integer age;
    @Value("${person.nickName}")
    private String nickName;



    public Person() {
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", nickName='" + nickName + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
