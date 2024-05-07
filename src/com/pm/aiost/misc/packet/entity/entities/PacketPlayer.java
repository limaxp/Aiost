package com.pm.aiost.misc.packet.entity.entities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.pm.aiost.Aiost;
import com.pm.aiost.entity.npc.profile.Profiles;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.packet.entity.PacketEntity;
import com.pm.aiost.misc.packet.entity.PacketEntityType;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.server.world.ServerWorld;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraft.world.entity.EntityType;

public class PacketPlayer extends PacketEntity {

	public static final byte SKIN_OVERLAY_VIEWABLE_MASK = 0x01 + 0x02 + 0x04 + 0x08 + 0x10 + 0x20 + 0x40;

	public static final EntityDataAccessor<Byte> SNIN_OVERLAY_VIEWABLE_WATCHER = new EntityDataAccessor<Byte>(16,
			EntityDataSerializers.BYTE);

	protected GameProfile profile;
	protected List<DataValue<?>> dataWatcher;

	public PacketPlayer(ServerWorld world) {
		super(world);
		initDatawatcher();
	}

	public PacketPlayer(ServerWorld world, GameProfile profile) {
		this(world);
		this.profile = profile;
	}

	protected void initDatawatcher() {
		dataWatcher = new ArrayList<DataValue<?>>();
		dataWatcher.add(DataValue.create(SNIN_OVERLAY_VIEWABLE_WATCHER, SKIN_OVERLAY_VIEWABLE_MASK));
	}

	@Override
	public void spawn() {
		PacketSender.sendNear_(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE,
				PacketFactory.packetPlayerInfo(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, profile),
				createSpawnPacket(), createMetadataPacket());

		Bukkit.getScheduler()
				.runTaskLater(
						Aiost.getPlugin(), () -> PacketSender.sendNear_(world.world, x, y, z,
								PACKET_OBJECT_VISIBILE_RANGE, PacketFactory.packetPlayerInfoRemove(profile.getId())),
						10);
	}

	@Override
	public void spawn(Player player) {
		PacketSender.send_(player,
				PacketFactory.packetPlayerInfo(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, profile),
				createSpawnPacket(), createMetadataPacket());

		Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(),
				() -> PacketSender.send_(player, PacketFactory.packetPlayerInfoRemove(profile.getId())), 10);
	}

	@Override
	public Object createSpawnPacket() {
		return PacketFactory.packetEntitySpawn(id, profile.getId(), x, y, z, yaw, pitch, EntityType.PLAYER);
	}

	protected Object createMetadataPacket() {
		return PacketFactory.packetEntityMetadata(id, dataWatcher);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		profile = Profiles.get(nbt.getString("profileName"));
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		super.save(nbt);
		nbt.putString("profileName", profile.getName());
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
