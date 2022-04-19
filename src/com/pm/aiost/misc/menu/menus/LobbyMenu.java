package com.pm.aiost.misc.menu.menus;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.menus.request.ServerMenu;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.Server;
import com.pm.aiost.server.ServerType;
import com.pm.aiost.server.messaging.PluginMessage;
import com.pm.aiost.server.messaging.ServerDataCache;
import com.pm.aiost.server.messaging.ServerDataRequester;

public class LobbyMenu extends ServerMenu {

	private static final LobbyMenu LOBBY_MENU = new LobbyMenu(false);

	private int viewerCount;

	public LobbyMenu(boolean hasBorder) {
		super(ChatColor.BOLD + "Lobby Menu", hasBorder);
		setBackLink(MainMenu.getMenu());
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			Server server = servers.get(InventoryMenu.parseIndex(event.getView().getTitle(), event.getSlot()));
			// TODO check if playerCount to high!
			PluginMessage.connect(serverPlayer, server);
		}
	}

	@Override
	public void onInventoryOpen(InventoryOpenEvent event) {
		super.onInventoryOpen(event);
		if (viewerCount++ == 0)
			ServerDataRequester.registerServerType(ServerType.LOBBY);
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent event) {
		super.onInventoryClose(event);
		if (viewerCount-- == 1)
			ServerDataRequester.unregisterServerType(ServerType.LOBBY);
	}

	public static void updateMenu() {
		if (LOBBY_MENU.viewerCount > 0)
			LOBBY_MENU.update(ServerDataCache.getServerList(ServerType.LOBBY));
	}

	public static InventoryMenu getMenu() {
		return LOBBY_MENU;
	}
}
