package com.pm.aiost.item.spell.spells;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;

import com.pm.aiost.item.spell.Spell;

public class HealSpell extends Spell {

	private final double health;

	public HealSpell(String name, int cooldown, double cost, double health) {
		this(name, name, cooldown, cost, health);
	}

	public HealSpell(String name, String displayName, int cooldown, double cost, double health) {
		super(name, displayName, cooldown, cost);
		this.health = health;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void cast(LivingEntity entity, int i) {
		double finalHealth = entity.getHealth() + health;
		entity.setHealth(finalHealth > entity.getMaxHealth() ? entity.getMaxHealth() : finalHealth);
		Location loc = entity.getLocation();
		loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 20, 0.5F, 0.5F, 0.5F, 0.1F, null, false);
	}
}
