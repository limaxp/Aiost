package com.pm.aiost.event.events;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.pm.aiost.player.ServerPlayer;

public abstract class ServerPlayerEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private ServerPlayer serverPlayer;

	protected ServerPlayerEvent(@Nonnull ServerPlayer serverPlayer) {
		this.serverPlayer = serverPlayer;
	}

	public ServerPlayer getServerPlayer() {
		return serverPlayer;
	}

	public Player getPlayer() {
		return serverPlayer.player;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
