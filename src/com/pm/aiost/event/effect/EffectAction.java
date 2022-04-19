package com.pm.aiost.event.effect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.bytes.ByteArrayList;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.bytes.ByteList;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2ByteMap;
import org.bukkit.inventory.EquipmentSlot;

public class EffectAction {

	private static final Object2ByteMap<String> NAME_MAP = new Object2ByteLinkedOpenHashMap<String>();
	private static final List<String> NAME_LIST = new ArrayList<String>();
	private static final ByteList MAIN_HAND_LIST = new ByteArrayList();
	private static final ByteList OFF_HAND_LIST = new ByteArrayList();

	private static final ByteList MAIN_LIST = new ByteArrayList();
	private static final List<Byte> MAIN_LIST_VIEW = Collections.unmodifiableList(MAIN_LIST);

	public static final byte BLOCK_BREAK = a("block_break", true);
	public static final byte BLOCK_PLACE = a("block_place", true);
	public static final byte BOW_SHOOT = a("bow_shoot", true);
	public static final byte PROJECTILE_LAUNCH = a("projectile_launch", true);
	public static final byte PROJECTILE_HIT = a("projectile_hit", false);
	public static final byte FISHING = a("fishing", true);
	public static final byte FISHING_ROD_LAUNCH = a("fishing_rod_launch", true);
	public static final byte CLICK = a("click", true);
	public static final byte LEFT_CLICK = a("left_click", true);
	public static final byte RIGHT_CLICK = a("right_click", true);
	public static final byte ITEM_PICKUP = a("item_pickup", false);
	public static final byte ITEM_DROP = a("item_drop", false);
	public static final byte ITEM_CONSUME = a("item_consume", true);
	public static final byte ITEM_MERGE = a("item_merge", false);
	public static final byte ITEM_SWAP = a("item_swap", false);
	public static final byte ITEM_HELD = a("item_held", false);
	public static final byte EQUIP = a("equip", false);
	public static final byte UNEQUIP = a("unequip", false);
	public static final byte INVENTORY_CLICK = a("inventory_click", false);
	public static final byte INVENTORY_PLACE = a("inventory_place", false);
	public static final byte INVENTORY_DRAG = a("inventory_drag", false);
	public static final byte INVENTORY_CREATIVE_CLICK = a("inventory_creative_click", false);
	public static final byte INVENTORY_CREATIVE_PLACE = a("inventory_creative_place", false);
	public static final byte MOVE = a("move", false);
	public static final byte DEATH = a("death", false);
	public static final byte TICK = a("tick", false);
	public static final byte JUMP = a("jump", false);

	public static final byte BLOCK_BREAK_MAIN_HAND = getMainHandAction(BLOCK_BREAK);
	public static final byte BLOCK_BREAK_OFF_HAND = getOffHandAction(BLOCK_BREAK);
	public static final byte BLOCK_PLACE_MAIN_HAND = getMainHandAction(BLOCK_PLACE);
	public static final byte BLOCK_PLACE_OFF_HAND = getOffHandAction(BLOCK_PLACE);
	public static final byte BOW_SHOOT_MAIN_HAND = getMainHandAction(BOW_SHOOT);
	public static final byte BOW_SHOOT_OFF_HAND = getOffHandAction(BOW_SHOOT);
	public static final byte PROJECTILE_LAUNCH_MAIN_HAND = getMainHandAction(PROJECTILE_LAUNCH);
	public static final byte PROJECTILE_LAUNCH_OFF_HAND = getOffHandAction(PROJECTILE_LAUNCH);
	public static final byte FISHING_MAIN_HAND = getMainHandAction(FISHING);
	public static final byte FISHING_OFF_HAND = getOffHandAction(FISHING);
	public static final byte FISHING_ROD_LAUNCH_MAIN_HAND = getMainHandAction(FISHING_ROD_LAUNCH);
	public static final byte FISHING_ROD_LAUNCH_OFF_HAND = getOffHandAction(FISHING_ROD_LAUNCH);
	public static final byte CLICK_MAIN_HAND = getMainHandAction(CLICK);
	public static final byte CLICK_OFF_HAND = getOffHandAction(CLICK);
	public static final byte LEFT_CLICK_MAIN_HAND = getMainHandAction(LEFT_CLICK);
	public static final byte LEFT_CLICK_OFF_HAND = getOffHandAction(LEFT_CLICK);
	public static final byte RIGHT_CLICK_MAIN_HAND = getMainHandAction(RIGHT_CLICK);
	public static final byte RIGHT_CLICK_OFF_HAND = getOffHandAction(RIGHT_CLICK);
	public static final byte ITEM_CONSUME_MAIN_HAND = getMainHandAction(ITEM_CONSUME);
	public static final byte ITEM_CONSUME_OFF_HAND = getOffHandAction(ITEM_CONSUME);

	public static byte a(String name, boolean hasHand) {
		return hasHand ? registerHand(name) : register(name);
	}

	public static byte register(String name) {
		byte id = (byte) size();
		register(name, id);
		MAIN_LIST.add(id);
		return id;
	}

	public static byte registerHand(String name) {
		byte id = (byte) size();
		byte mainhandId = (byte) (id + 1);
		byte offhandId = (byte) (id + 2);
		register(name, id, mainhandId, offhandId);
		MAIN_LIST.add(id);
		register(name + "_main_hand", mainhandId);
		register(name + "_off_hand", offhandId);
		return id;
	}

	private static void register(String name, byte id) {
		register(name, id, id, id);
	}

	private static void register(String name, byte id, byte mainHandId, byte offHandId) {
		NAME_MAP.put(name, id);
		NAME_LIST.add(name);
		MAIN_HAND_LIST.add(mainHandId);
		OFF_HAND_LIST.add(offHandId);
	}

	public static byte get(String name) {
		return NAME_MAP.getByte(name);
	}

	public static byte getIgnoreCase(String name) {
		return NAME_MAP.getByte(name.toLowerCase());
	}

	public static byte getHandAction(byte id, EquipmentSlot slot) {
		return slot == EquipmentSlot.HAND ? MAIN_HAND_LIST.getByte(id) : OFF_HAND_LIST.getByte(id);
	}

	public static byte getMainHandAction(byte id) {
		return MAIN_HAND_LIST.getByte(id);
	}

	public static byte getOffHandAction(byte id) {
		return OFF_HAND_LIST.getByte(id);
	}

	public static String getName(byte id) {
		return NAME_LIST.get(id);
	}

	public static int size() {
		return NAME_MAP.size();
	}

	public static List<Byte> getMainActions() {
		return MAIN_LIST_VIEW;
	}
}