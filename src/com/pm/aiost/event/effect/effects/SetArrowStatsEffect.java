package com.pm.aiost.event.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.EntityShootBowEvent;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.misc.menu.menus.request.BooleanMenu;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;

public class SetArrowStatsEffect extends Effect {

	private float velocityMultiplier;
	private float extraDamage;
	private int knockbackStrength;
	private int fireTicks;
	private boolean mustCrit;
	private boolean doesBounce;
	private boolean hasGravity;

	public SetArrowStatsEffect() {
	}

	public SetArrowStatsEffect(byte[] actions, byte condition, float velocityMultiplier, float extraDamage,
			int knockbackStrength, int fireTicks, boolean mustCrit, boolean doesBounce, boolean hasGravity) {
		super(actions, condition);
		this.velocityMultiplier = velocityMultiplier;
		this.extraDamage = extraDamage;
		this.knockbackStrength = knockbackStrength;
		this.fireTicks = fireTicks;
		this.mustCrit = mustCrit;
		this.doesBounce = doesBounce;
		this.hasGravity = hasGravity;
	}

	@Override
	public void onEntityShootBow(EntityShootBowEvent event) {
		setArrowStats((Arrow) event.getProjectile(), velocityMultiplier, extraDamage, knockbackStrength, fireTicks,
				mustCrit, doesBounce, hasGravity);
	}

	public static void setArrowStats(Arrow projectile, float velocityMultiplier, float extraDamage,
			int knockbackStrength, int fireTicks, boolean mustCrit, boolean doesBounce, boolean hasGravity) {
		projectile.setVelocity(projectile.getVelocity().multiply(velocityMultiplier));
		projectile.setDamage(projectile.getDamage() + extraDamage);
		projectile.setKnockbackStrength(knockbackStrength);
		projectile.setFireTicks(fireTicks);
		if (mustCrit)
			projectile.setCritical(true);
		projectile.setBounce(doesBounce);
		projectile.setGravity(hasGravity);
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		SetArrowStatsEffect setArrowStats = (SetArrowStatsEffect) effect;
		if (setArrowStats.velocityMultiplier != velocityMultiplier)
			return false;
		if (setArrowStats.extraDamage != extraDamage)
			return false;
		if (setArrowStats.knockbackStrength != knockbackStrength)
			return false;
		if (setArrowStats.fireTicks != fireTicks)
			return false;
		if (setArrowStats.mustCrit != mustCrit)
			return false;
		if (setArrowStats.doesBounce != doesBounce)
			return false;
		if (setArrowStats.hasGravity != hasGravity)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection effectSection) {
		super.load(effectSection);
		velocityMultiplier = (float) effectSection.getDouble("velocityMultiplier");
		extraDamage = (float) effectSection.getDouble("extraDamage");
		knockbackStrength = effectSection.getInt("knockbackStrength");
		fireTicks = effectSection.getInt("fireTicks");
		mustCrit = effectSection.getBoolean("mustCrit");
		doesBounce = effectSection.getBoolean("doesBounce");
		hasGravity = effectSection.getBoolean("hasGravity");
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		SetArrowStatsEffect setArrowStats = (SetArrowStatsEffect) effect;
		if (setArrowStats.velocityMultiplier != 0)
			velocityMultiplier = setArrowStats.velocityMultiplier;
		if (setArrowStats.extraDamage != 0)
			extraDamage = setArrowStats.extraDamage;
		if (setArrowStats.knockbackStrength != 0)
			knockbackStrength = setArrowStats.knockbackStrength;
		if (setArrowStats.fireTicks != 0)
			fireTicks = setArrowStats.fireTicks;
		if (setArrowStats.mustCrit == true)
			mustCrit = true;
		if (setArrowStats.doesBounce == true)
			doesBounce = true;
		if (setArrowStats.hasGravity == true)
			hasGravity = true;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		velocityMultiplier = nbt.getFloat("velocityMultiplier");
		extraDamage = nbt.getFloat("extraDamage");
		knockbackStrength = nbt.getInt("knockbackStrength");
		fireTicks = nbt.getInt("fireTicks");
		mustCrit = nbt.getBoolean("mustCrit");
		doesBounce = nbt.getBoolean("doesBounce");
		hasGravity = nbt.getBoolean("hasGravity");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setFloat("velocityMultiplier", velocityMultiplier);
		nbt.setFloat("extraDamage", extraDamage);
		nbt.setInt("knockbackStrength", knockbackStrength);
		nbt.setInt("fireTicks", fireTicks);
		nbt.setBoolean("mustCrit", mustCrit);
		nbt.setBoolean("doesBounce", doesBounce);
		nbt.setBoolean("hasGravity", hasGravity);
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer, new Supplier[] { EffectActionMenu::new,
				EffectConditionMenu::getMenu, () -> new NumberMenu(BOLD + "Choose velocity multiplier"),
				() -> new NumberMenu(BOLD + "Choose extra Damage"),
				() -> new NumberMenu(BOLD + "Choose knockback strength"),
				() -> new NumberMenu(BOLD + "Choose fire ticks"), () -> BooleanMenu.create(BOLD + "Must crit?"),
				() -> BooleanMenu.create(BOLD + "Does bounce?"), () -> BooleanMenu.create(BOLD + "Has gravity?") },
				new Consumer[] { this::setActions, this::setCondition,
						(velocityMultiplier) -> this.velocityMultiplier = ((Double) velocityMultiplier).floatValue(),
						(extraDamage) -> this.extraDamage = ((Double) extraDamage).floatValue(),
						(knockbackStrength) -> this.knockbackStrength = ((Double) knockbackStrength).intValue(),
						(fireTicks) -> this.fireTicks = ((Double) fireTicks).intValue(),
						(mustCrit) -> this.mustCrit = (boolean) mustCrit,
						(doesBounce) -> this.doesBounce = (boolean) doesBounce,
						(hasGravity) -> this.hasGravity = (boolean) hasGravity });
	}

	@Override
	public EffectType<? extends SetArrowStatsEffect> getType() {
		return EffectTypes.SET_ARROW_STATS;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		velocityMultiplier = 1;
		extraDamage = 0;
		knockbackStrength = 1;
		fireTicks = 0;
		mustCrit = false;
		doesBounce = false;
		hasGravity = true;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Velocity multiplier: " + ChatColor.DARK_GRAY + velocityMultiplier);
		list.add(ChatColor.GRAY + "damage: " + ChatColor.DARK_GRAY + extraDamage);
		list.add(ChatColor.GRAY + "Knockback strength: " + ChatColor.DARK_GRAY + knockbackStrength);
		list.add(ChatColor.GRAY + "Fire ticks: " + ChatColor.DARK_GRAY + fireTicks);
		list.add(ChatColor.GRAY + "Must crit: " + ChatColor.DARK_GRAY + mustCrit);
		list.add(ChatColor.GRAY + "Does bounce: " + ChatColor.DARK_GRAY + doesBounce);
		list.add(ChatColor.GRAY + "Has gravity: " + ChatColor.DARK_GRAY + hasGravity);
	}

	public float getVelocityMultiplier() {
		return velocityMultiplier;
	}

	public void setVelocityMultiplier(float velocityMultiplier) {
		this.velocityMultiplier = velocityMultiplier;
	}

	public float getExtraDamage() {
		return extraDamage;
	}

	public void setExtraDamage(float extraDamage) {
		this.extraDamage = extraDamage;
	}

	public int getKnockbackStrength() {
		return knockbackStrength;
	}

	public void setKnockbackStrength(int knockbackStrength) {
		this.knockbackStrength = knockbackStrength;
	}

	public int getFireTicks() {
		return fireTicks;
	}

	public void setFireTicks(int fireTicks) {
		this.fireTicks = fireTicks;
	}

	public boolean mustCrit() {
		return mustCrit;
	}

	public void setMustCrit(boolean mustCrit) {
		this.mustCrit = mustCrit;
	}

	public boolean doesBounce() {
		return doesBounce;
	}

	public void setBounce(boolean doesBounce) {
		this.doesBounce = doesBounce;
	}

	public boolean hasGravity() {
		return hasGravity;
	}

	public void setGravity(boolean hasGravity) {
		this.hasGravity = hasGravity;
	}
}
