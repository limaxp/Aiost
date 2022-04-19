package com.pm.aiost.misc.particleEffect.particle;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Particle.DustOptions;

public class RandomColorDustOptions extends DustOptions {
	private Random random = new Random();

	public RandomColorDustOptions(float size) {
		super(Color.WHITE, size);
	}

	@Override
	public Color getColor() {
		return Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}
}