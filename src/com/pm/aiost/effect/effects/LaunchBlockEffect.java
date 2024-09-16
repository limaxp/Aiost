package com.pm.aiost.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.effect.Effect;
import com.pm.aiost.effect.EffectType;
import com.pm.aiost.effect.EffectTypes;
import com.pm.aiost.effect.blueprints.SimpleLivingEntityEffect;
import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.event.eventHandler.handler.ProjectileEventHandler;
import com.pm.aiost.misc.menu.menus.request.CreateItemMenu;
import com.pm.aiost.misc.menu.menus.request.DoesUseEffectMenu;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest;
import com.pm.aiost.misc.utils.ProjectileHelper;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.nbt.CompoundTag;

public class LaunchBlockEffect extends SimpleLivingEntityEffect {

	private BlockData blockData;
	private float velocityMultiplier;
	private float damage;
	private float knockback;
	private Effect effect;

	public LaunchBlockEffect() {
	}

	public LaunchBlockEffect(byte[] actions, byte condition, Block block, float velocityMultiplier, float damage,
			float knockback) {
		this(actions, condition, block.getBlockData(), velocityMultiplier, damage, knockback, Effect.EMPTY);
	}

	public LaunchBlockEffect(byte[] actions, byte condition, BlockData blockData, float velocityMultiplier,
			float damage, float knockback) {
		this(actions, condition, blockData, velocityMultiplier, damage, knockback, Effect.EMPTY);
	}

	public LaunchBlockEffect(byte[] actions, byte condition, Block block, float velocityMultiplier, float damage,
			float knockback, Effect effect) {
		this(actions, condition, block.getBlockData(), velocityMultiplier, damage, knockback, effect);
	}

	public LaunchBlockEffect(byte[] actions, byte condition, BlockData blockData, float velocityMultiplier,
			float damage, float knockback, Effect effect) {
		super(actions, condition);
		this.blockData = blockData;
		this.velocityMultiplier = velocityMultiplier;
		this.damage = damage;
		this.knockback = knockback;
		this.effect = effect;
	}

	@Override
	public void runEffect(LivingEntity entity) {
		ProjectileEventHandler handler = new ProjectileEventHandler(entity);
		if (ProjectileHelper.launchProjectile(entity, AiostEntityTypes.BLOCK_PROJECTILE, 0.5F,
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
		LaunchBlockEffect launchBlock = (LaunchBlockEffect) effect;
		if (!launchBlock.blockData.equals(blockData))
			return false;
		if (launchBlock.velocityMultiplier != velocityMultiplier)
			return false;
		if (launchBlock.damage != damage)
			return false;
		if (launchBlock.knockback != knockback)
			return false;
		if (launchBlock.effect != effect)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		blockData = Material.valueOf(section.getString("block")).createBlockData();
		velocityMultiplier = (float) section.getDouble("velocityMultiplier");
		damage = (float) section.getDouble("damage");
		knockback = (float) section.getDouble("knockback");
		effect = Effect.loadConfiguration(section);
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		LaunchBlockEffect launchBlock = (LaunchBlockEffect) effect;
		if (launchBlock.blockData != null)
			blockData = launchBlock.blockData;
		if (launchBlock.velocityMultiplier != 0)
			velocityMultiplier = launchBlock.velocityMultiplier;
		if (launchBlock.damage != 0)
			damage = launchBlock.damage;
		if (launchBlock.knockback != 0)
			knockback = launchBlock.knockback;
		if (launchBlock.effect != null)
			effect = launchBlock.effect;
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		blockData = Material.valueOf(nbt.getString("block")).createBlockData();
		velocityMultiplier = nbt.getFloat("velocityMultiplier");
		damage = nbt.getFloat("damage");
		knockback = nbt.getFloat("knockback");
		effect = Effect.loadNBT(nbt.getCompound("effect"));
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		super.save(nbt);
		nbt.putString("block", blockData.getMaterial().toString());
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
		return new MultiMenuRequest(false, requestConsumer, targetConsumer,
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu,
						() -> serverPlayer.getOrCreateMenu(CreateItemMenu.class, CreateItemMenu::new),
						() -> new NumberMenu(BOLD + "Choose velocity"), () -> new NumberMenu(BOLD + "Choose damage"),
						() -> new NumberMenu(BOLD + "Choose knockback"), DoesUseEffectMenu::getMenu },
				new Consumer[] { this::setActions, this::setCondition,
						(itemStack) -> this.blockData = ((ItemStack) itemStack).getType().createBlockData(),
						(velocityMultiplier) -> this.velocityMultiplier = ((Double) velocityMultiplier).floatValue(),
						(damage) -> this.damage = ((Double) damage).floatValue(),
						(knockback) -> this.knockback = ((Double) knockback).floatValue(),
						(effect) -> this.effect = (Effect) effect });
	}

	@Override
	public EffectType<? extends LaunchBlockEffect> getType() {
		return EffectTypes.LAUNCH_BLOCK;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		setBlock(Material.STONE);
		velocityMultiplier = 1;
		damage = 1;
		knockback = 1;
		effect = Effect.EMPTY;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Block: " + ChatColor.DARK_GRAY + blockData.getMaterial());
		list.add(ChatColor.GRAY + "Velocity: " + ChatColor.DARK_GRAY + velocityMultiplier);
		list.add(ChatColor.GRAY + "Damage: " + ChatColor.DARK_GRAY + damage);
		list.add(ChatColor.GRAY + "Knockback: " + ChatColor.DARK_GRAY + knockback);
		list.add(null);
		list.add(ChatColor.GRAY + "Effect:");
		effect.createDescription(list);
	}

	public void setBlock(Material material) {
		this.blockData = material.createBlockData();
	}

	public void setBlock(Block block) {
		this.blockData = block.getBlockData();
	}

	public void setBlockData(BlockData blockData) {
		this.blockData = blockData;
	}

	public BlockData getBlockData() {
		return blockData;
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
