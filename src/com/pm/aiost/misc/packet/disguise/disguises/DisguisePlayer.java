package com.pm.aiost.misc.packet.disguise.disguises;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

import com.mojang.authlib.GameProfile;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.disguise.Disguise;
import com.pm.aiost.misc.packet.entity.entities.PacketPlayer;
import com.pm.aiost.misc.profile.ProfileBuilder;
import com.pm.aiost.misc.profile.Profiles;

import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraft.world.entity.EntityType;

public class DisguisePlayer implements Disguise {

	public static final List<DataValue<?>> DATA_WATCHER = PacketPlayer.createDatawatcher();

	protected GameProfile profile;

	public DisguisePlayer() {
	}

	public DisguisePlayer(GameProfile profile) {
		setProfile(profile);
	}

	@Override
	public void addPackets(LivingEntity entity, List<Object> packets) {
		Location loc = entity.getLocation();
		packets.add(PacketFactory.packetPlayerInfo(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, profile));
		packets.add(PacketFactory.packetEntitySpawn(entity.getEntityId(), profile.getId(), loc.getX(), loc.getY(),
				loc.getZ(), loc.getYaw(), loc.getPitch(), EntityType.PLAYER));
		addDataPackets(entity, packets);
//		packets.add(PacketFactory.packetPlayerInfoRemove(profile.getId()));
	}

	@Override
	public void addDataPackets(LivingEntity entity, List<Object> packets) {
		packets.add(PacketFactory.packetEntityMetadata(entity.getEntityId(), DATA_WATCHER));
	}

	@Override
	public void load(ConfigurationSection section) {
		setProfile(Profiles.get(section.getString("profileName")));
	}

	private void setProfile(GameProfile profile) {
		this.profile = ProfileBuilder.create(UUID.randomUUID(), profile);
	}

	public GameProfile getProfile() {
		return profile;
	}
}
