package com.pm.aiost.item.spell.spells;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.EntityHelper;
import com.pm.aiost.entity.entities.EntityProjectile;
import com.pm.aiost.item.spell.Spell;
import com.pm.aiost.misc.event.eventHandler.EventHandlerManager;
import com.pm.aiost.misc.event.eventHandler.handler.ProjectileEventHandler;
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
		net.minecraft.world.entity.LivingEntity source = NMS.to(entity);
		EntityProjectile projectile = new EntityProjectile(source.level(), source.getX(),
				source.getY() + source.getEyeHeight() - 0.10000000149011612D, source.getZ());
		projectile.setNoGravity(true);
		AiostEntityTypes.spawnEntity(projectile);
		if (EntityHelper.launch(source, projectile, 0.5F, power, 0.95F)) {
			entity.getWorld().playSound(entity.getLocation(), sound, SoundCategory.NEUTRAL, 0.5F,
					0.4F / (0.5F * 0.4F + 0.8F));
			ProjectileEventHandler handler = new ProjectileEventHandler(entity);
			modifyProjectile(handler);
			projectile.setProjectileHandler(handler);
			EventHandlerManager.setEntityHandler(NMS.from(projectile), handler);
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
