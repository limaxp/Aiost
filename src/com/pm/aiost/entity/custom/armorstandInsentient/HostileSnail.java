package com.pm.aiost.entity.custom.armorstandInsentient;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.ai.pathfinderGoal.custom.CustomPathfinderGoalLookAtPlayer;
import com.pm.aiost.entity.ai.pathfinderGoal.custom.CustomPathfinderGoalMeleeAttack;
import com.pm.aiost.entity.ai.pathfinderGoal.custom.CustomPathfinderGoalNearestAttackableTarget;
import com.pm.aiost.entity.ai.pathfinderGoal.custom.CustomPathfinderGoalRandomLookaround;
import com.pm.aiost.entity.ai.pathfinderGoal.custom.CustomPathfinderGoalRandomStrollLand;

import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.World;

public class HostileSnail extends Snail {

	public HostileSnail(EntityTypes<? extends HostileSnail> entitytypes, World world) {
		super(entitytypes, world);
	}

	public HostileSnail(World world, double d0, double d1, double d2) {
		super(world, d0, d1, d2);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void initPathfinder() {
		this.goalSelector.a(2, new CustomPathfinderGoalMeleeAttack(this, 1.0D, true));
		this.goalSelector.a(3, new CustomPathfinderGoalRandomStrollLand(this, 1.0D));
		this.goalSelector.a(4, new CustomPathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new CustomPathfinderGoalRandomLookaround(this));
		this.targetSelector.a(1, new CustomPathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
	}

	@Override
	public EntityTypes<?> getCustomEntityType() {
		return AiostEntityTypes.HOSTILE_SNAIL;
	}
}
