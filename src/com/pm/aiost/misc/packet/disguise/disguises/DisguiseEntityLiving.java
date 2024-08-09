package com.pm.aiost.misc.packet.disguise.disguises;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.disguise.Disguise;
import com.pm.aiost.misc.packet.disguise.DisguiseManager;

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
	public void addPackets(LivingEntity entity, List<Object> packets) {
		Location loc = entity.getLocation();
		packets.add(PacketFactory.packetEntitySpawn(entity.getEntityId(), entity.getUniqueId(), loc.getX(), loc.getY(),
				loc.getZ(), loc.getYaw(), loc.getPitch(), AiostEntityTypes.get(entityId)));
		DisguiseManager.addEntityStatePackets(NMS.to(entity), packets);
	}

	@Override
	public void removePackets(LivingEntity entity, List<Object> packets) {
	}

	@Override
	public void load(ConfigurationSection section) {
		String entityType = section.getString("entityType");
		if (entityType != null)
			entityId = AiostEntityTypes.getId(AiostEntityTypes.get(entityType.toLowerCase()));
		else
			entityId = section.getInt("entityId");
	}

	public int getEntityId() {
		return entityId;
	}
}
