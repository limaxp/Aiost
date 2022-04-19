package com.pm.aiost.entity.vanilla;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.AiostInsentient;

import net.minecraft.server.v1_15_R1.EntitySkeleton;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.World;

public class NoCombustSkeleton extends EntitySkeleton implements AiostInsentient {

	public NoCombustSkeleton(EntityTypes<? extends EntitySkeleton> entitytypes, World world) {
		super(EntityTypes.SKELETON, world);
	}

	public NoCombustSkeleton(World world) {
		super(EntityTypes.SKELETON, world);
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
