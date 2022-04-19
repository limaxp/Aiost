package com.pm.aiost.event.effect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class EffectType<T extends Effect> {

	static final List<String> EMPTY_LORE = new ArrayList<String>(0);

	public final String name;
	public final String displayName;
	public final List<String> lore;
	public final Supplier<T> constructor;

	public EffectType(String name, Supplier<T> constructor) {
		this(name, name, EMPTY_LORE, constructor);
	}

	public EffectType(String name, List<String> lore, Supplier<T> constructor) {
		this(name, name, lore, constructor);
	}

	public EffectType(String name, String displayName, Supplier<T> constructor) {
		this(name, displayName, EMPTY_LORE, constructor);
	}

	public EffectType(String name, String displayName, List<String> lore, Supplier<T> constructor) {
		this.name = name;
		this.displayName = displayName;
		this.lore = Collections.unmodifiableList(lore);
		this.constructor = constructor;
	}

	public T create() {
		return constructor.get();
	}
}
