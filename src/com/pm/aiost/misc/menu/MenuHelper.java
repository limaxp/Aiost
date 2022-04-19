package com.pm.aiost.misc.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.utils.meta.MetaHelper;

public class MenuHelper {

	public static ItemStack[] buildItemStacks(Set<String> names, ItemStack... itemStacks) {
		int itemStackSize = itemStacks.length;
		ItemStack[] arr = new ItemStack[names.size() * itemStackSize];
		int i = 0;
		for (String name : names) {
			for (int j = 0; j < itemStackSize; j++)
				arr[i + j] = MetaHelper.setMeta(itemStacks[j].clone(), ChatColor.RESET + name);
			i += itemStackSize;
		}
		return arr;
	}

	public static ItemStack[] buildItemStacks(Set<String> names, List<List<String>> descriptions,
			ItemStack... itemStacks) {
		int size = names.size();
		int itemStackSize = itemStacks.length;
		ItemStack[] arr = new ItemStack[size * itemStackSize];
		int i = 0;
		int counter = 0;
		for (String name : names) {
			for (int j = 0; j < itemStackSize; j++)
				arr[i + j] = MetaHelper.addLore(itemStacks[j].clone(), ChatColor.RESET + name,
						descriptions.get(counter));
			i += itemStackSize;
			counter++;
		}
		return arr;
	}

	public static ItemStack[] buildItemStacks(int size, int varietySize, BuildFunction buildFunction) {
		int length = size * varietySize;
		ItemStack[] arr = new ItemStack[length];
		for (int i = 0; i < size; i++)
			buildFunction.build(arr, i, i * varietySize);
		return arr;
	}

	public static List<String> createLore(List<String> firstLore, List<String> secondLore) {
		List<String> newLore = new ArrayList<>(firstLore);
		newLore.addAll(secondLore);
		return newLore;
	}

	@SafeVarargs
	public static List<String> createLore(List<String>... lores) {
		List<String> newLore = new ArrayList<>();
		for (List<String> lore : lores)
			newLore.addAll(lore);
		return newLore;
	}

	public static List<String> createLore(String firstLine, List<String> lore) {
		List<String> newLore = new ArrayList<>();
		newLore.add(firstLine);
		newLore.add("");
		newLore.addAll(lore);
		return newLore;
	}

	public static List<String> createLore(String firstLine, List<String> lore, String lastLine) {
		List<String> newLore = new ArrayList<>();
		newLore.add(firstLine);
		newLore.add("");
		newLore.addAll(lore);
		newLore.add("");
		newLore.add(lastLine);
		return newLore;
	}

	public static List<String> createLore(String firstLine, String prefix, List<String> lore) {
		List<String> newLore = new ArrayList<>();
		newLore.add(firstLine);
		newLore.add("");
		for (String loreString : lore)
			newLore.add(prefix + loreString);
		return newLore;
	}

	public static List<String> createLore(String firstLine, String prefix, List<String> lore, String lastLine) {
		List<String> newLore = new ArrayList<>();
		newLore.add(firstLine);
		newLore.add("");
		for (String loreString : lore)
			newLore.add(prefix + loreString);
		newLore.add("");
		newLore.add(lastLine);
		return newLore;
	}

	public static List<String> createLore(String firstLine, List<String> lore, String... lastLines) {
		List<String> newLore = new ArrayList<>();
		newLore.add(firstLine);
		newLore.add("");
		newLore.addAll(lore);
		newLore.add("");
		for (String lastLine : lastLines)
			newLore.add(lastLine);
		return newLore;
	}

	public static interface BuildFunction {

		public void build(ItemStack[] target, int index, int arrayIndex);
	}
}
