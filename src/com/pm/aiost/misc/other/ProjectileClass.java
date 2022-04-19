package com.pm.aiost.misc.other;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Trident;
import org.bukkit.entity.WitherSkull;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ProjectileClass {

	private static final List<Class<? extends Projectile>> PROJECTILE_CLASSES = new ArrayList<Class<? extends Projectile>>();
	private static final BiMap<String, Class<? extends Projectile>> NAME_MAP = HashBiMap.create();

	static {
		register("arrow", Arrow.class);
		register("dragon_fireball", DragonFireball.class);
		register("egg", Egg.class);
		register("ender_pearl", EnderPearl.class);
		register("fireball", Fireball.class);
		register("fish_hook", FishHook.class);
		register("large_fireball", LargeFireball.class);
		register("llama_spit", LlamaSpit.class);
		register("shulker_bullet", ShulkerBullet.class);
		register("small_fireball", SmallFireball.class);
		register("snowball", Snowball.class);
		register("spectral_arrow", SpectralArrow.class);
		register("thrown_exp_bottle", ThrownExpBottle.class);
		register("thrown_potion", ThrownPotion.class);
		register("trident", Trident.class);
		register("wither_skull", WitherSkull.class);
	}

	private static synchronized void register(String name, Class<? extends Projectile> clazz) {
		PROJECTILE_CLASSES.add(clazz);
		NAME_MAP.put(name, clazz);
	}

	public static Class<? extends Projectile> get(String name) {
		return NAME_MAP.get(name);
	}

	public static Class<? extends Projectile> get(int index) {
		return PROJECTILE_CLASSES.get(index);
	}

	public static String getName(Class<? extends Projectile> clazz) {
		return NAME_MAP.inverse().get(clazz);
	}

	public static Set<Entry<String, Class<? extends Projectile>>> getEntries() {
		return NAME_MAP.entrySet();
	}

	public static int size() {
		return PROJECTILE_CLASSES.size();
	}
}