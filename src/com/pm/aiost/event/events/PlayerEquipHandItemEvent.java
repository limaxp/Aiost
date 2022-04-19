package com.pm.aiost.event.events;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.player.ServerPlayer;

public class PlayerEquipHandItemEvent extends PlayerEquipItemEvent {

	private static final HandlerList handlers = new HandlerList();
	private boolean mainHand;

	public PlayerEquipHandItemEvent(@Nonnull ServerPlayer serverPlayer, @Nullable ItemStack is,
			@Nonnull EquipmentSlot slot, @Nonnull EquipmentAction action) {
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
