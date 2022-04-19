package com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;

public class SingleInventoryMenu extends InventoryMenu {

	protected Inventory inventory;

	public SingleInventoryMenu(String name, int rowCount, boolean hasBorder) {
		super(hasBorder);
		inventory = createInventory(name, rowCount * 9, hasBorder);
	}

	public SingleInventoryMenu(String name, int rowCount, ItemStack borderItem) {
		super(true);
		inventory = createInventory(name, rowCount * 9, borderItem);
	}

	public SingleInventoryMenu(String name, int rowCount, ItemStack borderItem1, ItemStack borderItem2) {
		super(true);
		inventory = createInventory(name, rowCount * 9, borderItem1, borderItem2);
	}

	@Override
	public Inventory getInventory(int index) {
		return inventory;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public Inventory[] getInventories() {
		return new Inventory[] { inventory };
	}

	@Override
	public Collection<Inventory> getInventoryCollection() {
		return Arrays.asList(inventory);
	}

	@Override
	public int getSize() {
		return 1;
	}
}
