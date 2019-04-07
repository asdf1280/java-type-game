package type.common.work;

import io.netty.util.AttributeKey;
import type.common.handler.ChannelState;

public class AttributeSaver {
	public static AttributeKey<String> username = AttributeKey.newInstance("username");
	public static AttributeKey<ChannelState> state = AttributeKey.newInstance("state");
	public static AttributeKey<Boolean> loggedin = AttributeKey.newInstance("loggedin");
	public static AttributeKey<String> id = AttributeKey.newInstance("id");
}
