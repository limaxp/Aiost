package com.pm.aiost.entity.ai.pathfinderGoal;

import java.util.function.Predicate;

import org.bukkit.craftbukkit.libs.jline.internal.Nullable;

import com.pm.aiost.entity.ownable.OwnableEntity;

import net.minecraft.server.v1_15_R1.EntityInsentient;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.PathfinderGoalNearestAttackableTarget;

public class PathfinderGoalNearestAttackableTargetExceptOwner<T extends EntityLiving>
		extends PathfinderGoalNearestAttackableTarget<T> {

	public PathfinderGoalNearestAttackableTargetExceptOwner(EntityInsentient entityinsentient, Class<T> oclass,
			boolean flag) {
		super(entityinsentient, oclass, flag, false);
	}

	public PathfinderGoalNearestAttackableTargetExceptOwner(EntityInsentient entityinsentient, Class<T> oclass,
			boolean flag, boolean flag1) {
		super(entityinsentient, oclass, 10, flag, flag1, null);
	}

	public PathfinderGoalNearestAttackableTargetExceptOwner(EntityInsentient entityinsentient, Class<T> oclass, int i,
			boolean flag, boolean flag1, @Nullable Predicate<EntityLiving> predicate) {
		super(entityinsentient, oclass, i, flag, flag1, predicate);
	}

	@Override
	public boolean a() {
		boolean a = super.a();
		if (c != null) {
			if (c == ((OwnableEntity) e).getOwner())
				return false;
		}
		return a;
	}
}
