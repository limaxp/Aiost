package com.pm.aiost.event.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimplePlayerEffect;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;

public class ManaRegenEffect extends SimplePlayerEffect {

	private double mana;

	public ManaRegenEffect() {
	}

	public ManaRegenEffect(byte[] actions, byte condition, double mana) {
		super(actions, condition);
		this.mana = mana;
	}

	@Override
	public void runEffect(ServerPlayer serverPlayer) {
		serverPlayer.addMana(mana);
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		if (((ManaRegenEffect) effect).mana != mana)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection effectSection) {
		super.load(effectSection);
		mana = effectSection.getDouble("mana");
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		ManaRegenEffect manaRegen = (ManaRegenEffect) effect;
		if (manaRegen.mana != 0)
			mana = manaRegen.mana;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		mana = nbt.getDouble("mana");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setDouble("mana", mana);
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu,
						() -> new NumberMenu(BOLD + "Choose mana value") },
				new Consumer[] { this::setActions, this::setCondition, (mana) -> this.mana = (Double) mana });
	}

	@Override
	public EffectType<? extends ManaRegenEffect> getType() {
		return EffectTypes.MANA_REGEN;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		mana = 1;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Mana: " + ChatColor.DARK_GRAY + mana);
	}

	public void setMana(double mana) {
		this.mana = mana;
	}

	public double getMana() {
		return mana;
	}
}
