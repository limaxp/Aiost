package com.pm.aiost.event.events;

import javax.annotation.Nonnull;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.pm.aiost.player.ServerPlayer;

public class PlayerJumpEvent extends ServerPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;

	public PlayerJumpEvent(@Nonnull ServerPlayer serverPlayer) {
		super(serverPlayer);
		cancelled = false;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}