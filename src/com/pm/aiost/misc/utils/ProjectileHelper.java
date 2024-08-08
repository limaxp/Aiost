package com.pm.aiost.misc.utils;

import org.bukkit.Material;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import com.pm.aiost.entity.EntityHelper;
import com.pm.aiost.entity.entities.projectile.EntityProjectile;
import com.pm.aiost.entity.entities.projectile.EntityProjectile.ProjectileEntity;
import com.pm.aiost.misc.event.AiostEventFactory;
import com.pm.aiost.misc.event.eventHandler.EventHandlerManager;
import com.pm.aiost.misc.event.eventHandler.handler.ProjectileEventHandler;
import com.pm.aiost.misc.nms.NMS;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

public class ProjectileHelper {

	private static final ItemStack EMPTY = new ItemStack(Material.AIR);

	public static boolean launchProjectile(LivingEntity source, EntityType<? extends EntityProjectile> type,
			float height, float power, float accuracy, ProjectileEventHandler handler, boolean gravity) {
		return launchProjectile(source, type, EMPTY, height, power, accuracy, handler, gravity);
	}

	public static boolean launchProjectile(LivingEntity source, EntityType<? extends EntityProjectile> type,
			float pitch, float yaw, float height, float power, float accuracy, ProjectileEventHandler handler,
			boolean gravity) {
		return launchProjectile(source, type, EMPTY, pitch, yaw, height, power, accuracy, handler, gravity);
	}

	public static boolean launchProjectile(LivingEntity source, EntityType<? extends EntityProjectile> type,
			ItemStack is, float height, float power, float accuracy, ProjectileEventHandler handler, boolean gravity) {
		return launchProjectile(source, type, is, source.getLocation().getPitch(), source.getLocation().getYaw(),
				height, power, accuracy, handler, gravity);
	}

	public static boolean launchProjectile(LivingEntity source, EntityType<? extends EntityProjectile> type,
			ItemStack is, float pitch, float yaw, float height, float power, float accuracy,
			ProjectileEventHandler handler, boolean gravity) {
		ProjectileEntity projectile = launchProjectile(source, type, pitch, yaw, height, power, accuracy);
		if (AiostEventFactory.callProjectileLaunchEvent(projectile).isCancelled()) {
			projectile.remove();
			return false;
		}
		projectile.setItem(is);
		projectile.setGravity(gravity);
		projectile.setProjectileHandler(handler);
		EventHandlerManager.setEntityHandler(projectile, handler);
		return true;
	}

	private static ProjectileEntity launchProjectile(LivingEntity source, EntityType<? extends EntityProjectile> type,
			float pitch, float yaw, float heigth, float power, float accuracy) {
		return (ProjectileEntity) NMS
				.from(EntityHelper.launchProjectile(NMS.to(source), type, pitch, yaw, heigth, power, accuracy));
	}

	public static void launch(LivingEntity source, Entity entity, float heigth, float power, float accuracy) {
		launch(source, entity, source.getLocation().getPitch(), source.getLocation().getYaw(), heigth, power, accuracy);
	}

	public static void launch(LivingEntity source, Entity entity, float pitch, float yaw, float heigth, float power,
			float accuracy) {
		EntityHelper.launch(NMS.to(source), NMS.to(entity), pitch, yaw, heigth, power, accuracy);
	}

	public static void shoot(Entity entity, float motX, float motY, float motZ, float power, float accuracy) {
		EntityHelper.shoot(NMS.to(entity), motX, motY, motZ, power, accuracy);
	}

	public static void damage(Entity projectile, ProjectileSource source, Entity hitEntity, double damage) {
		if (!(hitEntity instanceof LivingEntity))
			return;

		DamageSource.Builder damageSource = DamageSource.builder(DamageType.MAGIC);
		damageSource.withDirectEntity(projectile);
		damageSource.withDamageLocation(projectile.getLocation());
		if (source instanceof Entity)
			damageSource.withCausingEntity((Entity) source);
		((LivingEntity) hitEntity).damage(damage, damageSource.build());
	}

	public static void knockback(Entity projectile, Entity hitEntity, double knockback) {
		if (!(hitEntity instanceof LivingEntity) || knockback <= 0)
			return;

		net.minecraft.world.entity.Entity projectileNMS = NMS.to(projectile);
		net.minecraft.world.entity.LivingEntity hitNMS = NMS.to((LivingEntity) hitEntity);
		double d0 = Math.max(0.0, 1.0 - hitNMS.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
		Vec3 vec3d = projectileNMS.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize()
				.scale((double) knockback * 0.6 * d0);
		if (vec3d.lengthSqr() > 0.0)
			hitNMS.push(vec3d.x, 0.1, vec3d.z);
	}
}
