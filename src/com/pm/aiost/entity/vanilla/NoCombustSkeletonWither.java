package com.pm.aiost.entity.vanilla;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.AiostInsentient;

import net.minecraft.server.v1_15_R1.EntitySkeleton;
import net.minecraft.server.v1_15_R1.EntitySkeletonWither;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.World;

public class NoCombustSkeletonWither extends EntitySkeletonWither implements AiostInsentient {

	public NoCombustSkeletonWither(EntityTypes<? extends EntitySkeletonWither> entitytypes, World world) {
		super(EntityTypes.WITHER_SKELETON, world);
	}

	public NoCombustSkeletonWither(World world) {
		super(EntityTypes.WITHER_SKELETON, world);
	}

	@Override
	protected boolean en() {
		return false;
	}

	@Override
	public NBTTagCompound save(NBTTagCompound nbttagcompound) {
		super.b(nbttagcompound);
		saveNBT(nbttagcompound);
		return nbttagcompound;
	}

	@Override
	public EntityTypes<?> getCustomEntityType() {
		return AiostEntityTypes.NO_COMBUST_SKELETON;
	}
}
