package com.pm.aiost.item;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class AiostTier implements Tier {

	private static final Map<String, Tier> NAME_MAP = new HashMap<String, Tier>();

	public static final Tier WOOD = register("wood", Tiers.WOOD);

	public static final Tier STONE = register("stone", Tiers.STONE);

	public static final Tier IRON = register("iron", Tiers.IRON);

	public static final Tier GOLD = register("gold", Tiers.GOLD);

	public static final Tier DIAMOND = register("diamond", Tiers.DIAMOND);

	public static final Tier NETHERITE = register("netherite", Tiers.NETHERITE);

	public static final Tier COPPER = register("copper", 200, 1.5F, 5.0F, 8,
			Ingredient.of(net.minecraft.world.item.Items.DIAMOND));

	public static final Tier TIN = register("tin", 200, 1.5F, 5.0F, 8,
			Ingredient.of(net.minecraft.world.item.Items.DIAMOND));

	public static final Tier BRONZE = register("bronze", 250, 2.0F, 6.0F, 18,
			Ingredient.of(net.minecraft.world.item.Items.DIAMOND));

	public static final Tier EMERALD = register("emerald", 1000, 2.5F, 7.0F, 10,
			Ingredient.of(net.minecraft.world.item.Items.DIAMOND));

	public static final Tier RUBY = register("ruby", 1444, 2.5F, 7.0F, 14,
			Ingredient.of(net.minecraft.world.item.Items.DIAMOND));

	public static final Tier BLACK_DIAMOND = register("black_diamond", 2372, 4.0F, 9.0F, 18,
			Ingredient.of(net.minecraft.world.item.Items.DIAMOND));

	public static final Tier OBSIDIAN_CRYSTAL = register("obsidian_crystal", 3000, 4.0F, 9.0F, 12,
			Ingredient.of(net.minecraft.world.item.Items.DIAMOND));

	public static final Tier LAVA_CRYSTAL = register("lava_crystal", 2700, 4.0F, 9.0F, 20,
			Ingredient.of(net.minecraft.world.item.Items.DIAMOND));

	public static final Tier QUARTZ_CRYSTAL = register("quartz_crystal", 3500, 5.0F, 10.0F, 22,
			Ingredient.of(net.minecraft.world.item.Items.DIAMOND));

	public static Tier register(String name, int uses, float speed, float damage, int enchantmentValue,
			Ingredient repairItem) {
		return register(name, uses, speed, damage, null, enchantmentValue, repairItem);
	}

	public static Tier register(String name, int uses, float speed, float damage, TagKey<Block> incorrectBlocksForDrops,
			int enchantmentValue, Ingredient repairItem) {
		return register(name,
				new AiostTier(uses, speed, damage, incorrectBlocksForDrops, enchantmentValue, repairItem));
	}

	public static Tier register(String name, AiostTier toolMaterial) {
		NAME_MAP.put(name.toLowerCase(), toolMaterial);
		return toolMaterial;
	}

	public static Tier register(String name, Tier tier) {
		NAME_MAP.put(name.toLowerCase(), tier);
		return tier;
	}

	public static Tier get(String name) {
		return NAME_MAP.get(name);
	}

	public static Tier getOrDefault(String name, Tier defaultValue) {
		return NAME_MAP.getOrDefault(name, defaultValue);
	}

	public static Tier getIgnoreCase(String name) {
		return NAME_MAP.get(name.toLowerCase());
	}

	private final int uses;
	private final float speed;
	private final float damage;
	private final TagKey<Block> incorrectBlocksForDrops;
	private final int enchantmentValue;
	private final Ingredient repairItem;

	public AiostTier(int uses, float speed, float damage, TagKey<Block> incorrectBlocksForDrops,
			int enchantmentValue, Ingredient repairItem) {
		this.uses = uses;
		this.speed = speed;
		this.damage = damage;
		this.incorrectBlocksForDrops = incorrectBlocksForDrops;
		this.enchantmentValue = enchantmentValue;
		this.repairItem = repairItem;
	}

	@Override
	public int getUses() {
		return this.uses;
	}

	@Override
	public float getSpeed() {
		return this.speed;
	}

	@Override
	public float getAttackDamageBonus() {
		return this.damage;
	}

	@Override
	public TagKey<Block> getIncorrectBlocksForDrops() {
		return this.incorrectBlocksForDrops;
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantmentValue;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairItem;
	}
}