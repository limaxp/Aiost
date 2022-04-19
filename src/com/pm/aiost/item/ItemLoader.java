package com.pm.aiost.item;

import java.io.File;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.event.effect.EffectBuilder;
import com.pm.aiost.item.nms.NMSItemLoader;
import com.pm.aiost.item.nms.NMSItems;
import com.pm.aiost.misc.SpigotConfigManager;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.server.v1_15_R1.Item;
import net.minecraft.server.v1_15_R1.NBTTagCompound;

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
		NMSItems.cleanupItems();
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

	public static net.minecraft.server.v1_15_R1.ItemStack[] loadNMSItems(ConfigurationSection itemsSection) {
		return loadNMSItemsOrDefault(itemsSection, net.minecraft.server.v1_15_R1.Items.AIR);
	}

	public static ItemStack[] loadItemsOrDefault(ConfigurationSection itemsSection, Material defaultMaterial) {
		Set<String> itemNames = itemsSection.getKeys(false);
		ItemStack[] items = new ItemStack[itemNames.size()];
		int i = 0;
		for (String itemName : itemNames)
			items[i++] = loadItemOrDefault(itemsSection.get(itemName), defaultMaterial);
		return items;
	}

	public static net.minecraft.server.v1_15_R1.ItemStack[] loadNMSItemsOrDefault(ConfigurationSection itemsSection,
			Material defaultMaterial) {
		Set<String> itemNames = itemsSection.getKeys(false);
		net.minecraft.server.v1_15_R1.ItemStack[] items = new net.minecraft.server.v1_15_R1.ItemStack[itemNames.size()];
		int i = 0;
		for (String itemName : itemNames)
			items[i++] = loadNMSItemOrDefault(itemsSection.get(itemName), defaultMaterial);
		return items;
	}

	public static net.minecraft.server.v1_15_R1.ItemStack[] loadNMSItemsOrDefault(ConfigurationSection itemsSection,
			Item defaultMaterial) {
		Set<String> itemNames = itemsSection.getKeys(false);
		net.minecraft.server.v1_15_R1.ItemStack[] items = new net.minecraft.server.v1_15_R1.ItemStack[itemNames.size()];
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

	public static net.minecraft.server.v1_15_R1.ItemStack[] loadNMSItemsOrDefault(ConfigurationSection itemsSection,
			ItemStack defaultItem) {
		Set<String> itemNames = itemsSection.getKeys(false);
		net.minecraft.server.v1_15_R1.ItemStack[] items = new net.minecraft.server.v1_15_R1.ItemStack[itemNames.size()];
		int i = 0;
		for (String itemName : itemNames)
			items[i++] = loadNMSItemOrDefault(itemsSection.get(itemName), defaultItem);
		return items;
	}

	public static net.minecraft.server.v1_15_R1.ItemStack[] loadNMSItemsOrDefault(ConfigurationSection itemsSection,
			net.minecraft.server.v1_15_R1.ItemStack defaultItem) {
		Set<String> itemNames = itemsSection.getKeys(false);
		net.minecraft.server.v1_15_R1.ItemStack[] items = new net.minecraft.server.v1_15_R1.ItemStack[itemNames.size()];
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

	public static net.minecraft.server.v1_15_R1.ItemStack[] loadNMSItemsOrNull(ConfigurationSection itemsSection) {
		Set<String> itemNames = itemsSection.getKeys(false);
		net.minecraft.server.v1_15_R1.ItemStack[] items = new net.minecraft.server.v1_15_R1.ItemStack[itemNames.size()];
		int i = 0;
		for (String itemName : itemNames)
			items[i++] = loadNMSItemOrNull(itemsSection.get(itemName));
		return items;
	}

	public static ItemStack loadItem(Object itemObject) {
		return loadItemOrDefault(itemObject, Material.AIR);
	}

	public static net.minecraft.server.v1_15_R1.ItemStack loadNMSItem(Object itemObject) {
		return loadNMSItemOrDefault(itemObject, net.minecraft.server.v1_15_R1.Items.AIR);
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

	public static net.minecraft.server.v1_15_R1.ItemStack loadNMSItemOrDefault(Object itemObject,
			Material defaultMaterial) {
		if (itemObject instanceof String)
			return loadNMSItem((String) itemObject);
		else if (itemObject instanceof ConfigurationSection) {
			ConfigurationSection itemSection = (ConfigurationSection) itemObject;
			return loadNMSItem(itemSection.getConfigurationSection(itemSection.getKeys(false).iterator().next()),
					false);
		}
		return NMS.getNMS(new ItemStack(defaultMaterial));
	}

	public static net.minecraft.server.v1_15_R1.ItemStack loadNMSItemOrDefault(Object itemObject,
			Item defaultMaterial) {
		if (itemObject instanceof String)
			return loadNMSItem((String) itemObject);
		else if (itemObject instanceof ConfigurationSection) {
			ConfigurationSection itemSection = (ConfigurationSection) itemObject;
			return loadNMSItem(itemSection.getConfigurationSection(itemSection.getKeys(false).iterator().next()),
					false);
		}
		return new net.minecraft.server.v1_15_R1.ItemStack(defaultMaterial);
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

	public static net.minecraft.server.v1_15_R1.ItemStack loadNMSItemOrDefault(Object itemObject,
			ItemStack defaultItem) {
		if (itemObject instanceof String)
			return loadNMSItem((String) itemObject);
		else if (itemObject instanceof ConfigurationSection) {
			ConfigurationSection itemSection = (ConfigurationSection) itemObject;
			return loadNMSItem(itemSection.getConfigurationSection(itemSection.getKeys(false).iterator().next()),
					false);
		}
		return NMS.getNMS(defaultItem.clone());
	}

	public static net.minecraft.server.v1_15_R1.ItemStack loadNMSItemOrDefault(Object itemObject,
			net.minecraft.server.v1_15_R1.ItemStack defaultItem) {
		if (itemObject instanceof String)
			return loadNMSItem((String) itemObject);
		else if (itemObject instanceof ConfigurationSection) {
			ConfigurationSection itemSection = (ConfigurationSection) itemObject;
			return loadNMSItem(itemSection.getConfigurationSection(itemSection.getKeys(false).iterator().next()),
					false);
		}
		return defaultItem.cloneItemStack();
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

	public static net.minecraft.server.v1_15_R1.ItemStack loadNMSItemOrNull(Object itemObject) {
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

	public static net.minecraft.server.v1_15_R1.ItemStack loadNMSItem(String itemName) {
		return NMS.getNMS(loadItem(itemName));
	}

	public static ItemStack loadItem(ConfigurationSection itemSection) {
		return loadItem(itemSection, false);
	}

	private static ItemStack loadItem(ConfigurationSection itemSection, boolean canRegister) {
		return NMS.getBukkit(loadNMSItem(itemSection, canRegister));
	}

	public static net.minecraft.server.v1_15_R1.ItemStack loadNMSItem(ConfigurationSection itemSection) {
		return loadNMSItem(itemSection, false);
	}

	private static net.minecraft.server.v1_15_R1.ItemStack loadNMSItem(ConfigurationSection itemSection,
			boolean canRegister) {
		Material material = loadMaterial(itemSection);
		net.minecraft.server.v1_15_R1.ItemStack nmsIs;
		if (itemSection.contains("item") && canRegister)
			nmsIs = loadNMSItem(itemSection, material);
		else
			nmsIs = NMS.getNMS(new ItemStack(material));

		NBTTagCompound nbtTag;
		if (itemSection.contains("nbt"))
			nbtTag = loadNBT(itemSection);
		else
			nbtTag = new NBTTagCompound();

		NBTTagCompound display = NBTHelper.addDisplay(nbtTag);
		NBTHelper.setDisplayName(display, itemSection.getName());
		if (itemSection.contains("lore"))
			NBTHelper.setLore(display, itemSection.getStringList("lore"));

		if (itemSection.contains("predicate"))
			loadPredicates(itemSection, material, nbtTag);

		if (itemSection.contains("effects"))
			loadEffects(itemSection, nbtTag);

		nmsIs.setTag(nbtTag);
		return nmsIs;
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

	public static NBTTagCompound loadNBT(ConfigurationSection itemSection) {
		String nbtString = itemSection.getString("nbt");
		if (nbtString != null && !nbtString.isEmpty())
			return NBTHelper.fromString(nbtString);
		return new NBTTagCompound();
	}

	public static void loadPredicates(ConfigurationSection itemSection, Material material, NBTTagCompound nbtTag) {
		ConfigurationSection predicateSection = itemSection.getConfigurationSection("predicate");
		if (predicateSection.contains("durability")) {
			int durability = predicateSection.getInt("durability");
			if (durability != 0)
				NBTHelper.setDurability(nbtTag, (short) (material.getMaxDurability() - durability + 1));
		}
		if (predicateSection.contains("custom_model_data"))
			NBTHelper.setCustomModelData(nbtTag, predicateSection.getInt("custom_model_data"));
	}

	public static void loadEffects(ConfigurationSection itemSection, NBTTagCompound nbtTag) {
		NBTHelper.setItemEffect(nbtTag, EffectBuilder.loadEffects(itemSection.getConfigurationSection("effects"))
				.createItemEntry(itemSection.getName()));
	}

	public static net.minecraft.server.v1_15_R1.ItemStack loadNMSItem(ConfigurationSection itemSection, Material mat) {
		return new net.minecraft.server.v1_15_R1.ItemStack(
				NMSItemLoader.registerItem(itemSection.getName(), mat, itemSection.getConfigurationSection("item")));
	}
}
