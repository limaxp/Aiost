package com.pm.aiost.entity.ai.controller;

import com.pm.aiost.entity.CustomInsentient;

import net.minecraft.server.v1_15_R1.EntityArmorStand;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.Vector3f;

public class ArmorstandControllerLook extends CustomControllerLook {

	public ArmorstandControllerLook(CustomInsentient entity) {
		super(entity);
	}

	@Override
	public void a() {
		EntityArmorStand a_ = (EntityArmorStand) this.a_;
		if (b()) {
			a_.pitch = 0.0F;
		}

		if (this.d) {
			this.d = false;

			a_.aK = a(a_.aK, h(), this.b);
			a_.yaw = a_.aK;
			a_.setHeadPose(createVector(a_.aK, a_.pitch));
			a_.pitch = a(a_.pitch, g(), this.c);
		} else {
			a_.aK = a(a_.aK, a_.aI, 10.0F);
			a_.yaw = a_.aK;
		}

		if (!this.a.getNavigation().m()) {
			a_.aK = MathHelper.b(a_.aK, a_.aI, this.a.dV_());
			a_.yaw = a_.aK;
		}
	}

	private Vector3f createVector(float yaw, float pitch) {
		double xzLen = Math.cos(pitch);
		return new Vector3f((float) (Math.cos(yaw) * xzLen), (float) Math.sin(pitch), (float) (Math.sin(yaw) * xzLen));
	}
}
