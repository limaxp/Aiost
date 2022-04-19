package com.pm.aiost.misc.packet.disguise.disguises;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.pm.aiost.entity.npc.profile.Profiles;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.disguise.Disguise;
import com.pm.aiost.misc.utils.nms.NMS;

public class DisguisePlayer implements Disguise {

	protected GameProfile profile;

	public DisguisePlayer() {
	}

	public DisguisePlayer(GameProfile profile) {
		this.profile = profile;
	}

	@Override
	public void addPackets(Player player, List<Object> packets) {
		Location loc = player.getLocation();
		packets.add(PacketFactory.packetPlayerInfo_(PacketFactory.ENUM_PLAYER_INFO_ACTION_ADD_PLAYER, profile));
		packets.add(PacketFactory.packetNamedEntitySpawn(player.getEntityId(), profile.getId(), loc.getX(), loc.getY(),
				loc.getZ(), loc.getYaw(), loc.getPitch()));
		Disguise.addPlayerStatePackets(NMS.getNMS(player), packets);
	}

	@Override
	public void removePackets(Player player, List<Object> packets) {
		packets.add(PacketFactory.packetPlayerInfo_(PacketFactory.ENUM_PLAYER_INFO_ACTION_REMOVE_PLAYER, profile));
		packets.add(PacketFactory.packetPlayerInfo_(PacketFactory.ENUM_PLAYER_INFO_ACTION_ADD_PLAYER,
				NMS.getNMS(player).getProfile()));
	}

	@Override
	public void load(ConfigurationSection section) {
		profile = Profiles.get(section.getString("profileName"));
	}

	public GameProfile getProfile() {
		return profile;
	}
}
