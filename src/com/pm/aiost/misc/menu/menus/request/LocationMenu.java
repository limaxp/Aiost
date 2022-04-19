package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.Arrays;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.other.Banner;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class LocationMenu extends SingleInventoryMenu {

	private static final ItemStack RESET_ITEM = MetaHelper.setMeta(Material.LAVA_BUCKET, RED + BOLD + "Reset location",
			Arrays.asList(GRAY + "Click to reset location"));

	private static final ItemStack ACCEPT_ITEM = MetaHelper.setMeta(Material.NETHER_STAR,
			RED + BOLD + "Accept location", Arrays.asList(GRAY + "Click to choose this location"));

	private static final Location ZERO_LOCATION = new Location(null, 0, 0, 0);

	private Location sourceLocation;
	private double x;
	private double y;
	private double z;

	public LocationMenu() {
		this(ZERO_LOCATION);
	}

	public LocationMenu(Location loc) {
		super(BOLD + "Location Menu", 3, true);
		sourceLocation = loc;
		x = loc.getX();
		y = loc.getY();
		z = loc.getZ();
		set(Banner.create(Material.BLACK_BANNER, GRAY + BOLD + Double.toString(x),
				Arrays.asList(GRAY + "Click to set x value"), Banner.xPattern(DyeColor.BLACK, DyeColor.WHITE)),

				Banner.create(Material.BLACK_BANNER, GRAY + BOLD + Double.toString(y),
						Arrays.asList(GRAY + "Click to set y value"), Banner.yPattern(DyeColor.BLACK, DyeColor.WHITE)),

				Banner.create(Material.BLACK_BANNER, GRAY + BOLD + Double.toString(z),
						Arrays.asList(GRAY + "Click to set z value"), Banner.zPattern(DyeColor.WHITE)),

				null, null, RESET_ITEM, ACCEPT_ITEM);
		setBackLink(ServerPlayer::openMenuRequestPrevMenu);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		int slot = event.getSlot();
		if (slot == 10)
			createChangeXMenu().open(serverPlayer);
		else if (slot == 11)
			createChangeYMenu().open(serverPlayer);
		else if (slot == 12)
			createChangeZMenu().open(serverPlayer);
		else if (slot == 15)
			reset(event);
		else if (slot == 16)
			serverPlayer.setMenuRequestResult(new Location(serverPlayer.player.getWorld(), x, y, z));
		else
			event.getWhoClicked().sendMessage("Location[world=" + serverPlayer.player.getWorld().getName() + ",x=" + x
					+ ",y=" + y + ",z=" + z + "]");
	}

	private AnvilMenu createChangeXMenu() {
		AnvilMenu menu = new AnvilMenu(BOLD + "X", MetaHelper.setMeta(Material.PAPER, Double.toString(x))) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					setX(parseCoordinate(serverPlayer, event.getCurrentItem().getItemMeta().getDisplayName(),
							serverPlayer.player.getLocation().getX()));
					LocationMenu.this.open(serverPlayer);
				}
			}
		};
		menu.setBackLink(LocationMenu.this);
		return menu;
	}

	private AnvilMenu createChangeYMenu() {
		AnvilMenu menu = new AnvilMenu(BOLD + "Y", MetaHelper.setMeta(Material.PAPER, Double.toString(y))) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					setY(parseCoordinate(serverPlayer, event.getCurrentItem().getItemMeta().getDisplayName(),
							serverPlayer.player.getLocation().getY()));
					LocationMenu.this.open(serverPlayer);
				}
			}
		};
		menu.setBackLink(LocationMenu.this);
		return menu;
	}

	private AnvilMenu createChangeZMenu() {
		AnvilMenu menu = new AnvilMenu(BOLD + "Z", MetaHelper.setMeta(Material.PAPER, Double.toString(z))) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					setZ(parseCoordinate(serverPlayer, event.getCurrentItem().getItemMeta().getDisplayName(),
							serverPlayer.player.getLocation().getZ()));
					LocationMenu.this.open(serverPlayer);
				}
			}
		};
		menu.setBackLink(LocationMenu.this);
		return menu;
	}

	private static double parseCoordinate(ServerPlayer serverPlayer, String string, double currentValue) {
		if (string.charAt(0) == '~')
			if (string.length() > 1)
				return currentValue + parseCoord(serverPlayer, string.substring(1));
			else
				return currentValue;
		else
			return parseCoord(serverPlayer, string);
	}

	private static double parseCoord(ServerPlayer serverPlayer, String string) {
		try {
			return Double.parseDouble(string);
		} catch (NumberFormatException e) {
			serverPlayer.player.sendMessage(ChatColor.RED + "Your input must be a number!");
			return 0;
		}
	}

	private void setX(double value) {
		x = value;
		MetaHelper.setMeta(getInventory().getItem(10), GRAY + BOLD + Double.toString(value));
	}

	private void setY(double value) {
		y = value;
		MetaHelper.setMeta(getInventory().getItem(11), GRAY + BOLD + Double.toString(value));
	}

	private void setZ(double value) {
		z = value;
		MetaHelper.setMeta(getInventory().getItem(12), GRAY + BOLD + Double.toString(value));
	}

	private void reset(InventoryClickEvent event) {
		setX(sourceLocation.getX());
		setY(sourceLocation.getY());
		setZ(sourceLocation.getZ());
	}
}
