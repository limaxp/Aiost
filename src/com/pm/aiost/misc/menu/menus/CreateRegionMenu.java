package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.LocationMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.other.WordFilter;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.region.Region;

public class CreateRegionMenu extends SingleInventoryMenu {

	private static final ItemStack ACCEPT_ITEM = MetaHelper.setMeta(Material.NETHER_STAR, GREEN + BOLD + "Accept",
			Arrays.asList(GRAY + "Click to create region"));

	private static final ItemStack NO_NAME_ITEM = MetaHelper.setMeta(Material.BARRIER,
			RED + BOLD + "Name cannot be empty!", Arrays.asList(GRAY + "You have to choose a name first"));

	private static final ItemStack NO_EVENT_HANDLER_ITEM = MetaHelper.setMeta(Material.BARRIER,
			RED + BOLD + "Type cannot be empty!", Arrays.asList(GRAY + "You have to choose a type first"));

	private static final ItemStack FORBIDDEN_NAME_ITEM = MetaHelper.setMeta(Material.BARRIER,
			RED + BOLD + "Forbidden name!", Arrays.asList(GRAY + "The given name is not allowed"));

	private Location loc1;
	private Location loc2;
	private String name;
	private Supplier<EventHandler> eventHandler;

	public CreateRegionMenu() {
		super(BOLD + "Create Region", 3, true);
		set(MetaHelper.setMeta(Material.NAME_TAG, GRAY + BOLD + "Name",
				Arrays.asList(GRAY + "Click to change region name")),

				MetaHelper.setMeta(Material.RED_BANNER, GRAY + BOLD + "type",
						Arrays.asList(GRAY + "Click to change region type")),

				MetaHelper.setMeta(Material.STONE, GRAY + BOLD + "Position 1",
						Arrays.asList(GRAY + "Click to change postion 1", null, null, null, null)),

				MetaHelper.setMeta(Material.GOLD_BLOCK, GRAY + BOLD + "Position 2",
						Arrays.asList(GRAY + "Click to change postion 2", null, null, null, null)),

				null, null, ACCEPT_ITEM);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getCurrentItem() != null) {
			switch (event.getCurrentItem().getType()) {

			case NAME_TAG:
				renameRegionMenu().open(serverPlayer);
				break;

			case RED_BANNER:
				if (serverPlayer.isAdmin())
					serverPlayer.doMenuRequest(new SingleMenuRequest(EnumerationMenus.EVENT_HANDLER_MENU) {

						@Override
						protected void openRequest(ServerPlayer serverPlayer) {
							CreateRegionMenu.this.open(serverPlayer);
						}

						@SuppressWarnings("unchecked")
						@Override
						protected void onResult(ServerPlayer serverPlayer, Object obj) {
							setEventHandler((Supplier<EventHandler>) obj);
						}
					});
				else
					// TODO make it so these EventHandler actually work in a game!
					serverPlayer.doMenuRequest(new SingleMenuRequest(EnumerationMenus.REGION_EVENT_HANDLER_MENU) {

						@Override
						protected void openRequest(ServerPlayer serverPlayer) {
							CreateRegionMenu.this.open(serverPlayer);
						}

						@SuppressWarnings("unchecked")
						@Override
						protected void onResult(ServerPlayer serverPlayer, Object obj) {
							setEventHandler((Supplier<EventHandler>) obj);
						}
					});
				break;

			case STONE:
				serverPlayer.doMenuRequest(NO_NAME_ITEM, () -> new SingleMenuRequest(new LocationMenu(loc1)) {

					@Override
					public void openRequest(ServerPlayer serverPlayer) {
						CreateRegionMenu.this.open(serverPlayer);
					}

					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						setLocation1((Location) loc1);
					}
				});
				break;

			case GOLD_BLOCK:
				serverPlayer.doMenuRequest(NO_EVENT_HANDLER_ITEM, () -> new SingleMenuRequest(new LocationMenu(loc2)) {

					@Override
					public void openRequest(ServerPlayer serverPlayer) {
						CreateRegionMenu.this.open(serverPlayer);
					}

					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						setLocation2((Location) loc1);
					}
				});
				break;

			case NETHER_STAR:
				accept(serverPlayer, event);
				break;

			default:
				break;
			}
		}
	}

	private void accept(ServerPlayer serverPlayer, InventoryClickEvent event) {
		if (name == null)
			displayInSlot(event, NO_NAME_ITEM, ACCEPT_ITEM, 30);
		else if (eventHandler == null)
			displayInSlot(event, NO_EVENT_HANDLER_ITEM, ACCEPT_ITEM, 30);
		else if (WordFilter.containsBlocked(name))
			displayInSlot(event, FORBIDDEN_NAME_ITEM, ACCEPT_ITEM, 30);
		else {
			serverPlayer.closeInventory();
			createRegion(serverPlayer.getServerWorld());
			serverPlayer.sendMessage("Region '" + name + "' creation successful!");
		}
	}

	private Region createRegion(ServerWorld serverWorld) {
		Region region = new Region(name, loc1, loc2);
		serverWorld.addRegion(region);
		region.setEventHandler(eventHandler.get());
		return region;
	}

	private AnvilMenu renameRegionMenu() {
		AnvilMenu menu = new AnvilMenu(BOLD + "Choose region name",
				MetaHelper.setMeta(Material.PAPER, name == null ? "name" : name)) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					setName(event.getCurrentItem().getItemMeta().getDisplayName());
					CreateRegionMenu.this.open(serverPlayer);
				}
			}
		};
		menu.setBackLink(this);
		return menu;
	}

	public void setName(String name) {
		this.name = name;
		ItemStack is = getInventory().getItem(10);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(GRAY + BOLD + name);
		is.setItemMeta(im);
	}

	public String getName() {
		return name;
	}

	public void setEventHandler(Supplier<EventHandler> eventHandler) {
		this.eventHandler = eventHandler;
		ItemStack is = getInventory().getItem(11);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(GRAY + BOLD + AiostRegistry.EVENT_HANDLER.getKey(eventHandler));
		is.setItemMeta(im);
	}

	public Supplier<EventHandler> getEventHandler() {
		return eventHandler;
	}

	public void setLocation1(Location loc) {
		this.loc1 = loc;
		editLocationItem(getInventory().getItem(12), loc);
	}

	public Location getLocation1() {
		return loc1;
	}

	public void setLocation2(Location loc) {
		this.loc2 = loc;
		editLocationItem(getInventory().getItem(13), loc);
	}

	public Location getLocation2() {
		return loc2;
	}

	private void editLocationItem(ItemStack is, Location loc) {
		ItemMeta im = is.getItemMeta();
		List<String> lore = im.getLore();
		lore.set(2, GRAY + "x: " + DARK_GRAY + loc.getBlockX());
		lore.set(3, GRAY + "y: " + DARK_GRAY + loc.getBlockY());
		lore.set(4, GRAY + "z: " + DARK_GRAY + loc.getBlockZ());
		im.setLore(lore);
		is.setItemMeta(im);
	}
}
