package com.pm.aiost.event.events;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

public class ItemProjectileHitEvent extends ProjectileHitEvent {

	private final ItemStack item;

	public ItemProjectileHitEvent(@Nonnull Projectile projectile, @Nullable Entity hitEntity, @Nonnull ItemStack item) {
		super(projectile, hitEntity);
		this.item = item;
	}

	public ItemProjectileHitEvent(@Nonnull Projectile projectile, @Nullable Block hitBlock,
			@Nullable BlockFace blockface, @Nonnull ItemStack item) {
		super(projectile, null, hitBlock, blockface);
		this.item = item;
	}

	public ItemProjectileHitEvent(@Nonnull Projectile projectile, @Nullable Entity hitEntity, @Nullable Block hitBlock,
			@Nullable BlockFace blockface, @Nonnull ItemStack item) {
		super(projectile, hitEntity, hitBlock, blockface);
		this.item = item;
	}

	public ItemStack getItemStack() {
		return item;
	}
}