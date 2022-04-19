package com.pm.aiost.event.effect.blueprints;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.player.ServerPlayer;

public abstract class BlockEffect extends Effect {

	public BlockEffect() {
	}

	public BlockEffect(byte action) {
		super(action);
	}

	public BlockEffect(byte[] actions) {
		super(actions);
	}

	public BlockEffect(byte action, byte condition) {
		super(action, condition);
	}

	public BlockEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	public abstract void onPlayerEvent(PlayerEvent event);

	public abstract void onEntityEvent(EntityEvent event);

	public abstract void onBlockEvent(BlockEvent event);

	@Override
	public void onPlayerInteract(ServerPlayer serverPlayer, PlayerInteractEvent event) {
		onPlayerEvent(event);
	}

	@Override
	public void onBlockPlace(ServerPlayer serverPlayer, BlockPlaceEvent event) {
		onBlockEvent(event);
	}

	@Override
	public void onBlockBreak(ServerPlayer serverPlayer, BlockBreakEvent event) {
		onBlockEvent(event);
	}

	@Override
	public void onBlockDamage(ServerPlayer serverPlayer, BlockDamageEvent event) {
		onBlockEvent(event);
	}

	@Override
	public void onProjectileHit(ProjectileHitEvent event) {
		onEntityEvent(event);
	}
}