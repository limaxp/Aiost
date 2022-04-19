package com.pm.aiost.entity.ai.controller;

import com.pm.aiost.entity.CustomInsentient;

import net.minecraft.server.v1_15_R1.MathHelper;

public class PlayerControllerLook extends CustomControllerLook {

	public PlayerControllerLook(CustomInsentient entity) {
		super(entity);
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
			a_.yaw = a_.aK;
		} else {
			a_.aK = a(a_.aK, a_.aI, 10.0F);
			a_.yaw = a_.aK;
		}

		if (!this.a.getNavigation().m()) {
			a_.aK = MathHelper.b(a_.aK, a_.aI, this.a.dV_());
			a_.yaw = a_.aK;
		}
	}
}
