package com.pm.aiost.item.spell.spells;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.item.spell.Spell;
import com.pm.aiost.misc.event.eventHandler.handler.ProjectileEventHandler;
import com.pm.aiost.misc.utils.ProjectileHelper;

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
		ProjectileEventHandler handler = new ProjectileEventHandler(entity);
		if (ProjectileHelper.launchProjectile(entity, AiostEntityTypes.PROJECTILE, new ItemStack(Material.AIR), 0.5F,
				power * 0.4F, 0.95F, handler, false)) {
			modifyProjectile(handler);
			entity.getWorld().playSound(entity.getLocation(), sound, SoundCategory.NEUTRAL, 0.5F,
					0.4F / (0.5F * 0.4F + 0.8F));
		}
	}

	public abstract void modifyProjectile(ProjectileEventHandler projectile);

	public float getPower() {
		return power;
	}

	public Sound getSound() {
		return sound;
	}
}
