package com.pm.aiost.misc.event.eventHandler.handler;

import javax.annotation.Nullable;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

import com.pm.aiost.effect.Effect;
import com.pm.aiost.misc.event.eventHandler.EventHandler;
import com.pm.aiost.misc.event.eventHandler.TickableHandler;
import com.pm.aiost.misc.particle.IParticle;
import com.pm.aiost.misc.utils.ProjectileHelper;

public class ProjectileEventHandler implements EventHandler, TickableHandler {

	protected @Nullable ProjectileSource source;
	protected float damage = 0F;
	protected float knockback = 1F;
	protected int duration = -1;
	protected Effect effect = Effect.EMPTY;
	protected @Nullable IParticle particle;
	protected float explosionStrength;

	public ProjectileEventHandler() {
	}

	public ProjectileEventHandler(@Nullable ProjectileSource source) {
		this.source = source;
	}

	@Override
	public void onTick(Entity entity) {
		if (duration-- == 0) {
			entity.remove();
			if (doesExplode())
				entity.getWorld().createExplosion(entity.getLocation(), explosionStrength);
			return;
		}
		if (particle != null)
			particle.spawn(entity.getLocation());
	}

	@Override
	public void onProjectileHit(ProjectileHitEvent event) {
		if (event.getHitEntity() != null) {
			ProjectileHelper.damage(event.getEntity(), source, event.getHitEntity(), damage);
			ProjectileHelper.knockback(event.getEntity(), event.getHitEntity(), knockback);
			effect.onProjectileHit(event);
		}

		if (event.getHitBlock() != null)
			effect.onProjectileHit(event);

		if (doesExplode())
			event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), explosionStrength);
	}

	public @Nullable ProjectileSource getSource() {
		return source;
	}

	public void setSource(@Nullable ProjectileSource source) {
		this.source = source;
	}

	public float getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public float getKnockback() {
		return knockback;
	}

	public void setKnockback(float knockback) {
		this.knockback = knockback;
	}

	public Effect getEffect() {
		return effect;
	}

	public void setEffect(Effect effect) {
		this.effect = effect;
	}

	public @Nullable IParticle getParticle() {
		return particle;
	}

	public void setParticle(@Nullable IParticle particle) {
		this.particle = particle;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration / 4;
	}

	public boolean doesExplode() {
		return explosionStrength != 0;
	}

	public void setExplode(boolean explode) {
		if (explode)
			explosionStrength = 4F;
		else
			explosionStrength = 0;
	}

	public void setExplode(float explosionStrength) {
		this.explosionStrength = explosionStrength;
	}

	public float getExplode() {
		return explosionStrength;
	}
}
