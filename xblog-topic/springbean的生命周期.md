---
submitToken: 81CDD9C09CD82413364DEF14FA696BAA73010DE4D5D98B0FBF8805E7690FA04E
title: springbean的生命周期
channel: topic
labels: spring
---



- Spring启动，查找并加载需要被Spring管理的bean，进行Bean的实例化

- Bean实例化后对将Bean的引入和值注入到Bean的属性中

- 如果Bean实现了BeanNameAware接口的话，Spring将Bean的Id传递给setBeanName()方法

- 如果Bean实现了BeanFactoryAware接口的话，Spring将调用setBeanFactory()方法，将BeanFactory容器实例传入

- 如果Bean实现了ApplicationContextAware接口的话，Spring将调用Bean的setApplicationContext()方法，将bean所在应用上下文引用传入进来。

- 如果Bean实现了BeanPostProcessor接口，Spring就将调用他们的postProcessBeforeInitialization()方法。

- 如果Bean 实现了InitializingBean接口，Spring将调用他们的afterPropertiesSet()方法。类似的，如果bean使用init-method声明了初始化方法，该方法也会被调用

- 如果Bean 实现了BeanPostProcessor接口，Spring就将调用他们的postProcessAfterInitialization()方法。

- 此时，Bean已经准备就绪，可以被应用程序使用了。他们将一直驻留在应用上下文中，直到应用上下文被销毁。

- 如果bean实现了DisposableBean接口，Spring将调用它的destory()接口方法，同样，如果bean使用了destory-method 声明销毁方法，该方法也会被调用。
