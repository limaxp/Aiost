package com.pm.aiost.event.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.ProjectileLaunchEvent;

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

public class SetProjectileStatsEffect extends Effect {

	private float velocityMultiplier;
	private int fireTicks;
	private boolean hasGravity;

	public SetProjectileStatsEffect() {
	}

	public SetProjectileStatsEffect(byte[] actions, byte condition, float velocityMultiplier, int fireTicks,
			boolean hasGravity) {
		super(actions, condition);
		this.velocityMultiplier = velocityMultiplier;
		this.fireTicks = fireTicks;
		this.hasGravity = hasGravity;
	}

	@Override
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		setProjectileStats(event.getEntity(), velocityMultiplier, fireTicks, hasGravity);
	}

	public static void setProjectileStats(Entity projectile, float velocityMultiplier, int fireTicks,
			boolean hasGravity) {
		projectile.setVelocity(projectile.getVelocity().multiply(velocityMultiplier));
		projectile.setFireTicks(fireTicks);
		projectile.setGravity(hasGravity);
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		SetProjectileStatsEffect setProjectileStats = (SetProjectileStatsEffect) effect;
		if (setProjectileStats.velocityMultiplier != velocityMultiplier)
			return false;
		if (setProjectileStats.fireTicks != fireTicks)
			return false;
		if (setProjectileStats.hasGravity != hasGravity)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection effectSection) {
		super.load(effectSection);
		velocityMultiplier = (float) effectSection.getDouble("velocityMultiplier");
		fireTicks = effectSection.getInt("fireTicks");
		hasGravity = effectSection.getBoolean("hasGravity");
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		SetProjectileStatsEffect setProjectileStats = (SetProjectileStatsEffect) effect;
		if (setProjectileStats.velocityMultiplier != 0)
			velocityMultiplier = setProjectileStats.velocityMultiplier;
		if (setProjectileStats.fireTicks != 0)
			fireTicks = setProjectileStats.fireTicks;
		if (setProjectileStats.hasGravity == true)
			hasGravity = true;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		velocityMultiplier = nbt.getFloat("velocityMultiplier");
		fireTicks = nbt.getInt("fireTicks");
		hasGravity = nbt.getBoolean("hasGravity");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setFloat("velocityMultiplier", velocityMultiplier);
		nbt.setInt("fireTicks", fireTicks);
		nbt.setBoolean("hasGravity", hasGravity);
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer, new Supplier[] { EffectActionMenu::new,
				EffectConditionMenu::getMenu, () -> new NumberMenu(BOLD + "Choose velocity multiplier"),
				() -> new NumberMenu(BOLD + "Choose fire ticks"), () -> BooleanMenu.create(BOLD + "Has gravity?") },
				new Consumer[] { this::setActions, this::setCondition,
						(velocityMultiplier) -> this.velocityMultiplier = ((Double) velocityMultiplier).floatValue(),
						(fireTicks) -> this.fireTicks = ((Double) fireTicks).intValue(),
						(hasGravity) -> this.hasGravity = (boolean) hasGravity });
	}

	@Override
	public EffectType<? extends SetProjectileStatsEffect> getType() {
		return EffectTypes.SET_PROJECTILE_STATS;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		velocityMultiplier = 1;
		fireTicks = 0;
		hasGravity = true;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Velocity multiplier: " + ChatColor.DARK_GRAY + velocityMultiplier);
		list.add(ChatColor.GRAY + "Fire ticks: " + ChatColor.DARK_GRAY + fireTicks);
		list.add(ChatColor.GRAY + "Has gravity: " + ChatColor.DARK_GRAY + hasGravity);
	}

	public float getVelocityMultiplier() {
		return velocityMultiplier;
	}

	public void setVelocityMultiplier(float velocityMultiplier) {
		this.velocityMultiplier = velocityMultiplier;
	}

	public int getFireTicks() {
		return fireTicks;
	}

	public void setFireTicks(int fireTicks) {
		this.fireTicks = fireTicks;
	}

	public boolean hasGravity() {
		return hasGravity;
	}

	public void setGravity(boolean hasGravity) {
		this.hasGravity = hasGravity;
	}
}
