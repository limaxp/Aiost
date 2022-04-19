package com.pm.aiost.entity.vanilla;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.AiostInsentient;

import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EntitySkeleton;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.World;

public class MultishotSkeleton extends EntitySkeleton implements AiostInsentient {

	private static final int DEFAULT_SHOT_AMOUNT = 3;

	private int shotAmount = DEFAULT_SHOT_AMOUNT;

	public MultishotSkeleton(EntityTypes<? extends EntitySkeleton> entitytypes, World world) {
		super(EntityTypes.SKELETON, world);
	}

	public MultishotSkeleton(World world) {
		super(EntityTypes.SKELETON, world);
	}

	@Override
	public void a(EntityLiving entityliving, float f) {
		for (int i = 0; i < shotAmount; ++i) {
			super.a(entityliving, f);
		}
	}

	@Override
	public NBTTagCompound save(NBTTagCompound nbttagcompound) {
		super.b(nbttagcompound);
		saveNBT(nbttagcompound);
		return nbttagcompound;
	}

	@Override
	public EntityTypes<?> getCustomEntityType() {
		return AiostEntityTypes.MULTISHOT_SKELETON;
	}

	public void setShotAmount(int shotAmount) {
		this.shotAmount = shotAmount;
	}

	public int getShotAmount() {
		return shotAmount;
	}
}
