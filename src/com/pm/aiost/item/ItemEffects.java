package com.pm.aiost.item;

import java.util.List;

import com.pm.aiost.effect.Effect;
import com.pm.aiost.effect.collection.EffectEntry;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class ItemEffects {

	private static final Int2ObjectMap<Effect[]> EFFECTS = new Int2ObjectOpenHashMap<Effect[]>();
	private static final Int2ObjectMap<EffectEntry> SELF_EFFECTS = new Int2ObjectOpenHashMap<EffectEntry>();

	public static int add(String name, Effect... effects) {
		int id = name.hashCode();
		add(id, effects);
		return id;
	}

	public static int addSelf(String name, Effect... selfEffects) {
		int id = name.hashCode();
		addSelf(id, selfEffects);
		return id;
	}

	public static int add(String name, Effect[] effects, Effect[] selfEffects) {
		int id = name.hashCode();
		add(id, effects, selfEffects);
		return id;
	}

	public static synchronized void add(int id, Effect... effects) {
		EFFECTS.put(id, effects);
	}

	public static synchronized void addSelf(int id, Effect... selfEffects) {
		SELF_EFFECTS.put(id, new EffectEntry(selfEffects));
	}

	public static synchronized void add(int id, Effect[] effects, Effect[] selfEffects) {
		add(id, effects);
		addSelf(id, selfEffects);
	}

	public static synchronized void remove(int id) {
		SELF_EFFECTS.remove(id);
		EFFECTS.remove(id);
	}

	public static Effect[] get(int id) {
		return EFFECTS.getOrDefault(id, EffectEntry.EMPTY_EFFECTS);
	}

	public static List<Effect> getSelf(int id, byte action) {
		return SELF_EFFECTS.getOrDefault(id, EffectEntry.EMPTY).getOrEmpty(action);
	}

	public static Effect[] getSelf(int id) {
		return SELF_EFFECTS.getOrDefault(id, EffectEntry.EMPTY).getEffects();
	}
}