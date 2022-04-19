package com.pm.aiost.item.nms;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_15_R1.Block;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.IRegistry;
import net.minecraft.server.v1_15_R1.Item;
import net.minecraft.server.v1_15_R1.ItemBlock;
import net.minecraft.server.v1_15_R1.MinecraftKey;

public class NMSItems {

	private static final Map<Item, Item> BASE_MAP = new IdentityHashMap<Item, Item>();

	private static final List<Item> CHANGED_ITEMS = new ArrayList<Item>();

	protected static Item a(String name, Block base, Item item) {
		return registerItem(name, base.getItem(), item);
	}

	protected static Item a(String name, Item base, Item item) {
		return registerItem(name, base, item);
	}

	public static Item registerItem(String name, Item base, Item item) {
		registerItem(base, item);
		if (item instanceof ItemBlock) {
			((ItemBlock) item).a(Item.f, item);
		}
		return IRegistry.a(IRegistry.ITEM, Item.getId(base), name, item);
	}

	public static Item registerItem(MinecraftKey name, Item base, Item item) {
		registerItem(base, item);
		if (item instanceof ItemBlock) {
			((ItemBlock) item).a(Item.f, item);
		}
		return IRegistry.ITEM.a(Item.getId(base), name, item);
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
			IRegistry.a(IRegistry.ITEM, Item.getId(item), getKey(item).getKey(), item);
		}
		CHANGED_ITEMS.clear();
	}

	public static MinecraftKey getKey(Item item) {
		return IRegistry.ITEM.getKey(item);
	}

	public static Item fromKey(MinecraftKey key) {
		return IRegistry.ITEM.get(key);
	}

	public static int getId(Item item) {
		return Item.getId(item);
	}

	public static int getId(Block block) {
		return Block.getCombinedId(block.getBlockData());
	}

	public static int getId(IBlockData block) {
		return Block.getCombinedId(block);
	}

	public static Item fromId(int id) {
		return IRegistry.ITEM.fromId(id);
	}

	public static Iterator<Item> iterator() {
		return IRegistry.ITEM.iterator();
	}

	public static Item getBase(Item item) {
		return BASE_MAP.getOrDefault(item, item);
	}
}
