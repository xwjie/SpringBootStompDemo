package cn.xiaowenjie.basewebsocket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

/**
 * 发布一个名叫 basews 的websocket
 * 
 * @author 肖文杰 https://github.com/xwjie/
 */
@ServerEndpoint(value = "/basews")
@Component
public class BaseWebSocket {

	public BaseWebSocket() {
		System.out.println("\n-----new BaseWebSocket-----\n");
	}

	// FIXME @ServerEndpoint比较特殊，无法注入单例的bean ?
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