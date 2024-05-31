package com.pm.aiost.misc.packet.listen;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.disguise.disguises.DisguiseFurniture;
import com.pm.aiost.misc.packet.object.objects.Furniture;
import com.pm.aiost.player.ServerPlayer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;

public class AiostPacketEncoder extends MessageToMessageEncoder<Packet<?>> {

	protected final ServerPlayer serverPlayer;
	protected final Player player;

	public AiostPacketEncoder(final ServerPlayer serverPlayer) {
		this.serverPlayer = serverPlayer;
		this.player = serverPlayer.player;
	}

	@Override
	protected void encode(ChannelHandlerContext chc, Packet<?> packet, List<Object> out) throws Exception {
		if (packet instanceof ClientboundAddEntityPacket) {
			Player sender = Bukkit.getPlayer(((ClientboundAddEntityPacket) packet).getUUID());
			if (sender != null) {
				ServerPlayer senderServerPlayer = ServerPlayer.getByPlayer(sender);
				if (senderServerPlayer.hasDisguise())
					senderServerPlayer.getDisguise().addPackets(sender, out);
			}
		}

		else if (packet instanceof ClientboundSetEntityDataPacket) {
			int senderId = ((ClientboundSetEntityDataPacket) packet).id();
			@SuppressWarnings({ "resource" })
			Entity entity = NMS.to(player.getWorld()).entityManager.getEntityGetter().get(senderId);
			if (entity instanceof net.minecraft.world.entity.player.Player) {
				ServerPlayer senderServerPlayer = ServerPlayer.getByPlayer((Player) NMS.from(entity));
				if (senderServerPlayer != null && senderServerPlayer.hasDisguise()
						&& senderServerPlayer.getDisguise() instanceof DisguiseFurniture) {
					if (senderServerPlayer != serverPlayer) {
						out.add(PacketFactory.packetEntityMetadata(senderId, Furniture.DATA_WATCHER));
						return;
					}
				}
			}
		}

		else if (packet instanceof ClientboundMoveEntityPacket) {
			ClientboundMoveEntityPacket movePacket = (ClientboundMoveEntityPacket) packet;
			Entity entity = ((ClientboundMoveEntityPacket) packet).getEntity(NMS.to(player.getWorld()));
			if (entity instanceof net.minecraft.world.entity.player.Player) {
				ServerPlayer senderServerPlayer = ServerPlayer.getByPlayer((Player) NMS.from(entity));
				if (senderServerPlayer != null && senderServerPlayer.hasDisguise()
						&& senderServerPlayer.getDisguise() instanceof DisguiseFurniture) {
					try {
						NMS.ENTITYMOVE_SET_YA.invoke(packet, movePacket.getYa() - 1.188);
					} catch (Throwable e) {
						Logger.err("AiostPacketEncoder: Error on setting ya in ClientboundMoveEntityPacket", e);
					}
				}
			}
		}

		else if (packet instanceof ClientboundTeleportEntityPacket) {
			ClientboundTeleportEntityPacket teleportPacket = (ClientboundTeleportEntityPacket) packet;
			@SuppressWarnings({ "resource" })
			Entity entity = NMS.to(player.getWorld()).entityManager.getEntityGetter().get(teleportPacket.getId());
			if (entity instanceof net.minecraft.world.entity.player.Player) {
				ServerPlayer senderServerPlayer = ServerPlayer.getByPlayer((Player) NMS.from(entity));
				if (senderServerPlayer != null && senderServerPlayer.hasDisguise()
						&& senderServerPlayer.getDisguise() instanceof DisguiseFurniture) {
					try {
						NMS.ENTITYTELEPORT_SET_Y.invoke(packet, teleportPacket.getY() - 1.188);
					} catch (Throwable e) {
						Logger.err("AiostPacketEncoder: Error on setting y in ClientboundTeleportEntityPacket", e);
					}
				}
			}
		}

		else if (packet instanceof ClientboundSetEquipmentPacket) {
			int senderId = ((ClientboundSetEquipmentPacket) packet).getEntity();
			@SuppressWarnings({ "resource" })
			Entity entity = NMS.to(player.getWorld()).entityManager.getEntityGetter().get(senderId);
			if (entity instanceof net.minecraft.world.entity.player.Player) {
				ServerPlayer senderServerPlayer = ServerPlayer.getByPlayer((Player) NMS.from(entity));
				if (senderServerPlayer != null && senderServerPlayer.hasDisguise()
						&& senderServerPlayer.getDisguise() instanceof DisguiseFurniture) {
					out.add(PacketFactory.packetEntityEquipment(senderId, EquipmentSlot.HEAD,
							NMS.to(((DisguiseFurniture) senderServerPlayer.getDisguise()).getItemStackDirect())));
					return;
				}
			}
		}

		out.add(packet);
	}
}
