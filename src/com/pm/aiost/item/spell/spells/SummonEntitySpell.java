package com.pm.aiost.item.spell.spells;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.ownable.OwnableEntity;
import com.pm.aiost.item.spell.Spell;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.server.v1_15_R1.EntityTypes;

public class SummonEntitySpell extends Spell {

	protected final static int RANGE = 20;
	protected final static int DEFAULT_BOUND_TIME = 1200;

	private final EntityTypes<? extends OwnableEntity> entityType;
	private final int duration;

	public SummonEntitySpell(String name, int cooldown, double cost, EntityTypes<? extends OwnableEntity> entityType,
			int duration) {
		this(name, name, cooldown, cost, entityType, duration);
	}

	public SummonEntitySpell(String name, String displayName, int cooldown, double cost,
			EntityTypes<? extends OwnableEntity> entityType, int duration) {
		super(name, displayName, cooldown, cost);
		this.entityType = entityType;
		this.duration = duration;
	}

	@Override
	public void cast(LivingEntity entity, int i) {
		Location target = entity.getTargetBlock((Set<Material>) null, RANGE).getLocation().clone().add(0, 1, 0);
		if (target != null) {
			OwnableEntity ownable = ((OwnableEntity) spawnEntity(target));
			ownable.setOwner(entity);
			ownable.setBoundTime(duration);
			ownable.getBukkitEntity().setGlowing(true);
		}
	}

	@Override
	public boolean cast(ServerPlayer serverPlayer, int i) {
		Location target = serverPlayer.player.getTargetBlock((Set<Material>) null, RANGE).getLocation().clone().add(0,
				1, 0);
		if (target != null) {
			OwnableEntity ownable = ((OwnableEntity) spawnEntity(target));
			ownable.setOwner(serverPlayer.player);
			ownable.setBoundTime(duration);
			ownable.getBukkitEntity().setGlowing(true);
			return true;
		}
		return false;
	}

	protected OwnableEntity spawnEntity(Location loc) {
		loc.getWorld().spawnParticle(Particle.PORTAL, loc, 20, 0.5F, 0.5F, 0.5F, 0.1F, null, false);
		return (OwnableEntity) AiostEntityTypes.spawnEntity(entityType, loc);
	}

	public EntityTypes<? extends OwnableEntity> getEntityType() {
		return entityType;
	}

	public int getDuration() {
		return duration;
	}
}