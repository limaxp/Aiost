package com.pm.aiost.misc.packet.disguise.disguises;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.disguise.Disguise;
import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.server.v1_15_R1.EntityTypes;

public class DisguiseEntityLiving implements Disguise {

	protected int entityId;

	public DisguiseEntityLiving() {
	}

	public DisguiseEntityLiving(EntityTypes<?> type) {
		this(AiostEntityTypes.getId(type));
	}

	public DisguiseEntityLiving(int entityId) {
		this.entityId = entityId;
	}

	@Override
	public void addPackets(Player player, List<Object> packets) {
		Location loc = player.getLocation();
		packets.add(PacketFactory.packetEntityLivingSpawn(player.getEntityId(), player.getUniqueId(), entityId,
				loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch()));
		Disguise.addPlayerStatePackets(NMS.getNMS(player), packets);
	}

	@Override
	public void load(ConfigurationSection section) {
		String entityType = section.getString("entityType");
		if (entityType != null)
			entityId = AiostEntityTypes
					.getId(AiostEntityTypes.getByKey(NMS.createMinecraftKey(entityType.toLowerCase())));
		else
			entityId = section.getInt("entityId");
	}

	public int getEntityId() {
		return entityId;
	}
}
