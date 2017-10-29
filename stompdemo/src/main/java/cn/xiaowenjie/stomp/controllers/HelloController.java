package cn.xiaowenjie.stomp.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import cn.xiaowenjie.stomp.messages.Greeting;
import cn.xiaowenjie.stomp.messages.HelloMessage;

@Controller
public class HelloController {

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting hello(HelloMessage hello) {
		System.out.println("hello:" + hello);
		return Greeting.builder().content("hello:" + hello.getName()).build();
	}
}
