package com.pm.aiost.entity;

import org.bukkit.Location;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.player.Player;

public class SpecialEntity {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CaveSpider CaveSpider_AlwaysAttack(Location loc) {
		CaveSpider entity = AiostEntityTypes.spawnEntity(EntityType.CAVE_SPIDER, loc);
		entity.targetSelector.addGoal(2, (Goal) new NearestAttackableTargetGoal(entity, Player.class, true));
		return entity;
	}
}
