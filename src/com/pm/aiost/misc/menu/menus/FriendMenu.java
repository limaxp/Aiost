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
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.ViewInventoryMenu;
import com.pm.aiost.misc.rank.Ranks;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.player.handler.FriendHandler;

public class FriendMenu extends ViewInventoryMenu {

	private static final String CLICK_TEXT = GRAY + "Shift + right click to remove friend";

	private static final ItemStack VIEW_ITEM = MetaHelper.setMeta(Material.PAPER, AQUA + BOLD + "View requests",
			Arrays.asList(GRAY + "Click to view your friend requests"));

	private static final ItemStack SEND_ITEM = MetaHelper.setMeta(Material.WRITABLE_BOOK, AQUA + BOLD + "Send request",
			Arrays.asList(GRAY + "Click to send a friend request"));

	private ServerPlayer serverPlayer;
	private LongList ids;

	public FriendMenu(ServerPlayer serverPlayer) {
		super(BOLD + "Friends", true);
		this.serverPlayer = serverPlayer;
		setBackLink(SocialMenu.getMenu());
	}

	@Override
	public Inventory buildInventory(int index) {
		ids = new LongArrayList();
		Inventory inv = createInventory(index, 54);
		addBorderItems(inv, new int[] { 0, 8 }, VIEW_ITEM, SEND_ITEM);
		ResultSet resultSet = null;
		try {
			resultSet = DataAccess.getAccess().getFriends(serverPlayer.getDatabaseID(), index);
			set(inv, resultSet, (result) -> {
				ids.add(result.getLong(1));
				return createItem(result);
			});
		} catch (SQLException e) {
			Logger.err("FriendMenu: Could not load friends for player '" + serverPlayer.player.getName() + "'", e);
		} finally {
			DataAccess.getAccess().closeResult(resultSet);
		}
		return inv;
	}

	private static ItemStack createItem(ResultSet resultSet) throws SQLException {
		return MetaHelper.setMeta(Material.PLAYER_HEAD, BOLD + resultSet.getString(2),
				Arrays.asList(CLICK_TEXT, null, GRAY + "Rank: " + Ranks.get(resultSet.getByte(3)).name,
						GRAY + "Level: " + resultSet.getInt(4), GRAY + "Score: " + resultSet.getInt(5)));
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {
			case PAPER:
				serverPlayer.getOrCreateMenu(FriendRequestMenu.class, FriendRequestMenu::new).open(serverPlayer);
				break;

			case WRITABLE_BOOK:
				serverPlayer.getOrCreateMenu(SendFriendRequestMenu.class, SendFriendRequestMenu::new)
						.open(serverPlayer);
				break;

			case PLAYER_HEAD:
				if (event.getClick() == ClickType.SHIFT_RIGHT)
					createRemoveFreindMenu(ids.getLong(convertSlotToIndex(event.getSlot())),
							is.getItemMeta().getDisplayName(), event.getInventory()).open(serverPlayer);
				break;

			default:
				break;
			}
		}
	}

	private static YesNoMenu createRemoveFreindMenu(long Id, String name, Inventory inv) {
		YesNoMenu menu = new YesNoMenu(BOLD + "Remove " + name + "?",
				Arrays.asList(GRAY + "Click to " + GREEN + BOLD + "remove" + GRAY + " friend"));
		menu.setBackLink(inv);
		menu.setYesCallback((serverPlayer, event) -> {
			FriendHandler.remove(serverPlayer, Id);
			serverPlayer.player.openInventory(inv);
		});
		return menu;
	}
}
