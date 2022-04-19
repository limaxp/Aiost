package com.pm.aiost.misc.menu;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.pm.aiost.player.ServerPlayer;

public interface Menu {

	public static final BiConsumer<ServerPlayer, InventoryClickEvent> NULL_CLICK_CALLBACK = (serverPlayer, event) -> {
	};

	public static final Consumer<ServerPlayer> DEFAULT_BACK_LINK = (serverPlayer) -> {
		serverPlayer.player.closeInventory();
	};

	public void open(Player player);

	public void open(HumanEntity player);

	public void open(ServerPlayer serverPlayer);

	public default Sound getClickSound() {
		return Sound.BLOCK_LEVER_CLICK;
	}

	public default void playClickSound(Player player) {
		player.playSound(player.getLocation(), getClickSound(), 1, 1);
	}
}
