package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_PURPLE;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.YELLOW;

import java.util.Arrays;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.pm.aiost.game.GameKit;
import com.pm.aiost.game.GamePlayer;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.LazyInventoryMenu;
import com.pm.aiost.player.ServerPlayer;

public class GameKitMenu extends LazyInventoryMenu {

	private static final String CLICK_TEXT = GRAY + "Click to change kit";

	private GameKit[] kits;

	public GameKitMenu(GameKit[] kits) {
		this(BOLD + "Kit Menu", kits);
	}

	public GameKitMenu(String name, GameKit[] kits) {
		super(name, kits.length, true);
		this.kits = kits;
	}

	@Override
	public void buildInventory(Inventory inv, int index) {
		set(inv, 0, index * InventoryMenu.MAX_ITEMS_WITH_BORDER, kits.length,
				(kitIndex) -> kits[kitIndex].createMenuItem(CLICK_TEXT));
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getCurrentItem() != null)
			kitInventoryClick(serverPlayer, event);
	}

	protected void kitInventoryClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		int index = InventoryMenu.parseBorderedIndex(event.getView().getTitle(), event.getSlot());
		GamePlayer gamePlayer = serverPlayer.getGameData();
		if (gamePlayer.hasKit(index)) {
			ServerPlayer.sendActionBar(serverPlayer.player,
					YELLOW + gamePlayer.getKit().name + " kit is already selected!");
			serverPlayer.player.closeInventory();
			return;
		}
		if (serverPlayer.hasCooldown(GameKit.class)) {
			ServerPlayer.sendActionBar(serverPlayer.player,
					YELLOW + "You have to wait at least 2 seconds between changing kit!");
			serverPlayer.player.closeInventory();
			return;
		}
		GameKit kit = kits[index];
		if (kit.getPrice() > 0 && !serverPlayer.hasUnlockable(gamePlayer.game.getType(), (short) index)) {
			BuyMenu.openBuyUnlockableMenu(serverPlayer, DARK_PURPLE + BOLD + kit.name,
					Arrays.asList(kit.getDescription()), gamePlayer.game.getType().getId(), (short) index,
					kit.getPrice(), event.getInventory(), () -> {
					});
			return;
		}

		gamePlayer.equipKit(index);
		ServerPlayer.sendActionBar(serverPlayer.player, kit.name + " kit selected");
		serverPlayer.setCooldown(GameKit.class, 40);
		serverPlayer.player.closeInventory();
	}
}
