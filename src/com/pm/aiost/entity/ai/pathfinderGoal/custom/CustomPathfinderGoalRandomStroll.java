package com.pm.aiost.entity.ai.pathfinderGoal.custom;

import javax.annotation.Nullable;

import com.pm.aiost.entity.CustomInsentient;
import com.pm.aiost.entity.ai.RandomPositionGenerator;

import net.minecraft.server.v1_15_R1.Vec3D;

public class CustomPathfinderGoalRandomStroll extends net.minecraft.server.v1_15_R1.PathfinderGoalRandomStroll {

	protected final CustomInsentient a;

	public CustomPathfinderGoalRandomStroll(CustomInsentient entity, double var1) {
		super(null, var1);
		a = entity;
	}

	public CustomPathfinderGoalRandomStroll(CustomInsentient entity, double var1, int var3) {
		super(null, var1, var3);
		a = entity;
	}

	@Override
	public boolean a() {
		if (this.a.isVehicle()) {
			return false;
		}
		if (!this.g) {
			if (this.a.getEntity().cL() >= 100) {
				return false;
			}
			if (this.a.getRandom().nextInt(this.f) != 0) {
				return false;
			}
		}

		Vec3D var0 = g();

		if (var0 == null) {
			return false;
		}

		this.b = var0.x;
		this.c = var0.y;
		this.d = var0.z;
		this.g = false;
		return true;
	}

	@Override
	protected @Nullable Vec3D g() {
		return RandomPositionGenerator.a(this.a, 10, 7);
	}

	public boolean b() {
		return (!this.a.getNavigation().m() && !this.a.isVehicle());
	}

	@Override
	public void c() {
		this.a.getNavigation().a(this.b, this.c, this.d, this.e);
	}

	public void d() {
		this.a.getNavigation().o();
	}
}
