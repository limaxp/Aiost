package com.pm.aiost.item.custom;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.configuration.ConfigurationSection;

import net.minecraft.world.item.Item;

public class AiostItemFunctionRegistry {

	private static final Map<String, Function<ConfigurationSection, ? extends Item>> NAME_MAP = new HashMap<String, Function<ConfigurationSection, ? extends Item>>();

	static {
		register("Item", AiostItemLoader::loadBaseItem);
		register("Sword", AiostItemLoader::loadSword);
		register("Pickaxe", AiostItemLoader::loadPickaxe);
		register("Axe", AiostItemLoader::loadAxe);
		register("Shovel", AiostItemLoader::loadShovel);
		register("Hoe", AiostItemLoader::loadHoe);
		register("Shield", AiostItemLoader::loadShield);
		register("Bow", AiostItemLoader::loadBow);
		register("Crossbow", AiostItemLoader::loadCrossbow);
		register("Arrow", AiostItemLoader::loadArrow);
		register("FlintAndSteel", AiostItemLoader::loadFlintAndSteel);
		register("Armor", AiostItemLoader::loadArmor);
		register("Snowball", AiostItemLoader::loadSnowball);
		register("Egg", AiostItemLoader::loadEgg);
		register("EnderPearl", AiostItemLoader::loadEnderPearl);
	}

	public static void register(String name, Function<ConfigurationSection, ? extends Item> itemType) {
		NAME_MAP.put(name, itemType);
	}

	public static Function<ConfigurationSection, ? extends Item> get(String name) {
		return NAME_MAP.get(name);
	}
}
