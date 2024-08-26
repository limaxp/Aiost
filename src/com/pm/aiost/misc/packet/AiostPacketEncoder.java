package com.pm.aiost.misc.packet;

import java.util.List;

import org.bukkit.entity.Player;

import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.packet.disguise.Disguise;
import com.pm.aiost.misc.packet.disguise.DisguiseManager;
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
import net.minecraft.world.entity.LivingEntity;

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
			Entity entity = NMS.getEntity(player.getWorld(), ((ClientboundAddEntityPacket) packet).getId());
			if (entity instanceof LivingEntity) {
				org.bukkit.entity.LivingEntity bukkitEntity = NMS.from((LivingEntity) entity);
				Disguise disguise = DisguiseManager.getDisguise(bukkitEntity);
				if (disguise != null) {
					disguise.addPackets(bukkitEntity, out);
					return;
				}
			}
		}

		else if (packet instanceof ClientboundSetEntityDataPacket) {
			int senderId = ((ClientboundSetEntityDataPacket) packet).id();
			Entity entity = NMS.getEntity(player.getWorld(), senderId);
			if (entity instanceof LivingEntity) {
				org.bukkit.entity.LivingEntity bukkitEntity = NMS.from((LivingEntity) entity);
				Disguise disguise = DisguiseManager.getDisguise(bukkitEntity);
				if (disguise instanceof DisguiseFurniture) {
					if (bukkitEntity != player) {
						out.add(PacketFactory.packetEntityMetadata(senderId, Furniture.DATA_WATCHER));
						return;
					}
				}
			}
		}

		else if (packet instanceof ClientboundMoveEntityPacket) {
			try {
				int senderId = (int) NMS.ENTITYMOVE_GET_ENTITY_ID.invoke(packet);
				ClientboundMoveEntityPacket movePacket = (ClientboundMoveEntityPacket) packet;
				Entity entity = NMS.getEntity(player.getWorld(), senderId);
				if (entity instanceof LivingEntity) {
					org.bukkit.entity.LivingEntity bukkitEntity = NMS.from((LivingEntity) entity);
					Disguise disguise = DisguiseManager.getDisguise(bukkitEntity);
					if (disguise instanceof DisguiseFurniture)
						NMS.ENTITYMOVE_SET_YA.invoke(packet, movePacket.getYa() - 1.188);
				}
			} catch (Throwable e) {
				Logger.err("AiostPacketEncoder: Error on setting ya in ClientboundMoveEntityPacket", e);
			}
		}

		else if (packet instanceof ClientboundTeleportEntityPacket) {
			ClientboundTeleportEntityPacket teleportPacket = (ClientboundTeleportEntityPacket) packet;
			Entity entity = NMS.getEntity(player.getWorld(), teleportPacket.getId());
			if (entity instanceof LivingEntity) {
				org.bukkit.entity.LivingEntity bukkitEntity = NMS.from((LivingEntity) entity);
				Disguise disguise = DisguiseManager.getDisguise(bukkitEntity);
				if (disguise instanceof DisguiseFurniture) {
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
			Entity entity = NMS.getEntity(player.getWorld(), senderId);
			if (entity instanceof LivingEntity) {
				org.bukkit.entity.LivingEntity bukkitEntity = NMS.from((LivingEntity) entity);
				Disguise disguise = DisguiseManager.getDisguise(bukkitEntity);
				if (disguise instanceof DisguiseFurniture) {
					out.add(PacketFactory.packetEntityEquipment(senderId, EquipmentSlot.HEAD,
							NMS.to(((DisguiseFurniture) disguise).getItemStackDirect())));
					return;
				}
			}
		}

		out.add(packet);
	}
}