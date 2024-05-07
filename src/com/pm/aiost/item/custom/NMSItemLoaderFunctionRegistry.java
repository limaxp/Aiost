package com.pm.aiost.item.custom;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.configuration.ConfigurationSection;

import net.minecraft.world.item.Item;

public class NMSItemLoaderFunctionRegistry {

	private static final Map<String, Function<ConfigurationSection, ? extends Item>> NAME_MAP = new HashMap<String, Function<ConfigurationSection, ? extends Item>>();

	static {
		register("Item", NMSItemLoader::loadBaseItem);
		register("Sword", NMSItemLoader::loadSword);
		register("Pickaxe", NMSItemLoader::loadPickaxe);
		register("Axe", NMSItemLoader::loadAxe);
		register("Shovel", NMSItemLoader::loadShovel);
		register("Hoe", NMSItemLoader::loadHoe);
		register("Shield", NMSItemLoader::loadShield);
		register("Bow", NMSItemLoader::loadBow);
		register("Crossbow", NMSItemLoader::loadCrossbow);
		register("Arrow", NMSItemLoader::loadArrow);
		register("FlintAndSteel", NMSItemLoader::loadFlintAndSteel);
		register("Armor", NMSItemLoader::loadArmor);
		register("Snowball", NMSItemLoader::loadSnowball);
		register("Egg", NMSItemLoader::loadEgg);
		register("EnderPearl", NMSItemLoader::loadEnderPearl);
	}

	public static void register(String name, Function<ConfigurationSection, ? extends Item> itemType) {
		NAME_MAP.put(name, itemType);
	}

	public static Function<ConfigurationSection, ? extends Item> get(String name) {
		return NAME_MAP.get(name);
	}
}
