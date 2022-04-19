package com.pm.aiost.entity.ai.controller;

import com.pm.aiost.entity.CustomInsentient;

import net.minecraft.server.v1_15_R1.MathHelper;

public class PlayerAIBodyControl extends CustomEntityAIBodyControl {

	public PlayerAIBodyControl(CustomInsentient var0) {
		super(var0);
	}

	protected void c() {
		this.a_.aK = MathHelper.b(this.a_.aK, this.a_.aI, this.a.dV_());
		a_.yaw = a_.aK;
	}
}
