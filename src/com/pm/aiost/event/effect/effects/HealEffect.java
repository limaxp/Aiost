package com.pm.aiost.event.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimpleLivingEntityEffect;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;

public class HealEffect extends SimpleLivingEntityEffect {

	private double health;

	public HealEffect() {
	}

	public HealEffect(byte[] actions, byte condition, double health) {
		super(actions, condition);
		this.health = health;
	}

	@Override
	public void runEffect(LivingEntity entity) {
		heal(entity, health);
	}

	public static void heal(LivingEntity entity, double health) {
		double calculatedHealth = entity.getHealth() + health;
		entity.setHealth(calculatedHealth > entity.getMaxHealth() ? entity.getMaxHealth() : calculatedHealth);
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		if (((HealEffect) effect).health != health)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection effectSection) {
		super.load(effectSection);
		health = effectSection.getDouble("health");
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		HealEffect healEffect = (HealEffect) effect;
		if (healEffect.health != 0)
			health = healEffect.health;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		health = nbt.getDouble("health");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setDouble("health", health);
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu,
						() -> new NumberMenu(BOLD + "Choose health value") },
				new Consumer[] { this::setActions, this::setCondition, (health) -> this.health = (Double) health });
	}

	@Override
	public EffectType<? extends HealEffect> getType() {
		return EffectTypes.HEAL;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		health = 1;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Health: " + ChatColor.DARK_GRAY + health);
	}

	public void setHealth(double health) {
		this.health = health;
	}

	public double getHealth() {
		return health;
	}
}