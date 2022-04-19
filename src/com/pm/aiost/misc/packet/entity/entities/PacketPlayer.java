package com.pm.aiost.misc.packet.entity.entities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.pm.aiost.Aiost;
import com.pm.aiost.entity.dataWatcher.AiostDataWatcherObject;
import com.pm.aiost.entity.dataWatcher.AiostDataWatcherRegistry;
import com.pm.aiost.entity.dataWatcher.EmptyDataWatcher;
import com.pm.aiost.entity.npc.NpcBase;
import com.pm.aiost.entity.npc.profile.Profiles;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.packet.entity.PacketEntity;
import com.pm.aiost.misc.packet.entity.PacketEntityType;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.server.world.ServerWorld;

public class PacketPlayer extends PacketEntity {

	public static final AiostDataWatcherObject<Byte> SNIN_OVERLAY_VIEWABLE_WATCHER = new AiostDataWatcherObject<>(16,
			AiostDataWatcherRegistry.BYTE);
	protected GameProfile profile;
	protected EmptyDataWatcher dataWatcher;

	public PacketPlayer(ServerWorld world) {
		super(world);
		initDatawatcher();
	}

	public PacketPlayer(ServerWorld world, GameProfile profile) {
		this(world);
		this.profile = profile;
	}

	protected void initDatawatcher() {
		dataWatcher = new EmptyDataWatcher();
		dataWatcher.register(SNIN_OVERLAY_VIEWABLE_WATCHER, NpcBase.SKIN_OVERLAY_VIEWABLE_MASK);
	}

	@Override
	public void spawn() {
		PacketSender.sendNear_(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE,
				PacketFactory.packetPlayerInfo_(PacketFactory.ENUM_PLAYER_INFO_ACTION_ADD_PLAYER, profile),
				createSpawnPacket(), createMetadataPacket());

		Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(),
				() -> PacketSender.sendNear_(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE,
						PacketFactory.packetPlayerInfo_(PacketFactory.ENUM_PLAYER_INFO_ACTION_REMOVE_PLAYER, profile)),
				10);
	}

	@Override
	public void spawn(Player player) {
		PacketSender.send_(player,
				PacketFactory.packetPlayerInfo_(PacketFactory.ENUM_PLAYER_INFO_ACTION_ADD_PLAYER, profile),
				createSpawnPacket(), createMetadataPacket());

		Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(),
				() -> PacketSender.send_(player,
						PacketFactory.packetPlayerInfo_(PacketFactory.ENUM_PLAYER_INFO_ACTION_REMOVE_PLAYER, profile)),
				10);
	}

	@Override
	public Object createSpawnPacket() {
		return PacketFactory.packetNamedEntitySpawn(id, profile.getId(), x, y, z, yaw, pitch);
	}

	protected Object createMetadataPacket() {
		return PacketFactory.packetEntityMetadata(id, dataWatcher, true);
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		profile = Profiles.get(nbt.getString("profileName"));
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setString("profileName", profile.getName());
		return nbt;
	}

	@Override
	public String getName() {
		return profile.getName();
	}

	@Override
	public PacketEntityType<?> getPacketEntityType() {
		return PacketEntityTypes.ENTITY_PLAYER;
	}
}
