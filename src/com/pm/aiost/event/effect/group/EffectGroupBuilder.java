package com.pm.aiost.event.effect.group;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.bukkit.configuration.ConfigurationSection;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectBuilder;
import com.pm.aiost.event.effect.EffectRegistry;
import com.pm.aiost.misc.log.Logger;

public class EffectGroupBuilder {

	protected List<Supplier<? extends Effect>> constructors;
	protected List<Effect> effects;

	public EffectGroupBuilder() {
		constructors = new ArrayList<Supplier<? extends Effect>>();
		effects = new ArrayList<Effect>();
	}

	public void loadEffectGroups(ConfigurationSection effectGroupsSection) {
		Logger.log("EffectGroupBuilder: Start loading Effects.yml...");

		for (String effectGroupName : effectGroupsSection.getKeys(false)) {
			loadEffectGroup(effectGroupsSection.getConfigurationSection(effectGroupName));
			EffectRegistry.register(effectGroupName, createEffectGroup());
			clear();
		}

		Logger.log("EffectGroupBuilder: Loading Effects.yml finished!");
	}

	public void loadEffectGroup(ConfigurationSection effectGroupSection) {
		Set<String> effectNames = effectGroupSection.getKeys(false);
		if (effectNames.size() < 1) {
			Logger.warn(
					"EffectGroupBuilder: No effects defined for effect group '" + effectGroupSection.getName() + "'");
			return;
		}

		for (String effectName : effectNames)
			addEffects(effectGroupSection.getConfigurationSection(effectName));
	}

	public void addEffects(ConfigurationSection effectSection) {
		String effectName = effectSection.getName();
		if (effectName.contains("_"))
			effectName = effectName.substring(0, effectName.lastIndexOf("_"));

		Supplier<? extends Effect> constructor = EffectRegistry.getConstructor(effectName);
		if (constructor != null)
			addEffect(constructor, effectSection);

		EffectGroup group = EffectRegistry.getGroup(effectName);
		if (group != null)
			addEffectGroup(group, effectSection);
	}

	public void addEffect(Supplier<? extends Effect> constructor, ConfigurationSection effectSection) {
		Effect effect = EffectBuilder.createEffect(constructor, effectSection);
		addEffect(constructor, effect);
	}

	public void addEffect(Supplier<? extends Effect> constructor, Effect effect) {
		constructors.add(constructor);
		this.effects.add(effect);
	}

	public void addEffectGroup(EffectGroup effectGroup, ConfigurationSection effectSection) {
		Supplier<? extends Effect>[] constructors = effectGroup.constructors;
		Effect[] effects = effectGroup.createEffects(effectSection);
		int length = constructors.length;
		for (int effectIndex = 0; effectIndex < length; effectIndex++) {
			this.constructors.add(constructors[effectIndex]);
			this.effects.add(effects[effectIndex]);
		}
	}

	@SuppressWarnings("unchecked")
	public EffectGroup createEffectGroup() {
		return new EffectGroup(constructors.toArray(new Supplier[constructors.size()]),
				effects.toArray(new Effect[effects.size()]));
	}

	public void clear() {
		constructors.clear();
		effects.clear();
	}
}
