package com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;

/**
 * This class can't have custom borders because of custom constructor handling!
 * 
 * @author Max
 *
 */
public class ArrayInventoryMenu extends InventoryMenu {

	protected Inventory[] inventories;

	protected ArrayInventoryMenu(boolean hasBorder) {
		super(hasBorder);
	}

	public ArrayInventoryMenu(String name, int itemSize, boolean hasBorder) {
		super(hasBorder);
		createInventories(name, itemSize, hasBorder);
	}

	public ArrayInventoryMenu(String name, int inventoryCount, int rowCount, boolean hasBorder) {
		super(hasBorder);
		createInventories(name, inventoryCount, rowCount, hasBorder);
	}

	@Override
	public Inventory getInventory(int index) {
		return inventories[index];
	}

	@Override
	public Inventory getInventory() {
		return inventories[0];
	}

	@Override
	public Inventory[] getInventories() {
		return inventories;
	}

	@Override
	public Collection<Inventory> getInventoryCollection() {
		return Arrays.asList(inventories);
	}

	@Override
	public int getSize() {
		return inventories.length;
	}

	private void createInventories(String name, int itemSize, boolean hasBorder) {
		int maxItems = getMaxInventoryItems(hasBorder);
		int inventorySize = (int) Math.floor((double) itemSize / maxItems);
		inventories = new Inventory[inventorySize + 1];
		if (inventorySize == 0)
			inventories[0] = createInventoryPerSize(name, itemSize, hasBorder);
		else {
			InventoryConstructor constructor = getConstructor(hasBorder);
			int i;
			for (i = 0; i < inventorySize; i++)
				inventories[i] = constructor.create(createInventoryName(name, i), 54);
			inventories[i] = constructor.create(createInventoryName(name, i),
					calculateRowCount(itemSize - inventorySize * maxItems, hasBorder) * 9);
		}
	}

	private void createInventories(String name, int inventorySize, int rowCount, boolean hasBorder) {
		inventories = new Inventory[inventorySize];
		if (inventorySize == 1)
			inventories[0] = createInventory(name, rowCount * 9, hasBorder);
		else {
			InventoryConstructor constructor = getConstructor(hasBorder);
			int size = rowCount * 9;
			for (int i = 0; i < inventorySize; i++)
				inventories[i] = constructor.create(createInventoryName(name, i), size);
		}
	}

	protected String createInventoryName(String name, int index) {
		return name + NAME_SEPERATOR + (index + 1);
	}

	private InventoryConstructor getConstructor(boolean hasBorder) {
		if (hasBorder) {
			if (hasNextPrevButtons())
				return this::createInventoryWithButtonsAndBorder;
			else
				return this::createInventoryWithBorder;
		} else if (hasNextPrevButtons())
			return this::createInventoryWithButtonsNoBorder;
		else
			return this::createInventoryNormal;
	}

	protected Inventory createInventoryNormal(String name, int size) {
		return Bukkit.createInventory(this, size, name);
	}

	protected Inventory createInventoryWithBorder(String name, int size) {
		Inventory inventory = Bukkit.createInventory(this, size, name);
		createBorder(inventory);
		return inventory;
	}

	protected Inventory createInventoryWithButtonsAndBorder(String name, int size) {
		Inventory inventory = Bukkit.createInventory(this, size, name);
		createBorder(inventory);
		createNextPrevButtons(inventory);
		return inventory;
	}

	protected Inventory createInventoryWithButtonsNoBorder(String name, int size) {
		Inventory inventory = Bukkit.createInventory(this, size, name);
		createNextPrevButtons(inventory);
		createBackButtons(inventory);
		return inventory;
	}

	@FunctionalInterface
	private static interface InventoryConstructor {

		public Inventory create(String name, int size);
	}
}
