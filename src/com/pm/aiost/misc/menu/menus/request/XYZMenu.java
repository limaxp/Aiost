package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.Arrays;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.other.Banner;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class XYZMenu extends SingleInventoryMenu {

	private static final ItemStack RESET_ITEM = MetaHelper.setMeta(Material.LAVA_BUCKET, RED + BOLD + "Reset",
			Arrays.asList(GRAY + "Click to reset settings"));

	private static final ItemStack ACCEPT_ITEM = MetaHelper.setMeta(Material.NETHER_STAR, RED + BOLD + "Accept",
			Arrays.asList(GRAY + "Click to accept settings"));

	private double rx;
	private double ry;
	private double rz;
	private double x;
	private double y;
	private double z;

	public XYZMenu(String name, double radius) {
		this(name, radius, radius, radius);
	}

	public XYZMenu(String name, double rx, double ry, double rz) {
		super(name, 3, true);
		this.rx = x = rx;
		this.ry = y = ry;
		this.rz = z = rz;
		set(Banner.create(Material.BLACK_BANNER, GRAY + BOLD + Double.toString(rx),
				Arrays.asList(GRAY + "Click to set x value"), Banner.xPattern(DyeColor.BLACK, DyeColor.WHITE)),

				Banner.create(Material.BLACK_BANNER, GRAY + BOLD + Double.toString(ry),
						Arrays.asList(GRAY + "Click to set y value"), Banner.yPattern(DyeColor.BLACK, DyeColor.WHITE)),

				Banner.create(Material.BLACK_BANNER, GRAY + BOLD + Double.toString(rz),
						Arrays.asList(GRAY + "Click to set z value"), Banner.zPattern(DyeColor.WHITE)),

				null, null, RESET_ITEM, ACCEPT_ITEM);
		setBackLink(ServerPlayer::openMenuRequestPrevMenu);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		int slot = event.getSlot();
		if (slot == 10)
			serverPlayer.doMenuRequest(new SingleMenuRequest(() -> TextMenu.createNumber(BOLD + "Choose x", x)) {

				@Override
				public void openRequest(ServerPlayer serverPlayer) {
					XYZMenu.this.open(serverPlayer);
				}

				@Override
				public void onResult(ServerPlayer serverPlayer, Object obj) {
					setX((Double) obj);
				}
			});
		else if (slot == 11)
			serverPlayer.doMenuRequest(new SingleMenuRequest(() -> TextMenu.createNumber(BOLD + "Choose y", y)) {

				@Override
				public void openRequest(ServerPlayer serverPlayer) {
					XYZMenu.this.open(serverPlayer);
				}

				@Override
				public void onResult(ServerPlayer serverPlayer, Object obj) {
					setY((Double) obj);
				}
			});
		else if (slot == 12)
			serverPlayer.doMenuRequest(new SingleMenuRequest(() -> TextMenu.createNumber(BOLD + "Choose z", z)) {

				@Override
				public void openRequest(ServerPlayer serverPlayer) {
					XYZMenu.this.open(serverPlayer);
				}

				@Override
				public void onResult(ServerPlayer serverPlayer, Object obj) {
					setZ((Double) obj);
				}
			});
		else if (slot == 15)
			reset(event);
		else if (slot == 16)
			serverPlayer.setMenuRequestResult(new double[] { x, y, z });
		else
			event.getWhoClicked().sendMessage("[x=" + x + ",y=" + y + ",z=" + z + "]");
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
		setX(rx);
		setY(ry);
		setZ(rz);
	}
}
