package com.pm.aiost.entity.vanilla;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.AiostInsentient;

import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EntityZombie;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.World;

public class NoCombustZombie extends EntityZombie implements AiostInsentient {

	public NoCombustZombie(EntityTypes<? extends EntityZombie> entitytypes, World world) {
		super(EntityTypes.ZOMBIE, world);
	}

	public NoCombustZombie(World world) {
		super(EntityTypes.ZOMBIE, world);
	}

	@Override
	protected boolean K_() {
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
		return AiostEntityTypes.NO_COMBUST_ZOMBIE;
	}
}
