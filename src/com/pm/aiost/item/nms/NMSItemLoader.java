package com.pm.aiost.item.nms;

import java.util.List;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;

import com.pm.aiost.entity.mobEffect.AiostMobEffects;
import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectBuilder;
import com.pm.aiost.item.AiostToolMaterial;
import com.pm.aiost.item.custom.items.ItemGun;
import com.pm.aiost.item.custom.items.ItemLeftClickSpell;
import com.pm.aiost.item.custom.items.ItemLeftClickSpellScroll;
import com.pm.aiost.item.custom.items.ItemSpell;
import com.pm.aiost.item.custom.items.ItemSpellScroll;
import com.pm.aiost.item.nms.items.ItemEffectPotion;
import com.pm.aiost.item.nms.items.ItemMaterialBow;
import com.pm.aiost.item.spell.Spell;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.server.v1_15_R1.CreativeModeTab;
import net.minecraft.server.v1_15_R1.EnumArmorMaterial;
import net.minecraft.server.v1_15_R1.EnumItemRarity;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.FoodInfo;
import net.minecraft.server.v1_15_R1.Item;
import net.minecraft.server.v1_15_R1.Item.Info;
import net.minecraft.server.v1_15_R1.ItemArmor;
import net.minecraft.server.v1_15_R1.ItemArmorColorable;
import net.minecraft.server.v1_15_R1.ItemArrow;
import net.minecraft.server.v1_15_R1.ItemAxe;
import net.minecraft.server.v1_15_R1.ItemBow;
import net.minecraft.server.v1_15_R1.ItemCrossbow;
import net.minecraft.server.v1_15_R1.ItemEgg;
import net.minecraft.server.v1_15_R1.ItemEnderPearl;
import net.minecraft.server.v1_15_R1.ItemFlintAndSteel;
import net.minecraft.server.v1_15_R1.ItemHoe;
import net.minecraft.server.v1_15_R1.ItemPickaxe;
import net.minecraft.server.v1_15_R1.ItemShield;
import net.minecraft.server.v1_15_R1.ItemSnowball;
import net.minecraft.server.v1_15_R1.ItemSoup;
import net.minecraft.server.v1_15_R1.ItemSpade;
import net.minecraft.server.v1_15_R1.ItemSword;
import net.minecraft.server.v1_15_R1.MobEffect;
import net.minecraft.server.v1_15_R1.MobEffectList;

public class NMSItemLoader {

	public static Item registerItem(String name, Material mat, ConfigurationSection itemSection) {
		return NMSItems.registerItem(name.toLowerCase().replace(' ', '_'), CraftMagicNumbers.getItem(mat),
				loadItem(itemSection));
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

	public static ItemSword loadSword(ConfigurationSection section) {
		return new ItemSword(readToolMaterial(section), section.getInt("damage"),
				(float) section.getDouble("attackSpeed"), readInfo(section));
	}

	public static ItemPickaxe loadPickaxe(ConfigurationSection section) {
		return NMS.createItemPickaxe(readToolMaterial(section), section.getInt("damage"),
				(float) section.getDouble("attackSpeed"), readInfo(section));
	}

	public static ItemAxe loadAxe(ConfigurationSection section) {
		return NMS.createItemAxe(readToolMaterial(section), (float) section.getDouble("damage"),
				(float) section.getDouble("attackSpeed"), readInfo(section));
	}

	public static ItemSpade loadShovel(ConfigurationSection section) {
		return new ItemSpade(readToolMaterial(section), (float) section.getDouble("damage"),
				(float) section.getDouble("attackSpeed"), readInfo(section));
	}

	public static ItemHoe loadHoe(ConfigurationSection section) {
		return new ItemHoe(readToolMaterial(section), (float) section.getDouble("attackSpeed"), readInfo(section));
	}

	public static ItemShield loadShield(ConfigurationSection section) {
		return new ItemShield(readInfo(section));
	}

	public static ItemBow loadBow(ConfigurationSection section) {
		return new ItemBow(readInfo(section));
	}

	public static ItemCrossbow loadCrossbow(ConfigurationSection section) {
		return new ItemCrossbow(readInfo(section));
	}

	public static ItemMaterialBow loadMaterialBow(ConfigurationSection section) {
		return new ItemMaterialBow(readToolMaterial(section), (float) section.getDouble("damage"),
				(float) section.getDouble("attackSpeed"), readInfo(section));
	}

	public static ItemArrow loadArrow(ConfigurationSection section) {
		return new ItemArrow(readInfo(section));
	}

	public static ItemFlintAndSteel loadFlintAndSteel(ConfigurationSection section) {
		return new ItemFlintAndSteel(readInfo(section));
	}

	public static ItemSoup loadSoup(ConfigurationSection section) {
		return new ItemSoup(readInfo(section));
	}

	public static ItemArmor loadArmor(ConfigurationSection section) {
		return new ItemArmor(readArmorMaterial(section), readEnumItemSlot(section), readInfo(section));
	}

	public static ItemArmorColorable loadColoredArmor(ConfigurationSection section) {
		return new ItemArmorColorable(readArmorMaterial(section), readEnumItemSlot(section), readInfo(section));
	}

	public static ItemSnowball loadSnowball(ConfigurationSection section) {
		return new ItemSnowball(readInfo(section));
	}

	public static ItemEgg loadEgg(ConfigurationSection section) {
		return new ItemEgg(readInfo(section));
	}

	public static ItemEnderPearl loadEnderPearl(ConfigurationSection section) {
		return new ItemEnderPearl(readInfo(section));
	}

	public static ItemSpell loadSpell(ConfigurationSection section) {
		return new ItemSpell(Spell.load(section), readInfo(section));
	}

	public static ItemLeftClickSpell loadLeftClickSpell(ConfigurationSection section) {
		return new ItemLeftClickSpell(Spell.load(section), readInfo(section));
	}

	public static ItemSpellScroll loadScroll(ConfigurationSection section) {
		return new ItemSpellScroll(Spell.load(section), readInfo(section));
	}

	public static ItemLeftClickSpellScroll loadLeftClickScroll(ConfigurationSection section) {
		return new ItemLeftClickSpellScroll(Spell.load(section), readInfo(section));
	}

	public static ItemEffectPotion loadEffectPotion(ConfigurationSection section) {
		return new ItemEffectPotion(readEffect(section), section.getInt("time"), readInfo(section));
	}

	public static ItemGun loadGun(ConfigurationSection section) {
		return new ItemGun(readInfo(section));
	}

	public static Info readInfo(ConfigurationSection section) {
		Info info = new Info();
		if (section.contains("creativeModeTab"))
			info.a(readCreativeModeTab(section));
		if (section.contains("rarity"))
			info.a(readEnumItemRarity(section));
		if (section.contains("foodInfo"))
			info.a(readFoodInfo(section.getConfigurationSection("foodInfo")));
		if (section.contains("size"))
			info.a(section.getInt("size"));
		if (section.contains("durability"))
			info.c(section.getInt("durability"));
		return info;
	}

	public static CreativeModeTab readCreativeModeTab(ConfigurationSection section) {
		CreativeModeTab[] tabs = CreativeModeTab.a;
		int size = tabs.length;
		String name = section.getString("creativeModeTab");
		for (int i = 0; i < size; i++) {
			if (tabs[i].c().equals(name)) {
				return tabs[i];
			}
		}
		Logger.warn("NMSItemLoader: Could not find creative mode tab for name '" + name + "'");
		return CreativeModeTab.f; // misc
	}

	public static FoodInfo readFoodInfo(ConfigurationSection section) {
		// TODO: boolean fields c,d,e are unknown and not used!
		FoodInfo.a foodInfo = new FoodInfo.a();
		if (section.contains("nutrition"))
			foodInfo.a(section.getInt("nutrition"));
		if (section.contains("saturation"))
			foodInfo.a((float) section.getDouble("saturation"));
		if (section.contains("effects")) {
			ConfigurationSection effectsSection = section.getConfigurationSection("effects");
			for (String key : effectsSection.getKeys(false)) {
				ConfigurationSection effectSection = effectsSection.getConfigurationSection(key);
				foodInfo.a(readMobEffect(effectSection), (float) effectSection.getDouble("value", 1.0));
				// TODO: Do not know what value actually does!
			}
		}
		return foodInfo.d();
	}

	public static MobEffect readMobEffect(ConfigurationSection section) {
		MobEffectList mobEffect = AiostMobEffects.get(section.getName());
		if (mobEffect == null) {
			Logger.warn("NMSItemLoader: Could not find mob effect for name '" + section.getName() + "'");
			mobEffect = AiostMobEffects.ABSORBTION;
		}
		return new MobEffect(mobEffect, section.getInt("duration"), section.getInt("amplifier"));
	}

	public static EnumItemRarity readEnumItemRarity(ConfigurationSection section) {
		if (section.contains("rarity")) {
			EnumItemRarity rarity = EnumItemRarity.valueOf(section.getString("rarity").toUpperCase());
			if (rarity != null)
				return rarity;
			Logger.warn("NMSItemLoader: Could not find rarity for name '" + section.getString("rarity") + "'");
		}
		return EnumItemRarity.COMMON;
	}

	public static AiostToolMaterial readToolMaterial(ConfigurationSection section) {
		if (section.contains("toolMaterial")) {
			AiostToolMaterial toolmaterial = AiostToolMaterial.getIgnoreCase(section.getString("toolMaterial"));
			if (toolmaterial != null)
				return toolmaterial;
			Logger.warn(
					"NMSItemLoader: Could not find tool material for name '" + section.getString("toolMaterial") + "'");
		}
		return AiostToolMaterial.WOOD;
	}

	public static EnumArmorMaterial readArmorMaterial(ConfigurationSection section) {
		if (section.contains("armorMaterial")) {
			EnumArmorMaterial armorMaterial = EnumArmorMaterial
					.valueOf(section.getString("armorMaterial").toUpperCase());
			if (armorMaterial != null)
				return armorMaterial;
			Logger.warn("NMSItemLoader: Could not find armor material for name '" + section.getString("armorMaterial")
					+ "'");
		}
		return EnumArmorMaterial.LEATHER;
	}

	public static EnumItemSlot readEnumItemSlot(ConfigurationSection section) {
		if (section.contains("slot")) {
			EnumItemSlot slot = EnumItemSlot.valueOf(section.getString("slot").toUpperCase());
			if (slot != null)
				return slot;
			Logger.warn("NMSItemLoader: Could not find slot for name '" + section.getString("slot") + "'");
		}
		return EnumItemSlot.HEAD;
	}

	public static String[] readStringArray(ConfigurationSection section, String entry) {
		List<String> stringList = section.getStringList(entry);
		return stringList.toArray(new String[stringList.size()]);
	}

	public static Effect[] readEffect(ConfigurationSection section) {
		return EffectBuilder.loadEffects(section.getConfigurationSection("effects")).createEffectArray();
	}
}