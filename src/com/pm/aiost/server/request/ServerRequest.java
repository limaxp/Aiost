package com.pm.aiost.server.request;

import com.pm.aiost.misc.SpigotConfig;

public class ServerRequest {

	private static ServerRequestHandler handler;

	static {
		if (SpigotConfig.HAS_BUNGEE)
			handler = new BungeecordServerRequestHandler();
		else
			handler = new StandaloneServerRequestHandler();
	}

	public static ServerRequestHandler getHandler() {
		return handler;
	}
}
