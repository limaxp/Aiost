package com.pm.aiost.misc.packet.disguise.disguises;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.disguise.Disguise;
import com.pm.aiost.misc.profile.Profiles;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.world.entity.EntityType;

public class DisguisePlayer implements Disguise {

	protected GameProfile profile;

	public DisguisePlayer() {
	}

	public DisguisePlayer(GameProfile profile) {
		this.profile = profile;
	}

	@Override
	public void addPackets(Player player, List<Packet<?>> packets) {
		Location loc = player.getLocation();
		packets.add(PacketFactory.packetPlayerInfo(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, profile));
		packets.add(PacketFactory.packetEntitySpawn(player.getEntityId(), profile.getId(), loc.getX(), loc.getY(),
				loc.getZ(), loc.getYaw(), loc.getPitch(), EntityType.PLAYER));
		Disguise.addPlayerStatePackets(NMS.to(player), packets);
	}

	@Override
	public void removePackets(Player player, List<Packet<?>> packets) {
		packets.add(PacketFactory.packetPlayerInfoRemove(profile.getId()));
		packets.add(PacketFactory.packetPlayerInfo(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER,
				NMS.to(player).getGameProfile()));
	}

	@Override
	public void load(ConfigurationSection section) {
		profile = Profiles.get(section.getString("profileName"));
	}

	public GameProfile getProfile() {
		return profile;
	}
}
