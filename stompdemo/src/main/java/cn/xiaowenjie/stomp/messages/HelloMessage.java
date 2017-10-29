package cn.xiaowenjie.stomp.messages;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HelloMessage {

	private String name;
}
