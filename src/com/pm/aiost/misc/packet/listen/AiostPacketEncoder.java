package com.pm.aiost.misc.packet.listen;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.Player;

import com.pm.aiost.Aiost;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.packet.disguise.disguises.DisguiseFurniture;
import com.pm.aiost.misc.packet.object.objects.Furniture;
import com.pm.aiost.player.ServerPlayer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntity;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_15_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class AiostPacketEncoder extends MessageToMessageEncoder<Packet<?>> {

	protected final ServerPlayer serverPlayer;
	protected final Player player;

	public AiostPacketEncoder(final ServerPlayer serverPlayer) {
		this.serverPlayer = serverPlayer;
		this.player = serverPlayer.player;
	}

	@Override
	protected void encode(ChannelHandlerContext chc, Packet<?> packet, List<Object> out) throws Exception {
		if (packet instanceof PacketPlayOutNamedEntitySpawn) {
			try {
				Player sender = Bukkit.getPlayer((UUID) PacketFactory.NAMEDENTITYSPAWN_UUID_GET.invoke(packet));
				if (sender != null) {
					ServerPlayer senderServerPlayer = ServerPlayer.getByPlayer(sender);
					if (senderServerPlayer.hasDisguise()) {
						senderServerPlayer.getDisguise().addPackets(sender, out);
					}
				} else { // is NPC
					EntityPlayer entityPlayer = (EntityPlayer) ((CraftWorld) player.getWorld()).getHandle().entitiesById
							.get((int) PacketFactory.NAMEDENTITYSPAWN_ID_GET.invoke(packet));

					if (entityPlayer != null) { // is not PacketPlayer
						out.add(PacketFactory.packetPlayerInfo_(EnumPlayerInfoAction.ADD_PLAYER,
								entityPlayer.getProfile()));
						Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(),
								() -> PacketSender.send(player, PacketFactory.packetPlayerInfo_(
										EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer.getProfile())),
								10);
					}
				}
			} catch (Throwable e) {
				Logger.err("AiostPacketEncoder: Error on PacketPlayOutNamedEntitySpawn!", e);
			}
		}

		else if (packet instanceof PacketPlayOutEntityMetadata) {
			int senderId;
			try {
				senderId = (int) PacketFactory.ENTITYMETADATA_ID_GET.invoke(packet);
			} catch (Throwable e) {
				Logger.err("AiostPacketEncoder: Error on getting PacketPlayOutEntityMetadata id", e);
				return;
			}
			Entity entity = ((CraftWorld) player.getWorld()).getHandle().entitiesById.get(senderId);
			if (entity instanceof EntityPlayer) {
				ServerPlayer senderServerPlayer = ServerPlayer.getByPlayer(((EntityPlayer) entity).getBukkitEntity());
				if (senderServerPlayer != null && senderServerPlayer.hasDisguise()
						&& senderServerPlayer.getDisguise() instanceof DisguiseFurniture) {
					if (senderServerPlayer != serverPlayer) {
						out.add(PacketFactory.packetEntityMetadata(senderId, Furniture.DATA_WATCHER, true));
						return;
					}
				}
			}
		}

		else if (packet instanceof PacketPlayOutEntity) {
			try {
				Entity entity = ((CraftWorld) player.getWorld()).getHandle().entitiesById
						.get((int) PacketFactory.ENTITY_ID_GET.invoke(packet));
				if (entity instanceof EntityPlayer) {
					ServerPlayer senderServerPlayer = ServerPlayer
							.getByPlayer(((EntityPlayer) entity).getBukkitEntity());
					if (senderServerPlayer != null && senderServerPlayer.hasDisguise()
							&& senderServerPlayer.getDisguise() instanceof DisguiseFurniture) {
						PacketFactory.ENTITY_Y_SET.invoke(packet,
								(short) PacketFactory.ENTITY_Y_GET.invoke(packet) - 1.188);
					}
				}
			} catch (Throwable e) {
				Logger.err("AiostPacketEncoder: Error on PacketPlayOutEntity!", e);
			}
		}

		else if (packet instanceof PacketPlayOutEntityTeleport) {
			try {
				Entity entity = ((CraftWorld) player.getWorld()).getHandle().entitiesById
						.get((int) PacketFactory.ENTITYTELEPORT_ID_GET.invoke(packet));
				if (entity instanceof EntityPlayer) {
					ServerPlayer senderServerPlayer = ServerPlayer
							.getByPlayer(((EntityPlayer) entity).getBukkitEntity());
					if (senderServerPlayer != null && senderServerPlayer.hasDisguise()
							&& senderServerPlayer.getDisguise() instanceof DisguiseFurniture) {

						PacketFactory.ENTITYTELEPORT_Y_SET.invoke(packet,
								(short) PacketFactory.ENTITYTELEPORT_Y_GET.invoke(packet) - 1.188);
					}
				}
			} catch (Throwable e) {
				Logger.err("AiostPacketEncoder: Error on PacketPlayOutEntityTeleport!", e);
			}
		}

		else if (packet instanceof PacketPlayOutEntityEquipment) {
			int senderId;
			try {
				senderId = (int) PacketFactory.ENTITYEQUIPMENT_ID_GET.invoke(packet);
			} catch (Throwable e) {
				Logger.err("AiostPacketEncoder: Error on getting PacketPlayOutEntityEquipment id", e);
				return;
			}
			Entity entity = ((CraftWorld) player.getWorld()).getHandle().entitiesById.get(senderId);
			if (entity instanceof EntityPlayer) {
				ServerPlayer senderServerPlayer = ServerPlayer.getByPlayer(((EntityPlayer) entity).getBukkitEntity());
				if (senderServerPlayer != null && senderServerPlayer.hasDisguise()
						&& senderServerPlayer.getDisguise() instanceof DisguiseFurniture) {
					out.add(PacketFactory.packetEntityEquipment(senderId, EnumItemSlot.HEAD,
							((DisguiseFurniture) senderServerPlayer.getDisguise()).getItemStackDirect()));
					return;
				}
			}
		}

		out.add(packet);
	}
}
