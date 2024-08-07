package com.pm.aiost.entity.entities;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R4.CraftServer;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftLivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.event.eventHandler.handler.ProjectileEventHandler;
import com.pm.aiost.misc.nms.NMS;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityProjectile extends Entity {

	protected ProjectileEventHandler projectileHandler;

	public EntityProjectile(EntityType<? extends EntityProjectile> entitytypes, Level level) {
		super(entitytypes, level);
	}

	public EntityProjectile(Level level) {
		super(AiostEntityTypes.PROJECTILE, level);
	}

	public EntityProjectile(Level level, double x, double y, double z) {
		super(AiostEntityTypes.PROJECTILE, level);
		setPos(x, y, z);
	}

	@Override
	protected void defineSynchedData(Builder var1) {
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag var1) {
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

	@Override
	public CraftEntity getBukkitEntity() {
		CraftEntity bukkitEntity = NMS.getBukkitEntity(this);
		if (bukkitEntity == null)
			NMS.setBukkitEntity(this, bukkitEntity = new ProjectileEntity(this));
		return bukkitEntity;
	}

	public static class ProjectileEntity extends CraftEntity implements Projectile {

		public ProjectileEntity(EntityProjectile entity) {
			super((CraftServer) Bukkit.getServer(), entity);
		}

		@Override
		public EntityProjectile getHandle() {
			return (EntityProjectile) this.entity;
		}

		@Override
		public void setShooter(ProjectileSource shooter) {
			if (shooter instanceof CraftLivingEntity)
				getHandle().projectileHandler.setSource((CraftEntity) shooter);
			else
				getHandle().projectileHandler.setSource(null);
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
