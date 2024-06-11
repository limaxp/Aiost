package com.pm.aiost.item.custom;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_20_R4.util.CraftMagicNumbers;

import com.pm.aiost.effect.Effect;
import com.pm.aiost.effect.EffectBuilder;
import com.pm.aiost.item.AiostTier;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.nms.NMS;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;

public class AiostItemLoader {

	public static Item registerItem(String name, Material mat, ConfigurationSection itemSection) {
		NMS.unfreezeRegistry(BuiltInRegistries.ITEM);
		Item item = AiostItems.registerItem(name.replace(' ', '_').toLowerCase(), CraftMagicNumbers.getItem(mat),
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
		Function<ConfigurationSection, ? extends Item> func = AiostItemFunctionRegistry.get(name);
		if (func == null) {
			Logger.warn("NMSItemLoader: no registry found for name '" + name + "'");
			return loadBaseItem(section);
		}
		return func.apply(section);
	}

	public static Item loadBaseItem(ConfigurationSection section) {
		return new Item(readProperties(section));
	}

	public static SwordItem loadSword(ConfigurationSection section) {
		return new SwordItem(readTier(section), readProperties(section));
	}

	public static PickaxeItem loadPickaxe(ConfigurationSection section) {
		return new PickaxeItem(readTier(section), readProperties(section));
	}

	public static AxeItem loadAxe(ConfigurationSection section) {
		return new AxeItem(readTier(section), readProperties(section));
	}

	public static ShovelItem loadShovel(ConfigurationSection section) {
		return new ShovelItem(readTier(section), readProperties(section));
	}

	public static HoeItem loadHoe(ConfigurationSection section) {
		return new HoeItem(readTier(section), readProperties(section));
	}

	public static ShieldItem loadShield(ConfigurationSection section) {
		return new ShieldItem(readProperties(section));
	}

	public static BowItem loadBow(ConfigurationSection section) {
		return new BowItem(readProperties(section));
	}

	public static CrossbowItem loadCrossbow(ConfigurationSection section) {
		return new CrossbowItem(readProperties(section));
	}

	public static ArrowItem loadArrow(ConfigurationSection section) {
		return new ArrowItem(readProperties(section));
	}

	public static FlintAndSteelItem loadFlintAndSteel(ConfigurationSection section) {
		return new FlintAndSteelItem(readProperties(section));
	}

	public static ArmorItem loadArmor(ConfigurationSection section) {
		return new ArmorItem(readArmorMaterial(section), readArmorType(section), readProperties(section));
	}

	public static SnowballItem loadSnowball(ConfigurationSection section) {
		return new SnowballItem(readProperties(section));
	}

	public static EggItem loadEgg(ConfigurationSection section) {
		return new EggItem(readProperties(section));
	}

	public static EnderpearlItem loadEnderPearl(ConfigurationSection section) {
		return new EnderpearlItem(readProperties(section));
	}

	public static Item.Properties readProperties(ConfigurationSection section) {
		Item.Properties info = new Item.Properties();
		if (section.contains("rarity"))
			info.rarity(readRarity(section));
		if (section.contains("foodInfo"))
			info.food(readFoodProperties(section.getConfigurationSection("foodInfo")));
		if (section.contains("size"))
			info.stacksTo(section.getInt("size"));
		if (section.contains("durability"))
			info.durability(section.getInt("durability"));
		if (section.contains("fireResistant"))
			info.fireResistant();
		return info;
	}

	public static FoodProperties readFoodProperties(ConfigurationSection section) {
		FoodProperties.Builder foodInfo = new FoodProperties.Builder();
		if (section.contains("nutrition"))
			foodInfo.nutrition(section.getInt("nutrition"));
		if (section.contains("saturation"))
			foodInfo.saturationModifier((float) section.getDouble("saturation"));
		if (section.contains("alwaysEdible"))
			foodInfo.alwaysEdible();
		if (section.contains("fast"))
			foodInfo.fast();
		if (section.contains("effects")) {
			ConfigurationSection effectsSection = section.getConfigurationSection("effects");
			for (String key : effectsSection.getKeys(false)) {
				ConfigurationSection effectSection = effectsSection.getConfigurationSection(key);
				foodInfo.effect(readMobEffect(effectSection), (float) effectSection.getDouble("value", 1.0));
			}
		}
		return foodInfo.build();
	}

	public static Rarity readRarity(ConfigurationSection section) {
		if (section.contains("rarity")) {
			Rarity rarity = Rarity.valueOf(section.getString("rarity").toUpperCase());
			if (rarity != null)
				return rarity;
			Logger.warn("NMSItemLoader: Could not find rarity for name '" + section.getString("rarity") + "'");
		}
		return Rarity.COMMON;
	}

	public static Tier readTier(ConfigurationSection section) {
		if (section.contains("toolMaterial")) {
			Tier toolmaterial = AiostTier.getIgnoreCase(section.getString("toolMaterial"));
			if (toolmaterial != null)
				return toolmaterial;
			Logger.warn(
					"NMSItemLoader: Could not find tool material for name '" + section.getString("toolMaterial") + "'");
		}
		return Tiers.WOOD;
	}

	public static Holder<ArmorMaterial> readArmorMaterial(ConfigurationSection section) {
		if (section.contains("armorMaterial")) {
			Optional<Holder.Reference<ArmorMaterial>> armorMaterial = BuiltInRegistries.ARMOR_MATERIAL
					.getHolder(new ResourceLocation(section.getString("armorMaterial").toUpperCase()));
			if (armorMaterial.isPresent())
				return armorMaterial.get();
			Logger.warn("NMSItemLoader: Could not find armor material for name '" + section.getString("armorMaterial")
					+ "'");
		}
		return ArmorMaterials.LEATHER;
	}

	public static MobEffectInstance readMobEffect(ConfigurationSection section) {
		Optional<Holder.Reference<MobEffect>> optional = BuiltInRegistries.MOB_EFFECT
				.getHolder(new ResourceLocation(section.getName()));
		Holder<MobEffect> effect;
		if (optional.isEmpty()) {
			Logger.warn("NMSItemLoader: Could not find mob effect for name '" + section.getName() + "'");
			effect = MobEffects.ABSORPTION;
		} else
			effect = optional.get();
		return new MobEffectInstance(effect, section.getInt("duration"), section.getInt("amplifier"));
	}

	public static ArmorItem.Type readArmorType(ConfigurationSection section) {
		if (section.contains("slot")) {
			ArmorItem.Type slot = ArmorItem.Type.valueOf(section.getString("slot").toUpperCase());
			if (slot != null)
				return slot;
			Logger.warn("NMSItemLoader: Could not find slot for name '" + section.getString("slot") + "'");
		}
		return ArmorItem.Type.HELMET;
	}

	public static String[] readStringArray(ConfigurationSection section, String entry) {
		List<String> stringList = section.getStringList(entry);
		return stringList.toArray(new String[stringList.size()]);
	}

	public static Effect[] readEffect(ConfigurationSection section) {
		return EffectBuilder.loadEffects(section.getConfigurationSection("effects")).createEffectArray();
	}
}