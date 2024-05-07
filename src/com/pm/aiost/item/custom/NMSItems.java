package com.pm.aiost.item.custom;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class NMSItems {

	private static final Map<Item, Item> BASE_MAP = new IdentityHashMap<Item, Item>();

	private static final List<Item> CHANGED_ITEMS = new ArrayList<Item>();

	protected static Item a(String name, Block base, Item item) {
		return registerItem(name, base.asItem(), item);
	}

	protected static Item a(String name, Item base, Item item) {
		return registerItem(name, base, item);
	}

	public static Item registerItem(String name, Item base, Item item) {
		registerItem(base, item);
//		if (item instanceof BlockItem) {
//			((BlockItem) item).a(Item.f, item);
//		}
		return Registry.register(BuiltInRegistries.ITEM, name, item);
	}

	public static Item registerItem(ResourceLocation name, Item base, Item item) {
		registerItem(base, item);
//		if (item instanceof BlockItem) {
//			((BlockItem) item).a(Item.f, item);
//		}
		return Registry.register(BuiltInRegistries.ITEM, name, item);
	}

	private static void registerItem(Item base, Item item) {
		BASE_MAP.put(item, base);
		if (!CHANGED_ITEMS.contains(base))
			CHANGED_ITEMS.add(base);
	}

	public static void cleanupItems() {
		int size = CHANGED_ITEMS.size();
		for (int i = 0; i < size; i++) {
			Item item = CHANGED_ITEMS.get(i);
			Registry.registerForHolder(BuiltInRegistries.ITEM, getKey(item), item);
		}
		CHANGED_ITEMS.clear();
	}

	public static ResourceLocation getKey(Item item) {
		return BuiltInRegistries.ITEM.getKey(item);
	}

	public static Item fromKey(ResourceLocation key) {
		return BuiltInRegistries.ITEM.get(key);
	}

	public static int getId(Item item) {
		return Item.getId(item);
	}

//	public static int getId(Block block) {
//		return Block.getCombinedId(block.getBlockData());
//	}

	public static int getId(BlockState block) {
		return Block.getId(block);
	}

	public static Item fromId(int id) {
		return BuiltInRegistries.ITEM.byId(id);
	}

	public static Iterator<Item> iterator() {
		return BuiltInRegistries.ITEM.iterator();
	}

	public static Item getBase(Item item) {
		return BASE_MAP.getOrDefault(item, item);
	}
}
