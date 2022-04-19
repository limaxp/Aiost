package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.other.Banner;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class CreateTextMenu extends SingleInventoryMenu {

	private static final ItemStack CHOOSE_TEXT_ITEM_1 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Line 1",
			Arrays.asList(GRAY + "Click to set line 1"), Banner.onePattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack CHOOSE_TEXT_ITEM_2 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Line 2",
			Arrays.asList(GRAY + "Click to set line 2"), Banner.twoPattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack CHOOSE_TEXT_ITEM_3 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Line 3",
			Arrays.asList(GRAY + "Click to set line 3"), Banner.threePattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack CHOOSE_TEXT_ITEM_4 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Line 4",
			Arrays.asList(GRAY + "Click to set line 4"), Banner.fourPattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack CHOOSE_TEXT_ITEM_5 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Line 5",
			Arrays.asList(GRAY + "Click to set line 5"), Banner.fivePattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack CHOOSE_TEXT_ITEM_6 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Line 6",
			Arrays.asList(GRAY + "Click to set line 6"), Banner.sixPattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack CHOOSE_TEXT_ITEM_7 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Line 7",
			Arrays.asList(GRAY + "Click to set line 7"), Banner.sevenPattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack CHOOSE_TEXT_ITEM_8 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Line 8",
			Arrays.asList(GRAY + "Click to set line 8"), Banner.eightPattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack CHOOSE_TEXT_ITEM_9 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Line 9",
			Arrays.asList(GRAY + "Click to set line 9"), Banner.ninePattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack[] TEXT_ITEMS = new ItemStack[] { CHOOSE_TEXT_ITEM_1, CHOOSE_TEXT_ITEM_2,
			CHOOSE_TEXT_ITEM_3, CHOOSE_TEXT_ITEM_4, CHOOSE_TEXT_ITEM_5, CHOOSE_TEXT_ITEM_6, CHOOSE_TEXT_ITEM_7,
			CHOOSE_TEXT_ITEM_8, CHOOSE_TEXT_ITEM_9 };

	private static final ItemStack ADD_LINE_ITEM = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Add line",
			Arrays.asList(GRAY + "Click to add a line"), Banner.plusPattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack RESET_ITEM = MetaHelper.setMeta(Material.LAVA_BUCKET, RED + BOLD + "Reset settings",
			Arrays.asList(GRAY + "Click to reset all settings"));

	private static final ItemStack ACCEPT_ITEM = MetaHelper.setMeta(Material.NETHER_STAR,
			GOLD + BOLD + "Accept settings", Arrays.asList(GRAY + "Click to accept your settings"));

	private static final ItemStack NO_TEXT_ITEM = MetaHelper.setMeta(Material.BARRIER,
			RED + BOLD + "Text cannot be empty!", Arrays.asList(GRAY + "You have set at least one line"));

	protected final List<String> texts;

	public CreateTextMenu() {
		super(BOLD + "Create Text", 4, true);
		texts = new ArrayList<String>();
		Inventory inv = getInventory();
		inv.setItem(FIRST_BORDERED_SLOT, ADD_LINE_ITEM);
		inv.setItem(24, RESET_ITEM);
		inv.setItem(25, ACCEPT_ITEM);
		setBackLink(ServerPlayer::openMenuRequestPrevMenu);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);

		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {

			case BLACK_BANNER:
				createSetLineMenu(event.getSlot()).open(serverPlayer);
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

	private AnvilMenu createSetLineMenu(int slot) {
		int index = InventoryMenu.convertSlotToIndex(slot);
		AnvilMenu menu = new AnvilMenu(BOLD + "Set line " + (index + 1),
				MetaHelper.setMeta(Material.PAPER, index < texts.size() ? texts.get(index) : "text")) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					setText(slot, index, event.getCurrentItem().getItemMeta().getDisplayName());
					CreateTextMenu.this.open(serverPlayer);
				}
			}
		};
		menu.setBackLink(this);
		return menu;
	}

	private void setText(int slot, int index, String text) {
		if (index == texts.size()) {
			Inventory inv = getInventory();
			if (index < 8)
				inv.setItem(slot + (index == 6 ? 3 : 1), ADD_LINE_ITEM);
			inv.setItem(slot, TEXT_ITEMS[index].clone());
		}
		if (index < texts.size())
			texts.set(index, text);
		else
			texts.add(text);
		setItem(slot, text);
	}

	private void setItem(int slot, String text) {
		ItemStack is = getInventory().getItem(slot);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(GRAY + BOLD + text);
		is.setItemMeta(im);
	}

	private void reset() {
		int size = texts.size();
		Inventory inv = getInventory();
		for (int i = 1; i < size + 1; i++)
			inv.setItem(FIRST_BORDERED_SLOT + i + (i > 6 ? 2 : 0), null);
		inv.setItem(FIRST_BORDERED_SLOT, ADD_LINE_ITEM);
		texts.clear();
	}

	private void accept(ServerPlayer serverPlayer, InventoryClickEvent event) {
		if (texts.size() > 0)
			serverPlayer.setMenuRequestResult(texts);
		else
			displayInSlot(event, NO_TEXT_ITEM, ACCEPT_ITEM, 30);
	}
}