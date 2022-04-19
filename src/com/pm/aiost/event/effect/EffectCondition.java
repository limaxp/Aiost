package com.pm.aiost.event.effect;

import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2ByteMap;

import com.pm.aiost.collection.list.FastArrayList;

public class EffectCondition {

	private static final Object2ByteMap<String> NAME_MAP = new Object2ByteLinkedOpenHashMap<String>();
	private static final FastArrayList<String> NAMES_LIST = new FastArrayList<String>();

	public static final byte NONE = a("none"); // no condition set
	public static final byte MAIN_HAND = a("main_hand"); // only allowed in main hand
	public static final byte OFF_HAND = a("off_hand"); // only allowed in off hand
	public static final byte UNIQUE = a("unique"); // can only be once in EffectData
	public static final byte SELF = a("self"); // effect belongs to holder object (will not get saved in EffectData!)

	public static byte a(String name) {
		return register(name);
	}

	public static byte register(String name) {
		byte id = (byte) NAMES_LIST.insert(name);
		NAME_MAP.put(name, id);
		return id;
	}

	public static byte get(String name) {
		return NAME_MAP.getOrDefault(name, NONE);
	}

	public static byte getIgnoreCase(String name) {
		return NAME_MAP.getOrDefault(name.toLowerCase(), NONE);
	}

	public static String getName(int id) {
		return NAMES_LIST.get(id);
	}

	public static int size() {
		return NAME_MAP.size();
	}
}
