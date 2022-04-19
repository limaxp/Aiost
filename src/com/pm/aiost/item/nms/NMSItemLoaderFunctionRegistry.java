package com.pm.aiost.item.nms;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.configuration.ConfigurationSection;

import net.minecraft.server.v1_15_R1.Item;

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
		register("MaterialBow", NMSItemLoader::loadMaterialBow);
		register("Arrow", NMSItemLoader::loadArrow);
		register("FlintAndSteel", NMSItemLoader::loadFlintAndSteel);
		register("Soup", NMSItemLoader::loadSoup);
		register("Armor", NMSItemLoader::loadArmor);
		register("ArmorColorable", NMSItemLoader::loadColoredArmor);
		register("Snowball", NMSItemLoader::loadSnowball);
		register("Egg", NMSItemLoader::loadEgg);
		register("EnderPearl", NMSItemLoader::loadEnderPearl);
		register("Spell", NMSItemLoader::loadSpell);
		register("LeftClickSpell", NMSItemLoader::loadLeftClickSpell);
		register("Scroll", NMSItemLoader::loadScroll);
		register("LeftClickScroll", NMSItemLoader::loadLeftClickScroll);
		register("EffectPotion", NMSItemLoader::loadEffectPotion);
		register("Gun", NMSItemLoader::loadGun);
	}

	public static void register(String name, Function<ConfigurationSection, ? extends Item> itemType) {
		NAME_MAP.put(name, itemType);
	}

	public static Function<ConfigurationSection, ? extends Item> get(String name) {
		return NAME_MAP.get(name);
	}
}
