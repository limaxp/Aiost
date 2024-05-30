package com.pm.aiost.item.custom;

import java.util.List;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_20_R4.util.CraftMagicNumbers;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectBuilder;
import com.pm.aiost.item.AiostTier;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.nms.NMS;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;

public class NMSItemLoader {

	public static Item registerItem(String name, Material mat, ConfigurationSection itemSection) {
		NMS.unfreezeRegistry(BuiltInRegistries.ITEM);
		Item item = NMSItems.registerItem(name.replace(' ', '_').toLowerCase(), CraftMagicNumbers.getItem(mat),
				loadItem(itemSection));
		BuiltInRegistries.ITEM.freeze();
		return item;
	}

	public static Item loadItem(ConfigurationSection section) {
		String clazzName = section.getString("class");
		if (clazzName == null || clazzName.isEmpty()) {
			Logger.warn("NMSItemLoader: no class name found for section '" + section.getName() + "'");
			return loadBaseItem(section);
		}
		return loadItem(clazzName, section);
	}

	public static Item loadItem(String name, ConfigurationSection section) {
		Function<ConfigurationSection, ? extends Item> func = NMSItemLoaderFunctionRegistry.get(name);
		if (func == null) {
			Logger.warn("NMSItemLoader: no registry found for name '" + name + "'");
			return loadBaseItem(section);
		}
		return func.apply(section);
	}

	public static Item loadBaseItem(ConfigurationSection section) {
		return new Item(readInfo(section));
	}

	public static SwordItem loadSword(ConfigurationSection section) {
		return new SwordItem(readToolMaterial(section), readInfo(section));
	}

	public static PickaxeItem loadPickaxe(ConfigurationSection section) {
		return new PickaxeItem(readToolMaterial(section), readInfo(section));
	}

	public static AxeItem loadAxe(ConfigurationSection section) {
		return new AxeItem(readToolMaterial(section), readInfo(section));
	}

	public static ShovelItem loadShovel(ConfigurationSection section) {
		return new ShovelItem(readToolMaterial(section), readInfo(section));
	}

	public static HoeItem loadHoe(ConfigurationSection section) {
		return new HoeItem(readToolMaterial(section), readInfo(section));
	}

	public static ShieldItem loadShield(ConfigurationSection section) {
		return new ShieldItem(readInfo(section));
	}

	public static BowItem loadBow(ConfigurationSection section) {
		return new BowItem(readInfo(section));
	}

	public static CrossbowItem loadCrossbow(ConfigurationSection section) {
		return new CrossbowItem(readInfo(section));
	}

	public static ArrowItem loadArrow(ConfigurationSection section) {
		return new ArrowItem(readInfo(section));
	}

	public static FlintAndSteelItem loadFlintAndSteel(ConfigurationSection section) {
		return new FlintAndSteelItem(readInfo(section));
	}

	public static ArmorItem loadArmor(ConfigurationSection section) {
//		return new ArmorItem(readArmorMaterial(section), readEnumItemSlot(section), readInfo(section));
		return null;
	}

	public static SnowballItem loadSnowball(ConfigurationSection section) {
		return new SnowballItem(readInfo(section));
	}

	public static EggItem loadEgg(ConfigurationSection section) {
		return new EggItem(readInfo(section));
	}

	public static EnderpearlItem loadEnderPearl(ConfigurationSection section) {
		return new EnderpearlItem(readInfo(section));
	}

	public static Properties readInfo(ConfigurationSection section) {
//		Info info = new Info();
//		if (section.contains("creativeModeTab"))
//			info.a(readCreativeModeTab(section));
//		if (section.contains("rarity"))
//			info.a(readEnumItemRarity(section));
//		if (section.contains("foodInfo"))
//			info.a(readFoodInfo(section.getConfigurationSection("foodInfo")));
//		if (section.contains("size"))
//			info.a(section.getInt("size"));
//		if (section.contains("durability"))
//			info.c(section.getInt("durability"));
		return new Properties();
	}

	public static CreativeModeTab readCreativeModeTab(ConfigurationSection section) {
//		List<CreativeModeTab> tabs = CreativeModeTabs.allTabs();
//		int size = tabs.size();
//		String name = section.getString("creativeModeTab");
//		for (int i = 0; i < size; i++) {
//			CreativeModeTab tab = tabs.get(i);
//			if (tab.c().equals(name))
//				return tab;
//		}
//		Logger.warn("NMSItemLoader: Could not find creative mode tab for name '" + name + "'");
//		return CreativeModeTab.f; // misc
		return null;
	}

	public static FoodData readFoodInfo(ConfigurationSection section) {
//		// TODO: boolean fields c,d,e are unknown and not used!
//		FoodInfo.a foodInfo = new FoodInfo.a();
//		if (section.contains("nutrition"))
//			foodInfo.a(section.getInt("nutrition"));
//		if (section.contains("saturation"))
//			foodInfo.a((float) section.getDouble("saturation"));
//		if (section.contains("effects")) {
//			ConfigurationSection effectsSection = section.getConfigurationSection("effects");
//			for (String key : effectsSection.getKeys(false)) {
//				ConfigurationSection effectSection = effectsSection.getConfigurationSection(key);
////				foodInfo.a(readMobEffect(effectSection), (float) effectSection.getDouble("value", 1.0));
//				// TODO: Do not know what value actually does!
//			}
//		}
//		return foodInfo.d();
		return null;
	}

//	public static MobEffect readMobEffect(ConfigurationSection section) {
//		MobEffect mobEffect = AiostMobEffects.get(section.getName());
//		if (mobEffect == null) {
//			Logger.warn("NMSItemLoader: Could not find mob effect for name '" + section.getName() + "'");
//			mobEffect = AiostMobEffects.ABSORPTION;
//		}
//		return new MobEffect(mobEffect, section.getInt("duration"), section.getInt("amplifier"));
//	}

	public static Rarity readEnumItemRarity(ConfigurationSection section) {
		if (section.contains("rarity")) {
			Rarity rarity = Rarity.valueOf(section.getString("rarity").toUpperCase());
			if (rarity != null)
				return rarity;
			Logger.warn("NMSItemLoader: Could not find rarity for name '" + section.getString("rarity") + "'");
		}
		return Rarity.COMMON;
	}

	public static Tier readToolMaterial(ConfigurationSection section) {
		if (section.contains("toolMaterial")) {
			Tier toolmaterial = AiostTier.getIgnoreCase(section.getString("toolMaterial"));
			if (toolmaterial != null)
				return toolmaterial;
			Logger.warn(
					"NMSItemLoader: Could not find tool material for name '" + section.getString("toolMaterial") + "'");
		}
		return Tiers.WOOD;
	}

	public static ArmorMaterial readArmorMaterial(ConfigurationSection section) {
		if (section.contains("armorMaterial")) {
//			ArmorMaterial armorMaterial = ArmorMaterials.valueOf(section.getString("armorMaterial").toUpperCase());
//			if (armorMaterial != null)
//				return armorMaterial;
//			Logger.warn("NMSItemLoader: Could not find armor material for name '" + section.getString("armorMaterial")
//					+ "'");
		}
		return ArmorMaterials.LEATHER.value();
	}

	public static EquipmentSlot readEnumItemSlot(ConfigurationSection section) {
		if (section.contains("slot")) {
			EquipmentSlot slot = EquipmentSlot.valueOf(section.getString("slot").toUpperCase());
			if (slot != null)
				return slot;
			Logger.warn("NMSItemLoader: Could not find slot for name '" + section.getString("slot") + "'");
		}
		return EquipmentSlot.HEAD;
	}

	public static String[] readStringArray(ConfigurationSection section, String entry) {
		List<String> stringList = section.getStringList(entry);
		return stringList.toArray(new String[stringList.size()]);
	}

	public static Effect[] readEffect(ConfigurationSection section) {
		return EffectBuilder.loadEffects(section.getConfigurationSection("effects")).createEffectArray();
	}
}