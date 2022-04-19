package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pm.aiost.misc.dataAccess.DataAccess;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.ListInventoryMenu;
import com.pm.aiost.misc.other.WordFilter;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.EnvironmentHelper;
import com.pm.aiost.server.world.creation.WorldLoader;
import com.pm.aiost.server.world.type.AiostWorldType;

public class PlayerWorldMenu extends ListInventoryMenu {

	private static final ItemStack CANNOT_DELETE_ITEM = MetaHelper.setMeta(Material.BARRIER,
			RED + BOLD + "Cannot delete!", Arrays.asList(GRAY + "World is used in game!", GRAY + "Cannot be deleted!"));

	private static final String LOAD_TEXT = GRAY + "Left click to load world";
	private static final String RENAME_TEXT = GRAY + "Shift left click to rename world";
	private static final String DELETE_TEXT = GRAY + "Shift right click to delete world";

	private ServerPlayer serverPlayer;
	private List<PlayerWorldData> dataList;

	public PlayerWorldMenu(ServerPlayer serverPlayer) {
		super(BOLD + serverPlayer.player.getName() + "'s worlds", true);
		this.serverPlayer = serverPlayer;
		dataList = new ArrayList<PlayerWorldData>();
		setBackLink(WorldMenu.getMenu());
	}

	@Override
	public Inventory buildInventory(int index) {
		Inventory inv = createInventory(index, 54);
		ResultSet resultSet = null;
		try {
			resultSet = DataAccess.getAccess().getPlayerWorlds(serverPlayer.getDatabaseID(), 28, (index * 28));
			set(inv, resultSet, this::createItem);
		} catch (SQLException e) {
			Logger.err("PlayerWorldMenu: Could not load worlds for player '" + serverPlayer.player.getName() + "'", e);
		} finally {
			DataAccess.getAccess().closeResult(resultSet);
		}
		return inv;
	}

	private ItemStack createItem(ResultSet resultSet) throws SQLException {
		PlayerWorldData data = createData(resultSet);
		dataList.add(data);
		return createItem(data);
	}

	public static PlayerWorldData createData(ResultSet resultSet) throws SQLException {
		PlayerWorldData data = new PlayerWorldData();
		data.uuid = UUID.fromString(resultSet.getString(1));
		data.locationId = resultSet.getInt(2);
		data.name = resultSet.getString(3);
		data.environment = Environment.values()[resultSet.getByte(4)];
		data.worldType = AiostRegistry.WORLD_TYPES.get(resultSet.getInt(5));
		data.generateStructures = resultSet.getBoolean(6);
		data.saveDate = resultSet.getDate(7);
		data.saveTime = resultSet.getTime(7);
		return data;
	}

	public static ItemStack createItem(PlayerWorldData data) {
		return MetaHelper.setMeta(EnvironmentHelper.getMaterial(data.environment), BOLD + data.name,
				Arrays.asList(LOAD_TEXT, RENAME_TEXT, DELETE_TEXT, null, null,
						GRAY + "ID: " + DARK_GRAY + data.uuid.toString().substring(0, 18),
						GRAY + "Environment: " + DARK_GRAY + EnvironmentHelper.getDisplayName(data.environment),
						GRAY + "Type: " + DARK_GRAY + data.worldType.name,
						GRAY + "Generates structures: " + DARK_GRAY + (data.generateStructures ? "Yes" : "No"), null,
						GRAY + "Last saved: " + DARK_GRAY + data.saveDate + " " + data.saveTime));
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			int dataIndex = parseBorderedIndex(event.getView().getTitle(), event.getSlot());

			switch (event.getClick()) {
			case LEFT:
				createLoadWorldMenu(dataIndex, event.getInventory()).open(serverPlayer);
				break;

			case SHIFT_LEFT:
				renameWorldMenu(event.getCurrentItem(), dataIndex, event.getInventory()).open(serverPlayer);
				break;

			case SHIFT_RIGHT:
				createDeleteWorldMenu(event.getSlot(), dataIndex, event.getInventory()).open(serverPlayer);
				break;

			default:
				break;
			}
		}
	}

	private YesNoMenu createLoadWorldMenu(int dataIndex, Inventory inv) {
		PlayerWorldData data = dataList.get(dataIndex);
		YesNoMenu menu = new YesNoMenu(BOLD + "Load " + data.name + "?",
				Arrays.asList(GRAY + "Click to " + GREEN + BOLD + "load" + GRAY + " world"));
		menu.setBackLink(inv);
		menu.setYesCallback((serverPlayer, event) -> WorldLoader.loadPlayerWorld(serverPlayer, data.uuid, data.name,
				data.environment, data.worldType, data.generateStructures));
		return menu;
	}

	private AnvilMenu renameWorldMenu(ItemStack is, int dataIndex, Inventory inv) {
		PlayerWorldData data = dataList.get(dataIndex);
		AnvilMenu menu = new AnvilMenu(BOLD + "Rename world", MetaHelper.setMeta(Material.PAPER, data.name)) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					String newName = event.getCurrentItem().getItemMeta().getDisplayName();
					if (!WordFilter.containsBlocked(newName)) {
						WorldLoader.renamePlayerWorld(serverPlayer, data.uuid, newName, false);
						ItemMeta im = is.getItemMeta();
						im.setDisplayName(newName);
						is.setItemMeta(im);
					} else
						serverPlayer.player.sendMessage(RED + BOLD + "The given name is not allowed!");
					serverPlayer.player.openInventory(inv);
				}
			}
		};
		menu.setBackLink(inv);
		return menu;
	}

	private YesNoMenu createDeleteWorldMenu(int slot, int dataIndex, Inventory inv) {
		PlayerWorldData data = dataList.get(dataIndex);
		YesNoMenu menu = new YesNoMenu(BOLD + "Delete " + data.name + "?",
				Arrays.asList(GRAY + "Click to " + GREEN + BOLD + "delete" + GRAY + " world"));
		menu.setBackLink(inv);
		menu.setYesCallback((serverPlayer, event) -> {
			if (!WorldLoader.deletePlayerWorld(serverPlayer, data.uuid))
				displayInSlot(inv, CANNOT_DELETE_ITEM, slot, 100);
			open(serverPlayer, dataIndex / MAX_ITEMS_WITH_BORDER);
		});
		return menu;
	}

	@Override
	public void reset() {
		super.reset();
		dataList.clear();
	}

	public static class PlayerWorldData {
		public UUID uuid;
		public String name;
		public long locationId;
		public Environment environment;
		public AiostWorldType<?> worldType;
		public boolean generateStructures;
		public Date saveDate;
		public Time saveTime;
	}
}
