package com.pm.aiost.event.events;

import javax.annotation.Nonnull;

import org.bukkit.event.Cancellable;

import com.pm.aiost.misc.packet.PacketThing;
import com.pm.aiost.player.ServerPlayer;

public class PacketThingAttackEvent extends ServerPlayerEvent implements Cancellable {

	protected PacketThing packetThing;
	private boolean cancelled;

	public PacketThingAttackEvent(@Nonnull ServerPlayer serverPlayer, @Nonnull PacketThing packetThing) {
		super(serverPlayer);
		this.packetThing = packetThing;
	}

	public PacketThing getPacketThing() {
		return packetThing;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
