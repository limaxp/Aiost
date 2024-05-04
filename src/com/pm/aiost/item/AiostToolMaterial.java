package com.pm.aiost.item;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class AiostToolMaterial implements Tier {

	private static final Map<String, AiostToolMaterial> NAME_MAP = new HashMap<String, AiostToolMaterial>();

	public static final AiostToolMaterial COPPER = register("copper", 200, 1.5F, 5.0F, 8,
			Ingredient.of(net.minecraft.world.item.Items.DIAMOND));

	public static final AiostToolMaterial TIN = register("tin", 200, 1.5F, 5.0F, 8,
			Ingredient.of(net.minecraft.world.item.Items.DIAMOND));

	public static final AiostToolMaterial BRONZE = register("bronze", 250, 2.0F, 6.0F, 18,
			Ingredient.of(net.minecraft.world.item.Items.DIAMOND));

	public static final AiostToolMaterial EMERALD = register("emerald", 1000, 2.5F, 7.0F, 10,
			Ingredient.of(net.minecraft.world.item.Items.DIAMOND));

	public static final AiostToolMaterial RUBY = register("ruby", 1444, 2.5F, 7.0F, 14,
			Ingredient.of(net.minecraft.world.item.Items.DIAMOND));

	public static final AiostToolMaterial BLACK_DIAMOND = register("black_diamond", 2372, 4.0F, 9.0F, 18,
			Ingredient.of(net.minecraft.world.item.Items.DIAMOND));

	public static final AiostToolMaterial OBSIDIAN_CRYSTAL = register("obsidian_crystal", 3000, 4.0F, 9.0F, 12,
			Ingredient.of(net.minecraft.world.item.Items.DIAMOND));

	public static final AiostToolMaterial LAVA_CRYSTAL = register("lava_crystal", 2700, 4.0F, 9.0F, 20,
			Ingredient.of(net.minecraft.world.item.Items.DIAMOND));

	public static final AiostToolMaterial QUARTZ_CRYSTAL = register("quartz_crystal", 3500, 5.0F, 10.0F, 22,
			Ingredient.of(net.minecraft.world.item.Items.DIAMOND));

	public static AiostToolMaterial register(String name, int uses, float speed, float damage, int enchantmentValue,
			Ingredient repairItem) {
		return register(name, uses, speed, damage, null, enchantmentValue, repairItem);
	}

	public static AiostToolMaterial register(String name, int uses, float speed, float damage,
			TagKey<Block> incorrectBlocksForDrops, int enchantmentValue, Ingredient repairItem) {
		AiostToolMaterial aiostToolMaterial = new AiostToolMaterial(uses, speed, damage, incorrectBlocksForDrops,
				enchantmentValue, repairItem);
		register(name, aiostToolMaterial);
		return aiostToolMaterial;
	}

	public static void register(String name, AiostToolMaterial toolMaterial) {
		NAME_MAP.put(name.toLowerCase(), toolMaterial);
	}

	public static AiostToolMaterial get(String name) {
		return NAME_MAP.get(name);
	}

	public static AiostToolMaterial getOrDefault(String name, AiostToolMaterial defaultValue) {
		return NAME_MAP.getOrDefault(name, defaultValue);
	}

	public static AiostToolMaterial getIgnoreCase(String name) {
		return NAME_MAP.get(name.toLowerCase());
	}

	private final int uses;
	private final float speed;
	private final float damage;
	private final TagKey<Block> incorrectBlocksForDrops;
	private final int enchantmentValue;
	private final Ingredient repairItem;

	public AiostToolMaterial(int uses, float speed, float damage, TagKey<Block> incorrectBlocksForDrops,
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