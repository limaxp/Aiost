package com.pm.aiost.entity.entities.projectile;

import com.pm.aiost.entity.AiostEntityTypes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class BlockProjectile extends EntityProjectile {

	public BlockProjectile(EntityType<? extends EntityProjectile> entitytypes, Level level) {
		super(entitytypes, level);
	}

	@Override
	public EntityType<?> getType() {
		return EntityType.FALLING_BLOCK;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbttagcompound) {
		AiostEntityTypes.saveId(nbttagcompound, AiostEntityTypes.BLOCK_PROJECTILE);
	}
}
