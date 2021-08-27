---
submitToken: 7E99A3BAE0110F1215801B14049D78B5D8AB1272518FD142560EABAEEF478EB7
title: springmvc的工作流程
channel: topic
labels: spring
---

- 基于servlet实现

- 在servlet的中post方法中做转发

- 在启动spring的时候会扫描所有的注解，将controller放到一个hashmap中

- 然后在url请求的时候去hashmap通过key去匹配具体的method

- 然后在反射调用方法

>             method.invoke(this.controllerMap.get(url), paramValues);//第一个参数是method所对应的实例 在ioc容器中, 第2个参数是形参列表


- 然后在返回数据


```
   //模拟九大组件初始化
        System.out.println(" spring mvc 初始化开始");
        //第1大组件 1.加载配置文件
        initLoadConfig(config.getInitParameter("contextConfigLocation"));

        //2.初始化所有相关联的类,扫描用户设定的包下面所有的类 (需要依赖注入的类)
        doScanner(properties.getProperty("scanPackage"));

        //3.拿到扫描到的类,通过反射机制,实例化,并且放到ioc容器中(k-v  beanName-bean) beanName默认是首字母小写
        doInstance();

        // 4.将IOC容器中的service对象设置给controller层定义的field上
        doIoc();

        //第4大组件 .初始化HandlerMapping(将url和method对应上)
        initHandlerMappings();

        System.out.println(" spring mvc 初始化结束");
```