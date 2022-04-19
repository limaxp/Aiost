package com.pm.aiost.entity.vanilla;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.AiostInsentient;

import net.minecraft.server.v1_15_R1.DifficultyDamageScaler;
import net.minecraft.server.v1_15_R1.EntitySkeleton;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.World;

public class MeleeSkeleton extends EntitySkeleton implements AiostInsentient {

	public MeleeSkeleton(EntityTypes<? extends EntitySkeleton> entitytypes, World world) {
		super(EntityTypes.SKELETON, world);
	}

	public MeleeSkeleton(World world) {
		super(EntityTypes.SKELETON, world);
	}

	protected void a(DifficultyDamageScaler difficultydamagescaler) {
		super.a(difficultydamagescaler);
		setSlot(EnumItemSlot.MAINHAND, ItemStack.a);
	}

	@Override
	public NBTTagCompound save(NBTTagCompound nbttagcompound) {
		super.b(nbttagcompound);
		saveNBT(nbttagcompound);
		return nbttagcompound;
	}

	@Override
	public EntityTypes<?> getCustomEntityType() {
		return AiostEntityTypes.MELEE_SKELETON;
	}
}
