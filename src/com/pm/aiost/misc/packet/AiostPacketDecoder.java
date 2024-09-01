package com.pm.aiost.misc.packet;

import java.util.List;

import org.bukkit.Bukkit;

import com.pm.aiost.Aiost;
import com.pm.aiost.misc.event.AiostEventFactory;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.packet.object.PacketObject;
import com.pm.aiost.player.ServerPlayer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;

public class AiostPacketDecoder extends MessageToMessageDecoder<Packet<?>> {

	protected final ServerPlayer serverPlayer;

	public AiostPacketDecoder(final ServerPlayer serverPlayer) {
		this.serverPlayer = serverPlayer;
	}

	@Override
	protected void decode(ChannelHandlerContext chc, Packet<?> packet, List<Object> out) throws Exception {
		if (packet instanceof ServerboundInteractPacket) {
			ServerboundInteractPacket usePacket = (ServerboundInteractPacket) packet;
			int id;
			try {
				id = (int) NMS.SERVERBOUNDINTERACTPACKET_GET_ID.invoke(usePacket);
			} catch (Throwable e) {
				Logger.err("AiostPacketDecoder: Error on getting ServerboundInteractPacket id", e);
				return;
			}

			PacketObject packetObject = serverPlayer.getServerWorld().getPacketObject(id);
			if (packetObject != null) {
				usePacket.dispatch(new ServerboundInteractPacket.Handler() {

					public void onInteraction(InteractionHand var1) {
						Logger.log("AiostPacketDecoder: interact");
						Bukkit.getScheduler().runTask(Aiost.getPlugin(), () -> {
							if (!AiostEventFactory.callPacketObjectInteractEvent(serverPlayer, packetObject)
									.isCancelled())
								packetObject.onPlayerInteract(serverPlayer);
						});
					}

					public void onInteraction(InteractionHand var1, Vec3 var2) {
						Logger.log("AiostPacketDecoder: interact2");
					}

					public void onAttack() {
						Logger.log("AiostPacketDecoder: attack");
						Bukkit.getScheduler().runTask(Aiost.getPlugin(), () -> {
							if (!AiostEventFactory.callPacketObjectAttackEvent(serverPlayer, packetObject)
									.isCancelled())
								packetObject.onPlayerAttack(serverPlayer);
						});
					}
				});
			}
		}

		out.add(packet);
	}
}
