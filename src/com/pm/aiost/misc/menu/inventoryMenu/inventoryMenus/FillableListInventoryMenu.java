package com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus;

import org.bukkit.inventory.Inventory;

public class FillableListInventoryMenu extends ListInventoryMenu {

	public FillableListInventoryMenu(String name, boolean hasBorder) {
		super(name, hasBorder);
	}

	@Override
	protected Inventory buildInventory(int index) {
		return createInventory(index, 54);
	}

	@Override
	public Inventory getInventory() {
		if (!hasInventory(0))
			return createInventory(0);
		return inventories[0];
	}

	@Override
	public Inventory getInventory(int index) {
		if (!hasInventory(index))
			return createInventory(index);
		return inventories[index];
	}
}
