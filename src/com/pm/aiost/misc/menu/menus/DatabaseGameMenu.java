package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.AQUA;
import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.game.GameType;
import com.pm.aiost.game.GameTypes;
import com.pm.aiost.game.data.IGameData;
import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.menus.PlayerWorldMenu.PlayerWorldData;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.EnvironmentHelper;

public abstract interface DatabaseGameMenu {

	public static final ItemStack ENTER_NAME_ITEM = MetaHelper.setMeta(Material.WRITABLE_BOOK,
			AQUA + BOLD + "Enter name",
			Arrays.asList(GRAY + "Left click to enter a name", GRAY + "Right click to reset"));

	public static final ItemStack AUTHOR_NAME_ITEM = MetaHelper.setMeta(Material.BOOK,
			AQUA + BOLD + "Enter author name",
			Arrays.asList(GRAY + "Left click to enter an author name", GRAY + "Right click to reset"));

	public static final ItemStack ENTER_UUID_ITEM = MetaHelper.setMeta(Material.KNOWLEDGE_BOOK,
			AQUA + BOLD + "Enter id", Arrays.asList(GRAY + "Left click to enter an id", GRAY + "Right click to reset"));

	public static final ItemStack SORT_NEWEST_ITEM = MetaHelper.setMeta(Material.PAPER, GOLD + BOLD + "Newest",
			Arrays.asList(GRAY + "Click to change sort mode", "", GOLD + BOLD + "-Newest", GRAY + "-Oldest",
					GRAY + "-Best rating", GRAY + "-Worst rating", GRAY + "-Most rated"));

	public static final ItemStack SORT_OLDEST_ITEM = MetaHelper.setMeta(Material.PAPER, GOLD + BOLD + "Oldest",
			Arrays.asList(GRAY + "Click to change sort mode", "", GRAY + "-Newest", GOLD + BOLD + "-Oldest",
					GRAY + "-Best rating", GRAY + "-Worst rating", GRAY + "-Most rated"));

	public static final ItemStack SORT_BEST_RATE_ITEM = MetaHelper.setMeta(Material.PAPER, GOLD + BOLD + "Best rating",
			Arrays.asList(GRAY + "Click to change sort mode", "", GRAY + "-Newest", GRAY + "-Oldest",
					GOLD + BOLD + "-Best rating", GRAY + "-Worst rating", GRAY + "-Most rated"));

	public static final ItemStack SORT_WORST_RATE_ITEM = MetaHelper.setMeta(Material.PAPER,
			GOLD + BOLD + "Worst rating", Arrays.asList(GRAY + "Click to change sort mode", "", GRAY + "-Newest",
					GRAY + "-Oldest", GRAY + "-Best rating", GOLD + BOLD + "-Worst rating", GRAY + "-Most rated"));

	public static final ItemStack SORT_MOST_RATE_ITEM = MetaHelper.setMeta(Material.PAPER, GOLD + BOLD + "Most rated",
			Arrays.asList(GRAY + "Click to change sort mode", "", GRAY + "-Newest", GRAY + "-Oldest",
					GRAY + "-Best rating", GRAY + "-Worst rating", GOLD + BOLD + "-Most rated"));

	public static final int SORT_MODE_LAST_INDEX = 4;
	public static final ItemStack[] SORT_ITEMS = new ItemStack[] { SORT_NEWEST_ITEM, SORT_OLDEST_ITEM,
			SORT_BEST_RATE_ITEM, SORT_WORST_RATE_ITEM, SORT_MOST_RATE_ITEM };

	public static final int SORT_MODE_ITEM_SLOT = 0;
	public static final int UUID_ITEM_SLOT = 6;
	public static final int AUTHOR_NAME_ITEM_SLOT = 7;
	public static final int NAME_ITEM_SLOT = 8;

	public abstract void loadInventory(Inventory inv, int index);

	public abstract void resetMenu(Inventory currentlyUsed);

	public abstract void setGameName(String name);

	public abstract String getGameName();

	public abstract void setSortMode(int sortMode);

	public abstract byte getSortMode();

	public abstract void addData(GameData data);

	public abstract GameData getData(int index);

	public default ItemStack createItem(ResultSet resultSet, String clickText1, String clickText2) throws SQLException {
		GameData data = createData(resultSet);
		addData(data);
		return createItem(data, clickText1, clickText2);
	}

	public static GameData createData(ResultSet resultSet) throws SQLException {
		GameData data = new GameData();
		data.uuid = UUID.fromString(resultSet.getString(1));
		data.locationId = 0;
		data.name = resultSet.getString(2);
		data.gameType = GameTypes.get(resultSet.getInt(3));
//		long ownerID = resultSet.getLong(4); // TODO: delete ownerID here and in queries!
		data.authorName = resultSet.getString(5);
		data.environment = Environment.values()[resultSet.getByte(6)];
		data.worldType = AiostRegistry.WORLD_TYPES.get(resultSet.getInt(7));
		data.generateStructures = resultSet.getBoolean(8);
		data.rate = resultSet.getDouble(10);
		data.rateAmount = resultSet.getLong(9);
		data.releaseDate = resultSet.getDate(11);
		data.saveDate = resultSet.getDate(12);
		data.saveTime = resultSet.getTime(12);
		return data;
	}

	public static ItemStack createItem(GameData data, String clickText1, String clickText2) {
		return MetaHelper.setMeta(data.gameType.item.clone(), BOLD + data.name, Arrays.asList(clickText1, clickText2,
				null, GRAY + "ID: " + DARK_GRAY + data.uuid.toString().substring(0, 18),
				GRAY + "Type: " + DARK_GRAY + data.gameType.name, GRAY + "Author: " + DARK_GRAY + data.authorName,
				GRAY + "Environment: " + DARK_GRAY + EnvironmentHelper.getDisplayName(data.environment),
				GRAY + "World type: " + DARK_GRAY + data.worldType.name,
				GRAY + "Structures: " + DARK_GRAY + (data.generateStructures ? "Yes" : "no"),
				GRAY + "Rate: " + DARK_GRAY + data.rate, GRAY + "Rate amount: " + DARK_GRAY + data.rateAmount, null,
				GRAY + "Release date: " + DARK_GRAY + data.releaseDate,
				GRAY + "Last updated: " + DARK_GRAY + data.saveDate + " " + data.saveTime));
	}

	public default void changeSortType(Inventory inv, ClickType click) {
		byte sortMode = getSortMode();
		if (click == ClickType.LEFT) {
			if (sortMode < SORT_MODE_LAST_INDEX)
				setSortMode(sortMode + 1);
			else
				setSortMode(0);
		} else if (click == ClickType.RIGHT) {
			if (sortMode > 0)
				setSortMode(sortMode - 1);
			else
				setSortMode(SORT_MODE_LAST_INDEX);
		}
		inv.setItem(SORT_MODE_ITEM_SLOT, SORT_ITEMS[sortMode]);
		resetMenu(inv);
	}

	public default AnvilMenu createNameMenu(Inventory inv) {
		String gameName = getGameName();
		AnvilMenu menu = new AnvilMenu(BOLD + "Choose name",
				MetaHelper.setMeta(Material.PAPER, gameName == null ? "name" : gameName)) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					String text = event.getCurrentItem().getItemMeta().getDisplayName();
					if (text.isEmpty())
						setGameName(null);
					else
						setGameName(text);
					serverPlayer.player.openInventory(inv);
					resetMenu(inv);
				}
			}
		};
		menu.setBackLink(inv);
		return menu;
	}

	public default InventoryMenu createHostWorldMenu(int dataIndex, Inventory inv) {
		InventoryMenu menu = new GameStartMenu(getData(dataIndex));
		menu.setBackLink(inv);
		return menu;
	}

	public static class GameData extends PlayerWorldData implements IGameData {
		public GameType<?> gameType;
		public String authorName;
		public double rate;
		public long rateAmount;
		public Date releaseDate;

		@Override
		public int getId() {
			return 0;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public GameType<?> getType() {
			return gameType;
		}

		@Override
		public UUID getUniqueId() {
			return uuid;
		}

		@Override
		public String getAuthorName() {
			return authorName;
		}

		@Override
		public int getPlayerSize() {
			return 0;
		}

		@Override
		public int getMinPlayer() {
			return gameType.minPlayer;
		}

		@Override
		public int getMaxPlayer() {
			return gameType.maxPlayer;
		}

		@Override
		public boolean hasPassword() {
			return false;
		}

		@Override
		public boolean checkPassword(String password) {
			return true;
		}
	}
}
