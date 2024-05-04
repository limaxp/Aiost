package com.pm.aiost.entity;

import java.util.UUID;

import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;

public interface OwnableEntity {

	public CraftEntity getBukkitEntity();

	public void setOwnerUUID(UUID id);

	public UUID getOwnerUUID();

	public void setBoundTime(int time);

	public int getBoundTime();

	public default void setOwner(LivingEntity owner) {
	}

	public default void setOwner(net.minecraft.world.entity.player.Player player) {
	}

	public default net.minecraft.world.entity.player.Player getOwner() {
		try {
//			UUID var0 = getOwnerUUID();
//			if (var0 == null)
//				return null;
//			return getWorld().b(var0);
			return null;
		} catch (IllegalArgumentException var0) {
			return null;
		}
	}
}
