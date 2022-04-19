package com.pm.aiost.event.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimpleEntityEffect;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;

public class SetOnFireEffect extends SimpleEntityEffect {

	private int fireTicks;

	public SetOnFireEffect() {
	}

	public SetOnFireEffect(byte[] actions, byte condition, int fireTicks) {
		super(actions, condition);
		this.fireTicks = fireTicks;
	}

	@Override
	public void runEffect(Entity entity) {
		entity.setFireTicks(fireTicks);
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		if (((SetOnFireEffect) effect).fireTicks != fireTicks)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection effectSection) {
		super.load(effectSection);
		fireTicks = effectSection.getInt("fireTicks");
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		SetOnFireEffect setOnFireEffect = (SetOnFireEffect) effect;
		if (setOnFireEffect.fireTicks != 0)
			fireTicks = setOnFireEffect.fireTicks;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		fireTicks = nbt.getInt("fireTicks");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setDouble("fireTicks", fireTicks);
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu,
						() -> new NumberMenu(BOLD + "Choose fire ticks") },
				new Consumer[] { this::setActions, this::setCondition,
						(fireTicks) -> this.fireTicks = ((Double) fireTicks).intValue() });
	}

	@Override
	public EffectType<? extends SetOnFireEffect> getType() {
		return EffectTypes.SET_ON_FIRE;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		fireTicks = 100;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Fire ticks: " + ChatColor.DARK_GRAY + fireTicks);
	}

	public void setFireTicks(int fireTicks) {
		this.fireTicks = fireTicks;
	}

	public int getFireTicks() {
		return fireTicks;
	}
}