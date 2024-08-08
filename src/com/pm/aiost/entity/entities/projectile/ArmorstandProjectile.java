package com.pm.aiost.entity.entities.projectile;

import com.pm.aiost.entity.AiostEntityTypes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ArmorstandProjectile extends EntityProjectile {

	public ArmorstandProjectile(EntityType<? extends EntityProjectile> entitytypes, Level level) {
		super(entitytypes, level);
	}

	@Override
	public EntityType<?> getType() {
		return EntityType.ARMOR_STAND;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbttagcompound) {
		AiostEntityTypes.saveId(nbttagcompound, AiostEntityTypes.ARMORSTAND_PROJECTILE);
	}
}
