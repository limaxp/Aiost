package com.pm.aiost.event.events;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;

public class BlockProjectileHitEvent extends ProjectileHitEvent {

	private final Material material;
	private final int data;

	public BlockProjectileHitEvent(@Nonnull Projectile projectile, float damageMultiplier,
			@Nullable LivingEntity hitEntity, @Nonnull Material material, int data) {
		super(projectile, hitEntity);
		this.material = material;
		this.data = data;
	}

	public BlockProjectileHitEvent(@Nonnull Projectile projectile, float damageMultiplier, @Nullable Block hitBlock,
			@Nullable BlockFace blockface, @Nonnull Material material, int data) {
		super(projectile, null, hitBlock, blockface);
		this.material = material;
		this.data = data;
	}

	public BlockProjectileHitEvent(@Nonnull Projectile projectile, float damageMultiplier,
			@Nullable LivingEntity hitEntity, @Nullable Block hitBlock, @Nullable BlockFace blockface,
			@Nonnull Material material, int data) {
		super(projectile, hitEntity, hitBlock, blockface);
		this.material = material;
		this.data = data;
	}

	public Material getMaterial() {
		return material;
	}

	public int getData() {
		return data;
	}
}