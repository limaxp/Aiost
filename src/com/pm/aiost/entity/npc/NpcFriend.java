package com.pm.aiost.entity.npc;

import com.mojang.authlib.GameProfile;
import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.ai.pathfinderGoal.custom.CustomPathfinderGoalLookAtPlayer;
import com.pm.aiost.entity.ai.pathfinderGoal.custom.CustomPathfinderGoalRandomLookaround;
import com.pm.aiost.entity.ai.pathfinderGoal.custom.CustomPathfinderGoalRandomStrollLand;

import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.World;

public class NpcFriend extends NpcInsentient {

	public NpcFriend(EntityTypes<? extends NpcFriend> entitytypes, World world) {
		super(world);
	}

	public NpcFriend(World world) {
		super(world);
	}

	public NpcFriend(World world, GameProfile profile) {
		super(world, profile);
	}

	protected void initPathfinder() {
		this.goalSelector.a(2, new CustomPathfinderGoalRandomStrollLand(this, 1.0D));
		this.goalSelector.a(3, new CustomPathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(3, new CustomPathfinderGoalRandomLookaround(this));
	}

	@Override
	public EntityTypes<?> getCustomEntityType() {
		return AiostEntityTypes.NPC;
	}
}
