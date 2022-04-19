package com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;

public abstract class ListInventoryMenu extends InventoryMenu {

	protected final String name;
	protected Inventory[] inventories;

	public ListInventoryMenu(String name, boolean hasBorder) {
		super(hasBorder);
		this.name = name;
		inventories = new Inventory[5];
	}

	protected abstract Inventory buildInventory(int index);

	protected Inventory createInventory(int index) {
		ensureCapacity(index + 1);
		return inventories[index] = buildInventory(index);
	}

	@Override
	public void open(Player player) {
		if (!hasInventory(0))
			createInventory(0);
		super.open(player);
	}

	@Override
	public void open(Player player, int index) {
		if (!hasInventory(index))
			createInventory(index);
		super.open(player, index);
	}

	@Override
	public void openNext(Player player, String inventoryName) {
		int index = parseInventoryIndex(inventoryName);
		if (!hasInventory(index + 1))
			createInventory(index + 1);
		super.open(player, index + 1);
	}

	@Override
	public void openPrev(Player player, String inventoryName) {
		int index = parseInventoryIndex(inventoryName);
		if (index < 1)
			return;
		if (!hasInventory(index - 1))
			createInventory(index - 1);
		super.open(player, index - 1);
	}

	public void reset() {
		for (int i = 0; i < inventories.length; i++)
			inventories[i] = null;
	}

	protected Inventory createInventoryPerSize(int index, int itemSize) {
		return createInventoryPerSize(createInventoryName(index), itemSize);
	}

	protected Inventory createInventoryPerSize(int index, int itemSize, ItemStack borderItem) {
		return createInventoryPerSize(createInventoryName(index), itemSize, borderItem);
	}

	protected Inventory createInventoryPerSize(int index, int itemSize, ItemStack borderItem1, ItemStack borderItem2) {
		return createInventoryPerSize(createInventoryName(index), itemSize, borderItem1, borderItem2);
	}

	protected Inventory createInventory(int index, int size) {
		return createInventory(createInventoryName(index), size);
	}

	protected Inventory createInventory(int index, int size, ItemStack borderItem) {
		return createInventory(createInventoryName(index), size, borderItem);
	}

	protected Inventory createInventory(int index, int size, ItemStack borderItem1, ItemStack borderItem2) {
		return createInventory(createInventoryName(index), size, borderItem1, borderItem2);
	}

	protected String createInventoryName(int index) {
		return name + NAME_SEPERATOR + (index + 1);
	}

	public boolean hasInventory(int index) {
		if (index > -1 && index < inventories.length)
			return inventories[index] != null;
		return false;
	}

	@Override
	public Inventory getInventory() {
		return inventories[0];
	}

	@Override
	public Inventory getInventory(int index) {
		return inventories[index];
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

	@Override
	public boolean hasNextPrevButtons() {
		return true;
	}

	protected void ensureCapacity(int minCapacity) {
		int length = inventories.length;
		if (minCapacity > length)
			resize(Math.max(length * 2, minCapacity));
	}

	protected void resize(int size) {
		Inventory[] newArray = new Inventory[size];
		System.arraycopy(inventories, 0, newArray, 0, inventories.length);
		inventories = newArray;
	}
}
