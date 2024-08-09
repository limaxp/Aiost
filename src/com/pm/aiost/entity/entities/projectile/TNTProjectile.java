package com.pm.aiost.entity.entities.projectile;

import com.pm.aiost.entity.AiostEntityTypes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class TNTProjectile extends EntityProjectile {

	public TNTProjectile(EntityType<? extends EntityProjectile> entitytypes, Level level) {
		super(entitytypes, level);
		setProjectileType(EntityType.TNT);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbttagcompound) {
		AiostEntityTypes.saveId(nbttagcompound, AiostEntityTypes.TNT_PROJECTILE);
	}
}
