package com.pm.aiost.entity.projectile;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

import com.pm.aiost.event.AiostEventFactory;
import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EntityProjectile;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.Vec3D;

public class ProjectileHelper {

	public static boolean launchProjectile(LivingEntity shooter, CustomProjectile projectile, float power) {
		Entity entity = projectile.getEntity();
		if (!AiostEventFactory.callProjectileLaunchEvent(entity.getBukkitEntity()).isCancelled()) {
			Location loc = shooter.getLocation();
			projectile.launch(((CraftLivingEntity) shooter).getHandle(), loc.getPitch(), loc.getYaw(), 0.0F, power,
					1.0F);
			return projectile.getWorld().addEntity(entity);
		}
		return false;
	}

	public static boolean launchProjectile(EntityLiving shooter, CustomProjectile projectile, float power) {
		Entity entity = projectile.getEntity();
		if (!AiostEventFactory.callProjectileLaunchEvent(entity.getBukkitEntity()).isCancelled()) {
			projectile.launch(shooter, shooter.yaw, shooter.pitch, 0.0F, power, 1.0F);
			return projectile.getWorld().addEntity(entity);
		}
		return false;
	}

	public static boolean launchProjectile(LivingEntity shooter, EntityProjectile projectile, float power) {
		Location loc = shooter.getLocation();
		projectile.a(((CraftLivingEntity) shooter).getHandle(), loc.getPitch(), loc.getYaw(), 0.0F, power, 1.0F);
		return projectile.getWorld().addEntity(projectile);
	}

	public static boolean launchProjectile(EntityLiving shooter, EntityProjectile projectile, float power) {
		projectile.a(shooter, shooter.yaw, shooter.pitch, 0.0F, power, 1.0F);
		return projectile.getWorld().addEntity(projectile);
	}

	public static void launch(Entity entity, Entity shooter, float pitch, float yaw, float height, float power,
			float accuracity) {
		float motX = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		float motY = -MathHelper.sin((pitch + height) * 0.017453292F);
		float motZ = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);

		shoot(entity, motX, motY, motZ, power, accuracity);
		Vec3D vec3d = shooter.getMot();

		entity.setMot(entity.getMot().add(vec3d.x, shooter.onGround ? 0.0D : vec3d.y, vec3d.z));
	}

	public static void shoot(Entity entity, double motX, double motY, double motZ, float power, float accuracity) {
		Random random = NMS.getRandom(entity);
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
}
