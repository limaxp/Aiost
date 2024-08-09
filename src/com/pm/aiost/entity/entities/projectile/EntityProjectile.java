package com.pm.aiost.entity.entities.projectile;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R4.CraftServer;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftSnowball;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.event.eventHandler.handler.ProjectileEventHandler;
import com.pm.aiost.misc.nms.NMS;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;

public class EntityProjectile extends Snowball {

	private ProjectileEventHandler projectileHandler;
	private EntityType<?> projectileType = AiostEntityTypes.PROJECTILE;

	public EntityProjectile(EntityType<? extends EntityProjectile> entitytypes, Level level) {
		super(EntityType.SNOWBALL, level);
	}

	@Override
	public EntityType<?> getType() {
		return projectileType;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbttagcompound) {
		AiostEntityTypes.saveId(nbttagcompound, AiostEntityTypes.PROJECTILE);
	}

	public void setProjectileHandler(ProjectileEventHandler projectileHandler) {
		this.projectileHandler = projectileHandler;
	}

	public ProjectileEventHandler getProjectileHandler() {
		return projectileHandler;
	}

	public void setProjectileType(EntityType<?> projectileType) {
		this.projectileType = projectileType;
	}

	@Override
	public CraftEntity getBukkitEntity() {
		CraftEntity bukkitEntity = NMS.getBukkitEntity(this);
		if (bukkitEntity == null)
			NMS.setBukkitEntity(this, bukkitEntity = new ProjectileEntity(this));
		return bukkitEntity;
	}

	public static class ProjectileEntity extends CraftSnowball implements Projectile {

		public ProjectileEntity(EntityProjectile entity) {
			super((CraftServer) Bukkit.getServer(), entity);
		}

		@Override
		public EntityProjectile getHandle() {
			return (EntityProjectile) this.entity;
		}

		public void setProjectileHandler(ProjectileEventHandler projectileHandler) {
			getHandle().setProjectileHandler(projectileHandler);
		}

		public ProjectileEventHandler getProjectileHandler() {
			return getHandle().getProjectileHandler();
		}

		public void setProjectileType(EntityType<?> projectileType) {
			getHandle().projectileType = projectileType;
		}

		@Override
		public void setShooter(ProjectileSource shooter) {
			getHandle().projectileHandler.setSource(shooter);
			entity.projectileSource = shooter;
		}

		@Override
		public ProjectileSource getShooter() {
			return entity.projectileSource;
		}

		@Override
		public void setBounce(boolean doesBounce) {
		}

		@Override
		public boolean doesBounce() {
			return false;
		}
	}
}
