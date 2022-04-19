package com.pm.aiost.event.effect;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.bukkit.configuration.ConfigurationSection;

import com.pm.aiost.event.effect.collection.EffectEntryBuilder;
import com.pm.aiost.event.effect.group.EffectGroup;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompoundWrapper;

import net.minecraft.server.v1_15_R1.NBTTagCompound;

public class EffectBuilder {

	private static final Map<Supplier<? extends Effect>, List<Effect>> INSTANCE_MAP = new IdentityHashMap<Supplier<? extends Effect>, List<Effect>>();

	public static void initInstancesList(Supplier<? extends Effect> constructor) {
		INSTANCE_MAP.put(constructor, new ArrayList<Effect>());
	}

	public static void removeInstancesList(Supplier<? extends Effect> constructor) {
		INSTANCE_MAP.remove(constructor);
	}

	public static EffectEntryBuilder loadEffects(ConfigurationSection effectsSection) {
		return loadEffects(new EffectEntryBuilder(), effectsSection);
	}

	public static EffectEntryBuilder loadEffects(EffectEntryBuilder entryBuilder, ConfigurationSection effectsSection) {
		for (String effectName : effectsSection.getKeys(false)) {
			Effect[] effects = createEffects(effectsSection.getConfigurationSection(effectName));
			entryBuilder.addAll(effects);
		}
		return entryBuilder;
	}

	public static Effect[] createEffects(String name) {
		name = stripName(name);
		Supplier<? extends Effect> constructor = EffectRegistry.getConstructor(name);
		if (constructor != null)
			return new Effect[] { createEffect(constructor) };

		EffectGroup group = EffectRegistry.getGroup(name);
		if (group != null)
			return group.createEffects();

		return null;
	}

	public static Effect[] createEffects(String name, ConfigurationSection effectSection) {
		name = stripName(name);
		Supplier<? extends Effect> constructor = EffectRegistry.getConstructor(name);
		if (constructor != null)
			return new Effect[] { createEffect(constructor, effectSection) };

		EffectGroup group = EffectRegistry.getGroup(name);
		if (group != null)
			return group.createEffects(effectSection);

		return null;
	}

	public static Effect[] createEffects(ConfigurationSection effectSection) {
		String name = stripName(effectSection.getName());
		Supplier<? extends Effect> constructor = EffectRegistry.getConstructor(name);
		if (constructor != null)
			return new Effect[] { createEffect(constructor, effectSection) };

		EffectGroup group = EffectRegistry.getGroup(name);
		if (group != null)
			return group.createEffects(effectSection);

		return null;
	}

	public static Effect createEffect(String name) {
		Supplier<? extends Effect> constructor = EffectRegistry.getConstructor(name);
		if (constructor != null)
			return createEffect(constructor);
		return Effect.EMPTY;
	}

	public static Effect createEffect(String name, ConfigurationSection effectSection) {
		Supplier<? extends Effect> constructor = EffectRegistry.getConstructor(name);
		if (constructor != null)
			return createEffect(constructor, effectSection);
		return Effect.EMPTY;
	}

	public static Effect createEffect(String name, INBTTagCompound nbt) {
		Supplier<? extends Effect> constructor = EffectRegistry.getConstructor(name);
		if (constructor != null)
			return createEffect(constructor, nbt);
		return Effect.EMPTY;
	}

	public static Effect createEffect(String name, NBTTagCompound nbt) {
		Supplier<? extends Effect> constructor = EffectRegistry.getConstructor(name);
		if (constructor != null)
			return createEffect(constructor, nbt);
		return Effect.EMPTY;
	}

	public static Effect createEffect(ConfigurationSection effectSection) {
		Supplier<? extends Effect> constructor = EffectRegistry.getConstructor(effectSection.getName());
		if (constructor != null)
			return createEffect(constructor, effectSection);
		return Effect.EMPTY;
	}

	public static Effect createEffect(INBTTagCompound nbt) {
		Supplier<? extends Effect> constructor = EffectRegistry.getConstructor(nbt.getString("effectId"));
		if (constructor != null)
			return createEffect(constructor, nbt);
		return Effect.EMPTY;
	}

	public static Effect createEffect(NBTTagCompound nbt) {
		Supplier<? extends Effect> constructor = EffectRegistry.getConstructor(nbt.getString("effectId"));
		if (constructor != null)
			return createEffect(constructor, nbt);
		return Effect.EMPTY;
	}

	public static Effect createEffect(Supplier<? extends Effect> constructor) {
		return registerEffect(constructor, constructor.get());
	}

	public static Effect createEffect(Supplier<? extends Effect> constructor, ConfigurationSection effectSection) {
		Effect newEffect = constructor.get();
		newEffect.load(effectSection);
		return registerEffect(constructor, newEffect);
	}

	public static Effect createEffect(Supplier<? extends Effect> constructor, INBTTagCompound nbt) {
		Effect newEffect = constructor.get();
		newEffect.load(nbt);
		return registerEffect(constructor, newEffect);
	}

	public static Effect createEffect(Supplier<? extends Effect> constructor, NBTTagCompound nbt) {
		Effect newEffect = constructor.get();
		newEffect.load(new NBTCompoundWrapper(nbt));
		return registerEffect(constructor, newEffect);
	}

	public static Effect registerEffect(Supplier<? extends Effect> constructor, Effect effect) {
		List<Effect> instanceList = INSTANCE_MAP.get(constructor);
		for (Effect buildEffect : instanceList) {
			if (effect.equals(buildEffect))
				return buildEffect;
		}

		registerEffect(instanceList, effect);
		return effect;
	}

	private static synchronized void registerEffect(List<Effect> instanceList, Effect effect) {
		if (instanceList.size() >= 40)
			instanceList.remove(0);
		instanceList.add(effect);
	}

	public static synchronized void unregisterEffect(Supplier<? extends Effect> constructor, Effect effect) {
		INSTANCE_MAP.get(constructor).remove(effect);
	}

	public static byte[] readActions(ConfigurationSection effectSection) {
		List<String> actionStrings = effectSection.getStringList("actions");
		int actionStringSize = actionStrings.size();
		if (actionStringSize > 0) {
			byte[] actions = new byte[actionStringSize];
			for (int j = 0; j < actionStringSize; j++)
				actions[j] = EffectAction.getIgnoreCase(actionStrings.get(j));
			return actions;
		}
		return null;
	}

	public static byte readCondition(ConfigurationSection effectSection) {
		String conditionName = effectSection.getString("condition");
		if (conditionName != null)
			return EffectCondition.getIgnoreCase(conditionName);
		return EffectCondition.NONE;
	}

	private static String stripName(String name) {
		if (name.contains("_"))
			name = name.substring(0, name.lastIndexOf("_"));
		return name;
	}
}