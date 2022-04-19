package com.pm.aiost.item.spell.spells;

import org.bukkit.entity.LivingEntity;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.item.spell.Spell;
import com.pm.aiost.player.ServerPlayer;

public class EffectSpell extends Spell {

	public static final int DEFAULT_DURATION = 400;

	private final Effect effect;
	private final int duration;

	public EffectSpell(String name, int cooldown, double cost, int duration, Effect effect) {
		this(name, name, cooldown, cost, duration, effect);
	}

	public EffectSpell(String name, String displayName, int cooldown, double cost, int duration, Effect effect) {
		super(name, displayName, cooldown, cost);
		this.duration = duration;
		this.effect = effect;
	}

	@Override
	public void cast(LivingEntity entity, int i) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean cast(ServerPlayer serverPlayer, int i) {
		return serverPlayer.addEffect(duration, effect);
	}

	public Effect getEffect() {
		return effect;
	}

	public int getDuration() {
		return duration;
	}
}