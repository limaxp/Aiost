package com.pm.aiost.event.eventHandler.handler;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.pm.aiost.Aiost;
import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.game.Game;
import com.pm.aiost.player.ServerPlayer;

public interface SpectatorEventHandler extends LobbyEventHandler {

	public static final SpectatorEventHandler INSTANCE = new SpectatorEventHandler() {
	};

	public static SpectatorEventHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public default void onPlayerJoin(ServerPlayer serverPlayer) {
		Player player = serverPlayer.player;
		player.setGameMode(GameMode.SPECTATOR);
		serverPlayer.setInvisible();
		serverPlayer.showInvisibles();
		Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(), () -> {
			player.setGameMode(GameMode.SURVIVAL);
			player.setAllowFlight(true);
			player.setFlying(true);
		}, 5);
	}

	@Override
	public default void onPlayerQuit(ServerPlayer serverPlayer, QuitReason reason) {
		serverPlayer.setVisible();
		serverPlayer.hideInvisibles();
		serverPlayer.player.setAllowFlight(false);
		serverPlayer.player.setFlying(false);
	}

	@Override
	public default boolean canModifyWorld(ServerPlayer serverPlayer) {
		return false;
	}

	@Override
	default boolean allowsChange(EventHandler eventHandler) {
		return !(eventHandler instanceof Game);
	}

	@Override
	public default String getEventHandlerName() {
		return "Spectator";
	}
}
