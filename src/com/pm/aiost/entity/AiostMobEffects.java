package com.pm.aiost.entity;

import java.util.Iterator;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

public class AiostMobEffects extends MobEffects {

	public static MobEffect a(String name, MobEffect mobEffect) {
		return Registry.register(BuiltInRegistries.MOB_EFFECT, name, mobEffect);
	}

	public static MobEffect get(String name) {
		return BuiltInRegistries.MOB_EFFECT.get(new ResourceLocation(name));
	}

	public static Iterator<MobEffect> iterator() {
		return BuiltInRegistries.MOB_EFFECT.iterator();
	}
}
