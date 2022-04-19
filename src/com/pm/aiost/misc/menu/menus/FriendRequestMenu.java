package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.LongList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.dataAccess.DataAccess;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.ViewInventoryMenu;
import com.pm.aiost.misc.rank.Ranks;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.player.handler.FriendHandler;

public class FriendRequestMenu extends ViewInventoryMenu {

	private ServerPlayer serverPlayer;
	private LongList ids;

	private static final String ACCEPT_TEXT = GRAY + "Left click to accept friend request";
	private static final String DECLINE_TEXT = GRAY + "Right click to decline friend request";

	public FriendRequestMenu(ServerPlayer serverPlayer) {
		super(BOLD + "Friend requests", true);
		this.serverPlayer = serverPlayer;
		setBackLink((serverP) -> serverP.getMenu(FriendMenu.class).open(serverP));
	}

	@Override
	public Inventory buildInventory(int index) {
		ids = new LongArrayList();
		Inventory inv = createInventory(index, 54);
		ResultSet resultSet = null;
		try {
			resultSet = DataAccess.getAccess().getFriendRequests(serverPlayer.getDatabaseID(), index);
			set(inv, resultSet, (result) -> {
				ids.add(result.getLong(2));
				return createItem(result);
			});
		} catch (SQLException e) {
			Logger.err("FriendRequestMenu: Could not load friend requests for player '" + serverPlayer.player.getName()
					+ "'", e);
		} finally {
			DataAccess.getAccess().closeResult(resultSet);
		}
		return inv;
	}

	private static ItemStack createItem(ResultSet resultSet) throws SQLException {
		return MetaHelper.setMeta(Material.PLAYER_HEAD, BOLD + resultSet.getString(3),
				Arrays.asList(ACCEPT_TEXT, DECLINE_TEXT, null, GRAY + "Rank: " + Ranks.get(resultSet.getByte(4)).name,
						GRAY + "Level: " + resultSet.getInt(5), GRAY + "Score: " + resultSet.getInt(6), null,
						GRAY + "Date: " + resultSet.getDate(1)));
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			ClickType clickType = event.getClick();
			if (clickType == ClickType.LEFT)
				createAcceptFreindRequestMenu(ids.getLong(convertSlotToIndex(event.getSlot())),
						is.getItemMeta().getDisplayName(), event.getInventory()).open(serverPlayer);
			if (clickType == ClickType.RIGHT)
				createRemoveFreindRequestMenu(ids.getLong(convertSlotToIndex(event.getSlot())),
						is.getItemMeta().getDisplayName(), event.getInventory()).open(serverPlayer);
		}
	}

	private static YesNoMenu createAcceptFreindRequestMenu(long Id, String name, Inventory inv) {
		YesNoMenu menu = new YesNoMenu(BOLD + "Accept " + name + "?",
				Arrays.asList(GRAY + "Click to " + GREEN + BOLD + "accept" + GRAY + " friend request"));
		menu.setBackLink(inv);
		menu.setYesCallback((serverPlayer, event) -> {
			acceptFriendRequest(serverPlayer, Id);
			serverPlayer.player.openInventory(inv);
		});
		return menu;
	}

	private static YesNoMenu createRemoveFreindRequestMenu(long Id, String name, Inventory inv) {
		YesNoMenu menu = new YesNoMenu(BOLD + "Remove " + name + "?",
				Arrays.asList(GRAY + "Click to " + GREEN + BOLD + "remove" + GRAY + " friend request"));
		menu.setBackLink(inv);
		menu.setYesCallback((serverPlayer, event) -> {
			FriendHandler.removeRequest(serverPlayer, Id);
			serverPlayer.player.openInventory(inv);
		});
		return menu;
	}

	private static void acceptFriendRequest(ServerPlayer serverPlayer, long Id) {
		try {
			DataAccess.getAccess().addFriend(Id, serverPlayer.getDatabaseID());
		} catch (SQLException e) {
			Logger.err("FriendRequestMenu: Error! Could not remove friend request for player '"
					+ serverPlayer.player.getName() + "'", e);
			return;
		}
	}
}
