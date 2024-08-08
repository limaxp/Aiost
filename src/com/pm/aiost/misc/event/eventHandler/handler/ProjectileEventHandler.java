package com.pm.aiost.misc.event.eventHandler.handler;

import javax.annotation.Nullable;

import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

import com.pm.aiost.effect.Effect;
import com.pm.aiost.misc.event.eventHandler.EventHandler;
import com.pm.aiost.misc.event.eventHandler.TickableHandler;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.particle.IParticle;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

public class ProjectileEventHandler implements EventHandler, TickableHandler {

	protected @Nullable ProjectileSource source;
	protected float damage = 0F;
	protected float knockback = 1F;
	protected int duration = 10;
	protected Effect effect = Effect.EMPTY;
	protected @Nullable IParticle particle;

	public ProjectileEventHandler() {
	}

	public ProjectileEventHandler(@Nullable ProjectileSource source) {
		this.source = source;
	}

	@Override
	public void onTick(Entity entity) {
		if (duration-- < 0) {
			entity.remove();
			return;
		}
		if (particle != null)
			particle.spawn(entity.getLocation());
	}

	@Override
	public void onProjectileHit(ProjectileHitEvent event) {
		if (event.getHitEntity() != null) {
			applyDamage(event.getEntity(), event.getHitEntity());
			applyKnockback(event.getEntity(), event.getHitEntity());
			effect.onProjectileHit(event);
		}

		if (event.getHitBlock() != null)
			effect.onProjectileHit(event);
	}

	protected void applyDamage(Entity entity, Entity hitEntity) {
		if (!(hitEntity instanceof LivingEntity))
			return;

		DamageSource.Builder damageSource = DamageSource.builder(DamageType.MAGIC);
		damageSource.withDirectEntity(entity);
		damageSource.withDamageLocation(entity.getLocation());
		if (source instanceof Entity)
			damageSource.withCausingEntity((Entity) source);
		((LivingEntity) hitEntity).damage(damage, damageSource.build());
	}

	protected void applyKnockback(Entity entity, Entity hitEntity) {
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
}
