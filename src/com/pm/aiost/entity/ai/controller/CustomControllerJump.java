package com.pm.aiost.entity.ai.controller;

import com.pm.aiost.entity.CustomInsentient;

import net.minecraft.server.v1_15_R1.ControllerJump;

public class CustomControllerJump extends ControllerJump {

	protected final CustomInsentient b;

	public CustomControllerJump(CustomInsentient entity) {
		super(null);
		this.b = entity;
	}

	@Override
	public void b() {
		this.b.setJumping(this.a);
		this.a = false;
	}
}
