package com.pm.aiost.item.spell.spells;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;

import com.pm.aiost.entity.projectile.CustomProjectile;
import com.pm.aiost.entity.projectile.ProjectileHelper;
import com.pm.aiost.item.spell.Spell;

public abstract class ProjectileSpell extends Spell {

	private final float power;
	private final Sound sound;

	public ProjectileSpell(String name, int cooldown, double cost, float power, Sound sound) {
		this(name, name, cooldown, cost, power, sound);
	}

	public ProjectileSpell(String name, String displayName, int cooldown, double cost, float power, Sound sound) {
		super(name, displayName, cooldown, cost);
		this.power = power;
		this.sound = sound;
	}

	@Override
	public void cast(LivingEntity entity, int i) {
		CustomProjectile projectile = createProjectile(entity);
		projectile.setNoGravity(true);
		if (ProjectileHelper.launchProjectile(entity, projectile, power))
			entity.getWorld().playSound(entity.getLocation(), sound, SoundCategory.NEUTRAL, 0.5F,
					0.4F / (0.5F * 0.4F + 0.8F));
	}

	public abstract CustomProjectile createProjectile(LivingEntity entity);

	public float getPower() {
		return power;
	}

	public Sound getSound() {
		return sound;
	}
}
