# SpringBoot Stomp Demo

STOMP is the Simple (or Streaming) Text Orientated Messaging Protocol. http://stomp.github.io/

STOMP 是在原生的WebSocket上在包装了一层。基本的websocket和stomp都测试一遍。

# 原生websocket

原生socket需要用 `@ServerEndpoint` 发布服务， 使用 `@OnOpen`, `@OnClose`, `@OnMessage` , `@OnError` 等监听事件即可！

## 配置websocket

```
@Configuration
public class BaseWebSocketConfig {
  @Bean
  public ServerEndpointExporter serverEndpointExporter() {
    return new ServerEndpointExporter();
  }
}
```

## 发布服务

使用 `@ServerEndpoint` 发布服务。

每一个链接会新创建一个实例，所以完全可以把Session做为属性放在类里面。

这个bean的作用域【应该】是prototype。`@ServerEndpoint` 在里面注入其他的bean没有成功。增加了 @Scope（TARGET_CLASS）也不行。

所以暂时用静态方法。

```Java
@ServerEndpoint(value = "/basews")
@Component
public class BaseWebSocket {

  public BaseWebSocket() {
    System.out.println("\n-----new BaseWebSocket-----\n");
  }

  // FIXME 无法注入bean ?
  // @Autowired
  // SessionService sessionService;

  @OnOpen
  public void onOpen(Session session) {
    SessionService.newSession(session);
  }

  @OnClose
  public void onClose(Session session) {
    SessionService.closeSession(session);
  }

  @OnMessage
  public void onMessage(String message, Session session) {
    System.out.println(session.getId() + " > OnMessage: " + message);
    SessionService.sendMessage(session, "this is a message from server!");
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    System.out.println(session.getId() + " > OnError: " + throwable.getMessage());

    throwable.printStackTrace();

    SessionService.closeSession(session);
  }

}
```

## Session管理

单独整了个类来集中处理Session。

```Java
public class SessionService {

  private static AtomicInteger count = new AtomicInteger();

  private static Map<String, Session> sessionMap = new ConcurrentHashMap<>();

  public static void newSession(Session session) {
    sessionMap.put(session.getId(), session);

    System.out.println("new session, now count=" + count.incrementAndGet());
  }

  public static void closeSession(Session session) {
    sessionMap.remove(session.getId());

    System.out.println("close session, now count=" + count.decrementAndGet());
  }

  /**
   * 发送消息到客户端
   * 
   * @param session
   * @param message
   */
  @SneakyThrows
  public static void sendMessage(Session session, String message) {
    session.getBasicRemote().sendText(message);
    // session.getAsyncRemote().sendText(message);
  }

}
```

## 测试

打开chrome，在console里面测试

```
var ws = new WebSocket("ws://127.0.0.1:8080/basews");
ws.onmessage = function(msg){console.log("接受到消息", msg)};
ws.send("hello");

// 在开一个链接
var ws2 = new WebSocket("ws://127.0.0.1:8080/basews");
```

![chrome](/pictures/chromeconsole.png)

![chrome](/pictures/base-sb1.png)

# WebSocket 协议简单分析

建立连接使用http协议，服务器返回101切换协议。因为是http协议，所以可以在小提琴fiddler上监控到。

![chrome](/pictures/fiddler1.png)

可以看到cookie也带了，就是一个普通的http请求，只是多了一些头。


101之后切换协议，之后就不再是http协议了（但是端口还是http的8080），fiddler上无法监控到，但chrome上可以看到对应的frame帧。

![chrome](/pictures/chromeconsole2.png)


发送内容需要使用抓包工具，这里用的是wireshark大白鲨。

![chrome](/pictures/wireshark1.png)


可以设别为websocket协议，可以对着协议看每一个字节的含义。

![chrome](/pictures/wireshark2.png)


大白鲨如何抓本机的包，参考这里 https://www.bbsmax.com/A/A2dmxVpg5e/ 。 我这里使用的第二种，第一种方法才看到。


# spring 笔记

## 随机端口
使用随机端口，就可以启动2个SpringBoot了。使用 `@LocalServerPort` 注入当前的服务的端口。

```Java
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GreetingIntegrationTests {

  @LocalServerPort
  private int port;

}
```

## 代码修改后自动重启

```XML
<!-- 更新代码自动重启 -->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-devtools</artifactId>
</dependency>
```