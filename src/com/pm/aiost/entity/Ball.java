package com.pm.aiost.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;

public class Ball extends Slime {

	public Ball(EntityType<? extends Ball> entitytypes, Level level) {
		super(EntityType.SLIME, level);
		setNoAi(true);
	}

	@Override
	protected void registerGoals() {
	}

//	@Override
//	public void collide(Entity entity) {
//		float motX = -MathHelper.sin(entity.yaw * 0.017453292F) * MathHelper.cos(entity.pitch * 0.017453292F);
//		float motY = -MathHelper.sin((entity.pitch + 1.0F) * 0.017453292F);
//		float motZ = MathHelper.cos(entity.yaw * 0.017453292F) * MathHelper.cos(entity.pitch * 0.017453292F);
//		float f2 = (float) Math.sqrt(motX * motX + motY * motY + motZ * motZ);
//		motX /= f2;
//		motY /= f2;
//		motZ /= f2;
//		float power = Math.max(1, 4 - getSize());
//		setMot(motX * power, motY * power, motZ * power);
//	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbttagcompound) {
		super.addAdditionalSaveData(nbttagcompound);
		AiostEntityTypes.saveNBT(nbttagcompound, AiostEntityTypes.BALL);
	}
}
