package com.pm.aiost.server;

import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.event.eventHandler.handler.LobbyEventHandler;
import com.pm.aiost.event.eventHandler.handler.SurvivalEventHandler;

public class ServerTypeEventHandler {

	static {
		register(ServerType.LOBBY, LobbyEventHandler.INSTANCE);
		register(ServerType.SURVIVAL, SurvivalEventHandler.INSTANCE);
		register(ServerType.GAME, LobbyEventHandler.INSTANCE);
	}

	public static void register(ServerType serverType, EventHandler eventHandler) {
		serverType.setEventHandler(eventHandler);
	}

	public static EventHandler get(ServerType serverType) {
		if (serverType.getEventHandler() == null)
			return (EventHandler) ServerManager.DEFAULT_SERVER_TYPE.getEventHandler();
		return (EventHandler) serverType.getEventHandler();
	}
}
