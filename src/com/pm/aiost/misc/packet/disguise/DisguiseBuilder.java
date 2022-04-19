package com.pm.aiost.misc.packet.disguise;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import com.google.common.base.Supplier;
import com.pm.aiost.misc.packet.disguise.disguises.DisguiseBlock;
import com.pm.aiost.misc.packet.disguise.disguises.DisguiseEntityLiving;
import com.pm.aiost.misc.packet.disguise.disguises.DisguiseFurniture;
import com.pm.aiost.misc.packet.disguise.disguises.DisguisePlayer;

public class DisguiseBuilder {

	private static final Map<String, Supplier<Disguise>> NAME_MAP = new HashMap<String, Supplier<Disguise>>();

	static {
		register("entity_living", DisguiseEntityLiving::new);
		register("falling_block", DisguiseBlock::new);
		register("furniture", DisguiseFurniture::new);
		register("player", DisguisePlayer::new);
	}

	public static void register(String name, Supplier<Disguise> constructor) {
		NAME_MAP.put(name.toUpperCase(), constructor);
	}

	public static Disguise create(String name) {
		return NAME_MAP.get(name.toUpperCase()).get();
	}

	public static Disguise create(ConfigurationSection section) {
		Disguise disguise = create(section.getString("type"));
		disguise.load(section);
		return disguise;
	}
}
