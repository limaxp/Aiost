package com.pm.aiost.misc.event.eventHandler.handler;

import javax.annotation.Nullable;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.pm.aiost.effect.Effect;
import com.pm.aiost.misc.event.eventHandler.EventHandler;
import com.pm.aiost.misc.event.eventHandler.TickableHandler;
import com.pm.aiost.misc.particle.IParticle;

public class ProjectileEventHandler implements EventHandler, TickableHandler {

	protected Entity source;
	protected float damage = 0F;
	protected float knockback = 1F;
	protected Effect effect = Effect.EMPTY;
	protected @Nullable IParticle particle;
	protected int duration = 40;

	public ProjectileEventHandler() {
	}

	public ProjectileEventHandler(Entity source) {
		this.source = source;
	}

	@Override
	public void onTick(Entity entity) {
		if (duration-- < 0)
			entity.remove(); // TODO check for death event!
		if (particle != null)
			particle.spawn(entity.getLocation());
	}

	@Override
	public void onProjectileHit(ProjectileHitEvent event) {
		Entity hitEntity = event.getHitEntity();
		if (hitEntity instanceof LivingEntity)
			((LivingEntity) hitEntity).damage(damage, source);
		applyKnockback(hitEntity);
//		effect.onProjectileHit(AiostEventFactory.callProjectileHitEvent(this, hitEntity));
	}

	public static void applyKnockback(Entity entity) {
//		if (entity instanceof EntityLiving) {
//			EntityLiving entityliving = (EntityLiving) entity;
//			float knockback = getKnockback();
//			if (knockback > 0) {
//				Vec3D vec3d = getMot().d(1.0D, 0.0D, 1.0D).d().a(knockback * 0.6D);
//				if (vec3d.g() > 0.0D)
//					entityliving.h(vec3d.x, 0.1D, vec3d.z);
//			}
//		}
	}

	public Entity getSource() {
		return source;
	}

	public void setSource(Entity source) {
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
		this.duration = duration;
	}
}
