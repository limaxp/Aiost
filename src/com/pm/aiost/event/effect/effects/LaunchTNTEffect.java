package com.pm.aiost.event.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

import com.pm.aiost.entity.projectile.ProjectileHelper;
import com.pm.aiost.entity.projectile.projectiles.TNTProjectile;
import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimpleLivingEntityEffect;
import com.pm.aiost.misc.menu.menus.request.DoesUseEffectMenu;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompound;
import com.pm.aiost.player.ServerPlayer;

public class LaunchTNTEffect extends SimpleLivingEntityEffect {

	protected float velocityMultiplier;
	protected float damage;
	protected float knockback;
	protected int fuseTicks;
	protected Effect effect;

	public LaunchTNTEffect() {
	}

	public LaunchTNTEffect(byte[] actions, byte condition, float velocityMultiplier, float damage, float knockback,
			int fuseTicks) {
		this(actions, condition, velocityMultiplier, damage, knockback, fuseTicks, Effect.EMPTY);
	}

	public LaunchTNTEffect(byte[] actions, byte condition, float velocityMultiplier, float damage, float knockback,
			int fuseTicks, Effect effect) {
		super(actions, condition);
		this.velocityMultiplier = velocityMultiplier;
		this.damage = damage;
		this.knockback = knockback;
		this.fuseTicks = fuseTicks;
		this.effect = effect;
	}

	@Override
	public void runEffect(LivingEntity entity) {
		launchTNT(entity, velocityMultiplier, damage, knockback, fuseTicks, effect);
	}

	public static TNTProjectile launchTNT(LivingEntity entity, float velocityMultiplier, float damage, float knockback,
			int fuseTicks, Effect effect) {
		TNTProjectile projectile = new TNTProjectile(((CraftLivingEntity) entity).getHandle());
		projectile.setDamage(damage);
		projectile.setKnockback(knockback);
		projectile.setEffect(effect);
		projectile.setFuseTicks(fuseTicks);
		ProjectileHelper.launchProjectile(entity, projectile, velocityMultiplier);
		return projectile;
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		LaunchTNTEffect launchTNT = (LaunchTNTEffect) effect;
		if (launchTNT.velocityMultiplier != velocityMultiplier)
			return false;
		if (launchTNT.damage != damage)
			return false;
		if (launchTNT.knockback != knockback)
			return false;
		if (launchTNT.fuseTicks != fuseTicks)
			return false;
		if (launchTNT.effect != effect)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		velocityMultiplier = (float) section.getDouble("velocityMultiplier");
		damage = (float) section.getDouble("damage");
		knockback = (float) section.getDouble("knockback");
		fuseTicks = section.getInt("fuseTicks");
		effect = Effect.loadConfiguration(section);
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		LaunchTNTEffect launchTNT = (LaunchTNTEffect) effect;
		if (launchTNT.velocityMultiplier != 0)
			velocityMultiplier = launchTNT.velocityMultiplier;
		if (launchTNT.damage != 0)
			damage = launchTNT.damage;
		if (launchTNT.knockback != 0)
			knockback = launchTNT.knockback;
		if (launchTNT.fuseTicks != 0)
			fuseTicks = launchTNT.fuseTicks;
		if (launchTNT.effect != null)
			effect = launchTNT.effect;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		velocityMultiplier = nbt.getFloat("velocityMultiplier");
		damage = nbt.getFloat("damage");
		knockback = nbt.getFloat("knockback");
		fuseTicks = nbt.getInt("fuseTicks");
		effect = Effect.loadNBT(nbt.getCompound("effect"));
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setFloat("velocityMultiplier", velocityMultiplier);
		nbt.setFloat("damage", damage);
		nbt.setFloat("knockback", knockback);
		nbt.setFloat("fuseTicks", fuseTicks);
		nbt.set("effect", Effect.saveNBT(effect, new NBTCompound()));
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu,
						() -> new NumberMenu(BOLD + "Choose velocity"), () -> new NumberMenu(BOLD + "Choose damage"),
						() -> new NumberMenu(BOLD + "Choose knockback"),
						() -> new NumberMenu(BOLD + "Choose fuse time"), DoesUseEffectMenu::getMenu },
				new Consumer[] { this::setActions, this::setCondition,
						(velocityMultiplier) -> this.velocityMultiplier = ((Double) velocityMultiplier).floatValue(),
						(damage) -> this.damage = ((Double) damage).floatValue(),
						(knockback) -> this.knockback = ((Double) knockback).floatValue(),
						(fuseTicks) -> this.fuseTicks = ((Double) fuseTicks).intValue(),
						(effect) -> this.effect = (Effect) effect });
	}

	@Override
	public EffectType<? extends LaunchTNTEffect> getType() {
		return EffectTypes.LAUNCH_TNT;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		velocityMultiplier = 1;
		damage = 1;
		knockback = 1;
		fuseTicks = 60;
		effect = Effect.EMPTY;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Velocity: " + ChatColor.DARK_GRAY + velocityMultiplier);
		list.add(ChatColor.GRAY + "Damage: " + ChatColor.DARK_GRAY + damage);
		list.add(ChatColor.GRAY + "Knockback: " + ChatColor.DARK_GRAY + knockback);
		list.add(ChatColor.GRAY + "Fuse time: " + ChatColor.DARK_GRAY + fuseTicks);
		list.add(null);
		list.add(ChatColor.GRAY + "Effect:");
		effect.createDescription(list);
	}

	public void setVelocityMultiplier(float velocityMultiplier) {
		this.velocityMultiplier = velocityMultiplier;
	}

	public float getVelocityMultiplier() {
		return velocityMultiplier;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public float getDamage() {
		return damage;
	}

	public void setKnockback(float knockback) {
		this.knockback = knockback;
	}

	public float getKnockback() {
		return knockback;
	}

	public void setFuseTicks(int fuseTicks) {
		this.fuseTicks = fuseTicks;
	}

	public int getFuseTicks() {
		return fuseTicks;
	}

	public void setEffect(Effect effect) {
		this.effect = effect;
	}

	public Effect getEffect() {
		return effect;
	}
}
