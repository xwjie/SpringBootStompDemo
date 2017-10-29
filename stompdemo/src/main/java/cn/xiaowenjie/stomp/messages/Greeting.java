package cn.xiaowenjie.stomp.messages;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Greeting {

	private String content;
}
