package com.pm.aiost.player.unlockable;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_PURPLE;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.RED;
import static com.pm.aiost.misc.utils.ChatColor.YELLOW;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.MenuHelper;
import com.pm.aiost.misc.menu.menus.UnlockableMenu;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public abstract class UnlockableType<T> {

	private static final String BUY_TEXT = GRAY + "Click to " + GREEN + BOLD + "buy";
	private static final String ACTIVATE_TEXT = GRAY + "Click to " + DARK_PURPLE + BOLD + "activate";
	private static final String UNLOCKABLE_TEXT = GRAY + "Status: " + RED + BOLD + "Unlockable";
	private static final String UNLOCKED_TEXT = GRAY + "Status: " + GREEN + BOLD + "Unlocked";
	private static final String PRICE_TEXT = GRAY + "Price: " + YELLOW + BOLD;
	private static final ItemStack DEFAULT_DISPLAY_ITEM = new ItemStack(Material.BARRIER);

	public final int id;
	public final String name;
	private ItemStack displayItem;
	private ItemStack[] items;
	private T[] objects;
	private int[] prices;

	protected UnlockableType(String name) {
		this.name = name;
		id = UnlockableTypes.add(this);
		displayItem = DEFAULT_DISPLAY_ITEM;
	}

	public void init(Set<String> names, List<String>[] descriptions, int[] prices, T[] objects,
			Material unlockedMaterial) {
		init(names, descriptions, prices, objects, new ItemStack(unlockedMaterial));
	}

	public void init(Set<String> names, List<String>[] descriptions, int[] prices, T[] objects,
			ItemStack unlockedItem) {
		items = buildItemStacks(names, descriptions, prices, DEFAULT_DISPLAY_ITEM, unlockedItem);
		this.objects = objects;
		this.prices = prices;
		this.displayItem = unlockedItem;
	}

	public abstract void load(ConfigurationSection section);

	public abstract void init(ServerPlayer serverPlayer);

	public abstract short get(ServerPlayer serverPlayer);

	public abstract void set(ServerPlayer serverPlayer, short id);

	public abstract void remove(ServerPlayer serverPlayer, short id);

	public boolean initializePlayer() {
		return true;
	}

	public void remove(ServerPlayer serverPlayer) {
		remove(serverPlayer, (short) 0);
	}

	public UnlockableMenu createMenu(ServerPlayer serverPlayer) {
		return new UnlockableMenu(serverPlayer, this);
	}

	public ItemStack getDisplayItem() {
		return displayItem;
	}

	public ItemStack getItem(int index) {
		return items[index];
	}

	public T getObject(int index) {
		return objects[index];
	}

	public int getPrice(int index) {
		return prices[index];
	}

	public int size() {
		return objects.length;
	}

	private static ItemStack[] buildItemStacks(Set<String> names, List<String>[] descriptions, int[] prices,
			ItemStack lockedItem, ItemStack unlockedItem) {
		Iterator<String> iter = names.iterator();
		return MenuHelper.buildItemStacks(names.size(), 2, (target, i, j) -> {
			String name = iter.next();
			List<String> description = descriptions[i + 1];

			target[j] = MetaHelper.setMeta(lockedItem.clone(), RED + BOLD + name,
					MenuHelper.createLore(BUY_TEXT, description, UNLOCKABLE_TEXT, PRICE_TEXT + prices[i + 1]));
			target[j + 1] = MetaHelper.hideAttributes(unlockedItem.clone(), GREEN + BOLD + name,
					MenuHelper.createLore(ACTIVATE_TEXT, description, UNLOCKED_TEXT));
		});
	}
}
