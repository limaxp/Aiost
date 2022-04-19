package com.pm.aiost.event.effect.effects;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.block.BlockPlaceEvent;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectAction;
import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;

public class PlaceEffectBlockEffect extends Effect {

	private static final byte[] ACTIONS = new byte[] { EffectAction.BLOCK_PLACE };

	private int effectId;

	public PlaceEffectBlockEffect(int effectId) {
		super(ACTIONS, EffectCondition.NONE);
		this.effectId = effectId;
	}

	@Override
	public void onBlockPlace(ServerPlayer serverPlayer, BlockPlaceEvent event) {
		serverPlayer.getServerWorld().setEffect(event.getBlock().getLocation(), effectId);
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		PlaceEffectBlockEffect placeEffectBlock = (PlaceEffectBlockEffect) effect;
		if (placeEffectBlock.effectId != effectId)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection effectSection) {
		super.load(effectSection);
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		PlaceEffectBlockEffect placeEffectBlock = (PlaceEffectBlockEffect) effect;
		if (placeEffectBlock.effectId != 0)
			effectId = placeEffectBlock.effectId;
	}

	@Override
	public void load(INBTTagCompound nbt) {
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		return nbt;
	}

	@Override
	public EffectType<? extends PlaceEffectBlockEffect> getType() {
		return null;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		effectId = -1;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Effect id: " + ChatColor.DARK_GRAY + effectId);
	}
}
