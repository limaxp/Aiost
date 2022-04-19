package com.pm.aiost.event.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

import com.pm.aiost.entity.projectile.CustomProjectile;
import com.pm.aiost.entity.projectile.ProjectileHelper;
import com.pm.aiost.entity.projectile.projectiles.ParticleProjectile;
import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimpleLivingEntityEffect;
import com.pm.aiost.misc.menu.menus.request.ChooseParticleMenu;
import com.pm.aiost.misc.menu.menus.request.DoesUseEffectMenu;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleBuilder;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompound;
import com.pm.aiost.player.ServerPlayer;

public class LaunchParticleEffect extends SimpleLivingEntityEffect {

	protected IParticle particle;
	protected float velocityMultiplier;
	protected float damage;
	protected float knockback;
	protected Effect effect;

	public LaunchParticleEffect() {
	}

	public LaunchParticleEffect(byte[] actions, byte condition, IParticle particle, float velocityMultiplier,
			float damage, float knockback) {
		this(actions, condition, particle, velocityMultiplier, damage, knockback, Effect.EMPTY);
	}

	public LaunchParticleEffect(byte[] actions, byte condition, IParticle particle, float velocityMultiplier,
			float damage, float knockback, Effect effect) {
		super(actions, condition);
		this.particle = particle;
		this.velocityMultiplier = velocityMultiplier;
		this.damage = damage;
		this.knockback = knockback;
		this.effect = effect;
	}

	@Override
	public void runEffect(LivingEntity entity) {
		launchParticle(entity, particle, velocityMultiplier, damage, knockback, effect);
	}

	public static ParticleProjectile launchParticle(LivingEntity entity, IParticle particle, float velocityMultiplier,
			float damage, float knockback, Effect effect) {
		ParticleProjectile projectile = new ParticleProjectile(((CraftLivingEntity) entity).getHandle(), particle);
		projectile.setDamage(damage);
		projectile.setKnockback(knockback);
		projectile.setEffect(effect);
		ProjectileHelper.launchProjectile(entity, (CustomProjectile) projectile, velocityMultiplier);
		return projectile;
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		LaunchParticleEffect launchParticle = (LaunchParticleEffect) effect;
		if (!launchParticle.particle.equals(particle))
			return false;
		if (launchParticle.velocityMultiplier != velocityMultiplier)
			return false;
		if (launchParticle.damage != damage)
			return false;
		if (launchParticle.knockback != knockback)
			return false;
		if (launchParticle.effect != effect)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		particle = ParticleBuilder.create(section);
		velocityMultiplier = (float) section.getDouble("velocityMultiplier");
		damage = (float) section.getDouble("damage");
		knockback = (float) section.getDouble("knockback");
		effect = Effect.loadConfiguration(section);
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		LaunchParticleEffect launchItem = (LaunchParticleEffect) effect;
		if (launchItem.particle != null)
			particle = launchItem.particle;
		if (launchItem.velocityMultiplier != 0)
			velocityMultiplier = launchItem.velocityMultiplier;
		if (launchItem.damage != 0)
			damage = launchItem.damage;
		if (launchItem.knockback != 0)
			knockback = launchItem.knockback;
		if (launchItem.effect != null)
			effect = launchItem.effect;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		particle = ParticleBuilder.create(nbt);
		velocityMultiplier = nbt.getFloat("velocityMultiplier");
		damage = nbt.getFloat("damage");
		knockback = nbt.getFloat("knockback");
		effect = Effect.loadNBT(nbt.getCompound("effect"));
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		ParticleBuilder.save(particle, nbt);
		nbt.setFloat("velocityMultiplier", velocityMultiplier);
		nbt.setFloat("damage", damage);
		nbt.setFloat("knockback", knockback);
		nbt.set("effect", Effect.saveNBT(effect, new NBTCompound()));
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu, ChooseParticleMenu::getMenu,
						() -> new NumberMenu(BOLD + "Choose velocity"), () -> new NumberMenu(BOLD + "Choose damage"),
						() -> new NumberMenu(BOLD + "Choose knockback"), DoesUseEffectMenu::getMenu },
				new Consumer[] { this::setActions, this::setCondition,
						(particle) -> this.particle = (IParticle) particle,
						(velocityMultiplier) -> this.velocityMultiplier = ((Double) velocityMultiplier).floatValue(),
						(damage) -> this.damage = ((Double) damage).floatValue(),
						(knockback) -> this.knockback = ((Double) knockback).floatValue(),
						(effect) -> this.effect = (Effect) effect });
	}

	@Override
	public EffectType<? extends LaunchParticleEffect> getType() {
		return EffectTypes.LAUNCH_PARTICLE;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		particle = IParticle.DEFAULT;
		velocityMultiplier = 1;
		damage = 1;
		knockback = 1;
		effect = Effect.EMPTY;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Velocity: " + ChatColor.DARK_GRAY + velocityMultiplier);
		list.add(ChatColor.GRAY + "Damage: " + ChatColor.DARK_GRAY + damage);
		list.add(ChatColor.GRAY + "Knockback: " + ChatColor.DARK_GRAY + knockback);
		list.add(null);
		list.add(ChatColor.GRAY + "Particle:");
		particle.createDescription(list);
		list.add(null);
		list.add(ChatColor.GRAY + "Effect:");
		effect.createDescription(list);
	}

	public void setParticle(IParticle particle) {
		this.particle = particle;
	}

	public IParticle getParticle() {
		return particle;
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

	public void setEffect(Effect effect) {
		this.effect = effect;
	}

	public Effect getEffect() {
		return effect;
	}
}
