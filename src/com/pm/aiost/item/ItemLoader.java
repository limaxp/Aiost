package com.pm.aiost.item;

import java.io.File;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.effect.EffectBuilder;
import com.pm.aiost.item.custom.AiostItemLoader;
import com.pm.aiost.item.custom.AiostItems;
import com.pm.aiost.misc.SpigotConfigManager;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.nms.NBT;
import com.pm.aiost.misc.nms.NMS;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;

public class ItemLoader {

	static void loadItemsConfig(ConfigurationSection section) {
		Set<String> itemNames = section.getKeys(false);
		itemNames.remove("Groups");
		for (String itemName : itemNames)
			loadConfig(itemName, section.getConfigurationSection(itemName));
	}

	public static void loadConfig(File file) {
		if (!file.exists())
			return;
		if (file.isFile()) {
			loadConfig(file.getName(), SpigotConfigManager.loadConfig(file));
			return;
		}
		loadConfigs(file);
	}

	private static void loadConfigs(File dir) {
		for (File listFile : dir.listFiles()) {
			if (listFile.isFile())
				loadConfig(listFile.getName(), SpigotConfigManager.loadConfig(listFile));
			else if (listFile.isDirectory())
				loadConfigs(listFile);
		}
	}

	private static void loadConfig(String name, ConfigurationSection section) {
		Items.add(name.toLowerCase().replace(' ', '_'), loadGroup(section), loadItem(section, true));
	}

	private static ItemGroup loadGroup(ConfigurationSection itemSection) {
		if (itemSection.contains("group")) {
			ItemGroup group = ItemGroups.get(itemSection.getString("group").toLowerCase());
			if (group != null)
				return group;
			else
				Logger.warn("ItemLoader: No group found for name '" + itemSection.getString("group") + "'");
		} else
			Logger.warn("ItemLoader: No group defined for item '" + itemSection.getName() + "'");
		return ItemGroups.NONE;
	}

	public static ItemStack[] loadItems(ConfigurationSection itemsSection) {
		return loadItemsOrDefault(itemsSection, Material.AIR);
	}

	public static net.minecraft.world.item.ItemStack[] loadNMSItems(ConfigurationSection itemsSection) {
		return loadNMSItemsOrDefault(itemsSection, net.minecraft.world.item.Items.AIR);
	}

	public static ItemStack[] loadItemsOrDefault(ConfigurationSection itemsSection, Material defaultMaterial) {
		Set<String> itemNames = itemsSection.getKeys(false);
		ItemStack[] items = new ItemStack[itemNames.size()];
		int i = 0;
		for (String itemName : itemNames)
			items[i++] = loadItemOrDefault(itemsSection.get(itemName), defaultMaterial);
		return items;
	}

	public static net.minecraft.world.item.ItemStack[] loadNMSItemsOrDefault(ConfigurationSection itemsSection,
			Material defaultMaterial) {
		Set<String> itemNames = itemsSection.getKeys(false);
		net.minecraft.world.item.ItemStack[] items = new net.minecraft.world.item.ItemStack[itemNames.size()];
		int i = 0;
		for (String itemName : itemNames)
			items[i++] = loadNMSItemOrDefault(itemsSection.get(itemName), defaultMaterial);
		return items;
	}

	public static net.minecraft.world.item.ItemStack[] loadNMSItemsOrDefault(ConfigurationSection itemsSection,
			Item defaultMaterial) {
		Set<String> itemNames = itemsSection.getKeys(false);
		net.minecraft.world.item.ItemStack[] items = new net.minecraft.world.item.ItemStack[itemNames.size()];
		int i = 0;
		for (String itemName : itemNames)
			items[i++] = loadNMSItemOrDefault(itemsSection.get(itemName), defaultMaterial);
		return items;
	}

	public static ItemStack[] loadItemsOrDefault(ConfigurationSection itemsSection, ItemStack defaultItem) {
		Set<String> itemNames = itemsSection.getKeys(false);
		ItemStack[] items = new ItemStack[itemNames.size()];
		int i = 0;
		for (String itemName : itemNames)
			items[i++] = loadItemOrDefault(itemsSection.get(itemName), defaultItem);
		return items;
	}

	public static net.minecraft.world.item.ItemStack[] loadNMSItemsOrDefault(ConfigurationSection itemsSection,
			ItemStack defaultItem) {
		Set<String> itemNames = itemsSection.getKeys(false);
		net.minecraft.world.item.ItemStack[] items = new net.minecraft.world.item.ItemStack[itemNames.size()];
		int i = 0;
		for (String itemName : itemNames)
			items[i++] = loadNMSItemOrDefault(itemsSection.get(itemName), defaultItem);
		return items;
	}

	public static net.minecraft.world.item.ItemStack[] loadNMSItemsOrDefault(ConfigurationSection itemsSection,
			net.minecraft.world.item.ItemStack defaultItem) {
		Set<String> itemNames = itemsSection.getKeys(false);
		net.minecraft.world.item.ItemStack[] items = new net.minecraft.world.item.ItemStack[itemNames.size()];
		int i = 0;
		for (String itemName : itemNames)
			items[i++] = loadNMSItemOrDefault(itemsSection.get(itemName), defaultItem);
		return items;
	}

	public static ItemStack[] loadItemsOrNull(ConfigurationSection itemsSection) {
		Set<String> itemNames = itemsSection.getKeys(false);
		ItemStack[] items = new ItemStack[itemNames.size()];
		int i = 0;
		for (String itemName : itemNames)
			items[i++] = loadItemOrNull(itemsSection.get(itemName));
		return items;
	}

	public static net.minecraft.world.item.ItemStack[] loadNMSItemsOrNull(ConfigurationSection itemsSection) {
		Set<String> itemNames = itemsSection.getKeys(false);
		net.minecraft.world.item.ItemStack[] items = new net.minecraft.world.item.ItemStack[itemNames.size()];
		int i = 0;
		for (String itemName : itemNames)
			items[i++] = loadNMSItemOrNull(itemsSection.get(itemName));
		return items;
	}

	public static ItemStack loadItem(Object itemObject) {
		return loadItemOrDefault(itemObject, Material.AIR);
	}

	public static net.minecraft.world.item.ItemStack loadNMSItem(Object itemObject) {
		return loadNMSItemOrDefault(itemObject, net.minecraft.world.item.Items.AIR);
	}

	public static ItemStack loadItemOrDefault(Object itemObject, Material defaultMaterial) {
		if (itemObject instanceof String)
			return loadItem((String) itemObject);
		else if (itemObject instanceof ConfigurationSection) {
			ConfigurationSection itemSection = (ConfigurationSection) itemObject;
			return loadItem(itemSection.getConfigurationSection(itemSection.getKeys(false).iterator().next()), false);
		}
		return new ItemStack(defaultMaterial);
	}

	public static net.minecraft.world.item.ItemStack loadNMSItemOrDefault(Object itemObject, Material defaultMaterial) {
		if (itemObject instanceof String)
			return loadNMSItem((String) itemObject);
		else if (itemObject instanceof ConfigurationSection) {
			ConfigurationSection itemSection = (ConfigurationSection) itemObject;
			return loadNMSItem(itemSection.getConfigurationSection(itemSection.getKeys(false).iterator().next()),
					false);
		}
		return NMS.to(new ItemStack(defaultMaterial));
	}

	public static net.minecraft.world.item.ItemStack loadNMSItemOrDefault(Object itemObject, Item defaultMaterial) {
		if (itemObject instanceof String)
			return loadNMSItem((String) itemObject);
		else if (itemObject instanceof ConfigurationSection) {
			ConfigurationSection itemSection = (ConfigurationSection) itemObject;
			return loadNMSItem(itemSection.getConfigurationSection(itemSection.getKeys(false).iterator().next()),
					false);
		}
		return new net.minecraft.world.item.ItemStack(defaultMaterial);
	}

	public static ItemStack loadItemOrDefault(Object itemObject, ItemStack defaultItem) {
		if (itemObject instanceof String)
			return loadItem((String) itemObject);
		else if (itemObject instanceof ConfigurationSection) {
			ConfigurationSection itemSection = (ConfigurationSection) itemObject;
			return loadItem(itemSection.getConfigurationSection(itemSection.getKeys(false).iterator().next()), false);
		}
		return defaultItem.clone();
	}

	public static net.minecraft.world.item.ItemStack loadNMSItemOrDefault(Object itemObject, ItemStack defaultItem) {
		if (itemObject instanceof String)
			return loadNMSItem((String) itemObject);
		else if (itemObject instanceof ConfigurationSection) {
			ConfigurationSection itemSection = (ConfigurationSection) itemObject;
			return loadNMSItem(itemSection.getConfigurationSection(itemSection.getKeys(false).iterator().next()),
					false);
		}
		return NMS.to(defaultItem.clone());
	}

	public static net.minecraft.world.item.ItemStack loadNMSItemOrDefault(Object itemObject,
			net.minecraft.world.item.ItemStack defaultItem) {
		if (itemObject instanceof String)
			return loadNMSItem((String) itemObject);
		else if (itemObject instanceof ConfigurationSection) {
			ConfigurationSection itemSection = (ConfigurationSection) itemObject;
			return loadNMSItem(itemSection.getConfigurationSection(itemSection.getKeys(false).iterator().next()),
					false);
		}
		return defaultItem.copy();
	}

	public static ItemStack loadItemOrNull(Object itemObject) {
		if (itemObject instanceof String)
			return loadItem((String) itemObject);
		else if (itemObject instanceof ConfigurationSection) {
			ConfigurationSection itemSection = (ConfigurationSection) itemObject;
			return loadItem(itemSection.getConfigurationSection(itemSection.getKeys(false).iterator().next()), false);
		}
		return null;
	}

	public static net.minecraft.world.item.ItemStack loadNMSItemOrNull(Object itemObject) {
		if (itemObject instanceof String)
			return loadNMSItem((String) itemObject);
		else if (itemObject instanceof ConfigurationSection) {
			ConfigurationSection itemSection = (ConfigurationSection) itemObject;
			return loadNMSItem(itemSection.getConfigurationSection(itemSection.getKeys(false).iterator().next()),
					false);
		}
		return null;
	}

	public static ItemStack loadItem(String itemName) {
		try {
			return new ItemStack(Material.valueOf(((String) itemName).toUpperCase()));
		} catch (IllegalArgumentException e) {
			return Items.get(itemName).clone();
		}
	}

	public static net.minecraft.world.item.ItemStack loadNMSItem(String itemName) {
		return NMS.to(loadItem(itemName));
	}

	public static ItemStack loadItem(ConfigurationSection itemSection) {
		return loadItem(itemSection, false);
	}

	private static ItemStack loadItem(ConfigurationSection itemSection, boolean canRegister) {
		return NMS.from(loadNMSItem(itemSection, canRegister));
	}

	public static net.minecraft.world.item.ItemStack loadNMSItem(ConfigurationSection itemSection) {
		return loadNMSItem(itemSection, false);
	}

	private static net.minecraft.world.item.ItemStack loadNMSItem(ConfigurationSection itemSection,
			boolean canRegister) {
		Material material = loadMaterial(itemSection);
		net.minecraft.world.item.ItemStack nmsIs;
//		if (itemSection.contains("item") && canRegister)
//			nmsIs = loadNMSItem(itemSection, material);
//		else
		nmsIs = NMS.to(new ItemStack(material));

		CompoundTag tag;
		if (itemSection.contains("nbt"))
			tag = loadNBT(itemSection);
		else
			tag = new CompoundTag();

		tag.putString("id", AiostItems.getKey(nmsIs.getItem()).getPath());
		CompoundTag components = new CompoundTag();
		tag.put("components", components);

		NBT.setDisplayName(components, itemSection.getName());
		if (itemSection.contains("lore"))
			NBT.setLore(components, itemSection.getStringList("lore"));

		if (itemSection.contains("predicate"))
			loadPredicates(itemSection, material, components);

		if (itemSection.contains("effects"))
			loadEffects(itemSection, components);

		return NBT.loadNMSItem(tag, nmsIs);
	}

	public static Material loadMaterial(ConfigurationSection itemSection) {
		String materialString = itemSection.getString("material");
		if (materialString == null || materialString.isEmpty()) {
			Logger.warn("ItemLoader: No material for item '" + itemSection.getName() + "' defined!");
			return Material.AIR;
		}

		Material material = Material.valueOf(materialString.toUpperCase());
		if (material == null) {
			Logger.warn("ItemLoader: No material found for material string '" + materialString + "'");
			return Material.AIR;
		}
		return material;
	}

	public static CompoundTag loadNBT(ConfigurationSection itemSection) {
		String nbtString = itemSection.getString("nbt");
		if (nbtString != null && !nbtString.isEmpty())
			return NBT.fromString(nbtString);
		return new CompoundTag();
	}

	public static void loadPredicates(ConfigurationSection itemSection, Material material, CompoundTag tag) {
		ConfigurationSection predicateSection = itemSection.getConfigurationSection("predicate");
		if (predicateSection.contains("durability")) {
			int durability = predicateSection.getInt("durability");
			if (durability != 0)
				NBT.setDurability(tag, (short) (material.getMaxDurability() - durability + 1));
		}
		if (predicateSection.contains("custom_model_data"))
			NBT.setCustomModelData(tag, predicateSection.getInt("custom_model_data"));
	}

	public static void loadEffects(ConfigurationSection itemSection, CompoundTag tag) {
		NBT.setItemEffect(tag, EffectBuilder.loadEffects(itemSection.getConfigurationSection("effects"))
				.createItemEntry(itemSection.getName()));
	}

	public static net.minecraft.world.item.ItemStack loadNMSItem(ConfigurationSection itemSection, Material mat) {
		return new net.minecraft.world.item.ItemStack(
				AiostItemLoader.registerItem(itemSection.getName(), mat, itemSection.getConfigurationSection("item")));
	}
}
