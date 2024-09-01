package com.pm.aiost.misc.event.events;

import org.bukkit.event.Cancellable;

import com.pm.aiost.misc.packet.object.PacketObject;
import com.pm.aiost.player.ServerPlayer;

public class PacketObjectAttackEvent extends ServerPlayerEvent implements Cancellable {

	protected PacketObject packetObject;
	private boolean cancelled;

	public PacketObjectAttackEvent(ServerPlayer serverPlayer, PacketObject packetObject) {
		super(serverPlayer);
		this.packetObject = packetObject;
	}

	public PacketObject getPacketObject() {
		return packetObject;
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
