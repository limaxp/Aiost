package com.pm.aiost.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ProjectileHelper {

	public static Vec3 getMovementToShoot(Entity entity, double motX, double motY, double motZ, float power,
			float accuracy) {
		return (new Vec3(motX, motY, motZ)).normalize()
				.add(entity.random.triangle(0.0, 0.0172275 * (double) accuracy),
						entity.random.triangle(0.0, 0.0172275 * (double) accuracy),
						entity.random.triangle(0.0, 0.0172275 * (double) accuracy))
				.scale((double) power);
	}

	public static void shoot(Entity entity, double motX, double motY, double motZ, float power, float accuracy) {
		Vec3 vec3d = getMovementToShoot(entity, motX, motY, motZ, power, accuracy);
		entity.setDeltaMovement(vec3d);
		double d3 = vec3d.horizontalDistance();
		entity.setYRot((float) (Mth.atan2(vec3d.x, vec3d.z) * 57.2957763671875));
		entity.setXRot((float) (Mth.atan2(vec3d.y, d3) * 57.2957763671875));
		entity.yRotO = entity.getYRot();
		entity.xRotO = entity.getXRot();
	}

	public static void launch(Entity shooter, Entity entity, float pitch, float yaw, float heigth, float power,
			float accuracy) {
		float f5 = -Mth.sin(yaw * 0.017453292F) * Mth.cos(pitch * 0.017453292F);
		float f6 = -Mth.sin((pitch + heigth) * 0.017453292F);
		float f7 = Mth.cos(yaw * 0.017453292F) * Mth.cos(pitch * 0.017453292F);
		shoot(entity, (double) f5, (double) f6, (double) f7, power, accuracy);
		Vec3 vec3d = shooter.getDeltaMovement();
		entity.setDeltaMovement(entity.getDeltaMovement().add(vec3d.x, shooter.onGround() ? 0.0 : vec3d.y, vec3d.z));
	}
}
