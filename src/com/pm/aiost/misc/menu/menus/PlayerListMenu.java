package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.Aiost;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.ListInventoryMenu;
import com.pm.aiost.misc.other.PlayerHead;
import com.pm.aiost.player.ServerPlayer;

public class PlayerListMenu extends ListInventoryMenu {

	private final List<ServerPlayer> player;

	public PlayerListMenu(List<ServerPlayer> player) {
		super(BOLD + "Player list", true);
		this.player = player;
		setBackLink(ServerPlayer::openEventHandlerMenu);
	}

	@Override
	public Inventory buildInventory(int index) {
		Inventory inv = createInventory(index, 54);
		Bukkit.getScheduler().runTaskAsynchronously(Aiost.getPlugin(), () -> {
			int i = index * MAX_ITEMS;
			int maxLength = i + MAX_ITEMS;
			for (; i < maxLength && i < player.size(); i++) {
				ServerPlayer serverPlayer = player.get(i);
				inv.setItem(10 + i, PlayerHead.create(serverPlayer.player.getUniqueId(), serverPlayer.name));
			}
		});
		return inv;
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {

			default:
				break;
			}
		}
	}
}
