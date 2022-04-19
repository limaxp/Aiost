package com.pm.aiost.event.effect.blueprints;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.pm.aiost.player.ServerPlayer;

public abstract class SimpleEffect extends SimpleEntityEffect {

	public SimpleEffect() {
	}

	public SimpleEffect(byte action) {
		super(action);
	}

	public SimpleEffect(byte[] actions) {
		super(actions);
	}

	public SimpleEffect(byte action, byte condition) {
		super(action, condition);
	}

	public SimpleEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	public abstract void runEffect(Block block);

	@Override
	public void onPlayerInteract(ServerPlayer serverPlayer, PlayerInteractEvent event) {
		runEffect(event.getClickedBlock());
	}

	@Override
	public void onBlockPlace(ServerPlayer serverPlayer, BlockPlaceEvent event) {
		runEffect(event.getBlock());
	}

	@Override
	public void onBlockBreak(ServerPlayer serverPlayer, BlockBreakEvent event) {
		runEffect(event.getBlock());
	}

	@Override
	public void onBlockDamage(ServerPlayer serverPlayer, BlockDamageEvent event) {
		runEffect(event.getBlock());
	}

	@Override
	public void onProjectileHit(ProjectileHitEvent event) {
		runEffect(event.getHitBlock());
	}
}
