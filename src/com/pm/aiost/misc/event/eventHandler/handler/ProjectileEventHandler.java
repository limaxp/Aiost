package com.pm.aiost.misc.event.eventHandler.handler;

import java.util.Collection;

import javax.annotation.Nullable;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;

import com.pm.aiost.effect.Effect;
import com.pm.aiost.misc.event.AiostEventFactory;
import com.pm.aiost.misc.event.eventHandler.EventHandler;
import com.pm.aiost.misc.event.eventHandler.TickableHandler;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.particle.IParticle;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

public class ProjectileEventHandler implements EventHandler, TickableHandler {

	protected @Nullable Entity source;
	protected float damage = 0F;
	protected float knockback = 1F;
	protected int duration = 40;
	protected Effect effect = Effect.EMPTY;
	protected @Nullable IParticle particle;

	public ProjectileEventHandler() {
	}

	public ProjectileEventHandler(@Nullable Entity source) {
		this.source = source;
	}

	@Override
	public void onTick(Entity entity) {
		if (duration-- < 0) {
			entity.remove(); // TODO check for death event!
			return;
		}
		if (particle != null)
			particle.spawn(entity.getLocation());

		Collection<Entity> hitEntities = entity.getWorld().getNearbyEntities(entity.getLocation(), 0.25F, 0.25F, 0.25F,
				(e) -> e != entity);
		if (hitEntities.size() > 0) {
			for (Entity hitEntity : hitEntities)
				hit(entity, hitEntity);
			entity.remove(); // TODO check for death event!
		}
	}

	public void hit(Entity entity, Entity hitEntity) {
		System.out.println("HIT " + hitEntity);
		if (hitEntity instanceof LivingEntity)
			((LivingEntity) hitEntity).damage(damage, source);
		applyKnockback(entity, hitEntity);
		effect.onProjectileHit(AiostEventFactory.callProjectileHitEvent((Projectile) entity, hitEntity));
	}

	public void applyKnockback(Entity entity, Entity hitEntity) {
		if (!(entity instanceof LivingEntity) || knockback <= 0)
			return;

		net.minecraft.world.entity.Entity entityNMS = NMS.to(entity);
		net.minecraft.world.entity.LivingEntity hitliving = NMS.to((LivingEntity) hitEntity);
		double d0 = Math.max(0.0, 1.0 - hitliving.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
		Vec3 vec3d = entityNMS.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize()
				.scale((double) this.knockback * 0.6 * d0);
		if (vec3d.lengthSqr() > 0.0)
			hitliving.push(vec3d.x, 0.1, vec3d.z);
	}

	public @Nullable Entity getSource() {
		return source;
	}

	public void setSource(@Nullable Entity source) {
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
