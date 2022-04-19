package com.pm.aiost.entity.mobEffect;

import java.util.Iterator;

import net.minecraft.server.v1_15_R1.IRegistry;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.MobEffectList;
import net.minecraft.server.v1_15_R1.MobEffects;

public class AiostMobEffects extends MobEffects {

	public static MobEffectList a(int id, String name, MobEffectList mobEffect) {
		return IRegistry.a(IRegistry.MOB_EFFECT, id, name, mobEffect);
	}

	public static MobEffectList get(String name) {
		return IRegistry.MOB_EFFECT.get(new MinecraftKey(name));
	}

	public static Iterator<MobEffectList> iterator() {
		return IRegistry.MOB_EFFECT.iterator();
	}
}
