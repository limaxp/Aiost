package com.pm.aiost.item.spell.spells;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.pm.aiost.entity.EntityHelper;
import com.pm.aiost.item.spell.Spell;
import com.pm.aiost.misc.nms.NMS;

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
//		Entity projectile = createProjectile(entity);
//		projectile.setGravity(false);
//		if (EntityHelper.launch(NMS.to(entity), NMS.to(projectile), 0.5F, power, 0.95F))
//			entity.getWorld().playSound(entity.getLocation(), sound, SoundCategory.NEUTRAL, 0.5F,
//					0.4F / (0.5F * 0.4F + 0.8F));
	}

//	public abstract Entity createProjectile(LivingEntity entity);

	public float getPower() {
		return power;
	}

	public Sound getSound() {
		return sound;
	}
}
