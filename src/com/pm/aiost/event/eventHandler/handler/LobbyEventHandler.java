package com.pm.aiost.event.eventHandler.handler;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.event.events.PacketThingAttackEvent;
import com.pm.aiost.misc.scoreboard.scoreboards.LobbyScoreboard;
import com.pm.aiost.player.ServerPlayer;

public interface LobbyEventHandler extends EventHandler {

	public static final LobbyEventHandler INSTANCE = new LobbyEventHandler() {
	};

	public static LobbyEventHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public default void onPlayerJoin(ServerPlayer serverPlayer) {
		LobbyScoreboard.addPlayer(serverPlayer);
	}

	@Override
	public default void onPlayerQuit(ServerPlayer serverPlayer, QuitReason reason) {
		if (reason != QuitReason.DISABLE)
			LobbyScoreboard.removePlayer(serverPlayer);
	}

	@Override
	default void onEntityTargetPlayer(ServerPlayer serverPlayer, EntityTargetEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onPlayerDamage(ServerPlayer serverPlayer, EntityDamageEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onPlayerDamageByEntity(ServerPlayer serverPlayer, EntityDamageByEntityEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onBlockPlace(ServerPlayer serverPlayer, BlockPlaceEvent event) {
		if (!canModifyWorld(serverPlayer))
			event.setCancelled(true);
	}

	@Override
	public default void onBlockBreak(ServerPlayer serverPlayer, BlockBreakEvent event) {
		if (!canModifyWorld(serverPlayer))
			event.setCancelled(true);
	}

	@Override
	public default void onBlockDamage(ServerPlayer serverPlayer, BlockDamageEvent event) {
		if (!canModifyWorld(serverPlayer))
			event.setCancelled(true);
	}

	@Override
	public default void onPacketThingAttack(PacketThingAttackEvent event) {
		if (!canModifyWorld(event.getServerPlayer()))
			event.setCancelled(true);
	}

	@Override
	public default void onEntityDamage(EntityDamageEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onEntityExplode(EntityExplodeEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default boolean canModifyWorld(ServerPlayer serverPlayer) {
		return serverPlayer.isAdmin();
	}

	@Override
	public default String getEventHandlerName() {
		return "Lobby";
	}
}
