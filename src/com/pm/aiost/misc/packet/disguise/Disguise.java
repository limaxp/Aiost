package com.pm.aiost.misc.packet.disguise;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface Disguise {

	public void addPackets(LivingEntity entity, List<Object> packets);

	public default void removePackets(Player player, List<Object> packets) {
		removePackets((LivingEntity) player, packets);
	}

	public void removePackets(LivingEntity entity, List<Object> packets);

	public void load(ConfigurationSection section);
}
