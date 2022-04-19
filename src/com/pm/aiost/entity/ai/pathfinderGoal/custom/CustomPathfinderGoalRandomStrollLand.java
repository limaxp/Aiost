package com.pm.aiost.entity.ai.pathfinderGoal.custom;

import javax.annotation.Nullable;

import com.pm.aiost.entity.CustomInsentient;
import com.pm.aiost.entity.ai.RandomPositionGenerator;

import net.minecraft.server.v1_15_R1.Vec3D;

public class CustomPathfinderGoalRandomStrollLand extends CustomPathfinderGoalRandomStroll {

	protected final float h;

	public CustomPathfinderGoalRandomStrollLand(CustomInsentient entity, double var1) {
		this(entity, var1, 0.001F);
	}

	public CustomPathfinderGoalRandomStrollLand(CustomInsentient entity, double var1, float var3) {
		super(entity, var1);
		this.h = var3;
	}

	@Override
	@Nullable
	protected Vec3D g() {
		if (this.a.getEntity().az()) {
			Vec3D var0 = RandomPositionGenerator.b(this.a, 15, 7);
			return (var0 == null) ? super.g() : var0;
		}
		if (this.a.getRandom().nextFloat() >= this.h) {
			return RandomPositionGenerator.b(this.a, 10, 7);
		}
		return super.g();
	}
}
