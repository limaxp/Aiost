package com.pm.aiost.misc.packet.listen;

import java.util.List;

import org.bukkit.Bukkit;

import com.pm.aiost.Aiost;
import com.pm.aiost.event.AiostEventFactory;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketThing;
import com.pm.aiost.player.ServerPlayer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PacketPlayInUseEntity;

public class AiostPacketDecoder extends MessageToMessageDecoder<Packet<?>> {

	protected final ServerPlayer serverPlayer;

	public AiostPacketDecoder(final ServerPlayer serverPlayer) {
		this.serverPlayer = serverPlayer;
	}

	@Override
	protected void decode(ChannelHandlerContext chc, Packet<?> packet, List<Object> out) throws Exception {
		if (packet instanceof PacketPlayInUseEntity) {
			PacketPlayInUseEntity usePacket = (PacketPlayInUseEntity) packet;
			int id;
			try {
				id = (int) PacketFactory.USE_ENTITY_ID_GET.invoke(usePacket);
			} catch (Throwable e) {
				Logger.err("AiostPacketDecoder: Error on getting PacketPlayInUseEntity id", e);
				return;
			}

			PacketThing packetThing = serverPlayer.getServerWorld().getPacketThing(id);
			if (packetThing != null) {
				switch (usePacket.b()) {
				case ATTACK:
					Bukkit.getScheduler().runTask(Aiost.getPlugin(), () -> {
						if (!AiostEventFactory.callPacketThingAttackEvent(serverPlayer, packetThing).isCancelled())
							packetThing.onPlayerAttack(serverPlayer);
					});
					break;

				case INTERACT_AT:
					Bukkit.getScheduler().runTask(Aiost.getPlugin(), () -> {
						if (!AiostEventFactory.callPacketThingInteractEvent(serverPlayer, packetThing).isCancelled())
							packetThing.onPlayerInteract(serverPlayer);
					});
					break;

				case INTERACT:
					break;

				default:
					break;
				}
			}
		}

		out.add(packet);
	}
}
