package com.pm.aiost.item.custom;

import org.bukkit.inventory.EquipmentSlot;

import net.minecraft.server.v1_15_R1.EnumHand;
import net.minecraft.server.v1_15_R1.EnumItemSlot;

public enum Hand {

	MAIN(0, EnumHand.MAIN_HAND, EnumItemSlot.MAINHAND, EquipmentSlot.HAND),
	OFF(1, EnumHand.OFF_HAND, EnumItemSlot.OFFHAND, EquipmentSlot.OFF_HAND);

	public final byte id;
	public final String name;
	public final EnumHand hand;
	public final EnumItemSlot nmsSlot;
	public final EquipmentSlot bukkitSlot;

	Hand(int id, EnumHand hand, EnumItemSlot nmsSlot, EquipmentSlot bukkitSlot) {
		this.id = (byte) id;
		this.name = nmsSlot.getSlotName();
		this.hand = hand;
		this.nmsSlot = nmsSlot;
		this.bukkitSlot = bukkitSlot;
	}

	public static Hand get(EnumHand hand) {
		return hand == EnumHand.MAIN_HAND ? MAIN : OFF;
	}

	/**
	 * @return MAIN if given main hand otherwise OFF
	 */
	public static Hand get(EnumItemSlot nmsSlot) {
		return nmsSlot == EnumItemSlot.MAINHAND ? MAIN : OFF;
	}

	/**
	 * @return MAIN if given main hand otherwise OFF
	 */
	public static Hand get(EquipmentSlot bukkitSlot) {
		return bukkitSlot == EquipmentSlot.HAND ? MAIN : OFF;
	}
}
