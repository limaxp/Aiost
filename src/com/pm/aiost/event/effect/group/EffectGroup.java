package com.pm.aiost.event.effect.group;

import java.util.function.Supplier;

import org.bukkit.configuration.ConfigurationSection;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectBuilder;

public class EffectGroup {

	protected Supplier<? extends Effect>[] constructors;
	protected Effect[] effects;

	public EffectGroup(Supplier<? extends Effect>[] constructors, Effect[] effects) {
		this.constructors = constructors;
		this.effects = effects;
	}

	public Effect[] createEffects(ConfigurationSection effectSection) {
		int size = effects.length;
		Effect[] newEffects = new Effect[size];
		for (int i = 0; i < size; i++) {
			Supplier<? extends Effect> constructor = constructors[i];
			Effect newEffect = constructor.get();
			newEffect.load(effectSection);
			newEffect.load(effects[i]);
			newEffects[i] = EffectBuilder.registerEffect(constructor, newEffect);
		}
		return newEffects;
	}

	public Effect[] createEffects() {
		int size = effects.length;
		Effect[] newEffects = new Effect[size];
		for (int i = 0; i < size; i++) {
			Supplier<? extends Effect> constructor = constructors[i];
			Effect newEffect = constructor.get();
			newEffect.load(effects[i]);
			newEffects[i] = EffectBuilder.registerEffect(constructor, newEffect);
		}
		return newEffects;
	}
}
