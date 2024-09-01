package com.pm.aiost.misc.packet.entity.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.packet.entity.PacketEntity;
import com.pm.aiost.misc.packet.entity.PacketEntityType;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.misc.profile.ProfileBuilder;
import com.pm.aiost.misc.profile.Profiles;
import com.pm.aiost.world.ServerWorld;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraft.world.entity.EntityType;

public class PacketPlayer extends PacketEntity {

	public static final byte PLAYER_MODE_CUSTOMISATION_VIEWABLE_MASK = 0x01 + 0x02 + 0x04 + 0x08 + 0x10 + 0x20 + 0x40;

	public static final EntityDataAccessor<Byte> DATA_PLAYER_MODE_CUSTOMISATION = new EntityDataAccessor<Byte>(17,
			EntityDataSerializers.BYTE);

	private GameProfile profile;
	protected List<DataValue<?>> dataWatcher;

	public PacketPlayer(ServerWorld world) {
		super(world);
		dataWatcher = createDatawatcher();
	}

	public PacketPlayer(ServerWorld world, GameProfile profile) {
		this(world);
		setProfile(profile);
	}

	public static List<DataValue<?>> createDatawatcher() {
		List<DataValue<?>> dataWatcher = new ArrayList<DataValue<?>>();
		dataWatcher.add(DataValue.create(DATA_PLAYER_MODE_CUSTOMISATION, PLAYER_MODE_CUSTOMISATION_VIEWABLE_MASK));
		return dataWatcher;
	}

	@Override
	public void spawn() {
		PacketSender.sendNearby(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE,
				PacketFactory.packetPlayerInfo(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, profile),
				createSpawnPacket(), createMetadataPacket());

//		Bukkit.getScheduler()
//				.runTaskLater(
//						Aiost.getPlugin(), () -> PacketSender.sendNearby(world.world, x, y, z,
//								PACKET_OBJECT_VISIBILE_RANGE, PacketFactory.packetPlayerInfoRemove(profile.getId())),
//						10);
	}

	@Override
	public void spawn(Player player) {
		PacketSender.send(player,
				PacketFactory.packetPlayerInfo(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, profile),
				createSpawnPacket(), createMetadataPacket());

//		Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(),
//				() -> PacketSender.send(player, PacketFactory.packetPlayerInfoRemove(profile.getId())), 10);
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return PacketFactory.packetEntitySpawn(id, profile.getId(), x, y, z, yaw, pitch, EntityType.PLAYER);
	}

	protected Packet<?> createMetadataPacket() {
		return PacketFactory.packetEntityMetadata(id, dataWatcher);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		setProfile(Profiles.get(nbt.getString("profileName")));
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		super.save(nbt);
		nbt.putString("profileName", profile.getName());
		return nbt;
	}

	protected void setProfile(GameProfile profile) {
		this.profile = ProfileBuilder.create(UUID.randomUUID(), profile);
	}

	@Override
	public String getName() {
		return profile.getName();
	}

	@Override
	public PacketEntityType<?> getPacketEntityType() {
		return PacketEntityTypes.PLAYER;
	}
}
