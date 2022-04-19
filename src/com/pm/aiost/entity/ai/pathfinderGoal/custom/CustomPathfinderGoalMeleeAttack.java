package com.pm.aiost.entity.ai.pathfinderGoal.custom;

import java.util.EnumSet;

import com.pm.aiost.entity.CustomInsentient;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EnumHand;
import net.minecraft.server.v1_15_R1.IEntitySelector;
import net.minecraft.server.v1_15_R1.PathEntity;
import net.minecraft.server.v1_15_R1.PathfinderGoal;

public class CustomPathfinderGoalMeleeAttack extends PathfinderGoal {

	protected final CustomInsentient a;
	protected int b;
	private final double d;
	private final boolean e;
	private PathEntity f;
	private int g;
	private double h;
	private double i;
	private double j;
	protected final int c = 20;
	private long k;

	public CustomPathfinderGoalMeleeAttack(CustomInsentient var0, double var1, boolean var3) {
		this.a = var0;
		this.d = var1;
		this.e = var3;
		a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
	}

	public boolean a() {
		long var0 = this.a.getWorld().getTime();
		if (var0 - this.k < 20L) {
			return false;
		}

		this.k = var0;

		EntityLiving var2 = this.a.getGoalTarget();
		if (var2 == null) {
			return false;
		}
		if (!var2.isAlive()) {
			return false;
		}
		this.f = this.a.getNavigation().a(var2, 0);
		if (this.f != null) {
			return true;
		}
		if (a(var2) >= this.a.getEntity().g(var2.locX(), var2.locY(), var2.locZ())) {
			return true;
		}
		return false;
	}

	public boolean b() {
		EntityLiving var0 = this.a.getGoalTarget();
		if (var0 == null) {
			return false;
		}
		if (!var0.isAlive()) {
			return false;
		}
		if (!this.e) {
			return !this.a.getNavigation().m();
		}
		if (!this.a.a(new BlockPosition(var0))) {
			return false;
		}

		if (var0 instanceof EntityHuman && (var0.isSpectator() || ((EntityHuman) var0).isCreative())) {
			return false;
		}

		return true;
	}

	public void c() {
		this.a.getNavigation().a(this.f, this.d);
		this.a.q(true);
		this.g = 0;
	}

	public void d() {
		EntityLiving var0 = this.a.getGoalTarget();
		if (!IEntitySelector.e.test(var0)) {
			this.a.setGoalTarget(null);
		}
		this.a.q(false);
		this.a.getNavigation().o();
	}

	public void e() {
		EntityLiving var0 = this.a.getGoalTarget();
		this.a.getControllerLook().a(var0, 30.0F, 30.0F);
		double var1 = this.a.getEntity().g(var0.locX(), var0.locY(), var0.locZ());
		this.g--;

		if ((this.e || this.a.getEntitySenses().a(var0)) && this.g <= 0
				&& ((this.h == 0.0D && this.i == 0.0D && this.j == 0.0D) || var0.g(this.h, this.i, this.j) >= 1.0D
						|| this.a.getRandom().nextFloat() < 0.05F)) {
			this.h = var0.locX();
			this.i = var0.locY();
			this.j = var0.locZ();
			this.g = 4 + this.a.getRandom().nextInt(7);

			if (var1 > 1024.0D) {
				this.g += 10;
			} else if (var1 > 256.0D) {
				this.g += 5;
			}

			if (!this.a.getNavigation().a(var0, this.d)) {
				this.g += 15;
			}
		}

		this.b = Math.max(this.b - 1, 0);
		a(var0, var1);
	}

	protected void a(EntityLiving var0, double var1) {
		double var3 = a(var0);
		if (var1 <= var3 && this.b <= 0) {
			this.b = 20;
			this.a.getEntity().a(EnumHand.MAIN_HAND);
			this.a.getEntity().B(var0);
		}
	}

	protected double a(EntityLiving var0) {
		return (this.a.getWidth() * 2.0F * this.a.getWidth() * 2.0F + var0.getWidth());
	}
}
