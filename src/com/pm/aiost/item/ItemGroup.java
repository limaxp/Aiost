package com.pm.aiost.item;

import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.collection.list.FastArrayList;

public class ItemGroup {

	public final int id;
	public final String name;
	public final ItemStack icon;
	private FastArrayList<ItemStack> items;

	ItemGroup(int id, String name, Material icon) {
		this(id, name, new ItemStack(icon));
	}

	ItemGroup(int id, String name, ItemStack icon) {
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.items = new FastArrayList<ItemStack>();
	}

	int add(ItemStack item) {
		return items.insert(item);
	}

	public ItemStack get(int index) {
		return items.get(index);
	}

	public List<ItemStack> values() {
		return Collections.unmodifiableList(items);
	}

	public int size() {
		return items.size();
	}
}
