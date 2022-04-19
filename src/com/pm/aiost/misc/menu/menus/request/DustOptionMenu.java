package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.Arrays;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Particle.DustOptions;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.other.Banner;
import com.pm.aiost.misc.particleEffect.particle.RandomColorDustOptions;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class DustOptionMenu extends SingleInventoryMenu {

	private static final ItemStack SIZE_ITEM = MetaHelper.setMeta(Material.NAME_TAG, GRAY + BOLD + "Set size",
			Arrays.asList(GRAY + "Click to set size"));

	private static final ItemStack R_ITEM = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "R",
			Arrays.asList(GRAY + "Click to set color r"), Banner.rPattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack G_ITEM = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "G",
			Arrays.asList(GRAY + "Click to set color g"), Banner.gPattern(DyeColor.WHITE, DyeColor.WHITE));

	private static final ItemStack B_ITEM = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "B",
			Arrays.asList(GRAY + "Click to set color b"), Banner.bPattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack RANDOM_COLOR_OFF_ITEM = MetaHelper.setMeta(Material.GRAY_DYE,
			GRAY + BOLD + "Turn random color on", Arrays.asList(GRAY + "Click to turn random color on"));

	private static final ItemStack RANDOM_COLOR_ON_ITEM = MetaHelper.setMeta(Material.LIME_DYE,
			GRAY + BOLD + "Turn random color off", Arrays.asList(GRAY + "Click to turn random color off"));

	private static final ItemStack RESET_ITEM = MetaHelper.setMeta(Material.LAVA_BUCKET, RED + BOLD + "Reset settings",
			Arrays.asList(GRAY + "Click to reset all settings"));

	private static final ItemStack ACCEPT_ITEM = MetaHelper.setMeta(Material.NETHER_STAR,
			GOLD + BOLD + "Accept settings", Arrays.asList(GRAY + "Click to accept your settings"));

	private static final int SIZE_ITEM_SLOT = 10;
	private static final int R_ITEM_SLOT = 11;
	private static final int G_ITEM_SLOT = 12;
	private static final int B_ITEM_SLOT = 13;
	private static final int RANDOM_COLOR_ITEM_SLOT = 14;

	protected float size;
	protected int r;
	protected int g;
	protected int b;
	protected boolean randomColor;

	public DustOptionMenu() {
		super(BOLD + "Set dust options", 3, true);
		set(SIZE_ITEM.clone(), R_ITEM, G_ITEM, B_ITEM, RANDOM_COLOR_OFF_ITEM, RESET_ITEM, ACCEPT_ITEM);
		setBackLink(ServerPlayer::openMenuRequestPrevMenu);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);

		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {

			case NAME_TAG:
				serverPlayer.doMenuRequest(SIZE_ITEM, () -> new SingleMenuRequest(NumberMenu::new) {

					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						setSize((Double) obj);
					}

					@Override
					public void openRequest(ServerPlayer serverPlayer) {
						DustOptionMenu.this.open(serverPlayer);
					}
				});
				break;

			case BLACK_BANNER:
				switch (event.getSlot()) {
				case R_ITEM_SLOT:
					serverPlayer.doMenuRequest(R_ITEM, () -> new SingleMenuRequest(NumberMenu::new) {

						@Override
						public void onResult(ServerPlayer serverPlayer, Object obj) {
							setR((Double) obj);
						}

						@Override
						public void openRequest(ServerPlayer serverPlayer) {
							DustOptionMenu.this.open(serverPlayer);
						}
					});
					break;

				case G_ITEM_SLOT:
					serverPlayer.doMenuRequest(G_ITEM, () -> new SingleMenuRequest(NumberMenu::new) {

						@Override
						public void onResult(ServerPlayer serverPlayer, Object obj) {
							setG((Double) obj);
						}

						@Override
						public void openRequest(ServerPlayer serverPlayer) {
							DustOptionMenu.this.open(serverPlayer);
						}
					});
					break;

				case B_ITEM_SLOT:
					serverPlayer.doMenuRequest(B_ITEM, () -> new SingleMenuRequest(NumberMenu::new) {

						@Override
						public void onResult(ServerPlayer serverPlayer, Object obj) {
							setB((Double) obj);
						}

						@Override
						public void openRequest(ServerPlayer serverPlayer) {
							DustOptionMenu.this.open(serverPlayer);
						}
					});
					break;

				default:
					break;
				}
				break;

			case GRAY_DYE:
				randomColor = true;
				event.getInventory().setItem(RANDOM_COLOR_ITEM_SLOT, RANDOM_COLOR_ON_ITEM);
				break;

			case GREEN_DYE:
				randomColor = false;
				event.getInventory().setItem(RANDOM_COLOR_ITEM_SLOT, RANDOM_COLOR_OFF_ITEM);
				break;

			case LAVA_BUCKET:
				reset();
				break;

			case NETHER_STAR:
				accept(serverPlayer, event);
				break;

			default:
				break;
			}
		}
	}

	private void setSize(double size) {
		this.size = (float) size;
		setItem(SIZE_ITEM_SLOT, Double.toString(size));
	}

	private void setR(double r) {
		int value = (int) r;
		this.r = value;
		setItem(R_ITEM_SLOT, Integer.toString(value));
	}

	private void setG(double g) {
		int value = (int) g;
		this.g = value;
		setItem(G_ITEM_SLOT, Integer.toString(value));
	}

	private void setB(double b) {
		int value = (int) b;
		this.b = value;
		setItem(B_ITEM_SLOT, Integer.toString(value));
	}

	private void setItem(int slot, String text) {
		ItemStack is = getInventory().getItem(slot);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(GRAY + BOLD + text);
		is.setItemMeta(im);
	}

	private void reset() {
		size = 0;
		r = 0;
		g = 0;
		b = 0;
		randomColor = false;
		setItem(SIZE_ITEM_SLOT, "Set size");
		setItem(R_ITEM_SLOT, "R");
		setItem(G_ITEM_SLOT, "G");
		setItem(B_ITEM_SLOT, "B");
		getInventory().setItem(RANDOM_COLOR_ITEM_SLOT, RANDOM_COLOR_OFF_ITEM);
	}

	private void accept(ServerPlayer serverPlayer, InventoryClickEvent event) {
		serverPlayer.setMenuRequestResult(
				randomColor ? new RandomColorDustOptions(size) : new DustOptions(Color.fromRGB(r, g, b), size));
	}
}