package com.pm.aiost.event.eventHandler;

import java.util.function.Supplier;

import com.pm.aiost.event.eventHandler.handler.CancelEventHandler;
import com.pm.aiost.event.eventHandler.handler.DuelEventHandler;
import com.pm.aiost.event.eventHandler.handler.DuelRegionEventHandler;
import com.pm.aiost.event.eventHandler.handler.LobbyEventHandler;
import com.pm.aiost.event.eventHandler.handler.PlayerRegionEventHandler;
import com.pm.aiost.event.eventHandler.handler.PlayerWorldEventHandler;
import com.pm.aiost.event.eventHandler.handler.ReleasedWorldEventHandler;
import com.pm.aiost.event.eventHandler.handler.SpectatorEventHandler;
import com.pm.aiost.event.eventHandler.handler.SurvivalEventHandler;
import com.pm.aiost.game.GameLobby;
import com.pm.aiost.misc.registry.AiostRegistry;

public class EventHandlerRegistry {

	public static void init() {
		a(CancelEventHandler::getInstance);
		a(LobbyEventHandler::getInstance);
		a(SurvivalEventHandler::getInstance);
		a(PlayerRegionEventHandler::new);
		a(PlayerWorldEventHandler::new);
		a(ReleasedWorldEventHandler::new);
		a(GameLobby::new);
		a(SpectatorEventHandler::getInstance);
		a(DuelEventHandler::new);
		a(DuelRegionEventHandler::new);
	}

	public static void a(Supplier<EventHandler> supplier) {
		AiostRegistry.EVENT_HANDLER.register(supplier.get().getEventHandlerName(), supplier);
	}

	@SuppressWarnings("unchecked")
	public static Supplier<EventHandler>[] getRegionEventHandler() {
		return new Supplier[] { EventHandler.get("Lobby") };
	}
}
