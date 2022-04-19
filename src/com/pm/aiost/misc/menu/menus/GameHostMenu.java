package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.game.GameType;
import com.pm.aiost.misc.dataAccess.DataAccess;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.ViewInventoryMenu;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class GameHostMenu extends ViewInventoryMenu implements DatabaseGameMenu {

	private static final int[] BORDER_ITEM_SLOTS = new int[] { SORT_MODE_ITEM_SLOT, UUID_ITEM_SLOT,
			AUTHOR_NAME_ITEM_SLOT, NAME_ITEM_SLOT };

	private GameType<?> type;
	private String gameName;
	private byte sortMode;
	private String authorName;
	private String uuid;
	private GameData[] dataArray;
	private int dataIndex;

	public GameHostMenu(GameType<?> type) {
		super(BOLD + type.name, true);
		this.type = type;
		setBackLink(ServerPlayer::openMenuRequest);
	}

	@Override
	public Inventory buildInventory(int index) {
		Inventory inv = createInventory(index, 54);
		addBorderItems(inv, BORDER_ITEM_SLOTS, SORT_ITEMS[sortMode], ENTER_UUID_ITEM, AUTHOR_NAME_ITEM,
				ENTER_NAME_ITEM);
		loadInventory(inv, index);
		return inv;
	}

	@Override
	public void loadInventory(Inventory inv, int index) {
		dataArray = new GameData[InventoryMenu.MAX_ITEMS_WITH_BORDER];
		dataIndex = 0;
		ResultSet resultSet = null;
		try {
			resultSet = runDatabaseFunction(index);
			set(inv, resultSet, this::createItem);
		} catch (SQLException e) {
			Logger.err("HostGameMenu: Could not load games", e);
		} finally {
			DataAccess.getAccess().closeResult(resultSet);
		}
	}

	public ItemStack createItem(ResultSet resultSet) throws SQLException {
		return createItem(resultSet, GRAY + "click to host Game", null);
	}

	@Override
	public void resetMenu(Inventory currentlyUsed) {
		Inventory newInv = buildInventory(0);
		List<HumanEntity> viewer = currentlyUsed.getViewers();
		int length = viewer.size();
		for (int i = 0; i < length; i++)
			viewer.get(0).openInventory(newInv);
	}

	protected ResultSet runDatabaseFunction(int index) throws SQLException {
		if (uuid != null) {
			UUID parse = parseUUID(uuid);
			if (parse != null)
				return DataAccess.getAccess().getGame(parse);
		}
		if (gameName != null) {
			if (authorName != null)
				return DataAccess.getAccess().getGames(type.getId(), gameName, authorName, sortMode, index);
			else
				return DataAccess.getAccess().getGames(type.getId(), gameName, sortMode, index);
		} else if (authorName != null)
			return DataAccess.getAccess().getGamesPerPlayer(type.getId(), authorName, sortMode, index);
		else
			return DataAccess.getAccess().getGames(type.getId(), sortMode, index);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			int slot = event.getSlot();
			if (slot == SORT_MODE_ITEM_SLOT)
				changeSortType(event.getInventory(), event.getClick());
			else if (slot == UUID_ITEM_SLOT) {
				if (event.getClick() == ClickType.LEFT)
					createUUIDMenu(event.getInventory()).open(serverPlayer);
				else if (event.getClick() == ClickType.RIGHT)
					resetUUID(event.getInventory());
			} else if (slot == AUTHOR_NAME_ITEM_SLOT) {
				if (event.getClick() == ClickType.LEFT)
					createAuthorNameMenu(event.getInventory()).open(serverPlayer);
				else if (event.getClick() == ClickType.RIGHT)
					resetAuthorName(event.getInventory());
			} else if (slot == NAME_ITEM_SLOT) {
				if (event.getClick() == ClickType.LEFT)
					createNameMenu(event.getInventory()).open(serverPlayer);
				else if (event.getClick() == ClickType.RIGHT)
					resetGameName(event.getInventory());
			} else {
				int dataIndex = convertSlotToIndex(event.getSlot());
				createHostWorldMenu(dataIndex, event.getInventory()).open(serverPlayer);
			}
		}
	}

	protected void resetGameName(Inventory inv) {
		if (gameName != null) {
			gameName = null;
			resetMenu(inv);
		}
	}

	protected void resetAuthorName(Inventory inv) {
		if (authorName != null) {
			authorName = null;
			resetMenu(inv);
		}
	}

	protected void resetUUID(Inventory inv) {
		if (uuid != null) {
			uuid = null;
			resetMenu(inv);
		}
	}

	public AnvilMenu createAuthorNameMenu(Inventory inv) {
		AnvilMenu menu = new AnvilMenu(BOLD + "Choose author",
				MetaHelper.setMeta(Material.PAPER, authorName == null ? "name" : authorName)) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					String text = event.getCurrentItem().getItemMeta().getDisplayName();
					if (text.isEmpty())
						authorName = null;
					else
						authorName = text;
					serverPlayer.player.openInventory(inv);
					resetMenu(inv);
				}
			}
		};
		menu.setBackLink(inv);
		return menu;
	}

	public AnvilMenu createUUIDMenu(Inventory inv) {
		AnvilMenu menu = new AnvilMenu(BOLD + "Choose id",
				MetaHelper.setMeta(Material.PAPER, uuid == null ? "id" : uuid.toString())) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					uuid = event.getCurrentItem().getItemMeta().getDisplayName();
					serverPlayer.player.openInventory(inv);
					resetMenu(inv);
				}
			}
		};
		menu.setBackLink(inv);
		return menu;
	}

	void setGameType(GameType<?> type) {
		this.type = type;
		this.name = BOLD + type.name;
	}

	@Override
	public void setGameName(String name) {
		this.gameName = name;
	}

	@Override
	public String getGameName() {
		return gameName;
	}

	@Override
	public void setSortMode(int sortMode) {
		this.sortMode = (byte) sortMode;
	}

	@Override
	public byte getSortMode() {
		return sortMode;
	}

	@Override
	public void addData(GameData data) {
		dataArray[dataIndex++] = data;
	}

	@Override
	public GameData getData(int index) {
		return dataArray[index];
	}

	private static @Nullable UUID parseUUID(String s) {
		try {
			return UUID.fromString(s);
		} catch (Exception e) {
			return null;
		}
	}
}
