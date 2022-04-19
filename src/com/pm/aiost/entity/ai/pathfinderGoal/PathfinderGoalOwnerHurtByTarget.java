package com.pm.aiost.entity.ai.pathfinderGoal;

import java.util.EnumSet;

import org.bukkit.event.entity.EntityTargetEvent;

import com.pm.aiost.entity.ownable.OwnableInsentient;

import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.PathfinderGoal;
import net.minecraft.server.v1_15_R1.PathfinderGoalTarget;
import net.minecraft.server.v1_15_R1.PathfinderTargetCondition;

public class PathfinderGoalOwnerHurtByTarget extends PathfinderGoalTarget {

	private final OwnableInsentient a;
	private EntityLiving b;
	private int c;

	public PathfinderGoalOwnerHurtByTarget(OwnableInsentient ownableEntity) {
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
			this.b = entityliving.getLastDamager();
			int i = entityliving.cI();

			return (i != this.c && a(this.b, PathfinderTargetCondition.a));
		}

		return false;
	}

	public void c() {
		this.e.setGoalTarget(this.b, EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER, true);
		EntityLiving entityliving = this.a.getOwner();

		if (entityliving != null) {
			this.c = entityliving.cI();
		}

		super.c();
	}
}