package com.pm.aiost.entity.vanilla;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.AiostInsentient;

import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EntityWitch;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.World;

public class MultishotWitch extends EntityWitch implements AiostInsentient {

	private static final int DEFAULT_SHOT_AMOUNT = 3;

	private int shotAmount = DEFAULT_SHOT_AMOUNT;

	public MultishotWitch(EntityTypes<? extends EntityWitch> entitytypes, World world) {
		super(EntityTypes.WITCH, world);
	}

	public MultishotWitch(World world) {
		super(EntityTypes.WITCH, world);
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
		return AiostEntityTypes.MULTISHOT_WITCH;
	}

	public void setShotAmount(int shotAmount) {
		this.shotAmount = shotAmount;
	}

	public int getShotAmount() {
		return shotAmount;
	}
}
