package com.pm.aiost.misc.utils.wrapper;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

import com.pm.aiost.misc.event.eventHandler.handler.ProjectileEventHandler;

public class ProjectileWrapper extends EntityWrapper implements Projectile {

	protected ProjectileEventHandler projectileHandler;

	public ProjectileWrapper(Entity entity, ProjectileEventHandler projectileHandler) {
		super(entity);
		this.projectileHandler = projectileHandler;
	}

	@Override
	public ProjectileSource getShooter() {
		return projectileHandler.getSource();
	}

	@Override
	public void setShooter(ProjectileSource source) {
		projectileHandler.setSource(source);
	}

	@Override
	public boolean doesBounce() {
		return false;
	}

	@Override
	public void setBounce(boolean doesBounce) {
	}
}
