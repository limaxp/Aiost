package com.pm.aiost.item.custom;

import net.minecraft.server.v1_15_R1.EnumAnimation;

public enum ItemAnimation {

	NONE(EnumAnimation.NONE), BLOCK(EnumAnimation.BLOCK), BOW(EnumAnimation.BOW), CROSSBOW(EnumAnimation.CROSSBOW),
	DRINK(EnumAnimation.DRINK), EAT(EnumAnimation.EAT), SPEAR(EnumAnimation.SPEAR);

	public final EnumAnimation nms;

	ItemAnimation(EnumAnimation nms) {
		this.nms = nms;
	}
}