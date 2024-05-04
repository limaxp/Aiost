package com.pm.aiost.item.custom;

import org.bukkit.inventory.EquipmentSlot;

import net.minecraft.world.InteractionHand;

public enum Hand {

	MAIN(0, InteractionHand.MAIN_HAND, net.minecraft.world.entity.EquipmentSlot.MAINHAND, EquipmentSlot.HAND),
	OFF(1, InteractionHand.OFF_HAND, net.minecraft.world.entity.EquipmentSlot.OFFHAND, EquipmentSlot.OFF_HAND);

	public final byte id;
	public final String name;
	public final InteractionHand hand;
	public final net.minecraft.world.entity.EquipmentSlot nmsSlot;
	public final EquipmentSlot bukkitSlot;

	Hand(int id, InteractionHand hand, net.minecraft.world.entity.EquipmentSlot nmsSlot, EquipmentSlot bukkitSlot) {
		this.id = (byte) id;
		this.name = nmsSlot.getName();
		this.hand = hand;
		this.nmsSlot = nmsSlot;
		this.bukkitSlot = bukkitSlot;
	}

	public static Hand get(InteractionHand hand) {
		return hand == InteractionHand.MAIN_HAND ? MAIN : OFF;
	}

	/**
	 * @return MAIN if given main hand otherwise OFF
	 */
	public static Hand get(net.minecraft.world.entity.EquipmentSlot nmsSlot) {
		return nmsSlot == net.minecraft.world.entity.EquipmentSlot.MAINHAND ? MAIN : OFF;
	}

	/**
	 * @return MAIN if given main hand otherwise OFF
	 */
	public static Hand get(EquipmentSlot bukkitSlot) {
		return bukkitSlot == EquipmentSlot.HAND ? MAIN : OFF;
	}
}
