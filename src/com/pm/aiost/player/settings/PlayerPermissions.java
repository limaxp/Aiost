package com.pm.aiost.player.settings;

import java.util.List;

import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class PlayerPermissions {

	private static final Object2IntMap<String> NAME_MAP = new Object2IntOpenHashMap<String>();
	private static int runningId;

	public static int a(String name) {
		int id = runningId;
		register(name);
		return id;
	}

	public static void register(String name) {
		NAME_MAP.put(name.toLowerCase(), runningId++);
	}

	public static void register(List<String> permissions) {
		for (int i = 0; i < permissions.size(); i++)
			register(permissions.get(i));
	}

	public static int get(String name) {
		return NAME_MAP.getInt(name);
	}

	public static int getIgnoreCase(String name) {
		return NAME_MAP.getInt(name.toLowerCase());
	}

	public static int size() {
		return runningId;
	}
}