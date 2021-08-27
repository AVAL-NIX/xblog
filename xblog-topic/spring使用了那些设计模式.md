---
submitToken: DF4E75055A0BAD04EE7635DCDB5ACB5A35FC158CD53286CA4DA232FD72A03E7E
title: spring使用了那些设计模式?
channel: topic
labels: spring
---


- 1.工厂模式，这个很明显，在各种BeanFactory以及ApplicationContext创建中都用到了

- 2.模版模式，这个也很明显，在各种BeanFactory以及ApplicationContext实现中也都用到了

- 3.代理模式，在Aop实现中用到了JDK的动态代理；

- 4.策略模式，第一个地方，加载资源文件的方式，使用了不同的方法，比如：ClassPathResourece，FileSystemResource，ServletContextResource，UrlResource但他们都有共同的借口Resource；第二个地方就是在Aop的实现中，采用了两种不同的方式，JDK动态代理和CGLIB代理；

- 5.单例模式，这个比如在创建bean的时候。
