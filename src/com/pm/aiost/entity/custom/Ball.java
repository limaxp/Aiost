package com.pm.aiost.entity.custom;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.AiostInsentient;
import com.pm.aiost.entity.projectile.ProjectileHelper;

import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntitySlime;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.World;

public class Ball extends EntitySlime implements AiostInsentient {

	public Ball(EntityTypes<? extends Ball> entitytypes, World world) {
		super(EntityTypes.SLIME, world);
		setNoAI(true);
	}

	@Override
	protected void initPathfinder() {
	}

	@Override
	public void collide(Entity entity) {
		float motX = -MathHelper.sin(entity.yaw * 0.017453292F) * MathHelper.cos(entity.pitch * 0.017453292F);
		float motY = -MathHelper.sin((entity.pitch + 1.0F) * 0.017453292F);
		float motZ = MathHelper.cos(entity.yaw * 0.017453292F) * MathHelper.cos(entity.pitch * 0.017453292F);
		float f2 = MathHelper.sqrt(motX * motX + motY * motY + motZ * motZ);
		motX /= f2;
		motY /= f2;
		motZ /= f2;
		float power = Math.max(1, 4 - getSize());
		setMot(motX * power, motY * power, motZ * power);
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {
		Entity entity = damagesource.getEntity();
		ProjectileHelper.launch(this, entity, entity.pitch, entity.yaw, 90.0F, 4.0F + (f * 0.5F), 1.0F);
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
		return AiostEntityTypes.BALL;
	}
}
