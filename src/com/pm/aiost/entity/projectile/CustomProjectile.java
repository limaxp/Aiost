package com.pm.aiost.entity.projectile;

import java.util.Random;

import javax.annotation.Nullable;

import com.pm.aiost.entity.AiostEntity;
import com.pm.aiost.event.effect.Effect;

import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EnumMoveType;
import net.minecraft.server.v1_15_R1.IProjectile;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.MovingObjectPosition;
import net.minecraft.server.v1_15_R1.MovingObjectPositionEntity;
import net.minecraft.server.v1_15_R1.Particles;
import net.minecraft.server.v1_15_R1.Vec3D;

public interface CustomProjectile extends AiostEntity, IProjectile {

	public @Nullable EntityLiving getSource();

	public void setNoclip(boolean noclip);

	public boolean noclip();

	public void setDamage(float damage);

	public float getDamage();

	public void setKnockback(float knockback);

	public float getKnockback();

	public void setEffect(Effect effect);

	public Effect getEffect();

	public default void launch(Entity shooter, float pitch, float yaw, float height, float power, float accuracity) {
		float motX = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		float motY = -MathHelper.sin((pitch + height) * 0.017453292F);
		float motZ = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);

		shoot(motX, motY, motZ, power, accuracity);
		Vec3D vec3d = shooter.getMot();

		setMot(getMot().add(vec3d.x, shooter.onGround ? 0.0D : vec3d.y, vec3d.z));
	}

	@Override
	public default void shoot(double motX, double motY, double motZ, float power, float accuracity) {
		Entity entity = getEntity();
		Random random = getRandom();
		float f2 = MathHelper.sqrt(motX * motX + motY * motY + motZ * motZ);
		motX /= f2;
		motY /= f2;
		motZ /= f2;
		motX += random.nextGaussian() * 0.007499999832361937D * accuracity;
		motY += random.nextGaussian() * 0.007499999832361937D * accuracity;
		motZ += random.nextGaussian() * 0.007499999832361937D * accuracity;
		motX *= power;
		motY *= power;
		motZ *= power;
		entity.setMot(motX, motY, motZ);
		float f3 = MathHelper.sqrt(motX * motX + motZ * motZ);
		entity.yaw = (float) (MathHelper.d(motX, motZ) * 57.2957763671875D);
		entity.pitch = (float) (MathHelper.d(motY, f3) * 57.2957763671875D);
		entity.lastYaw = entity.yaw;
		entity.lastPitch = entity.pitch;
	}

// TODO: Newer version doesn't work for some reason!	
//	
//	@Override
//	public default void shoot(double motX, double motY, double motZ, float power, float accuracity) {
//		Entity entity = getEntity();
//		Random random = getRandom();
//		Vec3D vec3d = (new Vec3D(motX, motY, motZ)).d()
//				.add(random.nextGaussian() * 0.007499999832361937D * accuracity,
//						random.nextGaussian() * 0.007499999832361937D * accuracity,
//						random.nextGaussian() * 0.007499999832361937D * accuracity)
//				.a(power);
//
//		setMot(vec3d);
//		float f2 = MathHelper.sqrt(Entity.b(vec3d));
//
//		entity.yaw = (float) (MathHelper.d(vec3d.x, vec3d.z) * 57.2957763671875D);
//		entity.pitch = (float) (MathHelper.d(vec3d.y, f2) * 57.2957763671875D);
//		entity.lastYaw = entity.yaw;
//		entity.lastPitch = entity.pitch;
//	}

	public default void defaultUpdateLocation() {
		if (!isNoGravity())
			setMot(getMot().add(0.0D, -0.04D, 0.0D));
		move(EnumMoveType.SELF, getMot());
	}

	// TODO: doesn't work!
	public default void updateLocation() {
		Entity entity = getEntity();
		Vec3D vec3d = getMot();
		double x = entity.locX() + vec3d.x;
		double y = entity.locY() + vec3d.y;
		double z = entity.locZ() + vec3d.z;
		float f1;
		if (isInWater()) {
			for (int i = 0; i < 4; i++) {
				entity.world.addParticle(Particles.BUBBLE, x - vec3d.x * 0.25D, y - vec3d.y * 0.25D,
						z - vec3d.z * 0.25D, vec3d.x, vec3d.y, vec3d.z);
			}
			f1 = 0.8F;
		} else
			f1 = 0.99F;

		setMot(vec3d.a(f1));
		if (!entity.isNoGravity()) {
			Vec3D vec3d1 = getMot();
			setMot(vec3d1.x, vec3d1.y - getGravity(), vec3d1.z);
		}

		setPosition(x, y, z);
	}

	public default float getGravity() {
		return 0.03F;
	}

	public default void projectileHit(MovingObjectPosition movingObjectPosition) {
		if (movingObjectPosition.getType() == MovingObjectPosition.EnumMovingObjectType.ENTITY) {
			Entity entity = ((MovingObjectPositionEntity) movingObjectPosition).getEntity();
			if (entity.damageEntity(DamageSource.projectile(getEntity(), getSource()), getDamage()))
				applyKnockback(entity);
		}
	}

	public default void applyKnockback(Entity entity) {
		if (entity instanceof EntityLiving) {
			EntityLiving entityliving = (EntityLiving) entity;
			float knockback = getKnockback();
			if (knockback > 0) {
				Vec3D vec3d = getMot().d(1.0D, 0.0D, 1.0D).d().a(knockback * 0.6D);
				if (vec3d.g() > 0.0D)
					entityliving.h(vec3d.x, 0.1D, vec3d.z);
			}
		}
	}
}