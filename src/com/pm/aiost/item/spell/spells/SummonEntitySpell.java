package com.pm.aiost.item.spell.spells;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.item.spell.Spell;
import com.pm.aiost.misc.event.eventHandler.EventHandlerManager;
import com.pm.aiost.misc.event.eventHandler.handler.OwnableEventHandler;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

public class SummonEntitySpell extends Spell {

	protected final static int RANGE = 20;
	protected final static int DEFAULT_BOUND_TIME = 1200;

	private final EntityType<? extends Mob> entityType;
	private final int duration;

	public SummonEntitySpell(String name, int cooldown, double cost, EntityType<? extends Mob> entityType,
			int duration) {
		this(name, name, cooldown, cost, entityType, duration);
	}

	public SummonEntitySpell(String name, String displayName, int cooldown, double cost,
			EntityType<? extends Mob> entityType, int duration) {
		super(name, displayName, cooldown, cost);
		this.entityType = entityType;
		this.duration = duration;
	}

	@Override
	public void cast(LivingEntity entity, int i) {
		Location target = entity.getTargetBlock((Set<Material>) null, RANGE).getLocation().clone().add(0, 1, 0);
		if (target == null)
			return;

		spawnEntity(entity, target);
	}

	@Override
	public boolean cast(ServerPlayer serverPlayer, int i) {
		Location target = serverPlayer.player.getTargetBlock((Set<Material>) null, RANGE).getLocation().clone().add(0,
				1, 0);
		if (target == null)
			return false;

		spawnEntity(serverPlayer.player, target);
		return true;
	}

	protected void spawnEntity(LivingEntity owner, Location loc) {
		loc.getWorld().spawnParticle(Particle.PORTAL, loc, 20, 0.5F, 0.5F, 0.5F, 0.1F, null, false);
		LivingEntity ownable = NMS.from(AiostEntityTypes.spawnEntity(entityType, loc));
		OwnableEventHandler handler = new OwnableEventHandler(ownable, owner);
		EventHandlerManager.setEntityHandler(ownable, handler);
		handler.setDuration(duration);
		ownable.setGlowing(true);
	}

	public EntityType<? extends Mob> getEntityType() {
		return entityType;
	}

	public int getDuration() {
		return duration;
	}
}