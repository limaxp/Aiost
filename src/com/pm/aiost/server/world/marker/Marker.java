package com.pm.aiost.server.world.marker;

import java.util.ArrayList;
import java.util.List;

public class Marker {

	private static final List<String> VALUES = new ArrayList<String>();

	public static final String SPAWN = a("Spawn");
	public static final String GOAL = a("Goal");

	public static String a(String name) {
		VALUES.add(name);
		return name;
	}

	public static void add(String name) {
		VALUES.add(name);
	}

	public static String get(int index) {
		return VALUES.get(index);
	}

	public static int size() {
		return VALUES.size();
	}
}
