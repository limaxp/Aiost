package com.pm.aiost.item.custom;

import org.bukkit.inventory.EquipmentSlot;

import net.minecraft.server.v1_15_R1.EnumHand;
import net.minecraft.server.v1_15_R1.EnumItemSlot;

public enum Slot {

	MAIN_HAND(0, EnumHand.MAIN_HAND, EnumItemSlot.MAINHAND, EquipmentSlot.HAND),
	OFF_HAND(1, EnumHand.OFF_HAND, EnumItemSlot.OFFHAND, EquipmentSlot.OFF_HAND),
	FEET(2, null, EnumItemSlot.FEET, EquipmentSlot.FEET), LEGS(3, null, EnumItemSlot.LEGS, EquipmentSlot.LEGS),
	CHEST(4, null, EnumItemSlot.CHEST, EquipmentSlot.CHEST), HEAD(5, null, EnumItemSlot.HEAD, EquipmentSlot.HEAD);

	public final byte id;
	public final String name;
	public final EnumHand hand;
	public final EnumItemSlot nmsSlot;
	public final EquipmentSlot bukkitSlot;

	private Slot(int id, EnumHand hand, EnumItemSlot nmsSlot, EquipmentSlot bukkitSlot) {
		this.id = (byte) id;
		this.name = nmsSlot.getSlotName();
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

	public static Slot get(EnumHand hand) {
		return hand == EnumHand.MAIN_HAND ? MAIN_HAND : OFF_HAND;
	}

	public static Slot get(EnumItemSlot nmsSlot) {
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
