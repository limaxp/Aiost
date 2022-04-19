package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.Arrays;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.other.Banner;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class NumberMenu extends SingleInventoryMenu {

	public static final ItemStack NUMBER_0 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "0",
			Arrays.asList(GRAY + "Click to add number '0'"), Banner.zeroPattern(DyeColor.BLACK, DyeColor.WHITE));

	public static final ItemStack NUMBER_1 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "1",
			Arrays.asList(GRAY + "Click to add number '1'"), Banner.onePattern(DyeColor.BLACK, DyeColor.WHITE));

	public static final ItemStack NUMBER_2 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "2",
			Arrays.asList(GRAY + "Click to add number '2'"), Banner.twoPattern(DyeColor.BLACK, DyeColor.WHITE));

	public static final ItemStack NUMBER_3 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "3",
			Arrays.asList(GRAY + "Click to add number '3'"), Banner.threePattern(DyeColor.BLACK, DyeColor.WHITE));

	public static final ItemStack NUMBER_4 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "4",
			Arrays.asList(GRAY + "Click to add number '4'"), Banner.fourPattern(DyeColor.BLACK, DyeColor.WHITE));

	public static final ItemStack NUMBER_5 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "5",
			Arrays.asList(GRAY + "Click to add number '5'"), Banner.fivePattern(DyeColor.BLACK, DyeColor.WHITE));

	public static final ItemStack NUMBER_6 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "6",
			Arrays.asList(GRAY + "Click to add number '6'"), Banner.sixPattern(DyeColor.BLACK, DyeColor.WHITE));

	public static final ItemStack NUMBER_7 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "7",
			Arrays.asList(GRAY + "Click to add number '7'"), Banner.sevenPattern(DyeColor.BLACK, DyeColor.WHITE));

	public static final ItemStack NUMBER_8 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "8",
			Arrays.asList(GRAY + "Click to add number '8'"), Banner.eightPattern(DyeColor.BLACK, DyeColor.WHITE));

	public static final ItemStack NUMBER_9 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "9",
			Arrays.asList(GRAY + "Click to add number '9'"), Banner.ninePattern(DyeColor.BLACK, DyeColor.WHITE));

	public static final ItemStack DOT = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "dot",
			Arrays.asList(GRAY + "Click to make a semikolon"), Banner.dotPattern(DyeColor.BLACK, DyeColor.WHITE));

	public static final ItemStack MINUS = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "minus",
			Arrays.asList(GRAY + "Click to make number negative/positive"),
			Banner.minusPattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack RESET_ITEM = MetaHelper.setMeta(Material.LAVA_BUCKET, RED + BOLD + "Reset number",
			Arrays.asList(GRAY + "Click to reset number"));

	private static final ItemStack ACCEPT_ITEM = MetaHelper.setMeta(Material.NETHER_STAR, GOLD + BOLD + "Accept number",
			Arrays.asList(GRAY + "Click to choose this number"));

	private static final ItemStack[] ITEMS = new ItemStack[] { NUMBER_1, NUMBER_2, NUMBER_3, NUMBER_4, NUMBER_5,
			NUMBER_6, NUMBER_7, NUMBER_8, NUMBER_9, NUMBER_0, DOT, MINUS, RESET_ITEM, ACCEPT_ITEM };

	private static final byte MAX_SLOTS = 7;
	private static final byte FIRST_ANSWER_SLOT = 37;
	private static final byte MINUS_ANSWER_SLOT = 28;
	private static final byte NULL_DOT_SLOT = -1;

	private double number;
	private byte slot;
	private byte dotSlot;

	public NumberMenu() {
		this(BOLD + "Number Menu");
	}

	public NumberMenu(String name) {
		super(name, 6, true);
		number = slot = 0;
		dotSlot = NULL_DOT_SLOT;
		initMenu();
	}

	protected void initMenu() {
		set(ITEMS);
		getInventory().setItem(FIRST_ANSWER_SLOT, NUMBER_0);
		setBackLink(ServerPlayer::openMenuRequestPrevMenu);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		int slot = event.getSlot();
		if (slot > 9 && slot < 21)
			addNumber(slot < 17 ? slot - 9 : slot - 11, event);
		else if (slot == 21)
			addNumber(0, event);
		else if (slot == 22)
			setFloat(event);
		else if (slot == 23)
			switchNegative(event.getInventory());
		else if (slot == 24)
			reset(event);
		else if (slot == 25)
			serverPlayer.setMenuRequestResult(number);
		else
			event.getWhoClicked().sendMessage("Number: " + number);
	}

	private void addNumber(int i, InventoryClickEvent event) {
		if (slot < MAX_SLOTS) {
			if (number < 0)
				i *= -1;
			if (dotSlot != NULL_DOT_SLOT) {
				number += i * Math.pow(0.1, slot - dotSlot);
			} else
				number = number * 10 + i;
			Inventory inv = event.getInventory();
			inv.setItem(FIRST_ANSWER_SLOT + slot, inv.getItem(event.getSlot()));
			slot++;
		}
	}

	private void switchNegative(Inventory inv) {
		number *= -1;
		if (number < 0)
			inv.setItem(MINUS_ANSWER_SLOT, MINUS);
		else
			inv.setItem(MINUS_ANSWER_SLOT, null);
	}

	private void setFloat(InventoryClickEvent event) {
		if (dotSlot == NULL_DOT_SLOT) {
			Inventory inv = event.getInventory();
			inv.setItem(FIRST_ANSWER_SLOT + slot, inv.getItem(event.getSlot()));
			dotSlot = slot++;
		}
	}

	private void reset(InventoryClickEvent event) {
		if (slot > 0) {
			Inventory inv = event.getInventory();
			if (number < 0)
				inv.setItem(MINUS_ANSWER_SLOT, null);
			for (int i = 1; i < slot; i++)
				inv.setItem(FIRST_ANSWER_SLOT + i, null);

			inv.setItem(FIRST_ANSWER_SLOT, NUMBER_0);
			number = slot = 0;
			dotSlot = NULL_DOT_SLOT;
		}
	}
}
