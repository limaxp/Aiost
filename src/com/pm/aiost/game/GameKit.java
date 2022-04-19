package com.pm.aiost.game;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.YELLOW;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class GameKit {

	public static final GameKit EMPTY = new GameKit("Nothing", Material.BARRIER, 0);

	protected static final GameKit[] EMPTY_GROUP = new GameKit[] { EMPTY };

	public final String name;
	private final ItemStack menuItem;
	private final int price;
	private final String[] description;
	private final ItemStack[] items;

	public GameKit(String name, Material menuMaterial, int price) {
		this(name, new ItemStack(menuMaterial), price, new String[] {}, new ItemStack[] {});
	}

	public GameKit(String name, Material menuMaterial, int price, String[] description) {
		this(name, new ItemStack(menuMaterial), price, description, new ItemStack[] {});
	}

	public GameKit(String name, Material menuMaterial, int price, ItemStack[] items) {
		this(name, new ItemStack(menuMaterial), price, new String[] {}, items);
	}

	public GameKit(String name, Material menuMaterial, int price, String[] description, ItemStack[] items) {
		this(name, new ItemStack(menuMaterial), price, description, items);
	}

	public GameKit(String name, ItemStack menuItem, int price) {
		this(name, menuItem, price, new String[] {}, new ItemStack[] {});
	}

	public GameKit(String name, ItemStack menuItem, int price, String[] description) {
		this(name, menuItem, price, description, new ItemStack[] {});
	}

	public GameKit(String name, ItemStack menuItem, int price, ItemStack[] items) {
		this(name, menuItem, price, new String[] {}, items);
	}

	public GameKit(String name, ItemStack menuItem, int price, String[] description, ItemStack[] items) {
		this.name = name;
		this.menuItem = menuItem;
		this.price = price;
		this.description = description;
		this.items = items;
	}

	public final ItemStack getMenuItem() {
		return menuItem;
	}

	public ItemStack createMenuItem(String clickText) {
		List<String> lore = new ArrayList<String>();
		lore.add(clickText);
		lore.add(" ");
		for (int i = 0; i < description.length; i++)
			lore.add(GRAY + description[i]);
		lore.add(" ");
		lore.add(GRAY + "Price: " + YELLOW + BOLD + price);
		return MetaHelper.set(menuItem, BOLD + name, lore);
	}

	public final int getPrice() {
		return price;
	}

	public String[] getDescription() {
		return description;
	}

	public void apply(ServerPlayer serverPlayer) {
	}

	public void deapply(ServerPlayer serverPlayer) {
	}

	public final void equip(ServerPlayer serverPlayer) {
		serverPlayer.player.getInventory().addItem(items);
		apply(serverPlayer);
	}

	public final void unequip(ServerPlayer serverPlayer) {
		serverPlayer.player.getInventory().removeItem(items);
		deapply(serverPlayer);
	}
}
