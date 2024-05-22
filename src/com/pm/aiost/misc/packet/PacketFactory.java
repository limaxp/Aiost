package com.pm.aiost.misc.packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R4.CraftRegistry;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.nms.NMS;

import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.IntList;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.numbers.NumberFormat;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundOpenBookPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundResetScorePacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;

public class PacketFactory {

	public static ClientboundAddEntityPacket packetEntitySpawn(Entity entity) {
		return new ClientboundAddEntityPacket(entity);
	}

	public static ClientboundAddEntityPacket packetEntitySpawn(Entity entity, int flag) {
		return new ClientboundAddEntityPacket(entity, flag);
	}

	public static ClientboundAddEntityPacket packetEntitySpawn(Entity entity, int flag, BlockPos pos) {
		return new ClientboundAddEntityPacket(entity, flag, pos);
	}

	public static ClientboundAddEntityPacket packetEntitySpawn(int id, UUID uuid, double x, double y, double z,
			float yaw, float pitch, EntityType<?> type) {
		return packetEntitySpawn(id, uuid, x, y, z, yaw, pitch, type, 0, Vec3.ZERO, 0.0);
	}

	public static ClientboundAddEntityPacket packetEntitySpawn(int id, UUID uuid, double x, double y, double z,
			float yaw, float pitch, EntityType<?> type, int flag) {
		return packetEntitySpawn(id, uuid, x, y, z, yaw, pitch, type, flag, Vec3.ZERO, 0.0);
	}

	public static ClientboundAddEntityPacket packetEntitySpawn(int id, UUID uuid, double x, double y, double z,
			float yaw, float pitch, EntityType<?> type, int flag, Vec3 vec) {
		return packetEntitySpawn(id, uuid, x, y, z, yaw, pitch, type, flag, vec, 0.0);
	}

	public static ClientboundAddEntityPacket packetEntitySpawn(int id, UUID uuid, double x, double y, double z,
			float yaw, float pitch, EntityType<?> type, int flag, Vec3 vec, double d) {
		return new ClientboundAddEntityPacket(id, uuid, x, y, z, toCompressedAngle(yaw), toCompressedAngle(pitch), type,
				flag, vec, d);
	}

	public static ClientboundRemoveEntitiesPacket packetEntityDestroy(int... id) {
		return new ClientboundRemoveEntitiesPacket(id);
	}

	public static ClientboundRemoveEntitiesPacket packetEntityDestroy(IntList id) {
		return new ClientboundRemoveEntitiesPacket(id);
	}

	public static ClientboundSetEquipmentPacket packetEntityEquipment(int id,
			net.minecraft.world.entity.EquipmentSlot slot, net.minecraft.world.item.ItemStack item) {
		List<Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack>> list = new ArrayList<Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack>>();
		list.add(new Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack>(slot, item));
		return new ClientboundSetEquipmentPacket(id, list);
	}

	public static ClientboundSetEquipmentPacket packetEntityEquipment(int id,
			List<Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack>> list) {
		return new ClientboundSetEquipmentPacket(id, list);
	}

	public static ClientboundSetEntityDataPacket packetEntityMetadata(int id, List<DataValue<?>> data) {
		return new ClientboundSetEntityDataPacket(id, data);
	}

	public static ClientboundPlayerInfoUpdatePacket packetPlayerInfo(
			ClientboundPlayerInfoUpdatePacket.Action infoAction, ServerPlayer player) {
		return new ClientboundPlayerInfoUpdatePacket(infoAction, player);
	}

	public static ClientboundPlayerInfoUpdatePacket packetPlayerInfo(
			EnumSet<ClientboundPlayerInfoUpdatePacket.Action> infoAction, Collection<ServerPlayer> player) {
		return new ClientboundPlayerInfoUpdatePacket(infoAction, player);
	}

	public static ClientboundPlayerInfoUpdatePacket packetPlayerInfo(ClientboundPlayerInfoUpdatePacket.Action action,
			UUID uuid, GameProfile profile, int paramInt, GameType gamemode, Component chatComponent) {
		Set<ClientboundPlayerInfoUpdatePacket.Action> actionSet = new HashSet<ClientboundPlayerInfoUpdatePacket.Action>();
		List<ClientboundPlayerInfoUpdatePacket.Entry> entryList = new ArrayList<ClientboundPlayerInfoUpdatePacket.Entry>();
		actionSet.add(action);
		entryList.add(new ClientboundPlayerInfoUpdatePacket.Entry(uuid, profile, false, paramInt, gamemode,
				chatComponent, null));
		return packetPlayerInfo(actionSet, entryList);
	}

	public static ClientboundPlayerInfoUpdatePacket packetPlayerInfo(ClientboundPlayerInfoUpdatePacket.Action action,
			GameProfile profile) {
		Set<ClientboundPlayerInfoUpdatePacket.Action> actionSet = new HashSet<ClientboundPlayerInfoUpdatePacket.Action>();
		List<ClientboundPlayerInfoUpdatePacket.Entry> entryList = new ArrayList<ClientboundPlayerInfoUpdatePacket.Entry>();
		actionSet.add(action);
		entryList.add(new ClientboundPlayerInfoUpdatePacket.Entry(profile.getId(), profile, false, 0, GameType.SURVIVAL,
				NMS.createChatComponent(""), null));
		return packetPlayerInfo(actionSet, entryList);
	}

	public static ClientboundPlayerInfoUpdatePacket packetPlayerInfo(
			Set<ClientboundPlayerInfoUpdatePacket.Action> actionSet,
			List<ClientboundPlayerInfoUpdatePacket.Entry> entryList) {
		try {
			ClientboundPlayerInfoUpdatePacket packet = (ClientboundPlayerInfoUpdatePacket) NMS.PLAYERINFO_CONSTRUCTOR
					.invoke(new RegistryFriendlyByteBuf(Unpooled.buffer(0), CraftRegistry.getMinecraftRegistry()));
			NMS.PLAYERINFO_ACTIONSET_SET.invoke(packet, actionSet);
			NMS.PLAYERINFO_PLAYERLIST_SET.invoke(packet, entryList);
			return packet;
		} catch (Throwable e) {
			Logger.err("PacketFactory: Error on creating PacketPlayOutPlayerInfo!", e);
			return null;
		}
	}

	public static ClientboundPlayerInfoRemovePacket packetPlayerInfoRemove(UUID uuid) {
		return new ClientboundPlayerInfoRemovePacket(Arrays.asList(uuid));
	}

	public static ClientboundPlayerInfoRemovePacket packetPlayerInfoRemove(List<UUID> uuids) {
		return new ClientboundPlayerInfoRemovePacket(uuids);
	}

	public static ClientboundMoveEntityPacket.Pos packetRelativeMove(int id, short x, short y, short z,
			boolean onGround) {
		return new ClientboundMoveEntityPacket.Pos(id, x, y, z, onGround);
	}

	public static ClientboundMoveEntityPacket.Rot packetRelativeLook(int id, float yaw, float pitch, boolean onGround) {
		return new ClientboundMoveEntityPacket.Rot(id, toCompressedAngle(yaw), toCompressedAngle(pitch), onGround);
	}

	public static ClientboundMoveEntityPacket.PosRot packetRelEntityMoveLook(int id, short x, short y, short z,
			float yaw, float pitch, boolean onGround) {
		return new ClientboundMoveEntityPacket.PosRot(id, x, y, z, toCompressedAngle(yaw), toCompressedAngle(pitch),
				onGround);
	}

	public static ClientboundTeleportEntityPacket packetEntityTeleport(Entity entity) {
		return new ClientboundTeleportEntityPacket(entity);
	}

	public static ClientboundTeleportEntityPacket packetEntityTeleport(int id, Location loc, boolean onGround) {
		return packetEntityTeleport(id, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), onGround);
	}

	public static ClientboundTeleportEntityPacket packetEntityTeleport(int id, double x, double y, double z, float yaw,
			float pitch, boolean onGround) {
		FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer(31));
		buffer.writeVarInt(id);
		buffer.writeDouble(x);
		buffer.writeDouble(y);
		buffer.writeDouble(z);
		buffer.writeByte(toCompressedAngle(yaw));
		buffer.writeByte(toCompressedAngle(pitch));
		buffer.writeBoolean(onGround);
		try {
			return (ClientboundTeleportEntityPacket) NMS.ENTITYTELEPORT_CONSTRUCTOR.invoke(buffer);
		} catch (Throwable e) {
			Logger.err("PacketFactory: Error on creating PacketPlayOutEntityTeleport!", e);
			return null;
		}
	}

	public static ClientboundRotateHeadPacket packetHeadRotation(Entity entity, float yaw) {
		return new ClientboundRotateHeadPacket(entity, toCompressedAngle(yaw));
	}

	public static ClientboundAnimatePacket packetAnimation(Entity entity, int id) {
		return new ClientboundAnimatePacket(entity, id);
	}

	public static ClientboundEntityEventPacket packetEntityStatus(Entity entity, byte statusId) {
		return new ClientboundEntityEventPacket(entity, statusId);
	}

	public static ClientboundSetObjectivePacket packetScoreboardSetObjective(Objective objective, int method) {
		return new ClientboundSetObjectivePacket(objective, method);
	}

	public static ClientboundSetDisplayObjectivePacket packetScoreboardSetDisplayObjective(DisplaySlot slot,
			Objective objective) {
		return new ClientboundSetDisplayObjectivePacket(slot, objective);
	}

	public static ClientboundSetScorePacket packetScoreboardSetScore(String owner, String name, int score) {
		return packetScoreboardSetScore(owner, name, score, Optional.empty(), Optional.empty());
	}

	public static ClientboundSetScorePacket packetScoreboardSetScore(String owner, String name, int score,
			Optional<Component> display, Optional<NumberFormat> format) {
		return new ClientboundSetScorePacket(owner, name, score, display, format);
	}

	public static ClientboundResetScorePacket packetScoreboardResetScore(String owner, String name) {
		return new ClientboundResetScorePacket(owner, name);
	}

	public static ClientboundSystemChatPacket packetChat(BaseComponent[] components, boolean overlay) {
		return new ClientboundSystemChatPacket(components, overlay);
	}

	public static ClientboundSystemChatPacket packetChat(Component component, boolean overlay) {
		return new ClientboundSystemChatPacket(component, overlay);
	}

	public static ClientboundContainerSetSlotPacket packetSetSlot(int id, int slot, ItemStack is) {
		return new ClientboundContainerSetSlotPacket(id, 1, slot, CraftItemStack.asNMSCopy(is));
	}

	public static ClientboundContainerSetSlotPacket packetSetSlot(int id, int slot,
			net.minecraft.world.item.ItemStack is) {
		return new ClientboundContainerSetSlotPacket(id, 1, slot, is);
	}

	public static ClientboundContainerSetSlotPacket packetSetSlot(int id, int state, int slot, ItemStack is) {
		return new ClientboundContainerSetSlotPacket(id, state, slot, CraftItemStack.asNMSCopy(is));
	}

	public static ClientboundContainerSetSlotPacket packetSetSlot(int id, int state, int slot,
			net.minecraft.world.item.ItemStack is) {
		return new ClientboundContainerSetSlotPacket(id, state, slot, is);
	}

	public static <T extends ParticleOptions> ClientboundLevelParticlesPacket packetParticles(T particle,
			boolean longDistance, Location loc, float offsetX, float offsetY, float offsetZ, float data, int count) {
		return new ClientboundLevelParticlesPacket(particle, longDistance, loc.getX(), loc.getY(), loc.getZ(), offsetX,
				offsetY, offsetZ, data, count);
	}

	public static <T extends ParticleOptions> ClientboundLevelParticlesPacket packetParticles(T particle,
			boolean longDistance, Location loc, float offset, float data, int count) {
		return new ClientboundLevelParticlesPacket(particle, longDistance, loc.getX(), loc.getY(), loc.getZ(), offset,
				offset, offset, data, count);
	}

	public static <T extends ParticleOptions> ClientboundLevelParticlesPacket packetParticles(T particle,
			boolean longDistance, double x, double y, double z, float offsetX, float offsetY, float offsetZ, float data,
			int count) {
		return new ClientboundLevelParticlesPacket(particle, longDistance, x, y, z, offsetX, offsetY, offsetZ, data,
				count);
	}

	public static <T extends ParticleOptions> ClientboundLevelParticlesPacket packetParticles(T particle,
			boolean longDistance, double x, double y, double z, float offset, float data, int count) {
		return new ClientboundLevelParticlesPacket(particle, longDistance, x, y, z, offset, offset, offset, data,
				count);
	}

	public static ClientboundOpenScreenPacket packetOpenWindow(int windowId, MenuType<?> type, Component component) {
		return new ClientboundOpenScreenPacket(windowId, type, component);
	}

	public static ClientboundOpenScreenPacket packetOpenWindow(AbstractContainerMenu container) {
		return new ClientboundOpenScreenPacket(container.containerId, container.getType(), container.getTitle());
	}

	public static ClientboundOpenScreenPacket packetOpenWindow(AbstractContainerMenu container, Component title) {
		return new ClientboundOpenScreenPacket(container.containerId, container.getType(), title);
	}

	public static ClientboundOpenBookPacket packetOpenWindow(EquipmentSlot slot) {
		return slot == EquipmentSlot.HAND ? new ClientboundOpenBookPacket(InteractionHand.MAIN_HAND)
				: new ClientboundOpenBookPacket(InteractionHand.OFF_HAND);
	}

	public static ClientboundOpenBookPacket packetOpenWindow(InteractionHand hand) {
		return new ClientboundOpenBookPacket(hand);
	}

	public static ClientboundTabListPacket packetPlayerListHeaderFooter(Component header, Component footer) {
		return new ClientboundTabListPacket(header, footer);
	}

	private static byte toCompressedAngle(float f) {
		return (byte) (f * 256.0F / 360.0F);
	}
}
