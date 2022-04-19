package com.pm.aiost.entity.ai.pathfinderGoal.custom;

import javax.annotation.Nullable;

import org.bukkit.event.entity.EntityTargetEvent;

import com.pm.aiost.entity.CustomInsentient;

import net.minecraft.server.v1_15_R1.AttributeInstance;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.GenericAttributes;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.PathEntity;
import net.minecraft.server.v1_15_R1.PathPoint;
import net.minecraft.server.v1_15_R1.PathfinderGoal;
import net.minecraft.server.v1_15_R1.PathfinderTargetCondition;
import net.minecraft.server.v1_15_R1.ScoreboardTeamBase;

public abstract class CustomPathfinderGoalTarget extends PathfinderGoal {

	protected final CustomInsentient e;
	protected final boolean f;
	private final boolean a;
	private int b;
	private int c;
	private int d;
	protected EntityLiving g;
	protected int h;

	public CustomPathfinderGoalTarget(CustomInsentient entityinsentient, boolean flag) {
		this(entityinsentient, flag, false);
	}

	public CustomPathfinderGoalTarget(CustomInsentient entityinsentient, boolean flag, boolean flag1) {
		this.h = 60;
		this.e = entityinsentient;
		this.f = flag;
		this.a = flag1;
	}

	public boolean b() {
		EntityLiving entityliving = this.e.getGoalTarget();

		if (entityliving == null) {
			entityliving = this.g;
		}

		if (entityliving == null)
			return false;
		if (!entityliving.isAlive()) {
			return false;
		}
		ScoreboardTeamBase scoreboardteambase = this.e.getScoreboardTeam();
		ScoreboardTeamBase scoreboardteambase1 = entityliving.getScoreboardTeam();

		if (scoreboardteambase != null && scoreboardteambase1 == scoreboardteambase) {
			return false;
		}
		double d0 = k();

		if (this.e.getEntity().h(entityliving) > d0 * d0) {
			return false;
		}
		if (this.f) {
			if (this.e.getEntitySenses().a(entityliving)) {
				this.d = 0;
			} else if (++this.d > this.h) {
				return false;
			}
		}

		if (entityliving instanceof EntityHuman && ((EntityHuman) entityliving).abilities.isInvulnerable) {
			return false;
		}
		this.e.setGoalTarget(entityliving, EntityTargetEvent.TargetReason.CLOSEST_ENTITY, true);
		return true;
	}

	protected double k() {
		AttributeInstance attributeinstance = this.e.getAttributeInstance(GenericAttributes.FOLLOW_RANGE);

		return (attributeinstance == null) ? 16.0D : attributeinstance.getValue();
	}

	public void c() {
		this.b = 0;
		this.c = 0;
		this.d = 0;
	}

	public void d() {
		this.e.setGoalTarget(null, EntityTargetEvent.TargetReason.FORGOT_TARGET, true);
		this.g = null;
	}

	protected boolean a(@Nullable EntityLiving entityliving, PathfinderTargetCondition pathfindertargetcondition) {
		if (entityliving == null)
			return false;
		if (!pathfindertargetcondition.a(this.e.getEntity(), entityliving))
			return false;
		if (!this.e.a(new BlockPosition(entityliving))) {
			return false;
		}
		if (this.a) {
			if (--this.c <= 0) {
				this.b = 0;
			}

			if (this.b == 0) {
				this.b = a(entityliving) ? 1 : 2;
			}

			if (this.b == 2) {
				return false;
			}
		}

		return true;
	}

	private boolean a(EntityLiving entityliving) {
		this.c = 10 + this.e.getRandom().nextInt(5);
		PathEntity pathentity = this.e.getNavigation().a(entityliving, 0);

		if (pathentity == null) {
			return false;
		}
		PathPoint pathpoint = pathentity.c();

		if (pathpoint == null) {
			return false;
		}
		int i = pathpoint.a - MathHelper.floor(entityliving.locX());
		int j = pathpoint.c - MathHelper.floor(entityliving.locZ());

		return ((i * i + j * j) <= 2.25D);
	}

	public CustomPathfinderGoalTarget a(int i) {
		this.h = i;
		return this;
	}
}
