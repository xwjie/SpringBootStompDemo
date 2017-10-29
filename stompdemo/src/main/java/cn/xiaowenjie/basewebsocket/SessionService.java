package cn.xiaowenjie.basewebsocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.Session;

import lombok.SneakyThrows;

/**
 * 处理websocket的Session
 * 
 * TODO：实际应用需要注意避免Map的强引用导致的内存泄漏
 * 
 * @author 肖文杰 https://github.com/xwjie/
 *
 */
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
