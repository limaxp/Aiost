package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.collection.list.IdentityArrayList;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.FillableListInventoryMenu;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.Server;

public class ServerMenu extends FillableListInventoryMenu {

	private static final String CLICK_TEXT = GRAY + "Click to join server";

	protected List<Server> servers;

	public ServerMenu(String name, boolean hasBorder) {
		super(name, hasBorder);
		servers = new IdentityArrayList<Server>();
		setBackLink(ServerPlayer::openMenuRequestPrevMenu);
	}

	public ServerMenu(String name, boolean hasBorder, List<Server> servers) {
		super(name, hasBorder);
		this.servers = servers;
		setServers(servers);
		setBackLink(ServerPlayer::openMenuRequestPrevMenu);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getCurrentItem() != null)
			serverPlayer
					.setMenuRequestResult(servers.get(parseBorderedIndex(event.getView().getTitle(), event.getSlot())));
	}

	public void setServers(List<Server> serverList) {
		this.servers = serverList;
		int size = serverList.size();
		for (int i = 0; i < size; i++)
			set(i, createItem(serverList.get(i)));
	}

	public void update(List<Server> serverList) {
		int size = serverList.size();
		int oldSize = servers.size();
		if (size > oldSize) {
			for (int i = oldSize; i < size; i++) {
				Server server = serverList.get(i);
				set(i, createItem(server));
				servers.add(server);
			}
		} else if (size < oldSize) {
			for (int i = oldSize - 1; i >= size; i--) {
				set(i, (ItemStack) null);
				servers.remove(i);
			}
		}
		for (int i = 0; i < oldSize; i++) {
			Server server = serverList.get(i);
			if (server.getId() != servers.get(i).getId()) {
				set(i, createItem(server));
				servers.set(i, server);
			}
		}
	}

	public static ItemStack createItem(Server server) {
		return MetaHelper.setMeta(Material.GOLD_BLOCK, server.getName(),
				Arrays.asList(CLICK_TEXT, null, GRAY + "state: " + server.getState()));
	}
}
