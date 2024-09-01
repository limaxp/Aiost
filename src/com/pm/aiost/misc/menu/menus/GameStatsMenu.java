package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.dataAccess.DataAccess;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.ListInventoryMenu;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class GameStatsMenu extends ListInventoryMenu {

	private UUID uuid;

	public GameStatsMenu(UUID uuid) {
		super(BOLD + uuid + " stats", true);
		this.uuid = uuid;
		setBackLink(ServerPlayer::openMenuRequest);
	}

	@Override
	public Inventory buildInventory(int index) {
		Inventory inv = createInventory(index, 54);
		ResultSet resultSet = null;
		try {
			resultSet = DataAccess.getAccess().getGameStats(uuid, 28, (index * 28));
			set(inv, resultSet, GameStatsMenu::createItem);
		} catch (SQLException e) {
			Logger.err("PlayerWorldMenu: Could not load game stats for game '" + uuid + "'", e);
		} finally {
			DataAccess.getAccess().closeResult(resultSet);
		}
		return inv;
	}

	public static ItemStack createItem(ResultSet resultSet) throws SQLException {
		return MetaHelper.setMeta(Material.PAPER, BOLD + "Stats",
				Arrays.asList(GRAY + "Time: " + DARK_GRAY + resultSet.getInt(0),
						GRAY + "Player: " + DARK_GRAY + resultSet.getInt(1),
						GRAY + "Winner: " + DARK_GRAY + resultSet.getString(2),
						GRAY + "Player Data: " + DARK_GRAY + resultSet.getString(3),
						GRAY + "Date: " + DARK_GRAY + resultSet.getDate(4) + " " + resultSet.getTime(4)));
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
	}
}
