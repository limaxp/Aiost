package com.pm.aiost.misc.menu.inventoryMenu;

import java.util.List;

import org.bukkit.inventory.Inventory;

import com.pm.aiost.collection.list.IdentityArrayList;

public class InventoryMenuCustomAnimationHandler {

	private static final List<InventoryMenuAnimationHandler> ANIMATION_HANDLER = new IdentityArrayList<InventoryMenuAnimationHandler>();

	public static void registerAnimationHandler(InventoryMenuAnimationHandler handler) {
		ANIMATION_HANDLER.add(handler);
	}

	public static void unregisterAnimationHandler(InventoryMenuAnimationHandler handler) {
		ANIMATION_HANDLER.remove(handler);
	}

	public static void animateMenusSchedulerTick() {
		animateAll();
	}

	private static void animateAll() {
		for (InventoryMenuAnimationHandler handler : ANIMATION_HANDLER)
			handler.animate();
	}

	public abstract static class InventoryMenuAnimationHandler extends IdentityArrayList<Inventory> {

		protected abstract void animate(Inventory inv);

		public InventoryMenuAnimationHandler() {
			registerAnimationHandler(this);
		}

		public final void animate() {
			for (Inventory inv : this)
				animate(inv);
		}

		public final void unregister() {
			unregisterAnimationHandler(this);
		}
	}
}
