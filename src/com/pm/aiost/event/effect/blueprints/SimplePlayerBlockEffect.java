package com.pm.aiost.event.effect.blueprints;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.player.ServerPlayer;

public abstract class SimplePlayerBlockEffect extends Effect {

	public SimplePlayerBlockEffect() {
	}

	public SimplePlayerBlockEffect(byte action) {
		super(action);
	}

	public SimplePlayerBlockEffect(byte[] actions) {
		super(actions);
	}

	public SimplePlayerBlockEffect(byte action, byte condition) {
		super(action, condition);
	}

	public SimplePlayerBlockEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	public abstract void runEffect(ServerPlayer serverPlayer, Block block);

	@Override
	public void onPlayerInteract(ServerPlayer serverPlayer, PlayerInteractEvent event) {
		if (event.getClickedBlock() != null)
			runEffect(serverPlayer, event.getClickedBlock());
	}

	@Override
	public void onBlockPlace(ServerPlayer serverPlayer, BlockPlaceEvent event) {
		runEffect(serverPlayer, event.getBlock());
	}

	@Override
	public void onBlockBreak(ServerPlayer serverPlayer, BlockBreakEvent event) {
		runEffect(serverPlayer, event.getBlock());
	}

	@Override
	public void onBlockDamage(ServerPlayer serverPlayer, BlockDamageEvent event) {
		runEffect(serverPlayer, event.getBlock());
	}

	@Override
	public void onPlayerMove(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		runEffect(serverPlayer, event.getTo().getBlock());
	}

	@Override
	public void onPlayerChangeBlockPosition(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		runEffect(serverPlayer, event.getTo().getBlock());
	}

	@Override
	public void onPlayerChangeChunk(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		runEffect(serverPlayer, event.getTo().getBlock());
	}

	@Override
	public void onPlayerFish(ServerPlayer serverPlayer, PlayerFishEvent event) {
		runEffect(serverPlayer, event.getHook().getLocation().getBlock());
	}
}
