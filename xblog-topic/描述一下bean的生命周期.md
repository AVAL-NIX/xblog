---
submitToken: 681876B6E190A911C28C70BC03835451249645EB0594991CFEF4E90B64610742
title: 描述一下bean的生命周期
channel: topic
labels: spring
---

## BeanNameAware接口 (自己拿自己的bean名字)

如果某个bean需要访问配置文件中本身bean的id属性，这个bean类通过实现BeanNameAware接口，在依赖关系确定之后，初始化方法之前，提供回调自身的能力，从而获得本身bean的id属性，该接口提供了 void setBeanName(String name)方法，需要指出的时该方法的name参数就是该bean的id属性。回调该setBeanName方法可以让bean获取自身的id属性

## BeanFactoryAware接口（自己能拿到beanfactory的能力）

  实现了BeanFactoryAware接口的bean，可以直接通过beanfactory来访问spring的容器，当该bean被容器创建之后，会有一个相应的beanfactory的实例引用。该 接口有一个方法void setBeanFactory(BeanFactory beanFactory)方法通过这个方法的参数创建它的BeanFactory实例，实现了BeanFactoryAware接口，就可以让Bean拥有访问Spring容器的能力。

## ApplicationContextAware接口（bean能拿到上下文的能力）

在Bean类被初始化后，将会被注入applicationContext实例，该接口有一个方法，setApplicationContext(ApplicationContext context),使用其参数context用来创建它的applicationContext实例

## BeanPostProcessor接口

应该是在bean的实例化过程中对bean做一些包装处理，里边提供两个方法


　　1、在bean初始化之前执行

　　2、在bean的初始化之后执行
```
public abstract Object postProcessBeforeInitialization(Object obj, String s)
      throws BeansException;

  public abstract Object postProcessAfterInitialization(Object obj, String s)
      throws BeansException;
```

## 过程

Bean 的生命周期还是比较复杂的，下面来对上图每一个步骤做文字描述:

- Spring启动，查找并加载需要被Spring管理的bean，进行Bean的实例化

- Bean实例化后对将Bean的引入和值注入到Bean的属性中

- 如果Bean实现了BeanNameAware接口的话，Spring将Bean的Id传递给setBeanName()方法
- 如果Bean实现了BeanFactoryAware接口的话，Spring将调用setBeanFactory()方法，将BeanFactory容器实例传入
- 如果Bean实现了ApplicationContextAware接口的话，Spring将调用Bean的setApplicationContext()方法，将bean所在应用上下文引用传入进来
- 如果Bean实现了BeanPostProcessor接口，Spring就将调用他们的postProcessBeforeInitialization()方法。
- 如果Bean 实现了InitializingBean接口，Spring将调用他们的afterPropertiesSet()方法。类似的，如果bean使用init-method声明了初始化方法，该方法也会被调用
- 如果Bean 实现了BeanPostProcessor接口，Spring就将调用他们的postProcessAfterInitialization()方法。


此时，Bean已经准备就绪，可以被应用程序使用了。他们将一直驻留在应用上下文中，直到应用上下文被销毁。


- 如果bean实现了DisposableBean接口，Spring将调用它的destory()接口方法，同样，如果bean使用了destory-method 声明销毁方法，该方法也会被调用。
