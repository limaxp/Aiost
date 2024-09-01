package com.pm.aiost.misc.event.events;

import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.player.ServerPlayer;

public class PlayerEquipHandItemEvent extends PlayerEquipItemEvent {

	private static final HandlerList handlers = new HandlerList();
	private boolean mainHand;

	public PlayerEquipHandItemEvent(ServerPlayer serverPlayer, ItemStack is, EquipmentSlot slot,
			EquipmentAction action) {
		super(serverPlayer, is, slot, action);
		if (slot == EquipmentSlot.HAND)
			mainHand = true;
		else
			mainHand = false;
	}

	public boolean isMainHand() {
		return mainHand;
	}

	public boolean isOffHand() {
		return !mainHand;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
