package com.pm.aiost.event.eventHandler.handler;

import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.event.events.PacketThingAttackEvent;
import com.pm.aiost.misc.scoreboard.scoreboards.SurvivalScoreboard;
import com.pm.aiost.player.ServerPlayer;

public interface SurvivalEventHandler extends EventHandler {

	public static final SurvivalEventHandler INSTANCE = new SurvivalEventHandler() {
	};

	public static SurvivalEventHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public default void onPlayerJoin(ServerPlayer serverPlayer) {
		SurvivalScoreboard.addPlayer(serverPlayer);
	}

	@Override
	public default void onPlayerQuit(ServerPlayer serverPlayer, QuitReason reason) {
		if (reason != QuitReason.DISABLE)
			SurvivalScoreboard.removePlayer(serverPlayer);
	}

	@Override
	public default void onPacketThingAttack(PacketThingAttackEvent event) {
		event.getPacketThing().defaultPlayerAttack(event);
	}

	@Override
	public default boolean canModifyWorld(ServerPlayer serverPlayer) {
		return true;
	}

	@Override
	public default String getEventHandlerName() {
		return "Survival";
	}
}
