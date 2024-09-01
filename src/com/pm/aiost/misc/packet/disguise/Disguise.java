package com.pm.aiost.misc.packet.disguise;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

public interface Disguise {

	public void addPackets(LivingEntity entity, List<Object> packets);
	
	public void addDataPackets(LivingEntity entity, List<Object> packets);

	public void load(ConfigurationSection section);
}
