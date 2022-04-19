package com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;

public abstract class ViewInventoryMenu extends InventoryMenu {

	protected String name;
	protected Inventory inv;

	public ViewInventoryMenu(String name, boolean hasBorder) {
		super(hasBorder);
		this.name = name;
	}

	protected abstract Inventory buildInventory(int index);

	protected Inventory createInventory(int index) {
		return inv = buildInventory(index);
	}

	@Override
	public void open(Player player) {
		player.openInventory(createInventory(0));
	}

	@Override
	public void open(Player player, int index) {
		player.openInventory(createInventory(index));
	}

	@Override
	public void openNext(Player player, String inventoryName) {
		player.openInventory(createInventory(parseInventoryIndex(inventoryName) + 1));
	}

	@Override
	public void openPrev(Player player, String inventoryName) {
		int index = parseInventoryIndex(inventoryName);
		if (index > 0)
			player.openInventory(createInventory(index - 1));
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

	@Override
	public Inventory getInventory(int index) {
		return createInventory(index);
	}

	@Override
	public Inventory getInventory() {
		return inv;
	}

	@Override
	public Inventory[] getInventories() {
		return new Inventory[] { inv };
	}

	@Override
	public Collection<Inventory> getInventoryCollection() {
		return Arrays.asList(inv);
	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public boolean hasNextPrevButtons() {
		return true;
	}
}