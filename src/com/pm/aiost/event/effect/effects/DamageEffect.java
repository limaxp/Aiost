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

public class DamageEffect extends SimpleLivingEntityEffect {

	private double damage;

	public DamageEffect() {
	}

	public DamageEffect(byte[] actions, byte condition, double damage) {
		super(actions, condition);
		this.damage = damage;
	}

	@Override
	public void runEffect(LivingEntity entity) {
		damage(entity, damage);
	}

	public static void damage(LivingEntity entity, double damage) {
		double calculatedHealth = entity.getHealth() - damage;
		entity.setHealth(calculatedHealth > 0 ? calculatedHealth : 0);
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		if (((DamageEffect) effect).damage != damage)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection effectSection) {
		super.load(effectSection);
		damage = effectSection.getDouble("damage");
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		DamageEffect damageEffect = (DamageEffect) effect;
		if (damageEffect.damage != 0)
			damage = damageEffect.damage;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		damage = nbt.getDouble("damage");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setDouble("damage", damage);
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu,
						() -> new NumberMenu(BOLD + "Choose damage value") },
				new Consumer[] { this::setActions, this::setCondition, (damage) -> this.damage = (Double) damage });
	}

	@Override
	public EffectType<? extends DamageEffect> getType() {
		return EffectTypes.DAMAGE;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		damage = 1;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Damage: " + ChatColor.DARK_GRAY + damage);
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public double getDamage() {
		return damage;
	}
}