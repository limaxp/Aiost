package com.pm.aiost.entity.ai.pathfinderGoal;

import java.util.EnumSet;

import org.bukkit.event.entity.EntityTargetEvent;

import com.pm.aiost.entity.ownable.OwnableInsentient;

import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.PathfinderGoal;
import net.minecraft.server.v1_15_R1.PathfinderGoalTarget;
import net.minecraft.server.v1_15_R1.PathfinderTargetCondition;

public class PathfinderGoalOwnerHurtTarget extends PathfinderGoalTarget {

	private final OwnableInsentient a;
	private EntityLiving b;
	private int c;

	public PathfinderGoalOwnerHurtTarget(OwnableInsentient ownableEntity) {
		super(ownableEntity.getEntity(), false);
		this.a = ownableEntity;
		a(EnumSet.of(PathfinderGoal.Type.TARGET));
	}

	public boolean a() {
		if (!this.a.isSitting()) {
			EntityLiving entityliving = this.a.getOwner();

			if (entityliving == null) {
				return false;
			}
			this.b = entityliving.cJ();
			int i = entityliving.cK();

			return (i != this.c && a(this.b, PathfinderTargetCondition.a));
		}

		return false;
	}

	public void c() {
		this.e.setGoalTarget(this.b, EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET, true);
		EntityLiving entityliving = this.a.getOwner();

		if (entityliving != null) {
			this.c = entityliving.cK();
		}

		super.c();
	}
}
