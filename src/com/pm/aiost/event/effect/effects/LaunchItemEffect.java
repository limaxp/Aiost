package com.pm.aiost.event.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.entity.projectile.CustomProjectile;
import com.pm.aiost.entity.projectile.ProjectileHelper;
import com.pm.aiost.entity.projectile.projectiles.ItemProjectile;
import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimpleLivingEntityEffect;
import com.pm.aiost.item.ItemLoader;
import com.pm.aiost.misc.menu.menus.request.CreateItemMenu;
import com.pm.aiost.misc.menu.menus.request.DoesUseEffectMenu;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompound;
import com.pm.aiost.player.ServerPlayer;

public class LaunchItemEffect extends SimpleLivingEntityEffect {

	protected ItemStack itemStack;
	protected float velocityMultiplier;
	protected float damage;
	protected float knockback;
	protected Effect effect;

	public LaunchItemEffect() {
	}

	public LaunchItemEffect(byte[] actions, byte condition, ItemStack is, float velocityMultiplier, float damage,
			float knockback) {
		this(actions, condition, is, velocityMultiplier, damage, knockback, Effect.EMPTY);
	}

	public LaunchItemEffect(byte[] actions, byte condition, ItemStack is, float velocityMultiplier, float damage,
			float knockback, Effect effect) {
		super(actions, condition);
		this.itemStack = is;
		this.velocityMultiplier = velocityMultiplier;
		this.damage = damage;
		this.knockback = knockback;
		this.effect = effect;
	}

	@Override
	public void runEffect(LivingEntity entity) {
		launchItem(entity, itemStack, velocityMultiplier, damage, knockback, effect);
	}

	public static ItemProjectile launchItem(LivingEntity entity, ItemStack itemStack, float velocityMultiplier,
			float damage, float knockback, Effect effect) {
		ItemProjectile projectile = new ItemProjectile(((CraftLivingEntity) entity).getHandle(),
				CraftItemStack.asNMSCopy(itemStack));
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
		LaunchItemEffect launchItem = (LaunchItemEffect) effect;
		if (!launchItem.itemStack.equals(itemStack))
			return false;
		if (launchItem.velocityMultiplier != velocityMultiplier)
			return false;
		if (launchItem.damage != damage)
			return false;
		if (launchItem.knockback != knockback)
			return false;
		if (launchItem.effect != effect)
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
		effect = Effect.loadConfiguration(section);
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		LaunchItemEffect launchItem = (LaunchItemEffect) effect;
		if (launchItem.itemStack != null)
			itemStack = launchItem.itemStack.clone();
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
		itemStack = NBTHelper.loadItem(nbt.getCompound("item"));
		velocityMultiplier = nbt.getFloat("velocityMultiplier");
		damage = nbt.getFloat("damage");
		knockback = nbt.getFloat("knockback");
		effect = Effect.loadNBT(nbt.getCompound("effect"));
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.set("item", NBTHelper.saveItem(new NBTCompound(), itemStack));
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
						() -> serverPlayer.getOrCreateMenu(CreateItemMenu.class, CreateItemMenu::new),
						() -> new NumberMenu(BOLD + "Choose velocity"), () -> new NumberMenu(BOLD + "Choose damage"),
						() -> new NumberMenu(BOLD + "Choose knockback"), DoesUseEffectMenu::getMenu },
				new Consumer[] { this::setActions, this::setCondition,
						(itemStack) -> this.itemStack = (ItemStack) itemStack,
						(velocityMultiplier) -> this.velocityMultiplier = ((Double) velocityMultiplier).floatValue(),
						(damage) -> this.damage = ((Double) damage).floatValue(),
						(knockback) -> this.knockback = ((Double) knockback).floatValue(),
						(effect) -> this.effect = (Effect) effect });
	}

	@Override
	public EffectType<? extends LaunchItemEffect> getType() {
		return EffectTypes.LAUNCH_ITEM;
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
