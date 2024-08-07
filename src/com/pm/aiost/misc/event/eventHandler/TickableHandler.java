package com.pm.aiost.misc.event.eventHandler;

import org.bukkit.entity.Entity;

import com.pm.aiost.player.ServerPlayer;

public interface TickableHandler {

	public default void onTick(ServerPlayer serverPlayer) {
		onTick(serverPlayer.player);
	}

	public void onTick(Entity entity);
}
