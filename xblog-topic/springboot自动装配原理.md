---
submitToken: CFFB6B1CB0D5D62A535D66EE202C5C194FFEF02EF2ED4A4B2023FB0E65853C9F
title: springboot自动装配原理?
channel: topic
labels: spring
---

这个就得从springboot项目的核心注解@SpringbootApplication说起了，这个注解包含了三个注解，其中一个是@EnableAutoConfiguration注解，这个注解主要是开启自动配置的，这个注解会"猜"你将如何配置 spring，前提是你已经添加 了 jar 依赖项，比如项目中引入了 spring-boot-starter-web ，这个包里已经添加 Tomcat 和 SpringMVC，这个注解节就会自动假设您在开发一个 web 应用程序并添加相应的 spring 配置，springboot默认有一个spring-boot-autoconfigure包，大多数常用的第三方的配置都自动集成了，像redis、es等，这里边有一个META-INF/spring.factories文件，这里边定义了所有需要加载的bean的全路径，spring会根据反射的原理，创建这些对象，放到IOC容器中，加载时需要的参数，通过JavaConfig的方式加载配置文件中的参数然后创建了对应的对象，这就是自动配置的原理


## 自己实现一个自动装配bean

- 自己创建一个spring config 类，可能实例化了其他类，做一系列事

```
public class HelloWorldConfig {
    @Bean
    public HelloWorld helloWorld() {
        return new HelloWorld();
    }
}
```

> 这里`HelloWorldConfig` 是一个创建helloWorld对象的类

-  创建自定义模块装配注解

```
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({HelloWorldConfig.class})
public @interface EnableHelloWorld {
}
```
> 这个注解是启用 `HelloWorldConfig` 对象

- 通过 xxxxAutoConfig 注解 + 提交判断 启用   `HelloWorldConfig` 对象

```
@Configuration //Spring 模式装配
@EnableHelloWorld //Spring @Enable模块装配
@ConditionOnSystemProperty(name = "happy", value = "balabala") //条件装配
public class HelloWorldAutoConfiguration {
}
```
> `ConditionOnSystemProperty` 是一个条件装配

- 在resources目录下创建META-INF文件夹并创建spring.factories文件中添加自动装配类

```
# Auto Configure
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.xxx.HelloWorldAutoConfiguration
```


## 原理

> SpringFactoriesLoader.loadFactoryNames()
扫描所有jar包类路径下，META-INF/spring.factories
把扫描到的这些文件的内容包装成properties对象
从properties中获取到EnableAutoConfiguration.class类(类名)对应的值,然后把他们添加在
容器中


- 将类路径下META-INF/spring.factories里面配置的所有EnableAutoConfiguration的值加入到了容器中

- 根据 Condition 条件 + properties 配置 进行判断要不要启用自动装配


## 课外知识

- 复合注解

@Component注解是一个底层的标记为spring管理的注解
```
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {

    String name() default "";
}
```

@Server 注解是标记注解server层的，但继承上面的Component注解

```
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface Service {
}
```

如果只是把server注解注解在类上, spring是怎么知道的呢？

```
@Server
public class HelloWorld{}
```

答案就是，递归扫描所有的注解, 通过注解得到要判断的提交

比如： condition , 然后根据condition的值进行实例类

通过import注解的values，反射实现导入的类的实例

```
    /**
     * interface java.lang.annotation.Documented 等 存在循环，导致内存溢出，所以需要排除java的源注解
     * @param classz
     */
    private static void getAnnos(Class<?> classz){
        Annotation[] annotations = classz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() != Deprecated.class &&
                    annotation.annotationType() != SuppressWarnings.class &&
                    annotation.annotationType() != Override.class &&
                    annotation.annotationType() != PostConstruct.class &&
                    annotation.annotationType() != PreDestroy.class &&
                    annotation.annotationType() != Resource.class &&
                    annotation.annotationType() != Resources.class &&
                    annotation.annotationType() != Generated.class &&
                    annotation.annotationType() != Target.class &&
                    annotation.annotationType() != Retention.class &&
                    annotation.annotationType() != Documented.class &&
                    annotation.annotationType() != Inherited.class
                    ) {
                if (annotation.annotationType() == Component.class){
                    System.out.println(" 存在注解 @Component ");
                }else{
                    getAnnos(annotation.annotationType());
                }
            }
        }
    }
```