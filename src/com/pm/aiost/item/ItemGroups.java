package com.pm.aiost.item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class ItemGroups {

	public static final ItemGroup NONE = new ItemGroup(-1, "None", Material.BARRIER);

	private static final Map<String, ItemGroup> NAME_MAP = new HashMap<String, ItemGroup>();
	private static int runningId = 0;

	static void loadGroups(ConfigurationSection groupsSection) {
		for (String groupName : groupsSection.getKeys(false)) {
			ConfigurationSection groupSection = groupsSection.getConfigurationSection(groupName);
			String displayName;
			if (groupSection.contains("name"))
				displayName = groupSection.getString("name");
			else
				displayName = groupName;

			ItemStack is;
			if (groupSection.contains("item"))
				is = ItemLoader.loadItemOrDefault(groupSection.get("item"), Material.BARRIER);
			else
				is = new ItemStack(Material.BARRIER);
			add(groupName.toLowerCase(), displayName, is);
		}
	}

	private static void add(String name, String displayName, ItemStack is) {
		NAME_MAP.put(name, new ItemGroup(runningId++, displayName, is));
	}

	public static ItemGroup get(String name) {
		return NAME_MAP.get(name);
	}

	public static Collection<ItemGroup> values() {
		return NAME_MAP.values();
	}

	public static int size() {
		return NAME_MAP.size();
	}
}
