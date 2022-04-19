package com.pm.aiost.entity;

import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.RED;

import net.minecraft.server.v1_15_R1.AttributeInstance;
import net.minecraft.server.v1_15_R1.AttributeMapBase;
import net.minecraft.server.v1_15_R1.EntityInsentient;
import net.minecraft.server.v1_15_R1.GenericAttributes;

public class EntityHelper {

	public static final String HEALTH_SYMBOL = RED + "" + BOLD + "❤";

	public static final int MAX_LEVEL = 100;

	public static int calculateRandomLevel(EntityInsentient entity) {
		return calculateRandomLevel(entity, MAX_LEVEL);
	}

	public static int calculateRandomLevel(EntityInsentient entity, int maxLevel) {
		return entity.getRandom().nextInt(maxLevel);
	}

	public static void initRandomLevelAttributes(EntityInsentient entity, double baseDamage, double baseKnockback,
			double baseHealth, double baseMovementSpeed, double baseFollowRange, double baseArmor) {
		initLevelAttributes(entity, calculateRandomLevel(entity), baseDamage, baseKnockback, baseHealth,
				baseMovementSpeed, baseFollowRange, baseArmor);
	}

	public static void initLevelAttributes(EntityInsentient entity, int level, double baseDamage, double baseKnockback,
			double baseHealth, double baseMovementSpeed, double baseFollowRange, double baseArmor) {
		AttributeMapBase attributeMap = entity.getAttributeMap();
		attributeMap.b(GenericAttributes.ATTACK_DAMAGE).setValue(baseDamage + (baseDamage / 100) * level);
		attributeMap.b(GenericAttributes.ATTACK_KNOCKBACK).setValue(baseKnockback + (baseKnockback / 100) * level);
		attributeMap.b(GenericAttributes.MAX_HEALTH).setValue(baseHealth + ((baseHealth / 100D) * level));
		attributeMap.b(GenericAttributes.MOVEMENT_SPEED)
				.setValue(baseMovementSpeed + (baseMovementSpeed / 100) * level);
		attributeMap.b(GenericAttributes.FOLLOW_RANGE).setValue(baseFollowRange + (baseFollowRange / 100) * level);
		attributeMap.b(GenericAttributes.ARMOR).setValue(baseArmor + (baseArmor / 100) * level);
		attributeMap.b(GenericAttributes.ARMOR_TOUGHNESS).setValue(0.1 * level);
		attributeMap.b(GenericAttributes.KNOCKBACK_RESISTANCE).setValue(0.01 * level);
		attributeMap.b(GenericAttributes.LUCK).setValue(0.01 * level);
	}

	public static void setRandomLevelAttributes(EntityInsentient entity, double baseDamage, double baseKnockback,
			double baseHealth, double baseMovementSpeed, double baseFollowRange, double baseArmor) {
		setLevelAttributes(entity, calculateRandomLevel(entity), baseDamage, baseKnockback, baseHealth,
				baseMovementSpeed, baseFollowRange, baseArmor);
	}

	public static void setLevelAttributes(EntityInsentient entity, int level, double baseDamage, double baseKnockback,
			double baseHealth, double baseMovementSpeed, double baseFollowRange, double baseArmor) {
		entity.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(baseDamage + (baseDamage / 100) * level);
		entity.getAttributeInstance(GenericAttributes.ATTACK_KNOCKBACK)
				.setValue(baseKnockback + (baseKnockback / 100) * level);
		entity.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(baseHealth + (baseHealth / 100) * level);
		entity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED)
				.setValue(baseMovementSpeed + (baseMovementSpeed / 100) * level);
		entity.getAttributeInstance(GenericAttributes.FOLLOW_RANGE)
				.setValue(baseFollowRange + (baseFollowRange / 100) * level);
		entity.getAttributeInstance(GenericAttributes.ARMOR).setValue(baseArmor + (baseArmor / 100) * level);
		entity.getAttributeInstance(GenericAttributes.ARMOR_TOUGHNESS).setValue(0.1 * level);
		entity.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).setValue(0.01 * level);
		entity.getAttributeInstance(GenericAttributes.LUCK).setValue(0.01 * level);
	}

	public static void setRandomLevel(EntityInsentient entity) {
		setLevel(entity, calculateRandomLevel(entity));
	}

	public static void setLevel(EntityInsentient entity, int level) {
		AttributeInstance damageAttribute = entity.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE);
		double baseDamage = damageAttribute.getValue();
		damageAttribute.setValue(baseDamage + (baseDamage / 100) * level);

		AttributeInstance knockbackAttribute = entity.getAttributeInstance(GenericAttributes.ATTACK_KNOCKBACK);
		double baseKnockback = knockbackAttribute.getValue();
		knockbackAttribute.setValue(baseKnockback + (baseKnockback / 100) * level);

		AttributeInstance maxHealthAttribute = entity.getAttributeInstance(GenericAttributes.MAX_HEALTH);
		double baseHealth = maxHealthAttribute.getValue();
		maxHealthAttribute.setValue(baseHealth + (baseHealth / 100) * level);

		AttributeInstance movementSpeedAttribute = entity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
		double baseMovementSpeed = movementSpeedAttribute.getValue();
		movementSpeedAttribute.setValue(baseMovementSpeed + (baseMovementSpeed / 100) * level);

		AttributeInstance followRangeAttribute = entity.getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
		double baseFollowRange = followRangeAttribute.getValue();
		followRangeAttribute.setValue(baseFollowRange + (baseFollowRange / 100) * level);

		AttributeInstance armorAttribute = entity.getAttributeInstance(GenericAttributes.ARMOR);
		double baseArmor = armorAttribute.getValue();
		armorAttribute.setValue(baseArmor + (baseArmor / 100) * level);

		entity.getAttributeInstance(GenericAttributes.ARMOR_TOUGHNESS).setValue(0.1 * level);
		entity.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).setValue(0.01 * level);
		entity.getAttributeInstance(GenericAttributes.LUCK).setValue(0.01 * level);
	}
}
