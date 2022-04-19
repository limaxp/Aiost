package com.pm.aiost.misc.packet;

import java.lang.invoke.MethodHandle;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.mojang.authlib.GameProfile;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.nms.NMSUtils;
import com.pm.aiost.misc.utils.reflection.ReflectionUtils;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.ChatMessage;
import net.minecraft.server.v1_15_R1.ChatMessageType;
import net.minecraft.server.v1_15_R1.Container;
import net.minecraft.server.v1_15_R1.Containers;
import net.minecraft.server.v1_15_R1.DataWatcher;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EnumGamemode;
import net.minecraft.server.v1_15_R1.EnumHand;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.IScoreboardCriteria.EnumScoreboardHealthDisplay;
import net.minecraft.server.v1_15_R1.PacketPlayOutAnimation;
import net.minecraft.server.v1_15_R1.PacketPlayOutChat;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntity;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_15_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_15_R1.PacketPlayOutOpenBook;
import net.minecraft.server.v1_15_R1.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo.PlayerInfoData;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_15_R1.PacketPlayOutPosition;
import net.minecraft.server.v1_15_R1.PacketPlayOutPosition.EnumPlayerTeleportFlags;
import net.minecraft.server.v1_15_R1.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_15_R1.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_15_R1.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_15_R1.PacketPlayOutSetSlot;
import net.minecraft.server.v1_15_R1.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_15_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_15_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_15_R1.ParticleParam;
import net.minecraft.server.v1_15_R1.ScoreboardObjective;
import net.minecraft.server.v1_15_R1.ScoreboardServer;
import net.minecraft.server.v1_15_R1.Vec3D;

public class PacketFactory {

	public static final EnumPlayerInfoAction ENUM_PLAYER_INFO_ACTION_ADD_PLAYER = EnumPlayerInfoAction.ADD_PLAYER;
	public static final EnumPlayerInfoAction ENUM_PLAYER_INFO_ACTION_REMOVE_PLAYER = EnumPlayerInfoAction.REMOVE_PLAYER;
	public static final EnumPlayerInfoAction ENUM_PLAYER_INFO_ACTION_UPDATE_DISPLAY_NAME = EnumPlayerInfoAction.UPDATE_DISPLAY_NAME;
	public static final EnumPlayerInfoAction ENUM_PLAYER_INFO_ACTION_UPDATE_GAMEMODE = EnumPlayerInfoAction.UPDATE_GAME_MODE;
	public static final EnumPlayerInfoAction ENUM_PLAYER_INFO_ACTION_UPDATE_LATENCY = EnumPlayerInfoAction.UPDATE_LATENCY;

	public static final Class<?> PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING_CLASS;
	public static final Class<?> PACKET_PLAY_OUT_ENTITY_TELEPORT_CLASS;
	public static final Class<?> PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN_CLASS;
	public static final Class<?> PACKET_PLAY_OUT_PLAYER_INFO_CLASS;
	public static final Class<?> PACKET_PLAY_OUT_ENTITY_HEAD_ROTATION_CLASS;
	public static final Class<?> PACKET_PLAY_OUT_ANIMATION_CLASS;
	public static final Class<?> PACKET_PLAY_OUT_ENTITY_STATUS_CLASS;
	public static final Class<?> PACKET_PLAY_OUT_SCOREBOARD_OBJECTIVE_CLASS;
	public static final Class<?> PACKET_PLAY_OUT_SCOREBOARD_DISPLAY_OBJECTIVE_CLASS;
	public static final Class<?> PACKET_PLAY_OUT_ENTITY_CLASS;
	public static final Class<?> PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CLASS;
	public static final Class<?> PACKET_PLAY_OUT_ENTITY_METADATA_CLASS;

	public static final Class<?> PACKET_PLAY_IN_USE_ENTITY_CLASS;

	public static final MethodHandle SPAWNENTITYLIVING_ID_SET;
	public static final MethodHandle SPAWNENTITYLIVING_UUID_SET;
	public static final MethodHandle SPAWNENTITYLIVING_MOBID_SET;
	public static final MethodHandle SPAWNENTITYLIVING_X_SET;
	public static final MethodHandle SPAWNENTITYLIVING_Y_SET;
	public static final MethodHandle SPAWNENTITYLIVING_Z_SET;
	public static final MethodHandle SPAWNENTITYLIVING_G_SET;
	public static final MethodHandle SPAWNENTITYLIVING_H_SET;
	public static final MethodHandle SPAWNENTITYLIVING_I_SET;
	public static final MethodHandle SPAWNENTITYLIVING_YAW_SET;
	public static final MethodHandle SPAWNENTITYLIVING_PITCH_SET;
	public static final MethodHandle SPAWNENTITYLIVING_L_SET;

	public static final MethodHandle ENTITYTELEPORT_ID_SET;
	public static final MethodHandle ENTITYTELEPORT_ID_GET;
	public static final MethodHandle ENTITYTELEPORT_X_SET;
	public static final MethodHandle ENTITYTELEPORT_Y_SET;
	public static final MethodHandle ENTITYTELEPORT_Y_GET;
	public static final MethodHandle ENTITYTELEPORT_Z_SET;
	public static final MethodHandle ENTITYTELEPORT_YAW_SET;
	public static final MethodHandle ENTITYTELEPORT_PITCH_SET;
	public static final MethodHandle ENTITYTELEPORT_ONGROUND_SET;

	public static final MethodHandle NAMEDENTITYSPAWN_ID_SET;
	public static final MethodHandle NAMEDENTITYSPAWN_ID_GET;
	public static final MethodHandle NAMEDENTITYSPAWN_UUID_SET;
	public static final MethodHandle NAMEDENTITYSPAWN_UUID_GET;
	public static final MethodHandle NAMEDENTITYSPAWN_X_SET;
	public static final MethodHandle NAMEDENTITYSPAWN_Y_SET;
	public static final MethodHandle NAMEDENTITYSPAWN_Z_SET;
	public static final MethodHandle NAMEDENTITYSPAWN_YAW_SET;
	public static final MethodHandle NAMEDENTITYSPAWN_PITCH_SET;

	public static final MethodHandle PLAYERINFO_ACTION_SET;
	public static final MethodHandle PLAYERINFO_PLAYERLIST_SET;
	public static final MethodHandle PLAYERINFO_PLAYERLIST_GET;

	public static final MethodHandle ENTITYHEADROTATION_ID_SET;
	public static final MethodHandle ENTITYHEADROTATION_YAW_SET;

	public static final MethodHandle ANIMATION_ID_SET;
	public static final MethodHandle ANIMATION_ANIMATIONID_SET;

	public static final MethodHandle ENTITYSTATUS_ID_SET;
	public static final MethodHandle ENTITYSTATUS_STATUSID_SET;

	public static final MethodHandle SCOREBOARDOBJECTIVE_NAME_SET;
	public static final MethodHandle SCOREBOARDOBJECTIVE_DISPLAYNAME_SET;
	public static final MethodHandle SCOREBOARDOBJECTIVE_TYPE_SET;
	public static final MethodHandle SCOREBOARDOBJECTIVE_ACTION_SET;

	public static final MethodHandle SCOREBOARDDISPLAYOBJECTIVE_SLOTID_SET;
	public static final MethodHandle SCOREBOARDDISPLAYOBJECTIVE_NAME_SET;

	public static final MethodHandle USE_ENTITY_ID_GET;

	public static final MethodHandle ENTITY_ID_GET;
	public static final MethodHandle ENTITY_Y_GET;
	public static final MethodHandle ENTITY_Y_SET;

	public static final MethodHandle ENTITYEQUIPMENT_ID_GET;

	public static final MethodHandle ENTITYMETADATA_ID_GET;

	static {
		try {
			PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING_CLASS = NMSUtils.getNMSClass("PacketPlayOutSpawnEntityLiving");
			SPAWNENTITYLIVING_ID_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING_CLASS, "a");
			SPAWNENTITYLIVING_UUID_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING_CLASS,
					"b");
			SPAWNENTITYLIVING_MOBID_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING_CLASS,
					"c");
			SPAWNENTITYLIVING_X_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING_CLASS, "d");
			SPAWNENTITYLIVING_Y_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING_CLASS, "e");
			SPAWNENTITYLIVING_Z_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING_CLASS, "f");
			SPAWNENTITYLIVING_G_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING_CLASS, "g");
			SPAWNENTITYLIVING_H_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING_CLASS, "h");
			SPAWNENTITYLIVING_I_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING_CLASS, "i");
			SPAWNENTITYLIVING_YAW_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING_CLASS, "j");
			SPAWNENTITYLIVING_PITCH_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING_CLASS,
					"k");
			SPAWNENTITYLIVING_L_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING_CLASS, "l");

			PACKET_PLAY_OUT_ENTITY_TELEPORT_CLASS = NMSUtils.getNMSClass("PacketPlayOutEntityTeleport");
			ENTITYTELEPORT_ID_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_ENTITY_TELEPORT_CLASS, "a");
			ENTITYTELEPORT_ID_GET = ReflectionUtils.unreflectGetter(PACKET_PLAY_OUT_ENTITY_TELEPORT_CLASS, "a");
			ENTITYTELEPORT_X_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_ENTITY_TELEPORT_CLASS, "b");
			ENTITYTELEPORT_Y_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_ENTITY_TELEPORT_CLASS, "c");
			ENTITYTELEPORT_Y_GET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_ENTITY_TELEPORT_CLASS, "c");
			ENTITYTELEPORT_Z_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_ENTITY_TELEPORT_CLASS, "d");
			ENTITYTELEPORT_YAW_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_ENTITY_TELEPORT_CLASS, "e");
			ENTITYTELEPORT_PITCH_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_ENTITY_TELEPORT_CLASS, "f");
			ENTITYTELEPORT_ONGROUND_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_ENTITY_TELEPORT_CLASS, "g");

			PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN_CLASS = NMSUtils.getNMSClass("PacketPlayOutNamedEntitySpawn");
			NAMEDENTITYSPAWN_ID_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN_CLASS, "a");
			NAMEDENTITYSPAWN_ID_GET = ReflectionUtils.unreflectGetter(PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN_CLASS, "a");
			NAMEDENTITYSPAWN_UUID_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN_CLASS, "b");
			NAMEDENTITYSPAWN_UUID_GET = ReflectionUtils.unreflectGetter(PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN_CLASS, "b");
			NAMEDENTITYSPAWN_X_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN_CLASS, "c");
			NAMEDENTITYSPAWN_Y_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN_CLASS, "d");
			NAMEDENTITYSPAWN_Z_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN_CLASS, "e");
			NAMEDENTITYSPAWN_YAW_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN_CLASS, "f");
			NAMEDENTITYSPAWN_PITCH_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_NAMED_ENTITY_SPAWN_CLASS, "g");

			PACKET_PLAY_OUT_PLAYER_INFO_CLASS = NMSUtils.getNMSClass("PacketPlayOutPlayerInfo");
			PLAYERINFO_ACTION_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_PLAYER_INFO_CLASS, "a");
			PLAYERINFO_PLAYERLIST_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_PLAYER_INFO_CLASS, "b");
			PLAYERINFO_PLAYERLIST_GET = ReflectionUtils.unreflectGetter(PACKET_PLAY_OUT_PLAYER_INFO_CLASS, "b");

			PACKET_PLAY_OUT_ENTITY_HEAD_ROTATION_CLASS = NMSUtils.getNMSClass("PacketPlayOutEntityHeadRotation");
			ENTITYHEADROTATION_ID_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_ENTITY_HEAD_ROTATION_CLASS,
					"a");
			ENTITYHEADROTATION_YAW_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_ENTITY_HEAD_ROTATION_CLASS,
					"b");

			PACKET_PLAY_OUT_ANIMATION_CLASS = NMSUtils.getNMSClass("PacketPlayOutAnimation");
			ANIMATION_ID_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_ANIMATION_CLASS, "a");
			ANIMATION_ANIMATIONID_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_ANIMATION_CLASS, "b");

			PACKET_PLAY_OUT_ENTITY_STATUS_CLASS = NMSUtils.getNMSClass("PacketPlayOutEntityStatus");
			ENTITYSTATUS_ID_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_ENTITY_STATUS_CLASS, "a");
			ENTITYSTATUS_STATUSID_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_ENTITY_STATUS_CLASS, "b");

			PACKET_PLAY_OUT_SCOREBOARD_OBJECTIVE_CLASS = NMSUtils.getNMSClass("PacketPlayOutScoreboardObjective");
			SCOREBOARDOBJECTIVE_NAME_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_SCOREBOARD_OBJECTIVE_CLASS,
					"a");
			SCOREBOARDOBJECTIVE_DISPLAYNAME_SET = ReflectionUtils
					.unreflectSetter(PACKET_PLAY_OUT_SCOREBOARD_OBJECTIVE_CLASS, "b");
			SCOREBOARDOBJECTIVE_TYPE_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_SCOREBOARD_OBJECTIVE_CLASS,
					"c");
			SCOREBOARDOBJECTIVE_ACTION_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_SCOREBOARD_OBJECTIVE_CLASS,
					"d");

			PACKET_PLAY_OUT_SCOREBOARD_DISPLAY_OBJECTIVE_CLASS = NMSUtils
					.getNMSClass("PacketPlayOutScoreboardDisplayObjective");
			SCOREBOARDDISPLAYOBJECTIVE_SLOTID_SET = ReflectionUtils
					.unreflectSetter(PACKET_PLAY_OUT_SCOREBOARD_DISPLAY_OBJECTIVE_CLASS, "a");
			SCOREBOARDDISPLAYOBJECTIVE_NAME_SET = ReflectionUtils
					.unreflectSetter(PACKET_PLAY_OUT_SCOREBOARD_DISPLAY_OBJECTIVE_CLASS, "b");

			PACKET_PLAY_IN_USE_ENTITY_CLASS = NMSUtils.getNMSClass("PacketPlayInUseEntity");
			USE_ENTITY_ID_GET = ReflectionUtils.unreflectGetter(PACKET_PLAY_IN_USE_ENTITY_CLASS, "a");

			PACKET_PLAY_OUT_ENTITY_CLASS = NMSUtils.getNMSClass("PacketPlayOutEntity");
			ENTITY_ID_GET = ReflectionUtils.unreflectGetter(PACKET_PLAY_OUT_ENTITY_CLASS, "a");
			ENTITY_Y_SET = ReflectionUtils.unreflectSetter(PACKET_PLAY_OUT_ENTITY_CLASS, "c");
			ENTITY_Y_GET = ReflectionUtils.unreflectGetter(PACKET_PLAY_OUT_ENTITY_CLASS, "c");

			PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CLASS = NMSUtils.getNMSClass("PacketPlayOutEntityEquipment");
			ENTITYEQUIPMENT_ID_GET = ReflectionUtils.unreflectGetter(PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CLASS, "a");

			PACKET_PLAY_OUT_ENTITY_METADATA_CLASS = NMSUtils.getNMSClass("PacketPlayOutEntityMetadata");
			ENTITYMETADATA_ID_GET = ReflectionUtils.unreflectGetter(PACKET_PLAY_OUT_ENTITY_METADATA_CLASS, "a");

		} catch (NoSuchFieldException | SecurityException | ClassNotFoundException | IllegalAccessException e) {
			Logger.err("PacketFactory: Error on packet field reflection!", e);
			throw new RuntimeException();
		}
	}

	public static PacketPlayOutSpawnEntity packetEntitySpawn(Entity entity) {
		return new PacketPlayOutSpawnEntity(entity);
	}

	public static PacketPlayOutSpawnEntity packetEntitySpawn(Entity entity, int flag) {
		return new PacketPlayOutSpawnEntity(entity, flag);
	}

	public static PacketPlayOutSpawnEntity packetEntitySpawn(Entity entity, EntityTypes<?> type, int flag,
			BlockPosition pos) {
		return new PacketPlayOutSpawnEntity(entity, type, flag, pos);
	}

	public static PacketPlayOutSpawnEntity packetEntitySpawn(int id, UUID uuid, double x, double y, double z, float yaw,
			float pitch, EntityTypes<?> type, int flag, Vec3D vec) {
		return new PacketPlayOutSpawnEntity(id, uuid, x, y, z, yaw, pitch, type, flag, vec);
	}

	public static PacketPlayOutEntityDestroy packetEntityDestroy(int... id) {
		return new PacketPlayOutEntityDestroy(id);
	}

	public static PacketPlayOutSpawnEntityLiving packetEntityLivingSpawn(EntityLiving entity) {
		return new PacketPlayOutSpawnEntityLiving(entity);
	}

	public static PacketPlayOutSpawnEntityLiving packetEntityLivingSpawn(EntityLiving entity, int id) {
		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entity);
		try {
			SPAWNENTITYLIVING_ID_SET.invoke(packet, id);
		} catch (Throwable e) {
			Logger.err("PacketFactory: Error on creating PacketPlayOutSpawnEntityLiving!", e);
		}
		return packet;
	}

	public static PacketPlayOutSpawnEntityLiving packetEntityLivingSpawn(int id, UUID uuid, int mobID, Location loc) {
		return packetEntityLivingSpawn(mobID, uuid, mobID, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(),
				loc.getPitch());
	}

	public static PacketPlayOutSpawnEntityLiving packetEntityLivingSpawn(int id, UUID uuid, int mobID, double x,
			double y, double z, float yaw, float pitch) {
		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
		try {
			SPAWNENTITYLIVING_ID_SET.invoke(packet, id);
			SPAWNENTITYLIVING_UUID_SET.invoke(packet, uuid);
			SPAWNENTITYLIVING_MOBID_SET.invoke(packet, mobID);
			SPAWNENTITYLIVING_X_SET.invoke(packet, x);
			SPAWNENTITYLIVING_Y_SET.invoke(packet, y);
			SPAWNENTITYLIVING_Z_SET.invoke(packet, z);
			SPAWNENTITYLIVING_G_SET.invoke(packet, 0);
			SPAWNENTITYLIVING_H_SET.invoke(packet, 0);
			SPAWNENTITYLIVING_I_SET.invoke(packet, 0);
			SPAWNENTITYLIVING_YAW_SET.invoke(packet, toCompressedAngle(yaw));
			SPAWNENTITYLIVING_PITCH_SET.invoke(packet, toCompressedAngle(pitch));
			SPAWNENTITYLIVING_L_SET.invoke(packet, (byte) 0);
		} catch (Throwable e) {
			Logger.err("PacketFactory: Error on creating PacketPlayOutSpawnEntityLiving!", e);
		}
		return packet;
	}

	public static PacketPlayOutNamedEntitySpawn packetNamedEntitySpawn(EntityHuman entity) {
		return new PacketPlayOutNamedEntitySpawn(entity);
	}

	public static PacketPlayOutNamedEntitySpawn packetNamedEntitySpawn(EntityHuman entity, int id) {
		PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn(entity);
		try {
			NAMEDENTITYSPAWN_ID_SET.invoke(packet, id);
		} catch (Throwable e) {
			Logger.err("PacketFactory: Error on creating PacketPlayOutNamedEntitySpawn!", e);
		}
		return packet;
	}

	public static PacketPlayOutNamedEntitySpawn packetNamedEntitySpawn(int id, UUID uuid, Location loc,
			DataWatcher dataWatcher) {
		return packetNamedEntitySpawn(id, uuid, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	}

	public static PacketPlayOutNamedEntitySpawn packetNamedEntitySpawn(int id, UUID uuid, double x, double y, double z,
			float yaw, float pitch) {
		PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
		try {
			NAMEDENTITYSPAWN_ID_SET.invoke(packet, id);
			NAMEDENTITYSPAWN_UUID_SET.invoke(packet, uuid);
			NAMEDENTITYSPAWN_X_SET.invoke(packet, x);
			NAMEDENTITYSPAWN_Y_SET.invoke(packet, y);
			NAMEDENTITYSPAWN_Z_SET.invoke(packet, z);
			NAMEDENTITYSPAWN_YAW_SET.invoke(packet, toCompressedAngle(yaw));
			NAMEDENTITYSPAWN_PITCH_SET.invoke(packet, toCompressedAngle(pitch));
		} catch (Throwable e) {
			Logger.err("PacketFactory: Error on creating PacketPlayOutNamedEntitySpawn!", e);
		}
		return packet;
	}

	public static PacketPlayOutEntityEquipment packetEntityEquipment(int id, EnumItemSlot slot, ItemStack is) {
		return new PacketPlayOutEntityEquipment(id, slot, CraftItemStack.asNMSCopy(is));
	}

	public static PacketPlayOutEntityEquipment packetEntityEquipment(int id, EnumItemSlot slot,
			net.minecraft.server.v1_15_R1.ItemStack is) {
		return new PacketPlayOutEntityEquipment(id, slot, is);
	}

	public static PacketPlayOutEntityMetadata packetEntityMetadata(int id, DataWatcher dataWatcher, boolean bool) {
		return new PacketPlayOutEntityMetadata(id, dataWatcher, bool);
	}

	public static PacketPlayOutPlayerInfo packetPlayerInfo(EnumPlayerInfoAction infoAction, EntityPlayer... player) {
		return new PacketPlayOutPlayerInfo(infoAction, player);
	}

	public static PacketPlayOutPlayerInfo packetPlayerInfo(EnumPlayerInfoAction infoAction,
			Iterable<EntityPlayer> player) {
		return new PacketPlayOutPlayerInfo(infoAction, player);
	}

	public static PacketPlayOutPlayerInfo packetPlayerInfo(GameProfile profile, int paramInt, EnumGamemode gamemode,
			IChatBaseComponent chatComponent, EnumPlayerInfoAction infoAction) {
		PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo();
		try {
			List<PlayerInfoData> players = (List<PlayerInfoData>) PLAYERINFO_PLAYERLIST_GET.invoke(playerInfo);
			players.add(playerInfo.new PlayerInfoData(profile, paramInt, gamemode, chatComponent));
			PLAYERINFO_ACTION_SET.invoke(playerInfo, infoAction);
			PLAYERINFO_PLAYERLIST_SET.invoke(playerInfo, players);
		} catch (Throwable e) {
			Logger.err("PacketFactory: Error on creating PacketPlayOutPlayerInfo!", e);
		}
		return playerInfo;
	}

	public static PacketPlayOutPlayerInfo packetPlayerInfo_(EnumPlayerInfoAction infoAction, GameProfile profile) {
		PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo();
		try {
			List<PlayerInfoData> players = (List<PlayerInfoData>) PLAYERINFO_PLAYERLIST_GET.invoke(playerInfo);
			players.add(playerInfo.new PlayerInfoData(profile, 0, EnumGamemode.SURVIVAL, null));
			PLAYERINFO_ACTION_SET.invoke(playerInfo, infoAction);
			PLAYERINFO_PLAYERLIST_SET.invoke(playerInfo, players);
		} catch (Throwable e) {
			Logger.err("PacketFactory: Error on creating PacketPlayOutPlayerInfo!", e);
		}
		return playerInfo;
	}

	public static PacketPlayOutPlayerInfo packetPlayerInfo_(EnumPlayerInfoAction infoAction,
			Iterable<GameProfile> profiles) {
		PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo();
		try {
			List<PlayerInfoData> players = (List<PlayerInfoData>) PLAYERINFO_PLAYERLIST_GET.invoke(playerInfo);
			for (GameProfile profile : profiles)
				players.add(playerInfo.new PlayerInfoData(profile, 0, EnumGamemode.SURVIVAL, null));
			PLAYERINFO_ACTION_SET.invoke(playerInfo, infoAction);
			PLAYERINFO_PLAYERLIST_SET.invoke(playerInfo, players);
		} catch (Throwable e) {
			Logger.err("PacketFactory: Error on creating PacketPlayOutPlayerInfo!", e);
		}
		return playerInfo;
	}

	public static PacketPlayOutRelEntityMoveLook packetRelEntityMoveLook(int id, short x, short y, short z, float yaw,
			float pitch, boolean onGround) {
		return new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(id, x, y, z, toCompressedAngle(yaw),
				toCompressedAngle(pitch), onGround);
	}

	public static PacketPlayOutRelEntityMove packetRelativeMove(int id, short x, short y, short z, boolean onGround) {
		return new PacketPlayOutEntity.PacketPlayOutRelEntityMove(id, x, y, z, onGround);
	}

	public static PacketPlayOutEntityTeleport packetEntityTeleport(Entity entity) {
		return new PacketPlayOutEntityTeleport(entity);
	}

	public static PacketPlayOutEntityTeleport packetEntityTeleport(int id, Location loc, boolean onGround) {
		return packetEntityTeleport(id, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), onGround);
	}

	public static PacketPlayOutEntityTeleport packetEntityTeleport(int id, double x, double y, double z, float yaw,
			float pitch, boolean onGround) {
		PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
		try {
			ENTITYTELEPORT_ID_SET.invoke(packet, id);
			ENTITYTELEPORT_X_SET.invoke(packet, x);
			ENTITYTELEPORT_Y_SET.invoke(packet, y);
			ENTITYTELEPORT_Z_SET.invoke(packet, z);
			ENTITYTELEPORT_YAW_SET.invoke(packet, toCompressedAngle(yaw));
			ENTITYTELEPORT_PITCH_SET.invoke(packet, toCompressedAngle(pitch));
			ENTITYTELEPORT_ONGROUND_SET.invoke(packet, onGround);
		} catch (Throwable e) {
			Logger.err("PacketFactory: Error on creating PacketPlayOutEntityTeleport!", e);
		}
		return packet;
	}

	public static PacketPlayOutPosition packetPosition(double x, double y, double z, float yaw, float pitch,
			Set<EnumPlayerTeleportFlags> teleportFlags, int id) {
		return new PacketPlayOutPosition(x, y, z, yaw, pitch, teleportFlags, id);
	}

	public static PacketPlayOutEntityHeadRotation packetHeadRotation(Entity entity, float yaw) {
		return new PacketPlayOutEntityHeadRotation(entity, toCompressedAngle(yaw));
	}

	public static PacketPlayOutEntityHeadRotation packetHeadRotation(int id, float yaw) {
		PacketPlayOutEntityHeadRotation packet = new PacketPlayOutEntityHeadRotation();
		try {
			ENTITYHEADROTATION_ID_SET.invoke(packet, id);
			ENTITYHEADROTATION_YAW_SET.invoke(packet, toCompressedAngle(yaw));
		} catch (Throwable e) {
			Logger.err("PacketFactory: Error on creating PacketPlayOutEntityHeadRotation!", e);
		}
		return packet;
	}

	public static PacketPlayOutAnimation packetAnimation(Entity entity, int animationId) {
		return new PacketPlayOutAnimation(entity, animationId);
	}

	public static PacketPlayOutAnimation packetAnimation(int id, int animationId) {
		PacketPlayOutAnimation packet = new PacketPlayOutAnimation();
		try {
			ANIMATION_ID_SET.invoke(packet, id);
			ANIMATION_ANIMATIONID_SET.invoke(packet, animationId);
		} catch (Throwable e) {
			Logger.err("PacketFactory: Error on creating PacketPlayOutAnimation!", e);
		}
		return packet;
	}

	public static PacketPlayOutEntityStatus packetEntityStatus(Entity entity, byte statusId) {
		return new PacketPlayOutEntityStatus(entity, statusId);
	}

	public static PacketPlayOutEntityStatus packetEntityStatus(int id, byte statusId) {
		PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus();
		try {
			ENTITYSTATUS_ID_SET.invoke(packet, id);
			ENTITYSTATUS_STATUSID_SET.invoke(packet, statusId);
		} catch (Throwable e) {
			Logger.err("PacketFactory: Error on creating PacketPlayOutEntityStatus!", e);
		}
		return packet;
	}

	public static PacketPlayOutScoreboardObjective packetScoreboardObjective(ScoreboardObjective objective,
			int action) {
		return new PacketPlayOutScoreboardObjective(objective, action);
	}

	public static PacketPlayOutScoreboardObjective packetScoreboardObjective(String name,
			IChatBaseComponent displayName, EnumScoreboardHealthDisplay scoreDisplayType, int action) {
		PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
		try {
			SCOREBOARDOBJECTIVE_NAME_SET.invoke(packet, name);
			SCOREBOARDOBJECTIVE_DISPLAYNAME_SET.invoke(packet, displayName);
			SCOREBOARDOBJECTIVE_TYPE_SET.invoke(packet, scoreDisplayType);
			SCOREBOARDOBJECTIVE_ACTION_SET.invoke(packet, action);
		} catch (Throwable e) {
			Logger.err("PacketFactory: Error on creating PacketPlayOutScoreboardObjective!", e);
		}
		return packet;
	}

	public static PacketPlayOutScoreboardDisplayObjective packetScoreboardDisplayObjective(int i,
			ScoreboardObjective objective) {
		return new PacketPlayOutScoreboardDisplayObjective(i, objective);
	}

	public static PacketPlayOutScoreboardDisplayObjective packetScoreboardDisplayObjective(int i, String name) {
		PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective();
		try {
			SCOREBOARDDISPLAYOBJECTIVE_SLOTID_SET.invoke(packet, i);
			SCOREBOARDDISPLAYOBJECTIVE_NAME_SET.invoke(packet, name);
		} catch (Throwable e) {
			Logger.err("PacketFactory: Error on creating PacketPlayOutScoreboardDisplayObjective!", e);
		}
		return packet;
	}

	public static PacketPlayOutScoreboardScore packetScoreboardScoreChange(@Nullable String objectiveName,
			String scoreName, int score) {
		return new PacketPlayOutScoreboardScore(ScoreboardServer.Action.CHANGE, objectiveName, scoreName, score);
	}

	public static PacketPlayOutScoreboardScore packetScoreboardScoreRemove(@Nullable String objectiveName,
			String scoreName) {
		return new PacketPlayOutScoreboardScore(ScoreboardServer.Action.REMOVE, objectiveName, scoreName, 0);
	}

	public static PacketPlayOutChat packetChat(String msg) {
		return new PacketPlayOutChat(new ChatMessage(msg));
	}

	public static PacketPlayOutChat packetChat(IChatBaseComponent chatComponent) {
		return new PacketPlayOutChat(chatComponent);
	}

	public static PacketPlayOutChat packetChat(String msg, ChatMessageType type) {
		return new PacketPlayOutChat(new ChatMessage(msg), type);
	}

	public static PacketPlayOutChat packetChat(IChatBaseComponent chatComponent, ChatMessageType type) {
		return new PacketPlayOutChat(chatComponent, type);
	}

	public static PacketPlayOutSetSlot packetSetSlot(int windowId, int slot, ItemStack is) {
		return new PacketPlayOutSetSlot(windowId, slot, CraftItemStack.asNMSCopy(is));
	}

	public static PacketPlayOutSetSlot packetSetSlot(int windowId, int slot,
			net.minecraft.server.v1_15_R1.ItemStack is) {
		return new PacketPlayOutSetSlot(windowId, slot, is);
	}

	public static <T extends ParticleParam> PacketPlayOutWorldParticles packetParticles(T particle,
			boolean longDistance, Location loc, float offsetX, float offsetY, float offsetZ, float data, int count) {
		return new PacketPlayOutWorldParticles(particle, longDistance, loc.getX(), loc.getY(), loc.getZ(), offsetX,
				offsetY, offsetZ, data, count);
	}

	public static <T extends ParticleParam> PacketPlayOutWorldParticles packetParticles(T particle,
			boolean longDistance, Location loc, float offset, float data, int count) {
		return new PacketPlayOutWorldParticles(particle, longDistance, loc.getX(), loc.getY(), loc.getZ(), offset,
				offset, offset, data, count);
	}

	public static <T extends ParticleParam> PacketPlayOutWorldParticles packetParticles(T particle,
			boolean longDistance, double x, double y, double z, float offsetX, float offsetY, float offsetZ, float data,
			int count) {
		return new PacketPlayOutWorldParticles(particle, longDistance, x, y, z, offsetX, offsetY, offsetZ, data, count);
	}

	public static <T extends ParticleParam> PacketPlayOutWorldParticles packetParticles(T particle,
			boolean longDistance, double x, double y, double z, float offset, float data, int count) {
		return new PacketPlayOutWorldParticles(particle, longDistance, x, y, z, offset, offset, offset, data, count);
	}

	public static PacketPlayOutOpenWindow packetOpenWindow(int windowId, Containers<?> containers, String title) {
		return new PacketPlayOutOpenWindow(windowId, containers, new ChatMessage(title));
	}

	public static PacketPlayOutOpenWindow packetOpenWindow(int windowId, Containers<?> containers,
			IChatBaseComponent title) {
		return new PacketPlayOutOpenWindow(windowId, containers, title);
	}

	public static PacketPlayOutOpenWindow packetOpenWindow(Container container) {
		return new PacketPlayOutOpenWindow(container.windowId, container.getType(), container.getTitle());
	}

	public static PacketPlayOutOpenWindow packetOpenWindow(Container container, String title) {
		return new PacketPlayOutOpenWindow(container.windowId, container.getType(), new ChatMessage(title));
	}

	public static PacketPlayOutOpenWindow packetOpenWindow(Container container, IChatBaseComponent title) {
		return new PacketPlayOutOpenWindow(container.windowId, container.getType(), title);
	}

	public static PacketPlayOutOpenBook packetOpenWindow(EquipmentSlot slot) {
		return slot == EquipmentSlot.HAND ? new PacketPlayOutOpenBook(EnumHand.MAIN_HAND)
				: new PacketPlayOutOpenBook(EnumHand.OFF_HAND);
	}

	public static PacketPlayOutOpenBook packetOpenWindow(EnumHand hand) {
		return new PacketPlayOutOpenBook(hand);
	}

	public static PacketPlayOutPlayerListHeaderFooter packetPlayerListHeaderFooter(String header, String footer) {
		return packetPlayerListHeaderFooter(new ChatMessage(header), new ChatMessage(footer));
	}

	public static PacketPlayOutPlayerListHeaderFooter packetPlayerListHeaderFooter(IChatBaseComponent header,
			IChatBaseComponent footer) {
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
		packet.header = header;
		packet.footer = footer;
		return packet;
	}

	private static byte toCompressedAngle(float f) {
		return (byte) (f * 256.0F / 360.0F);
	}
}
