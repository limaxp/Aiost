package com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class LazyInventoryMenu extends ArrayInventoryMenu {

	protected final String name;
	protected final int itemSize;
	protected ItemStack borderItem1 = BORDER_ITEM;
	protected ItemStack borderItem2 = BORDER_ITEM_2;

	public LazyInventoryMenu(String name, int itemSize, ItemStack borderItem) {
		this(name, itemSize, true);
		borderItem1 = borderItem2 = borderItem;
	}

	public LazyInventoryMenu(String name, int itemSize, ItemStack borderItem1, ItemStack borderItem2) {
		this(name, itemSize, true);
		this.borderItem1 = borderItem1;
		this.borderItem2 = borderItem2;
	}

	public LazyInventoryMenu(String name, int itemSize, boolean hasBorder) {
		super(hasBorder);
		this.name = name;
		this.itemSize = itemSize;
		initInventories(hasBorder);
	}

	protected abstract void buildInventory(Inventory inv, int index);

	protected void initInventories(boolean hasBorder) {
		int maxItems = hasBorder ? MAX_ITEMS_WITH_BORDER : MAX_ITEMS;
		int inventorySize = (int) Math.ceil((float) itemSize / maxItems) - 1;
		if (inventorySize == -1)
			inventorySize++;
		inventories = new Inventory[inventorySize + 1];
	}

	@Override
	public void open(Player player) {
		checkInitialized(0);
		super.open(player);
	}

	@Override
	public void open(Player player, int index) {
		checkInitialized(index);
		super.open(player, index);
	}

	@Override
	public void openNext(Player player, String inventoryName) {
		int index = addToPageIndex(inventoryName);
		checkInitialized(index);
		super.open(player, index);
	}

	@Override
	public void openPrev(Player player, String inventoryName) {
		int index = substractFromPageIndex(inventoryName);
		checkInitialized(index);
		super.open(player, index);
	}

	private void checkInitialized(int index) {
		if (!hasbuildInventory(index))
			initializeInventory(index);
	}

	private void initializeInventory(int index) {
		Inventory inv = createInventory(index);
		inventories[index] = inv;
		buildInventory(inv, index);
	}

	private Inventory createInventory(int index) {
		if (hasBorder()) {
			if (index == inventories.length - 1)
				return createInventoryPerSize(createInventoryName(index), itemSize - index * MAX_ITEMS_WITH_BORDER,
						borderItem1, borderItem2);
			else
				return createInventory(createInventoryName(index), 54, borderItem1, borderItem2);
		} else {
			if (index == inventories.length - 1)
				return createInventoryPerSize(createInventoryName(index), itemSize - index * MAX_ITEMS, false);
			else
				return createInventory(createInventoryName(index), 54, false);
		}
	}

	protected String createInventoryName(int index) {
		return inventories.length > 1 ? name + NAME_SEPERATOR + (index + 1) : name;
	}

	@Override
	public void set(int index, ItemStack is) {
		int inventoryIndex;
		if (hasBorder()) {
			inventoryIndex = index / MAX_ITEMS_WITH_BORDER;
			if (hasbuildInventory(inventoryIndex))
				inventories[inventoryIndex].setItem(convertIndexToSlot(index - inventoryIndex * MAX_ITEMS_WITH_BORDER),
						is);
		} else {
			inventoryIndex = index / MAX_ITEMS;
			if (hasbuildInventory(inventoryIndex))
				inventories[inventoryIndex].setItem(index - inventoryIndex * MAX_ITEMS, is);
		}
	}

	public boolean hasbuildInventory(int index) {
		return inventories[index] != null;
	}

	public int getItemSize() {
		return itemSize;
	}
}
