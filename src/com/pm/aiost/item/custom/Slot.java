package com.pm.aiost.item.custom;

import org.bukkit.inventory.EquipmentSlot;

import net.minecraft.world.InteractionHand;

public enum Slot {

	MAIN_HAND(0, InteractionHand.MAIN_HAND, net.minecraft.world.entity.EquipmentSlot.MAINHAND, EquipmentSlot.HAND),
	OFF_HAND(1, InteractionHand.OFF_HAND, net.minecraft.world.entity.EquipmentSlot.OFFHAND, EquipmentSlot.OFF_HAND),
	FEET(2, null, net.minecraft.world.entity.EquipmentSlot.FEET, EquipmentSlot.FEET),
	LEGS(3, null, net.minecraft.world.entity.EquipmentSlot.LEGS, EquipmentSlot.LEGS),
	CHEST(4, null, net.minecraft.world.entity.EquipmentSlot.CHEST, EquipmentSlot.CHEST),
	HEAD(5, null, net.minecraft.world.entity.EquipmentSlot.HEAD, EquipmentSlot.HEAD);

	public final byte id;
	public final String name;
	public final InteractionHand hand;
	public final net.minecraft.world.entity.EquipmentSlot nmsSlot;
	public final EquipmentSlot bukkitSlot;

	private Slot(int id, InteractionHand hand, net.minecraft.world.entity.EquipmentSlot nmsSlot,
			EquipmentSlot bukkitSlot) {
		this.id = (byte) id;
		this.name = nmsSlot.getName();
		this.hand = hand;
		this.nmsSlot = nmsSlot;
		this.bukkitSlot = bukkitSlot;
	}

	public boolean isHand() {
		return hand != null;
	}

	public boolean isArmor() {
		return hand == null;
	}

	public static final int MAIN_HAND_ID = 0;
	public static final int OFF_HAND_ID = 1;
	public static final int FEET_ID = 2;
	public static final int LEGS_ID = 3;
	public static final int CHEST_ID = 4;
	public static final int HEAD_ID = 5;
	public static final int SLOT_SIZE = 6;

	public static Slot get(InteractionHand hand) {
		return hand == InteractionHand.MAIN_HAND ? MAIN_HAND : OFF_HAND;
	}

	public static Slot get(net.minecraft.world.entity.EquipmentSlot nmsSlot) {
		switch (nmsSlot) {
		case MAINHAND:
			return MAIN_HAND;

		case OFFHAND:
			return OFF_HAND;

		case FEET:
			return FEET;

		case LEGS:
			return LEGS;

		case CHEST:
			return CHEST;

		case HEAD:
			return HEAD;

		default:
			return null;
		}
	}

	public static Slot get(EquipmentSlot bukkitSlot) {
		switch (bukkitSlot) {
		case HAND:
			return MAIN_HAND;

		case OFF_HAND:
			return OFF_HAND;

		case FEET:
			return FEET;

		case LEGS:
			return LEGS;

		case CHEST:
			return CHEST;

		case HEAD:
			return HEAD;

		default:
			return null;
		}
	}
}
