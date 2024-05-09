package com.pm.aiost.entity;

import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.RED;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class EntityHelper {

	public static final String HEALTH_SYMBOL = RED + "" + BOLD + "❤";

	public static final int MAX_LEVEL = 100;

	public static int calculateRandomLevel(Mob entity) {
		return calculateRandomLevel(entity, MAX_LEVEL);
	}

	public static int calculateRandomLevel(Mob entity, int maxLevel) {
		return entity.getRandom().nextInt(maxLevel);
	}

	public static void initRandomLevelAttributes(Mob entity, double baseDamage, double baseKnockback, double baseHealth,
			double baseMovementSpeed, double baseFollowRange, double baseArmor) {
		initLevelAttributes(entity, calculateRandomLevel(entity), baseDamage, baseKnockback, baseHealth,
				baseMovementSpeed, baseFollowRange, baseArmor);
	}

	public static void initLevelAttributes(Mob entity, int level, double baseDamage, double baseKnockback,
			double baseHealth, double baseMovementSpeed, double baseFollowRange, double baseArmor) {
		AttributeMap attributeMap = entity.getAttributes();
		attributeMap.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(baseDamage + (baseDamage / 100) * level);
		attributeMap.getInstance(Attributes.ATTACK_KNOCKBACK)
				.setBaseValue(baseKnockback + (baseKnockback / 100) * level);
		attributeMap.getInstance(Attributes.MAX_HEALTH).setBaseValue(baseHealth + ((baseHealth / 100D) * level));
		attributeMap.getInstance(Attributes.MOVEMENT_SPEED)
				.setBaseValue(baseMovementSpeed + (baseMovementSpeed / 100) * level);
		attributeMap.getInstance(Attributes.FOLLOW_RANGE)
				.setBaseValue(baseFollowRange + (baseFollowRange / 100) * level);
		attributeMap.getInstance(Attributes.ARMOR).setBaseValue(baseArmor + (baseArmor / 100) * level);
		attributeMap.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(0.1 * level);
		attributeMap.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.01 * level);
		attributeMap.getInstance(Attributes.LUCK).setBaseValue(0.01 * level);
	}

	public static void setRandomLevelAttributes(Mob entity, double baseDamage, double baseKnockback, double baseHealth,
			double baseMovementSpeed, double baseFollowRange, double baseArmor) {
		setLevelAttributes(entity, calculateRandomLevel(entity), baseDamage, baseKnockback, baseHealth,
				baseMovementSpeed, baseFollowRange, baseArmor);
	}

	public static void setLevelAttributes(Mob entity, int level, double baseDamage, double baseKnockback,
			double baseHealth, double baseMovementSpeed, double baseFollowRange, double baseArmor) {
		entity.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(baseDamage + (baseDamage / 100) * level);
		entity.getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue(baseKnockback + (baseKnockback / 100) * level);
		entity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(baseHealth + (baseHealth / 100) * level);
		entity.getAttribute(Attributes.MOVEMENT_SPEED)
				.setBaseValue(baseMovementSpeed + (baseMovementSpeed / 100) * level);
		entity.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(baseFollowRange + (baseFollowRange / 100) * level);
		entity.getAttribute(Attributes.ARMOR).setBaseValue(baseArmor + (baseArmor / 100) * level);
		entity.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(0.1 * level);
		entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.01 * level);
		entity.getAttribute(Attributes.LUCK).setBaseValue(0.01 * level);
	}

	public static void setRandomLevel(Mob entity) {
		setLevel(entity, calculateRandomLevel(entity));
	}

	public static void setLevel(Mob entity, int level) {
		AttributeInstance damageAttribute = entity.getAttribute(Attributes.ATTACK_DAMAGE);
		double baseDamage = damageAttribute.getValue();
		damageAttribute.setBaseValue(baseDamage + (baseDamage / 100) * level);

		AttributeInstance knockbackAttribute = entity.getAttribute(Attributes.ATTACK_KNOCKBACK);
		double baseKnockback = knockbackAttribute.getValue();
		knockbackAttribute.setBaseValue(baseKnockback + (baseKnockback / 100) * level);

		AttributeInstance maxHealthAttribute = entity.getAttribute(Attributes.MAX_HEALTH);
		double baseHealth = maxHealthAttribute.getValue();
		maxHealthAttribute.setBaseValue(baseHealth + (baseHealth / 100) * level);

		AttributeInstance movementSpeedAttribute = entity.getAttribute(Attributes.MOVEMENT_SPEED);
		double baseMovementSpeed = movementSpeedAttribute.getValue();
		movementSpeedAttribute.setBaseValue(baseMovementSpeed + (baseMovementSpeed / 100) * level);

		AttributeInstance followRangeAttribute = entity.getAttribute(Attributes.FOLLOW_RANGE);
		double baseFollowRange = followRangeAttribute.getValue();
		followRangeAttribute.setBaseValue(baseFollowRange + (baseFollowRange / 100) * level);

		AttributeInstance armorAttribute = entity.getAttribute(Attributes.ARMOR);
		double baseArmor = armorAttribute.getValue();
		armorAttribute.setBaseValue(baseArmor + (baseArmor / 100) * level);

		entity.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(0.1 * level);
		entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.01 * level);
		entity.getAttribute(Attributes.LUCK).setBaseValue(0.01 * level);
	}

	public <T extends LivingEntity> void applyNearestAttackableTargetGoal(Mob mob, Class<T> clazz) {
		mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<T>(mob, clazz, true));
	}

	public void applyMeleeAttackGoal(PathfinderMob mob, Class<? extends LivingEntity> clazz) {
		mob.goalSelector.addGoal(1, new MeleeAttackGoal(mob, 1.0D, true));
	}

	public void applyCantBurnInSun(Mob mob) {
		mob.equipItemIfPossible(new ItemStack(Items.IRON_HELMET));
	}
}
