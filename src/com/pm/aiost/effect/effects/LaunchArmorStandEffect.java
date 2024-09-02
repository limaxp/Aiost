package com.pm.aiost.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.effect.Effect;
import com.pm.aiost.effect.EffectBuilder;
import com.pm.aiost.effect.EffectType;
import com.pm.aiost.effect.EffectTypes;
import com.pm.aiost.effect.blueprints.SimpleLivingEntityEffect;
import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.item.ItemLoader;
import com.pm.aiost.misc.event.eventHandler.handler.ProjectileEventHandler;
import com.pm.aiost.misc.menu.menus.request.CreateItemMenu;
import com.pm.aiost.misc.menu.menus.request.DoesUseEffectMenu;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest;
import com.pm.aiost.misc.nms.NBT;
import com.pm.aiost.misc.utils.ProjectileHelper;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.nbt.CompoundTag;

public class LaunchArmorStandEffect extends SimpleLivingEntityEffect {

	protected ItemStack itemStack;
	protected float velocityMultiplier;
	protected float damage;
	protected float knockback;
	protected Effect effect;

	public LaunchArmorStandEffect() {
	}

	public LaunchArmorStandEffect(byte[] actions, byte condition, ItemStack is, float velocityMultiplier, float damage,
			float knockback) {
		this(actions, condition, is, velocityMultiplier, damage, knockback, Effect.EMPTY);
	}

	public LaunchArmorStandEffect(byte[] actions, byte condition, ItemStack item, float velocityMultiplier,
			float damage, float knockback, Effect effect) {
		super(actions, condition);
		this.itemStack = item;
		this.velocityMultiplier = velocityMultiplier;
		this.damage = damage;
		this.knockback = knockback;
		this.effect = effect;
	}

	@Override
	public void runEffect(LivingEntity entity) {
		ProjectileEventHandler handler = new ProjectileEventHandler(entity);
		if (ProjectileHelper.launchProjectile(entity, AiostEntityTypes.ARMORSTAND_PROJECTILE, 0.5F,
				velocityMultiplier * 0.4F, 0.95F, handler, false) != null) {
			handler.setDamage(damage);
			handler.setKnockback(knockback);
			handler.setEffect(effect);
		}
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		LaunchArmorStandEffect launchArmorStand = (LaunchArmorStandEffect) effect;
		if (!launchArmorStand.itemStack.equals(itemStack))
			return false;
		if (launchArmorStand.velocityMultiplier != velocityMultiplier)
			return false;
		if (launchArmorStand.damage != damage)
			return false;
		if (launchArmorStand.knockback != knockback)
			return false;
		if (launchArmorStand.effect != effect)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		itemStack = ItemLoader.loadItemOrNull(section.get("item"));
		velocityMultiplier = (float) section.getDouble("velocityMultiplier");
		damage = (float) section.getDouble("damage");
		knockback = (float) section.getDouble("knockback");
		ConfigurationSection effectSection = section.getConfigurationSection("effect");
		effect = EffectBuilder
				.createEffect(effectSection.getConfigurationSection(effectSection.getKeys(false).iterator().next()));
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		LaunchArmorStandEffect launchArmorStand = (LaunchArmorStandEffect) effect;
		if (launchArmorStand.itemStack != null)
			itemStack = launchArmorStand.itemStack.clone();
		if (launchArmorStand.velocityMultiplier != 0)
			velocityMultiplier = launchArmorStand.velocityMultiplier;
		if (launchArmorStand.damage != 0)
			damage = launchArmorStand.damage;
		if (launchArmorStand.knockback != 0)
			knockback = launchArmorStand.knockback;
		if (launchArmorStand.effect != null)
			effect = launchArmorStand.effect;
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		itemStack = NBT.loadItem(nbt.getCompound("item"));
		velocityMultiplier = nbt.getFloat("velocityMultiplier");
		damage = nbt.getFloat("damage");
		knockback = nbt.getFloat("knockback");
		effect = Effect.loadNBT(nbt.getCompound("effect"));
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		super.save(nbt);
		nbt.put("item", NBT.saveItem(new CompoundTag(), itemStack));
		nbt.putFloat("velocityMultiplier", velocityMultiplier);
		nbt.putFloat("damage", damage);
		nbt.putFloat("knockback", knockback);
		nbt.put("effect", Effect.saveNBT(effect, new CompoundTag()));
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new MultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu,
						() -> serverPlayer.getOrCreateMenu(CreateItemMenu.class, CreateItemMenu::new),
						() -> new NumberMenu(BOLD + "Choose velocity multiplier"),
						() -> new NumberMenu(BOLD + "Choose damage"), () -> new NumberMenu(BOLD + "Choose knockback"),
						DoesUseEffectMenu::getMenu },
				new Consumer[] { this::setActions, this::setCondition,
						(itemStack) -> this.itemStack = (ItemStack) itemStack,
						(velocityMultiplier) -> this.velocityMultiplier = ((Double) velocityMultiplier).floatValue(),
						(damage) -> this.damage = ((Double) damage).floatValue(),
						(knockback) -> this.knockback = ((Double) knockback).floatValue(),
						(effect) -> this.effect = (Effect) effect });
	}

	@Override
	public EffectType<? extends LaunchArmorStandEffect> getType() {
		return EffectTypes.LAUNCH_ARMORSTAND;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		itemStack = new ItemStack(Material.SNOWBALL);
		velocityMultiplier = 1;
		damage = 1;
		knockback = 1;
		effect = Effect.EMPTY;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Item: " + ChatColor.DARK_GRAY + itemStack);
		list.add(ChatColor.GRAY + "Velocity: " + ChatColor.DARK_GRAY + velocityMultiplier);
		list.add(ChatColor.GRAY + "Damage: " + ChatColor.DARK_GRAY + damage);
		list.add(ChatColor.GRAY + "Knockback: " + ChatColor.DARK_GRAY + knockback);
		list.add(null);
		list.add(ChatColor.GRAY + "Effect:");
		effect.createDescription(list);
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public ItemStack getItemStack() {
		return itemStack;
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