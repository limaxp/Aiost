package com.pm.aiost.event.effect;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.pm.aiost.event.effect.group.EffectGroup;

public class EffectRegistry {

	private static final Map<String, Supplier<? extends Effect>> CONSTRUCTORS = new HashMap<String, Supplier<? extends Effect>>();
	private static final Map<String, EffectGroup> GROUPS = new HashMap<String, EffectGroup>();

	public static synchronized void register(String name, Supplier<? extends Effect> constructor) {
		CONSTRUCTORS.put(name, constructor);
		EffectBuilder.initInstancesList(constructor);
	}

	public static synchronized void register(String name, EffectGroup group) {
		GROUPS.put(name, group);
	}

	public static synchronized void unregisterConstructor(String name) {
		Supplier<? extends Effect> constructor = CONSTRUCTORS.remove(name);
		if (constructor != null)
			EffectBuilder.removeInstancesList(constructor);
	}

	public static synchronized void unregisterGroup(String name) {
		GROUPS.remove(name);
	}

	public static Supplier<? extends Effect> getConstructor(String name) {
		return CONSTRUCTORS.get(name);
	}

	public static EffectGroup getGroup(String name) {
		return GROUPS.get(name);
	}
}
