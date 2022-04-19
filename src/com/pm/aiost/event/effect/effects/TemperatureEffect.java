package com.pm.aiost.event.effect.effects;

import java.util.Arrays;

import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.pm.aiost.event.effect.EffectAction;
import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.blueprints.SingletonEffect;
import com.pm.aiost.player.ServerPlayer;

public class TemperatureEffect extends SingletonEffect {

	public static final TemperatureEffect INSTANCE = new TemperatureEffect();

	private static final int OVERHEAT_TEMPERATURE = 40;
	private static final int FREEZE_TEMPERATURE = -20;

	protected TemperatureEffect() {
		super(EffectAction.TICK, EffectCondition.UNIQUE);
	}

	@Override
	public void onTick(ServerPlayer serverPlayer) {
		updateTemperature(serverPlayer);
	}

	public static void updateTemperature(ServerPlayer serverPlayer) {
		Player player = serverPlayer.player;
		Biome biome = player.getLocation().getBlock().getBiome();
		float newTemperature = serverPlayer.changeTemperature(BiomeTemperatures.get(biome));
		if (newTemperature > OVERHEAT_TEMPERATURE)
			overheatDamage(player);
		else if (newTemperature < FREEZE_TEMPERATURE)
			freezeDamage(player);
	}

	private static void overheatDamage(Player player) {
		if (player.getHealth() > 0.1) {
			player.damage(0.1);
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 1));
		} else {
			player.setHealth(0.0);
		}
	}

	private static void freezeDamage(Player player) {
		if (player.getHealth() > 0.1) {
			player.damage(0.1);
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 1));
		} else {
			player.setHealth(0.0);
		}
	}

	public static class BiomeTemperatures {

		private static final float[] BIOME_TEMPERATURES;

		static {
			BIOME_TEMPERATURES = new float[Biome.values().length];
			Arrays.fill(BIOME_TEMPERATURES, ServerPlayer.BASE_TEMPERATURE);

			set(Biome.DESERT_LAKES, 30);
			set(Biome.DESERT, 40);
			set(Biome.DESERT_HILLS, 50);

			set(Biome.FROZEN_RIVER, -20);
			set(Biome.FROZEN_OCEAN, -30);
			set(Biome.DEEP_FROZEN_OCEAN, -40);
		}

		public static void set(Biome biome, double value) {
			BIOME_TEMPERATURES[biome.ordinal()] = (float) value;
		}

		public static float get(Biome biome) {
			return BIOME_TEMPERATURES[biome.ordinal()];
		}
	}

	@Override
	public EffectType<? extends SingletonEffect> getType() {
		return null;
	}
}
