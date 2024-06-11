package com.pm.aiost.effect.effects;

import java.util.List;

import org.bukkit.ChatColor;

import com.pm.aiost.effect.Effect;
import com.pm.aiost.effect.EffectCondition;
import com.pm.aiost.effect.EffectType;

public class EmptyEffect extends Effect {

	private static final EffectType<EmptyEffect> TYPE = new EffectType<>("None", null);

	public EmptyEffect() {
		super(new byte[] {}, EffectCondition.NONE);
	}

	@Override
	public EffectType<? extends Effect> getType() {
		return TYPE;
	}

	@Override
	public void createDescription(List<String> list) {
		list.add(ChatColor.GRAY + "Type: " + ChatColor.DARK_GRAY + getType().displayName);
	}
}
