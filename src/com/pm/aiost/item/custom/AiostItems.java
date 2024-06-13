package com.pm.aiost.item.custom;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.NamespacedKey;

import com.pm.aiost.misc.nms.NMS;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class AiostItems extends Items {

	private static final Map<Item, Item> BASE_MAP = new IdentityHashMap<Item, Item>();

	private static final List<Item> CHANGED_ITEMS = new ArrayList<Item>();

	public static Item registerItem(String name, Item base, Item item) {
		return registerItem(new ResourceLocation(name), base, item);
	}

	public static Item registerItem(ResourceLocation name, Item base, Item item) {
		mapItem(base, item);
		if (item instanceof BlockItem)
			((BlockItem) item).registerBlocks(Item.BY_BLOCK, item);
		return Registry.register(BuiltInRegistries.ITEM, getKey(base), item);
	}

	private static void mapItem(Item base, Item item) {
		BASE_MAP.put(item, base);
		if (!CHANGED_ITEMS.contains(base))
			CHANGED_ITEMS.add(base);
	}

	public static void cleanupItems() {
		int size = CHANGED_ITEMS.size();
		for (int i = 0; i < size; i++) {
			Item base = CHANGED_ITEMS.get(i);
			Registry.register(BuiltInRegistries.ITEM, getKey(base), base);
		}
		CHANGED_ITEMS.clear();
	}

	public static ResourceLocation getKey(Item item) {
		return BuiltInRegistries.ITEM.getKey(item);
	}

	public static int getId(Item item) {
		return BuiltInRegistries.ITEM.getId(item);
	}

	public static Item get(ResourceLocation key) {
		return BuiltInRegistries.ITEM.get(key);
	}

	public static Item get(int id) {
		return BuiltInRegistries.ITEM.byId(id);
	}

	public static Item get(String key) {
		return get(new ResourceLocation(key));
	}

	public static Item get(NamespacedKey key) {
		return get(NMS.to(key));
	}

	public static Iterator<Item> iterator() {
		return BuiltInRegistries.ITEM.iterator();
	}

	public static Item getBase(Item item) {
		return BASE_MAP.getOrDefault(item, item);
	}
}
