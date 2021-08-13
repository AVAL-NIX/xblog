---
submitToken: 988C32265CA8E898C73B8AF86CDFCD844215DB495757B5574BAAC55D3368DDD4
title: 设计一个RPC系统
channel: topic
labels: base
---
![image.png](https://cdn.nlark.com/yuque/0/2021/png/410813/1619942162689-9414fce0-da0b-4288-938c-b36cc528919c.png#align=left&display=inline&height=824&margin=%5Bobject%20Object%5D&name=image.png&originHeight=824&originWidth=1087&size=426683&status=done&style=none&width=1087)
![image.png](https://cdn.nlark.com/yuque/0/2021/png/410813/1619942600127-02912898-9c42-4527-86cc-f8b020dda4d3.png#align=left&display=inline&height=633&margin=%5Bobject%20Object%5D&name=image.png&originHeight=633&originWidth=1074&size=283215&status=done&style=none&width=1074)
# 设计一个API接口
IHelloService.class
```
/**
 * @author zhengxin
 * @date 2021/5/1
 */
public interface IHelloService {

    String sayHello(String content);


    String saveUser(User user);
}

```
RpcRequest.class
```
public class RpcRequest implements Serializable {


    private String className;

    private String methodName;

    private Object[] parameters;


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
}
```
User.class
```
public class User {

    private String name;

    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
```
#  开发一个服务端(提供者）
Application.java
```
public class Application {

    public static void main(String[] args) {
       IHelloService helloService = new HelloServiceImpl();
       RpcProxyServer proxyServer = new RpcProxyServer();
       proxyServer.publisher(helloService, 8080);
    }

}
```
HelloServiceImpl.java
接口实现类
```
public class HelloServiceImpl implements IHelloService {


    @Override
    public String sayHello(String content) {
        System.out.println("  request in sayHello: " + content);
        return "Say Hello  sayHello : " + content;
    }

    @Override
    public String saveUser(User user) {
        System.out.println(" request in saveUser: " + user.toString());
        return "Say Hello saveUser : " + user.toString();
    }
}

```
ProcessorHandler.java
流程处理
```
public class ProcessorHandler implements Runnable {

    private Socket socket;
    //实例对象
    private Object service;

    public ProcessorHandler(Object server, Socket socket) {
        this.socket = socket;
        this.service = server;
    }

    @Override
    public void run() {
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;


        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            //输入流 ， 数据呗。 。。 。
            RpcRequest request = (RpcRequest) objectInputStream.readObject();
            Object result = invoke(request);

            //输出
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(objectOutputStream != null){
                    objectOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     *  反射调用
     * @param request
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private Object invoke(RpcRequest request) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //
        Object[] args = request.getParameters();
        Class<?>[] types = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            types[i] = args[i].getClass();
        }
        Class clazz = Class.forName(request.getClassName()); // 有可能是一个impl
        Method method = clazz.getMethod(request.getMethodName(), types);

        Object result = method.invoke(service, args);
        return result;
    }
}
```
RpcProxyServer.java
连接的代理类
```
public class RpcProxyServer {

    ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * server  是要调用的实例
     * @param server
     * @param port
     */
    public void publisher(Object server , int port) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(new ProcessorHandler(server, socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
```
# 编写一个客户端(消费者)
App.java
启动类
```
public class App {

    public static void main(String[] args) {
        RpcProxyClient rpcProxyClient = new RpcProxyClient();
        IHelloService iHelloService = rpcProxyClient.clientProxy(IHelloService.class, "localhost", 8080);
        String result = iHelloService.sayHello("mic");
        System.out.println(result);
    }
}
```
RemoteInvcationHandler
代理类
```
public class RemoteInvocationHandler implements InvocationHandler {

    private String host;
    private int port;


    public RemoteInvocationHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 代理调用
        System.out.println(" come in ");
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameters(args);
        // 远程网络通信
        RpcNetTransport netTransport = new RpcNetTransport(host,port);

        return netTransport.send(rpcRequest);
    }
}
```
RpcNetTransport.java
网络传输类
```
public class RpcNetTransport {

    private String host;

    private int port;


    public Object send(RpcRequest request) {

        Socket socket = null;
        Object result = null;
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        try {
            socket = new Socket(host, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(request);  //  序列化
            outputStream.flush();

            inputStream = new ObjectInputStream(socket.getInputStream());
            result = inputStream.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    public RpcNetTransport(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

```
RpcProxyClient.java
代理客户端
```
public class RpcProxyClient {


    public <T> T clientProxy(final Class<T> interfaceCls, final String host, final int port) {
        return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(), new Class<?>[]{interfaceCls}, new RemoteInvocationHandler(host, port));
    }
}
```
# 启动
先启动Application.java， 提供者
在启动App.java , 消费者
这样就成功了。 后续优化
