package com.pm.aiost.misc.utils;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.entity.EntityHelper;
import com.pm.aiost.entity.entities.projectile.EntityProjectile;
import com.pm.aiost.entity.entities.projectile.EntityProjectile.ProjectileEntity;
import com.pm.aiost.misc.event.AiostEventFactory;
import com.pm.aiost.misc.event.eventHandler.EventHandlerManager;
import com.pm.aiost.misc.event.eventHandler.handler.ProjectileEventHandler;
import com.pm.aiost.misc.nms.NMS;

import net.minecraft.world.entity.EntityType;

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
}
