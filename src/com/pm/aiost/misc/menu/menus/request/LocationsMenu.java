package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.other.Banner;
import com.pm.aiost.misc.utils.LocationHelper;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class LocationsMenu extends SingleInventoryMenu {

	protected static final ItemStack ADD_LOCATION_ITEM = Banner.create(Material.BLACK_BANNER,
			GRAY + BOLD + "Add location", Arrays.asList(GRAY + "Click to add a location"),
			Banner.plusPattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack RESET_ITEM = MetaHelper.setMeta(Material.LAVA_BUCKET, RED + BOLD + "Reset locations",
			Arrays.asList(GRAY + "Click to reset locations"));

	private static final ItemStack ACCEPT_ITEM = MetaHelper.setMeta(Material.NETHER_STAR,
			RED + BOLD + "Accept locations", Arrays.asList(GRAY + "Click to accept locations"));

	protected List<Location> locations;

	public LocationsMenu() {
		super(BOLD + "Create locations", 6, false);
		locations = new ArrayList<Location>();
		set(ADD_LOCATION_ITEM);
		setBackLink(ServerPlayer::openMenuRequestPrevMenu);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);

		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {

			case BLACK_BANNER:
				serverPlayer.doMenuRequest(ADD_LOCATION_ITEM, () -> new SingleMenuRequest(new LocationMenu()) {

					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						addLocation((Location) obj, event.getSlot());
					}

					@Override
					public void openRequest(ServerPlayer serverPlayer) {
						LocationsMenu.this.open(serverPlayer);
					}
				});

				break;

			case RED_BANNER:
				serverPlayer.doMenuRequest(locations,
						() -> new SingleMenuRequest(new LocationMenu(locations.get(event.getSlot()))) {

							@Override
							public void onResult(ServerPlayer serverPlayer, Object obj) {
								setLocation((Location) obj, event.getSlot());
							}

							@Override
							public void openRequest(ServerPlayer serverPlayer) {
								LocationsMenu.this.open(serverPlayer);
							}
						});
				break;

			case LAVA_BUCKET:
				resetClick(serverPlayer, event);
				break;

			case NETHER_STAR:
				acceptClick(serverPlayer, event);
				break;

			default:
				break;
			}
		}
	}

	private void addLocation(Location location, int slot) {
		addNextItem(slot);
		setItem(location, slot);
		locations.add(location);
	}

	private void setLocation(Location location, int slot) {
		setItem(location, slot);
		locations.set(slot, location);
	}

	private void addNextItem(int slot) {
		if (slot < 42) {
			Inventory inv = getInventory();
			inv.setItem(slot + 1, ADD_LOCATION_ITEM);
			inv.setItem(slot + 2, RESET_ITEM);
			inv.setItem(slot + 3, ACCEPT_ITEM);
		}
	}

	private void setItem(Location location, int slot) {
		List<String> lore = new ArrayList<String>();
		lore.add(GRAY + "Click to modify location");
		lore.add(null);
		LocationHelper.createDescription(lore, location);
		getInventory().setItem(slot,
				MetaHelper.setMeta(new ItemStack(Material.RED_BANNER), GRAY + BOLD + "Location " + (slot + 1), lore));
	}

	private void resetClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		inv.setItem(0, ADD_LOCATION_ITEM);
		for (int i = 1; i < locations.size() + 3; i++)
			inv.setItem(i, null);
		locations.clear();
	}

	private void acceptClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		serverPlayer.setMenuRequestResult(locations);
	}
}