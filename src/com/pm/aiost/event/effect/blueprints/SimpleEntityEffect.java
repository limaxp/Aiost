package com.pm.aiost.event.effect.blueprints;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import com.pm.aiost.player.ServerPlayer;

public abstract class SimpleEntityEffect extends SimplePlayerEffect {

	public SimpleEntityEffect() {
	}

	public SimpleEntityEffect(byte action) {
		super(action);
	}

	public SimpleEntityEffect(byte[] actions) {
		super(actions);
	}

	public SimpleEntityEffect(byte action, byte condition) {
		super(action, condition);
	}

	public SimpleEntityEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	@Override
	public void runEffect(ServerPlayer serverPlayer) {
		runEffect(serverPlayer.player);
	}

	public abstract void runEffect(Entity entity);

	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		runEffect(event.getEntity());
	}

	@Override
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		runEffect(event.getEntity());
	}

	@Override
	public void onEntityDamageByPlayer(ServerPlayer serverPlayer, EntityDamageByEntityEvent event) {
		runEffect(event.getEntity());
	}

	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		runEffect(event.getEntity());
	}

	@Override
	public void onEntityExplode(EntityExplodeEvent event) {
		runEffect(event.getEntity());
	}

	@Override
	public void onEntityPickupItem(EntityPickupItemEvent event) {
		runEffect(event.getEntity());
	}

	@Override
	public void onEntityShootBow(EntityShootBowEvent event) {
		runEffect(event.getEntity());
	}

	@Override
	public void onEntitySpawn(EntitySpawnEvent event) {
		runEffect(event.getEntity());
	}

	@Override
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		runEffect(event.getEntity());
	}

	@Override
	public void onProjectileHit(ProjectileHitEvent event) {
		runEffect(event.getEntity());
	}

	@Override
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		runEffect(event.getEntity());
	}

	@Override
	public void onTick(Entity entity) {
		runEffect(entity);
	}
}
