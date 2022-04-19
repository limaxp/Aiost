package com.pm.aiost.event.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectHelper;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.misc.menu.menus.request.DoesUseEffectMenu;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompound;
import com.pm.aiost.player.ServerPlayer;

public class ThrowItemEffect extends Effect {

	private float velocityMultiplier;
	private float damage;
	private float knockback;
	private Effect effect;

	public ThrowItemEffect() {
	}

	public ThrowItemEffect(byte[] actions, byte condition, float velocityMultiplier, float damage, float knockback) {
		this(actions, condition, velocityMultiplier, damage, knockback, Effect.EMPTY);
	}

	public ThrowItemEffect(byte[] actions, byte condition, float velocityMultiplier, float damage, float knockback,
			Effect effect) {
		super(actions, condition);
		this.velocityMultiplier = velocityMultiplier;
		this.damage = damage;
		this.knockback = knockback;
		this.effect = effect;
	}

	@Override
	public void onPlayerInteract(ServerPlayer serverPlayer, PlayerInteractEvent event) {
		ItemStack is;
		if (event.getHand() == EquipmentSlot.HAND)
			is = serverPlayer.player.getInventory().getItemInMainHand();
		else
			is = serverPlayer.player.getInventory().getItemInOffHand();
		throwItem(event.getPlayer(), is, velocityMultiplier, damage, knockback, effect);
		EffectHelper.decrementItemStack(serverPlayer, is, event.getHand(), event);
	}

	@Override
	public void onPlayerFish(ServerPlayer serverPlayer, PlayerFishEvent event) {
		ItemStack is = serverPlayer.lastRightClickedIS;
		throwItem(event.getPlayer(), is, velocityMultiplier, damage, knockback, effect);
		EffectHelper.decrementItemStack(serverPlayer, is, serverPlayer.lastRightClickedEquipmentSlot, event);
	}

	@Override
	public void onPlayerProjectileLaunch(ServerPlayer serverPlayer, ProjectileLaunchEvent event) {
		ItemStack is = serverPlayer.lastRightClickedIS;
		throwItem(serverPlayer.player, is, velocityMultiplier, damage, knockback, effect);
		EffectHelper.decrementItemStack(serverPlayer, is, serverPlayer.lastRightClickedEquipmentSlot, event);
	}

	public static void throwItem(LivingEntity entity, ItemStack itemStack, float velocityMultiplier, float damage,
			float knockback, Effect effect) {
		itemStack = itemStack.clone();
		itemStack.setAmount(1);
		LaunchItemEffect.launchItem(entity, itemStack, velocityMultiplier, damage, knockback, effect);
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		ThrowItemEffect throwItem = (ThrowItemEffect) effect;
		if (throwItem.velocityMultiplier != velocityMultiplier)
			return false;
		if (throwItem.damage != damage)
			return false;
		if (throwItem.knockback != knockback)
			return false;
		if (throwItem.effect != effect)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		velocityMultiplier = (float) section.getDouble("velocityMultiplier");
		damage = (float) section.getDouble("damage");
		knockback = (float) section.getDouble("knockback");
		effect = Effect.loadConfiguration(section);
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		ThrowItemEffect throwItem = (ThrowItemEffect) effect;
		if (throwItem.velocityMultiplier != 0)
			velocityMultiplier = throwItem.velocityMultiplier;
		if (throwItem.damage != 0)
			damage = throwItem.damage;
		if (throwItem.knockback != 0)
			knockback = throwItem.knockback;
		if (throwItem.effect != null)
			effect = throwItem.effect;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		velocityMultiplier = nbt.getFloat("velocityMultiplier");
		damage = nbt.getFloat("damage");
		knockback = nbt.getFloat("knockback");
		effect = Effect.loadNBT(nbt.getCompound("effect"));
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
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
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu,
						() -> new NumberMenu(BOLD + "Choose velocity"), () -> new NumberMenu(BOLD + "Choose damage"),
						() -> new NumberMenu(BOLD + "Choose knockback"), DoesUseEffectMenu::getMenu },
				new Consumer[] { this::setActions, this::setCondition,
						(velocityMultiplier) -> this.velocityMultiplier = ((Double) velocityMultiplier).floatValue(),
						(damage) -> this.damage = ((Double) damage).floatValue(),
						(knockback) -> this.knockback = ((Double) knockback).floatValue(),
						(effect) -> this.effect = (Effect) effect });
	}

	@Override
	public EffectType<? extends ThrowItemEffect> getType() {
		return EffectTypes.THROW_ITEM;
	}

	@Override
	public void setDefault() {
		super.setDefault();
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

	public void setEffect(Effect effect) {
		this.effect = effect;
	}

	public Effect getEffect() {
		return effect;
	}
}
