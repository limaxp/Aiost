package com.pm.aiost.entity;

import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.RED;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import com.pm.aiost.Aiost;
import com.pm.aiost.misc.event.AiostEventFactory;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.particle.IParticle;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.phys.Vec3;

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

	public static boolean launch(LivingEntity shooter, Entity entity, float heigth, float power, float accuracy) {
		return launch(shooter, entity, shooter.getXRot(), shooter.getYHeadRot(), heigth, power, accuracy);
	}

	public static boolean launch(LivingEntity shooter, Entity entity, float pitch, float yaw, float heigth, float power,
			float accuracy) {
		if (AiostEventFactory.callProjectileLaunchEvent(entity.getBukkitEntity()).isCancelled())
			return false;

		float f5 = -Mth.sin(yaw * 0.017453292F) * Mth.cos(pitch * 0.017453292F);
		float f6 = -Mth.sin((pitch + heigth) * 0.017453292F);
		float f7 = Mth.cos(yaw * 0.017453292F) * Mth.cos(pitch * 0.017453292F);
		shoot(entity, f5, f6, f7, power, accuracy);
		Vec3 vec3d = shooter.getDeltaMovement();
		entity.setDeltaMovement(entity.getDeltaMovement().add(vec3d.x, shooter.onGround() ? 0.0 : vec3d.y, vec3d.z));
		return true;
	}

	public static void shoot(Entity entity, float motX, float motY, float motZ, float power, float accuracy) {
		Vec3 vec3d = movementToShoot(entity, motX, motY, motZ, power, accuracy);
		entity.setDeltaMovement(vec3d);
		double d3 = vec3d.horizontalDistance();
		entity.setYRot((float) (Mth.atan2(vec3d.x, vec3d.z) * 57.2957763671875));
		entity.setXRot((float) (Mth.atan2(vec3d.y, d3) * 57.2957763671875));
		entity.yRotO = entity.getYRot();
		entity.xRotO = entity.getXRot();
	}

	public static Vec3 movementToShoot(Entity entity, float motX, float motY, float motZ, float power, float accuracy) {
		return (new Vec3(motX, motY, motZ)).normalize().add(entity.random.triangle(0.0, 0.0172275F * accuracy),
				entity.random.triangle(0.0, 0.0172275F * accuracy), entity.random.triangle(0.0, 0.0172275F * accuracy))
				.scale(power);
	}

	public <T extends LivingEntity> void nearestAttackableTargetGoal(Mob entity, Class<T> clazz) {
		entity.targetSelector.addGoal(1, new NearestAttackableTargetGoal<T>(entity, clazz, true));
	}

	public void meleeAttackGoal(PathfinderMob entity, Class<? extends LivingEntity> clazz) {
		entity.goalSelector.addGoal(1, new MeleeAttackGoal(entity, 1.0D, true));
	}

	public void cantBurnInSun(Mob entity) {
		entity.equipItemIfPossible(new ItemStack(Items.IRON_HELMET));
	}

	public void particle(Entity entity, IParticle particle) {
		World world = NMS.from(entity.level());
		new BukkitRunnable() {
			@Override
			public void run() {
				if (!entity.isAlive())
					cancel();
				particle.spawn(world, entity.getX(), entity.getY(), entity.getZ());
			}
		}.runTaskTimer(Aiost.getPlugin(), 0, 5);
	}
}
