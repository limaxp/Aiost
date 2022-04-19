package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.dataAccess.DataAccess;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.ListInventoryMenu;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.creation.WorldLoader;

public class ReleasedGameMenu extends ListInventoryMenu implements DatabaseGameMenu {

	private static final int[] BORDER_ITEM_SLOTS = new int[] { SORT_MODE_ITEM_SLOT, NAME_ITEM_SLOT };

	private ServerPlayer serverPlayer;
	protected String gameName;
	protected byte sortMode;
	private List<GameData> dataList;

	public ReleasedGameMenu(ServerPlayer serverPlayer) {
		super(BOLD + "Your released games", true);
		this.serverPlayer = serverPlayer;
		dataList = new ArrayList<GameData>();
		setBackLink(WorldMenu.getMenu());
	}

	@Override
	protected Inventory buildInventory(int index) {
		Inventory inv = createInventory(index, 54);
		addBorderItems(inv, BORDER_ITEM_SLOTS, SORT_ITEMS[sortMode], ENTER_NAME_ITEM);
		loadInventory(inv, index);
		return inv;
	}

	@Override
	public void loadInventory(Inventory inv, int index) {
		ResultSet resultSet = null;
		try {
			resultSet = runDatabaseFunction(index);
			set(inv, resultSet, this::createItem);
		} catch (SQLException e) {
			Logger.err("ReleasedGameMenu: Could not load worlds for player '" + serverPlayer.player.getName() + "'", e);
		} finally {
			DataAccess.getAccess().closeResult(resultSet);
		}
	}

	public ItemStack createItem(ResultSet resultSet) throws SQLException {
		return createItem(resultSet, GRAY + "Left click to host game", GRAY + "Right click to open game world");
	}

	@Override
	public void resetMenu(Inventory currentlyUsed) {
		reset();
		dataList.clear();
		Inventory newInv = createInventory(0);
		List<HumanEntity> viewer = currentlyUsed.getViewers();
		int length = viewer.size();
		for (int i = 0; i < length; i++)
			viewer.get(i).openInventory(newInv);
	}

	protected ResultSet runDatabaseFunction(int index) throws SQLException {
		if (gameName != null)
			return DataAccess.getAccess().getGames(gameName, serverPlayer.getDatabaseID(), sortMode, index);
		else
			return DataAccess.getAccess().getGamesPerPlayer(serverPlayer.getDatabaseID(), sortMode, index);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			int slot = event.getSlot();
			if (slot == SORT_MODE_ITEM_SLOT)
				changeSortType(event.getInventory(), event.getClick());
			else if (slot == NAME_ITEM_SLOT) {
				if (event.getClick() == ClickType.LEFT)
					createNameMenu(event.getInventory()).open(serverPlayer);
				else if (event.getClick() == ClickType.RIGHT)
					resetGameName(event.getInventory());
			} else {
				int dataIndex = parseBorderedIndex(event.getView().getTitle(), event.getSlot());
				if (event.getClick() == ClickType.LEFT)
					createHostWorldMenu(dataIndex, event.getInventory()).open(serverPlayer);
				else if (event.getClick() == ClickType.RIGHT)
					createOpenWorldMenu(dataIndex, event.getInventory()).open(serverPlayer);
			}
		}
	}

	public YesNoMenu createOpenWorldMenu(int dataIndex, Inventory inv) {
		GameData data = getData(dataIndex);
		YesNoMenu menu = new YesNoMenu(BOLD + "Open " + data.name + "?",
				Arrays.asList(GRAY + "Click to " + GREEN + BOLD + "open" + GRAY + " game world"));
		menu.setBackLink(inv);
		menu.setYesCallback((serverPlayer, event) -> WorldLoader.loadReleasedWorld(serverPlayer, data.uuid, data.name,
				data.environment, data.worldType, data.generateStructures, data.gameType));
		return menu;
	}

	protected void resetGameName(Inventory inv) {
		if (gameName != null) {
			gameName = null;
			resetMenu(inv);
		}
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
		dataList.add(data);
	}

	@Override
	public GameData getData(int index) {
		return dataList.get(index);
	}
}
