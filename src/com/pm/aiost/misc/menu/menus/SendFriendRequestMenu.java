package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.AQUA;
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
import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.ViewInventoryMenu;
import com.pm.aiost.misc.rank.Ranks;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.player.handler.FriendHandler;

public class SendFriendRequestMenu extends ViewInventoryMenu {

	private static final ItemStack SEND_ITEM = MetaHelper.setMeta(Material.WRITABLE_BOOK, AQUA + BOLD + "Send request",
			Arrays.asList(GRAY + "Click to send a friend request", "",
					GRAY + "Or use " + BOLD + "/friend [name] " + GRAY + "instead"));

	private static final String CLICK_TEXT = GRAY + "Shift + right click to remove friend request";

	private ServerPlayer serverPlayer;
	private LongList ids;

	public SendFriendRequestMenu(ServerPlayer serverPlayer) {
		super(BOLD + "Send Friend requests", true);
		this.serverPlayer = serverPlayer;
		setBackLink((serverP) -> serverP.getMenu(FriendMenu.class).open(serverP));
	}

	@Override
	public Inventory buildInventory(int index) {
		ids = new LongArrayList();
		Inventory inv = createInventory(index, 54);
		addBorderItem(inv, 8, SEND_ITEM);
		ResultSet resultSet = null;
		try {
			resultSet = DataAccess.getAccess().getSendFriendRequests(serverPlayer.getDatabaseID(), index);
			set(inv, resultSet, (result) -> {
				ids.add(result.getLong(2));
				return createItem(result);
			});
		} catch (SQLException e) {
			Logger.err("SendFriendRequestMenu: Could not load send friend requests for player '"
					+ serverPlayer.player.getName() + "'", e);
		} finally {
			DataAccess.getAccess().closeResult(resultSet);
		}
		return inv;
	}

	private static ItemStack createItem(ResultSet resultSet) throws SQLException {
		return MetaHelper.setMeta(Material.PLAYER_HEAD, BOLD + resultSet.getString(3),
				Arrays.asList(CLICK_TEXT, null, GRAY + "Rank: " + Ranks.get(resultSet.getByte(4)).name,
						GRAY + "Level: " + resultSet.getInt(5), GRAY + "Score: " + resultSet.getInt(6), null,
						GRAY + "Date: " + resultSet.getDate(1)));
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {
			case PLAYER_HEAD:
				if (event.getClick() == ClickType.SHIFT_RIGHT)
					createRemoveFreindRequestMenu(ids.getLong(convertSlotToIndex(event.getSlot())),
							is.getItemMeta().getDisplayName(), event.getInventory()).open(serverPlayer);
				break;

			case WRITABLE_BOOK:
				createFriendRequestMenu().open(serverPlayer);
				break;

			default:
				break;
			}
		}
	}

	private AnvilMenu createFriendRequestMenu() {
		AnvilMenu menu = new AnvilMenu(BOLD + "Choose name") {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					FriendHandler.sendRequest(serverPlayer, event.getCurrentItem().getItemMeta().getDisplayName());
					SendFriendRequestMenu.this.open(serverPlayer);
				}
			}
		};
		menu.setBackLink(this);
		return menu;
	}

	private static YesNoMenu createRemoveFreindRequestMenu(long Id, String name, Inventory inv) {
		YesNoMenu menu = new YesNoMenu(BOLD + "Remove " + name + "?",
				Arrays.asList(GRAY + "Click to " + GREEN + BOLD + "remove" + GRAY + " friend request"));
		menu.setBackLink(inv);
		menu.setYesCallback((serverPlayer, event) -> {
			FriendHandler.removeRequest(Id, serverPlayer);
			serverPlayer.player.openInventory(inv);
		});
		return menu;
	}
}
