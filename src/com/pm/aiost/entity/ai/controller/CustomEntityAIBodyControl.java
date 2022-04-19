package com.pm.aiost.entity.ai.controller;

import com.pm.aiost.entity.CustomInsentient;

import net.minecraft.server.v1_15_R1.EntityAIBodyControl;
import net.minecraft.server.v1_15_R1.EntityInsentient;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.MathHelper;

public class CustomEntityAIBodyControl extends EntityAIBodyControl {

	protected final CustomInsentient a;
	protected final EntityLiving a_;
	protected int b;
	protected float c;

	public CustomEntityAIBodyControl(CustomInsentient var0) {
		super(null);
		a = var0;
		a_ = var0.getEntity();
	}

	public void a() {
		if (f()) {
			this.a_.aI = this.a_.yaw;
			c();

			this.c = this.a_.aK;
			this.b = 0;

			return;
		}
		if (e()) {
			if (Math.abs(this.a_.aK - this.c) > 15.0F) {

				this.b = 0;
				this.c = this.a_.aK;
				b();
			} else {
				this.b++;
				if (this.b > 10) {

					d();
				}
			}
		}
	}

	protected void b() {
		this.a_.aI = MathHelper.b(this.a_.aI, this.a_.aK, this.a.dV_());
	}

	protected void c() {
		this.a_.aK = MathHelper.b(this.a_.aK, this.a_.aI, this.a.dV_());
	}

	protected void d() {
		int var0 = this.b - 10;

		float var1 = MathHelper.a(var0 / 10.0F, 0.0F, 1.0F);

		float var2 = this.a.dV_() * (1.0F - var1);

		this.a_.aI = MathHelper.b(this.a_.aI, this.a_.aK, var2);
	}

	protected boolean e() {
		return (this.a.getPassengers().isEmpty() || !(this.a.getPassengers().get(0) instanceof EntityInsentient));
	}

	protected boolean f() {
		double var0 = this.a.locX() - this.a_.lastX;
		double var2 = this.a.locZ() - this.a_.lastZ;

		return (var0 * var0 + var2 * var2 > 2.500000277905201E-7D);
	}
}
