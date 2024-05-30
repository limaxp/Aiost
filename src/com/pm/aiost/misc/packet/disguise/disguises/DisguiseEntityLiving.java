package com.pm.aiost.misc.packet.disguise.disguises;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.disguise.Disguise;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;

public class DisguiseEntityLiving implements Disguise {

	protected int entityId;

	public DisguiseEntityLiving() {
	}

	public DisguiseEntityLiving(EntityType<?> type) {
		this(AiostEntityTypes.getId(type));
	}

	public DisguiseEntityLiving(int entityId) {
		this.entityId = entityId;
	}

	@Override
	public void addPackets(Player player, List<Packet<?>> packets) {
		Location loc = player.getLocation();
		packets.add(PacketFactory.packetEntitySpawn(player.getEntityId(), player.getUniqueId(), loc.getX(), loc.getY(),
				loc.getZ(), loc.getYaw(), loc.getPitch(), AiostEntityTypes.getById(entityId)));
		Disguise.addPlayerStatePackets(NMS.to(player), packets);
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
