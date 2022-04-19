package com.pm.aiost.entity.ai.pathfinderGoal.custom;

import java.util.EnumSet;

import com.pm.aiost.entity.CustomInsentient;

import net.minecraft.server.v1_15_R1.PathfinderGoal;

public class CustomPathfinderGoalRandomLookaround extends PathfinderGoal {

	protected final CustomInsentient a;
	private double b;
	private double c;
	private int d;

	public CustomPathfinderGoalRandomLookaround(CustomInsentient entity) {
		a = entity;
		a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
	}

	@Override
	public boolean a() {
		return (this.a.getRandom().nextFloat() < 0.02F);
	}

	@Override
	public boolean b() {
		return (this.d >= 0);
	}

	@Override
	public void c() {
		double var0 = 6.283185307179586D * this.a.getRandom().nextDouble();
		this.b = Math.cos(var0);
		this.c = Math.sin(var0);
		this.d = 20 + this.a.getRandom().nextInt(20);
	}

	@Override
	public void e() {
		this.d--;
		this.a.getControllerLook().a(this.a.locX() + this.b, this.a.locY() + this.a.getHeadHeight(),
				this.a.locZ() + this.c);
	}
}
