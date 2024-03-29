---
submitToken: 6415D19EE03D6C7FDDA04011D765C4EEF61EF9E472C3914763B83B26803C1BFE
title: 获取 Class 对象的四种方式
channel: topic
labels: juc
---
如果我们动态获取到这些信息，我们需要依靠 Class 对象。Class 类对象将一个类的方法、变量等信息告诉运行的程序。Java 提供了四种方式获取 Class 对象:

1.知道具体类的情况下可以使用：
```
Class alunbarClass = TargetObject.class;
```
但是我们一般是不知道具体类的，基本都是通过遍历包下面的类来获取 Class 对象，通过此方式获取Class对象不会进行初始化

2.通过 Class.forName()传入类的路径获取：

```
Class alunbarClass1 = Class.forName("cn.javaguide.TargetObject");
```

Class.forName(className)方法，内部实际调用的是一个native方法 forName0(className, true, ClassLoader.getClassLoader(caller), caller);
第2个boolean参数表示类是否需要初始化，Class.forName(className)默认是需要初始化。
一旦初始化，就会触发目标对象的 static块代码执行，static参数也会被再次初始化。

3.通过对象实例instance.getClass()获取：

```
Employee e = new Employee();
Class alunbarClass2 = e.getClass();
```

4.通过类加载器xxxClassLoader.loadClass()传入类路径获取

```
class clazz = ClassLoader.LoadClass("cn.javaguide.TargetObject");
```

通过类加载器获取Class对象不会进行初始化，意味着不进行包括初始化等一些列步骤，静态块和静态对象不会得到执行