package com.pm.aiost.misc.event.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.player.ServerPlayer;

public class PlayerEquipItemEvent extends ServerPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private ItemStack is;
	private EquipmentSlot slot;
	private EquipmentAction action;
	private boolean cancelled;

	public PlayerEquipItemEvent(ServerPlayer serverPlayer, ItemStack is, EquipmentSlot slot, EquipmentAction action) {
		super(serverPlayer);
		this.is = is;
		this.slot = slot;
		this.action = action;
		cancelled = false;
	}

	public ItemStack getItemStack() {
		return is;
	}

	public EquipmentSlot getSlot() {
		return slot;
	}

	public EquipmentAction getAction() {
		return action;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public enum EquipmentAction {
		CUSTOM, JOIN, DEATH, HELD, SWAP, PICKUP, INTERACT, DROP, DISPENSE, BREAK, INVENTORY_CLICK, INVENTORY_DRAG, GIVE;
	}
}
