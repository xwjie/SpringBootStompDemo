package cn.xiaowenjie.basewebsocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 支持websocket
 * 
 * @author 肖文杰 https://github.com/xwjie/
 */
@Configuration
public class BaseWebSocketConfig {
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
}