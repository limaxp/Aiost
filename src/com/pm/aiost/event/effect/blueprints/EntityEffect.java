package com.pm.aiost.event.effect.blueprints;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.player.ServerPlayer;

public abstract class EntityEffect extends Effect {

	public EntityEffect() {
	}

	public EntityEffect(byte action) {
		super(action);
	}

	public EntityEffect(byte[] actions) {
		super(actions);
	}

	public EntityEffect(byte action, byte condition) {
		super(action, condition);
	}

	public EntityEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	public abstract void onEntityEvent(EntityEvent event);

	@Override
	public abstract void onTick(Entity entity);

	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		onEntityEvent(event);
	}

	@Override
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		onEntityEvent(event);
	}

	@Override
	public void onEntityDamageByPlayer(ServerPlayer serverPlayer, EntityDamageByEntityEvent event) {
		onEntityEvent(event);
	}

	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		onEntityEvent(event);
	}

	@Override
	public void onEntityExplode(EntityExplodeEvent event) {
		onEntityEvent(event);
	}

	@Override
	public void onEntityPickupItem(EntityPickupItemEvent event) {
		onEntityEvent(event);
	}

	@Override
	public void onEntityShootBow(EntityShootBowEvent event) {
		onEntityEvent(event);
	}

	@Override
	public void onEntitySpawn(EntitySpawnEvent event) {
		onEntityEvent(event);
	}

	@Override
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		onEntityEvent(event);
	}

	@Override
	public void onProjectileHit(ProjectileHitEvent event) {
		onEntityEvent(event);
	}

	@Override
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		onEntityEvent(event);
	}
}