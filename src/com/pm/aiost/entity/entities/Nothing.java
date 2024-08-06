package com.pm.aiost.entity.entities;

import com.pm.aiost.entity.AiostEntityTypes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class Nothing extends Entity {

	public Nothing(EntityType<? extends Nothing> entitytypes, Level level) {
		super(entitytypes, level);
	}

	public Nothing(Level level) {
		super(AiostEntityTypes.NOTHING, level);
	}

	public Nothing(Level level, double x, double y, double z) {
		super(AiostEntityTypes.NOTHING, level);
		setPos(x, y, z);
	}

	@Override
	protected void defineSynchedData(Builder var1) {
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag var1) {
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag var1) {
	}
}
