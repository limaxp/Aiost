package com.pm.aiost.entity.ai.controller;

import com.pm.aiost.entity.CustomInsentient;

import net.minecraft.server.v1_15_R1.ControllerLook;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.MathHelper;

public class CustomControllerLook extends ControllerLook {

	protected final CustomInsentient a;
	protected final EntityLiving a_;

	public CustomControllerLook(CustomInsentient entity) {
		super(null);
		a = entity;
		a_ = a.getEntity();
	}

	@Override
	public void a(double var0, double var2, double var4) {
		a(var0, var2, var4, this.a.dW_(), this.a.dU_());
	}

	@Override
	public void a() {
		if (b()) {
			a_.pitch = 0.0F;
		}

		if (this.d) {
			this.d = false;

			a_.aK = a(a_.aK, h(), this.b);
			a_.pitch = a(a_.pitch, g(), this.c);
		} else {
			a_.aK = a(a_.aK, a_.aI, 10.0F);
		}

		if (!this.a.getNavigation().m()) {
			a_.aK = MathHelper.b(a_.aK, a_.aI, this.a.dV_());
		}
	}

	@Override
	protected float g() {
		double var0 = this.e - a.locX();
		double var2 = this.f - a.getHeadY();
		double var4 = this.g - a.locZ();
		double var6 = MathHelper.sqrt(var0 * var0 + var4 * var4);
		return (float) -(MathHelper.d(var2, var6) * 57.2957763671875D);
	}

	@Override
	protected float h() {
		double var0 = this.e - a.locX();
		double var2 = this.g - a.locZ();
		return (float) (MathHelper.d(var2, var0) * 57.2957763671875D) - 90.0F;
	}
}
