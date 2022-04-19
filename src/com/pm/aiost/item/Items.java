package com.pm.aiost.item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.item.crafting.RecipeManager;
import com.pm.aiost.misc.SpigotConfig;
import com.pm.aiost.misc.SpigotConfigManager;
import com.pm.aiost.misc.resourcePack.ResourcePackBuilder;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.misc.utils.nms.NMS;

public class Items {

	public static final ItemStack AIR = new ItemStack(Material.AIR);

	private static final Map<String, ItemStack> NAME_MAP = new HashMap<String, ItemStack>();

	public static void init() {
		FileConfiguration itemsConfig = SpigotConfigManager.getItemConfig();
		ItemGroups.loadGroups(itemsConfig.getConfigurationSection("Groups"));
		ItemLoader.loadItemsConfig(itemsConfig);
		ItemLoader.loadConfig(SpigotConfigManager.getItemFolder());
		if (!SpigotConfig.HAS_BUNGEE) // TODO: change when resourcepack handling is complete!
			ResourcePackBuilder.checkResourcePack(itemsConfig);
		ResourcePackBuilder.checkDefaultPack();
		RecipeManager.init();
	}

	public static void add(String name, ItemGroup group, ItemStack is) {
		NAME_MAP.put(name, is);
		group.add(is);
	}

	public static ItemStack get(String name) {
		return NAME_MAP.get(name);
	}

	public static ItemStack getClone(String name) {
		return NAME_MAP.get(name).clone();
	}

	public static org.bukkit.inventory.ItemStack get(ItemGroup group, int index) {
		return group.get(index);
	}

	public static Collection<ItemStack> values() {
		return NAME_MAP.values();
	}

	public static int size() {
		return NAME_MAP.size();
	}

	public static Collection<org.bukkit.inventory.ItemStack> values(ItemGroup group) {
		return group.values();
	}

	public static int getEffectID(ItemStack is) {
		return getEffectID(NMS.getNMS(is));
	}

	public static int getEffectID(net.minecraft.server.v1_15_R1.ItemStack nmsIs) {
		if (nmsIs.hasTag())
			return NBTHelper.getItemEffect(nmsIs.getTag());
		return 0;
	}

	// TODO: Custom NMS items don't work after creative inventory open

	// TODO: Custom NMS items get saved as air in shapeless recipe so every custom
	// item works

	public static void onFurnaceSmelt(FurnaceSmeltEvent event) {
		// TODO: FIX CUSTOM ITEM SMELTING!

//		System.out.println(NMS.getNMS(event.getResult()));
		if (event.getResult().getType() == Material.AIR && event.getResult().getAmount() > 0) {
//			Furnace furnace = (Furnace) event.getBlock().getState();
//			System.out.println(furnace.getInventory().getResult());
//			System.out.println(NMS.getNMS(furnace.getInventory().getResult()));

//			Block block = event.getBlock();
//			TileEntity tileEntity = NMS.getNMS(block.getWorld())
//					.getTileEntity(NMS.createBlockPosition(block.getX(), block.getY(), block.getZ()));
//			TileEntityFurnace furnace = (TileEntityFurnace) tileEntity;
//			System.out.println(furnace.getItem(0)); // aluminium_ore
//			System.out.println(furnace.getItem(2)); // target == air
			event.setCancelled(true);
//			furnace.setItem(1, result);
		}
	}

	public static void onInventoryDrag(InventoryDragEvent event) {
		// TODO: make custom item drag work!
		// Maybe set item data in NBT
		org.bukkit.inventory.ItemStack oldCursor = event.getOldCursor();
		if (oldCursor.getType() == Material.AIR && oldCursor.getAmount() > 0) {
			event.setCancelled(true); // currently just cancelled!
		}
	}
}
