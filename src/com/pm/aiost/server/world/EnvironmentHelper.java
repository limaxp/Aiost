package com.pm.aiost.server.world;

import org.bukkit.Material;
import org.bukkit.World.Environment;

public class EnvironmentHelper {

	private static final String[] ENVIRONMENT_NAMES;

	static {
		Environment[] environments = Environment.values();
		int length = environments.length;
		ENVIRONMENT_NAMES = new String[length];
		for (int i = 0; i < length; i++) {
			String name = environments[i].name().replace("_", " ");
			name = name.substring(0, 1) + name.substring(1).toLowerCase();
			ENVIRONMENT_NAMES[i] = name;
		}
	}

	public static Material getMaterial(int index) {
		return getMaterial(Environment.values()[index]);
	}

	public static Material getMaterial(Environment environment) {
		switch (environment) {
		case NORMAL:
			return Material.GRASS_BLOCK;
		case NETHER:
			return Material.NETHERRACK;
		case THE_END:
			return Material.END_STONE;
		default:
			return Material.BARRIER;
		}
	}

	public static String getDisplayName(int index) {
		return ENVIRONMENT_NAMES[index];
	}

	public static String getDisplayName(Environment environment) {
		return getDisplayName(environment.ordinal());
	}
}
