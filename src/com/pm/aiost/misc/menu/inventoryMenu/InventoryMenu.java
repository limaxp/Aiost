package com.pm.aiost.misc.menu.inventoryMenu;

import static com.pm.aiost.misc.utils.ChatColor.BLUE;
import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.Aiost;
import com.pm.aiost.misc.menu.InventoryEventHandler;
import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenuCustomAnimationHandler.InventoryMenuAnimationHandler;
import com.pm.aiost.misc.other.Banner;
import com.pm.aiost.misc.other.PlayerHead;
import com.pm.aiost.misc.other.interfaces.ExpandedIntFunction;
import com.pm.aiost.misc.other.interfaces.ThrowingFunction;
import com.pm.aiost.misc.other.interfaces.ThrowingIntFunction;
import com.pm.aiost.misc.other.interfaces.TriConsumer;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public abstract class InventoryMenu implements Menu, InventoryEventHandler, InventoryHolder {

	public static final char NAME_SEPERATOR = ' ';
	public static final byte MAX_ITEMS = 45;
	public static final byte MAX_ITEMS_WITH_BORDER = 28;

	public static final int[] BORDER_SLOTS = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24,
			25, 26 };
	public static final int[] BORDER_SLOTS_2 = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 28, 29, 30, 31,
			32, 33, 34, 35 };
	public static final int[] BORDER_SLOTS_3 = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38,
			39, 40, 41, 42, 43, 44 };
	public static final int[] BORDER_SLOTS_4 = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 26, 35, 44, 18, 27, 36, 45,
			46, 47, 48, 49, 50, 51, 52, 53 };

	public static final int[] BORDER_SLOTS_NO_NEXT_PREV = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 21, 22, 23 };
	public static final int[] BORDER_SLOTS_NO_NEXT_PREV_2 = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 30,
			31, 32 };
	public static final int[] BORDER_SLOTS_NO_NEXT_PREV_3 = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27,
			35, 39, 40, 41 };
	public static final int[] BORDER_SLOTS_NO_NEXT_PREV_4 = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 18, 27, 36, 17,
			26, 35, 44, 48, 49, 50 };

	public static final int[] PREV_SLOTS = new int[] { 9, 10, 11 };
	public static final int[] BACK_SLOTS = new int[] { 12, 13, 14 };
	public static final int[] NEXT_SLOTS = new int[] { 15, 16, 17 };
	public static final int[] PREV_SLOTS_2 = new int[] { 18, 19, 20 };
	public static final int[] BACK_SLOTS_2 = new int[] { 21, 22, 23 };
	public static final int[] NEXT_SLOTS_2 = new int[] { 24, 25, 26 };
	public static final int[] PREV_SLOTS_3 = new int[] { 27, 28, 29 };
	public static final int[] BACK_SLOTS_3 = new int[] { 30, 31, 32 };
	public static final int[] NEXT_SLOTS_3 = new int[] { 33, 34, 35 };
	public static final int[] PREV_SLOTS_4 = new int[] { 36, 37, 38 };
	public static final int[] BACK_SLOTS_4 = new int[] { 39, 40, 41 };
	public static final int[] NEXT_SLOTS_4 = new int[] { 42, 43, 44 };
	public static final int[] PREV_SLOTS_5 = new int[] { 45, 46, 47 };
	public static final int[] BACK_SLOTS_5 = new int[] { 48, 49, 50 };
	public static final int[] NEXT_SLOTS_5 = new int[] { 51, 52, 53 };

	public static final ItemStack BORDER_ITEM;
	public static final ItemStack BORDER_ITEM_2;
	public static final ItemStack NEXT_ITEM;
	public static final ItemStack NEXT_ITEM_2;
	public static final ItemStack PREV_ITEM;
	public static final ItemStack PREV_ITEM_2;

	public static final ItemStack NEXT_ITEM_HEAD = PlayerHead.create("MHF_ArrowRight", GREEN + BOLD + "Next row",
			Arrays.asList(GRAY + "Click to open next row"));

	public static final ItemStack NEXT_ITEM_BANNER = Banner.create(Material.BLACK_BANNER, GREEN + BOLD + "Next row",
			Arrays.asList(GRAY + "Click to open next row"), Banner.plusPattern(DyeColor.BLACK, DyeColor.WHITE));

	public static final ItemStack PREV_ITEM_HEAD = PlayerHead.create("MHF_ArrowLeft", RED + BOLD + "Previous row",
			Arrays.asList(GRAY + "Click to open previous row"));

	public static final ItemStack PREV_ITEM_BANNER = Banner.create(Material.BLACK_BANNER, GREEN + BOLD + "Previous row",
			Arrays.asList(GRAY + "Click to open previous row"), Banner.minusPattern(DyeColor.BLACK, DyeColor.WHITE));

	public static final int FIRST_BORDER_INDEX = 17;
	public static final int LAST_BORDER_INDEX = 43;
	public static final int LAST_BORDERLESS_INDEX = 44;

	public static final int FIRST_BORDERED_SLOT = 10;

	static {
		List<String> borderItemLore = Arrays.asList(GRAY + "Click to close menu");
		BORDER_ITEM = MetaHelper.setMeta(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), BLUE + BOLD + "Close",
				borderItemLore);
		BORDER_ITEM_2 = MetaHelper.setMeta(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE), BLUE + BOLD + "Close",
				borderItemLore);

		List<String> nextItemLore = Arrays.asList(GRAY + "Click to open next page");
		NEXT_ITEM = MetaHelper.setMeta(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), GREEN + BOLD + "Next page",
				nextItemLore);
		NEXT_ITEM_2 = MetaHelper.setMeta(new ItemStack(Material.LIME_STAINED_GLASS_PANE), GREEN + BOLD + "Next page",
				nextItemLore);

		List<String> prevItemLore = Arrays.asList(GRAY + "Click to open previous page");
		PREV_ITEM = MetaHelper.setMeta(new ItemStack(Material.RED_STAINED_GLASS_PANE), RED + BOLD + "Previous page",
				prevItemLore);
		PREV_ITEM_2 = MetaHelper.setMeta(new ItemStack(Material.PINK_STAINED_GLASS_PANE), RED + BOLD + "Previous page",
				prevItemLore);
	}

	private Consumer<ServerPlayer> backLink;
	private BiConsumer<ServerPlayer, InventoryClickEvent> clickCallback;
	private byte border; // 0 = no border, 1 = border, 2 = animated border
	private InventoryMenuAnimationHandler animationHandler;

	protected InventoryMenu(boolean hasBorder) {
		border = (byte) (hasBorder ? 1 : 0);
		backLink = DEFAULT_BACK_LINK;
		clickCallback = NULL_CLICK_CALLBACK;
		animationHandler = null;
	}

	public abstract Inventory getInventory(int index);

	public abstract Inventory[] getInventories();

	public abstract Collection<Inventory> getInventoryCollection();

	public abstract int getSize();

	protected Inventory createInventoryPerSize(String name, int itemSize) {
		boolean hasBorder = hasBorder();
		return createInventory(name, calculateRowCount(itemSize, hasBorder) * 9, hasBorder);
	}

	protected Inventory createInventoryPerSize(String name, int itemSize, boolean hasBorder) {
		return createInventory(name, calculateRowCount(itemSize, hasBorder) * 9, hasBorder);
	}

	protected Inventory createInventory(String name, int size) {
		return createInventory(name, size, hasBorder());
	}

	protected Inventory createInventory(String name, int size, boolean hasBorder) {
		Inventory inventory = Bukkit.createInventory(this, size, name);
		if (hasBorder) {
			createBorder(inventory);
			if (hasNextPrevButtons())
				createNextPrevButtons(inventory);
		} else {
			if (hasNextPrevButtons())
				createNextPrevButtons(inventory);
			else
				InventoryMenu.createNextPrevButtons(inventory, InventoryMenu.BORDER_ITEM_2);
			InventoryMenu.createBackButtons(inventory);
		}
		return inventory;
	}

	protected Inventory createInventoryPerSize(String name, int itemSize, ItemStack borderItem) {
		return createInventory(name, calculateRowCount(itemSize, true) * 9, borderItem, borderItem);
	}

	protected Inventory createInventory(String name, int size, ItemStack borderItem) {
		return createInventory(name, size, borderItem, borderItem);
	}

	protected Inventory createInventoryPerSize(String name, int itemSize, ItemStack borderItem1,
			ItemStack borderItem2) {
		return createInventory(name, calculateRowCount(itemSize, true) * 9, borderItem1, borderItem2);
	}

	protected Inventory createInventory(String name, int size, ItemStack borderItem1, ItemStack borderItem2) {
		Inventory inventory = Bukkit.createInventory(this, size, name);
		createBorder(inventory, borderItem1, borderItem2);
		if (hasNextPrevButtons())
			createNextPrevButtons(inventory);
		return inventory;
	}

	public void setCategories(ItemStack... itemStacks) {
		setCategories_(PREV_ITEM_HEAD, NEXT_ITEM_HEAD, itemStacks);
	}

	public void setCategories(ItemStack borderItem, ItemStack... itemStacks) {
		int size = itemStacks.length;
		int index = 0;
		int inventoryIndex = 0;
		Inventory inv = getInventory(inventoryIndex);
		for (int i = 0; i < size; i++) {
			if (index >= 5) {
				inv = getInventory(++inventoryIndex);
				index = 0;
			}
			int categoryIndex = index * 9;
			inv.setItem(categoryIndex, itemStacks[i]);
			inv.setItem(categoryIndex + 1, borderItem);
			index++;
		}
	}

	public void setCategories_(ItemStack prevItem, ItemStack nextItem, ItemStack... itemStacks) {
		int size = itemStacks.length;
		int index = 0;
		int inventoryIndex = 0;
		Inventory inv = getInventory(inventoryIndex);
		for (int i = 0; i < size; i++) {
			if (index >= 5) {
				inv = getInventory(++inventoryIndex);
				index = 0;
			}
			int categoryIndex = index * 9;
			inv.setItem(categoryIndex, itemStacks[i]);
			inv.setItem(categoryIndex + 1, prevItem);
			inv.setItem(categoryIndex + 8, nextItem);
			index++;
		}
	}

	public void setCategories(Iterable<ItemStack> itemStacks) {
		setCategories(PREV_ITEM_HEAD, NEXT_ITEM_HEAD, itemStacks);
	}

	public void setCategories(ItemStack borderItem, Iterable<ItemStack> itemStacks) {
		int index = 0;
		int inventoryIndex = 0;
		Inventory inv = getInventory(inventoryIndex);
		for (ItemStack itemStack : itemStacks) {
			if (index >= 5) {
				inv = getInventory(++inventoryIndex);
				index = 0;
			}
			int categoryIndex = index * 9;
			inv.setItem(categoryIndex, itemStack);
			inv.setItem(categoryIndex + 1, borderItem);
			index++;
		}
	}

	public void setCategories(ItemStack prevItem, ItemStack nextItem, Iterable<ItemStack> itemStacks) {
		int index = 0;
		int inventoryIndex = 0;
		Inventory inv = getInventory(inventoryIndex);
		for (ItemStack itemStack : itemStacks) {
			if (index >= 5) {
				inv = getInventory(++inventoryIndex);
				index = 0;
			}
			int categoryIndex = index * 9;
			inv.setItem(categoryIndex, itemStack);
			inv.setItem(categoryIndex + 1, prevItem);
			inv.setItem(categoryIndex + 8, nextItem);
			index++;
		}
	}

	public void setCategorized(Iterable<ItemStack>[] itemstacks) {
		for (int i = 0; i < itemstacks.length; i++) {
			Iterable<ItemStack> collection = itemstacks[i];
			if (collection != null) {
				Inventory inv = getInventory(i / 5);
				int rowIndex = i % 5;
				int index = 2;
				for (ItemStack is : collection)
					inv.setItem((rowIndex * 9) + index++, is);
			}
		}
	}

	public void setCategorized(int inventoryIndex, Iterable<ItemStack>[] itemstacks) {
		Inventory inv = getInventory(inventoryIndex);
		for (int i = inventoryIndex * 5, lastId = i + 5; i < lastId; i++) {
			Iterable<ItemStack> collection = itemstacks[i];
			if (collection != null) {
				int rowIndex = i % 5;
				int index = 2;
				for (ItemStack is : collection)
					inv.setItem((rowIndex * 9) + index++, is);
			}
		}
	}

	public void set(int index, ItemStack is) {
		if (hasBorder()) {
			int inventoryIndex = index / MAX_ITEMS_WITH_BORDER;
			getInventory(inventoryIndex).setItem(convertIndexToSlot(index - inventoryIndex * MAX_ITEMS_WITH_BORDER),
					is);
		} else {
			int inventoryIndex = index / MAX_ITEMS;
			getInventory(inventoryIndex).setItem(index - inventoryIndex * MAX_ITEMS, is);
		}
	}

	public void set(ItemStack... itemStacks) {
		if (hasBorder())
			setBordered(10, 0, FIRST_BORDER_INDEX, itemStacks);
		else
			setBorderless(0, 0, itemStacks);
	}

	public void set(Iterable<ItemStack> itemStacks) {
		if (hasBorder())
			setBordered(10, 0, FIRST_BORDER_INDEX, itemStacks);
		else
			setBorderless(0, 0, itemStacks);
	}

	public void set(int index, ItemStack... itemStacks) {
		if (hasBorder()) {
			int inventoryIndex = index / MAX_ITEMS_WITH_BORDER;
			index = index - inventoryIndex * MAX_ITEMS_WITH_BORDER + 10;
			setBordered(index, inventoryIndex, getNextBorderIndex(index), itemStacks);
		} else {
			int inventoryIndex = index / MAX_ITEMS;
			index = index - inventoryIndex * MAX_ITEMS;
			setBorderless(index, inventoryIndex, itemStacks);
		}
	}

	public void set(int index, Iterable<ItemStack> itemStacks) {
		if (hasBorder()) {
			int inventoryIndex = index / MAX_ITEMS_WITH_BORDER;
			index = index - inventoryIndex * MAX_ITEMS_WITH_BORDER + 10;
			setBordered(index, inventoryIndex, getNextBorderIndex(index), itemStacks);
		} else {
			int inventoryIndex = index / MAX_ITEMS;
			index = index - inventoryIndex * MAX_ITEMS;
			setBorderless(index, inventoryIndex, itemStacks);
		}
	}

	private void setBordered(int index, int inventoryIndex, int borderIndex, ItemStack... itemStacks) {
		int size = itemStacks.length;
		Inventory inv = getInventory(inventoryIndex);
		for (int i = 0; i < size; i++) {
			if (index > LAST_BORDER_INDEX) {
				inv = getInventory(++inventoryIndex);
				index = 10;
				borderIndex = FIRST_BORDER_INDEX;
			} else if (index >= borderIndex) {
				index += 2;
				borderIndex = getNextBorderIndex(index);
			}
			inv.setItem(index, itemStacks[i]);
			index++;
		}
	}

	private void setBordered(int index, int inventoryIndex, int borderIndex, Iterable<ItemStack> itemStacks) {
		Inventory inv = getInventory(inventoryIndex);
		for (ItemStack itemStack : itemStacks) {
			if (index > LAST_BORDER_INDEX) {
				inv = getInventory(++inventoryIndex);
				index = 10;
				borderIndex = FIRST_BORDER_INDEX;
			} else if (index >= borderIndex) {
				index += 2;
				borderIndex = getNextBorderIndex(index);
			}
			inv.setItem(index, itemStack);
			index++;
		}
	}

	private void setBorderless(int index, int inventoryIndex, ItemStack... itemStacks) {
		int size = itemStacks.length;
		Inventory inv = getInventory(inventoryIndex);
		for (int i = 0; i < size; i++) {
			if (index >= MAX_ITEMS) {
				inv = getInventory(++inventoryIndex);
				index = 0;
			}
			inv.setItem(index, itemStacks[i]);
			index++;
		}
	}

	private void setBorderless(int index, int inventoryIndex, Iterable<ItemStack> itemStacks) {
		Inventory inv = getInventory(inventoryIndex);
		for (ItemStack itemStack : itemStacks) {
			if (index >= MAX_ITEMS) {
				inv = getInventory(++inventoryIndex);
				index = 0;
			}
			inv.setItem(index, itemStack);
			index++;
		}
	}

	public void set(int inventoryIndex, int index, ItemStack... itemStacks) {
		set(getInventory(inventoryIndex), index, itemStacks);
	}

	public void set(Inventory inv, ItemStack... itemStacks) {
		if (hasBorder())
			setInventoryBordered(inv, 10, FIRST_BORDER_INDEX, itemStacks);
		else {
			int size = itemStacks.length;
			for (int i = 0; i < size; i++)
				inv.setItem(i, itemStacks[i]);
		}
	}

	public void set(Inventory inv, int index, ItemStack... itemStacks) {
		if (hasBorder()) {
			index += 10;
			setInventoryBordered(inv, index, getNextBorderIndex(index), itemStacks);
		} else {
			int size = itemStacks.length;
			for (int i = 0; i < size; i++)
				inv.setItem(index++, itemStacks[i]);
		}
	}

	private void setInventoryBordered(Inventory inv, int index, int borderIndex, ItemStack... itemStacks) {
		int size = itemStacks.length;
		for (int i = 0; i < size; i++) {
			if (index >= borderIndex) {
				index += 2;
				borderIndex = getNextBorderIndex(index);
			}
			inv.setItem(index, itemStacks[i]);
			index++;
		}
	}

	public void set(Inventory inv, int index, ItemStack is) {
		inv.setItem(convertIndexToSlot(index), is);
	}

	public void set(int inventoryIndex, int index, int counter, int limiter,
			ExpandedIntFunction<ItemStack> isSupplier) {
		set(getInventory(inventoryIndex), index, counter, limiter, 1, isSupplier);
	}

	public void set(Inventory inv, int index, int counter, int limiter, ExpandedIntFunction<ItemStack> isSupplier) {
		set(inv, index, counter, limiter, 1, isSupplier);
	}

	public void set(int inventoryIndex, int index, int counter, int limiter, int add,
			ExpandedIntFunction<ItemStack> isSupplier) {
		set(getInventory(inventoryIndex), index, counter, limiter, add, isSupplier);
	}

	public void set(Inventory inv, int index, int counter, int limiter, int add,
			ExpandedIntFunction<ItemStack> isSupplier) {
		try {
			set_(inv, index, counter, limiter, add, isSupplier);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public <E extends Exception> void set_(int inventoryIndex, int index, int counter, int limiter,
			ThrowingIntFunction<ItemStack, E> isSupplier) throws E {
		set_(getInventory(inventoryIndex), index, counter, limiter, 1, isSupplier);
	}

	public <E extends Exception> void set_(Inventory inv, int index, int counter, int limiter,
			ThrowingIntFunction<ItemStack, E> isSupplier) throws E {
		set_(inv, index, counter, limiter, 1, isSupplier);
	}

	public <E extends Exception> void set_(int inventoryIndex, int index, int counter, int limiter, int add,
			ThrowingIntFunction<ItemStack, E> isSupplier) throws E {
		set_(getInventory(inventoryIndex), index, counter, limiter, add, isSupplier);
	}

	public <E extends Exception> void set_(Inventory inv, int index, int counter, int limiter, int add,
			ThrowingIntFunction<ItemStack, E> isSupplier) throws E {
		int endIndex = counter + getMaxInventoryItems();
		if (endIndex > limiter)
			endIndex = limiter;

		if (hasBorder()) {
			index += 10;
			int nextBorderIndex = getNextBorderIndex(index);
			for (; counter < endIndex; counter++, index += add) {
				if (index >= nextBorderIndex) {
					index += 2;
					nextBorderIndex = getNextBorderIndex(index);
				}
				inv.setItem(index, isSupplier.apply(counter));
			}
		} else {
			for (; counter < endIndex; counter++, index += add)
				inv.setItem(index, isSupplier.apply(counter));
		}
	}

	public <E extends SQLException> void set(int inventoryIndex, ResultSet resultSet,
			ThrowingFunction<ResultSet, ItemStack, E> isSupplier) throws SQLException {
		set(getInventory(inventoryIndex), resultSet, 1, isSupplier);
	}

	public <E extends SQLException> void set(Inventory inv, ResultSet resultSet,
			ThrowingFunction<ResultSet, ItemStack, E> isSupplier) throws SQLException {
		set(inv, resultSet, 1, isSupplier);
	}

	public <E extends SQLException> void set(int inventoryIndex, ResultSet resultSet, int add,
			ThrowingFunction<ResultSet, ItemStack, E> isSupplier) throws SQLException {
		set(getInventory(inventoryIndex), resultSet, add, isSupplier);
	}

	public <E extends SQLException> void set(Inventory inv, ResultSet resultSet, int add,
			ThrowingFunction<ResultSet, ItemStack, E> isSupplier) throws SQLException {
		int index = 0;
		if (hasBorder()) {
			index += 10;
			int nextBorderIndex = getNextBorderIndex(index);
			while (resultSet.next()) {
				if (index >= nextBorderIndex) {
					index += 2;
					nextBorderIndex = getNextBorderIndex(index);
				}
				inv.setItem(index, isSupplier.apply(resultSet));
				index += add;
			}
		} else {
			while (resultSet.next()) {
				inv.setItem(index, isSupplier.apply(resultSet));
				index += add;
			}
		}
	}

	public void remove(int index) {
		int maxInventoryItems = getMaxInventoryItems();
		int inventoryIndex = index / maxInventoryItems;
		Inventory prevInv = getInventory(inventoryIndex);
		int inventorySize = getSize() - 1;

		if (hasBorder()) {
			removeBordered(prevInv, index - inventoryIndex * maxInventoryItems);
			if (inventoryIndex < inventorySize) {
				do {
					Inventory nextInv = getInventory(++inventoryIndex);
					prevInv.setItem(LAST_BORDER_INDEX, nextInv.getItem(10));
					removeBordered(nextInv, 0);
					prevInv = nextInv;
				} while (inventoryIndex < inventorySize);
			}
		} else {
			removeBorderless(prevInv, index - inventoryIndex * maxInventoryItems);
			if (inventoryIndex < inventorySize) {
				do {
					Inventory nextInv = getInventory(++inventoryIndex);
					prevInv.setItem(LAST_BORDERLESS_INDEX, nextInv.getItem(0));
					removeButtonedBorderless(nextInv, 0);
					prevInv = nextInv;
				} while (inventoryIndex < inventorySize);
			}
		}
	}

	public void remove(Inventory inv, int index) {
		if (hasBorder())
			removeBordered(inv, index);
		else
			removeBorderless(inv, index);
	}

	public void removeBorderless(Inventory inv, int index) {
		int inventorySize = inv.getSize();
		int size;
		if (inventorySize > 1)
			size = inventorySize - 10;
		else
			size = inventorySize - 1;

		index++;
		for (; index < size; index++)
			inv.setItem(index - 1, inv.getItem(index));
	}

	public void removeButtonedBorderless(Inventory inv, int index) {
		int size = inv.getSize() - 10;
		index++;
		for (; index < size; index++)
			inv.setItem(index - 1, inv.getItem(index));
	}

	public void removeBordered(Inventory inv, int index) {
		int size = inv.getSize() - 10;
		index += 11;
		int nextBorderIndex = getNextBorderIndex(index);
		for (; index < size; index++) {
			if (index >= nextBorderIndex) {
				inv.setItem(index - 1, inv.getItem(index += 2));
				nextBorderIndex = getNextBorderIndex(index);
			} else
				inv.setItem(index - 1, inv.getItem(index));
		}
	}

	public ItemStack get(int index) {
		if (hasBorder())
			return getBordered(index);
		else
			return getBorderless(index);
	}

	public ItemStack getBorderless(int index) {
		int inventoryIndex = index / MAX_ITEMS;
		return getBorderless(getInventory(inventoryIndex), index - inventoryIndex * MAX_ITEMS);
	}

	public ItemStack getBordered(int index) {
		int inventoryIndex = index / MAX_ITEMS_WITH_BORDER;
		return getBordered(getInventory(inventoryIndex), index - inventoryIndex * MAX_ITEMS_WITH_BORDER);
	}

	public ItemStack get(Inventory inv, int index) {
		if (hasBorder())
			return getBordered(inv, index);
		else
			return getBorderless(inv, index);
	}

	public ItemStack getBorderless(Inventory inv, int index) {
		return inv.getItem(index);
	}

	public ItemStack getBordered(Inventory inv, int index) {
		return inv.getItem(convertIndexToSlot(index));
	}

	public void addBorderItem(int borderIndex, ItemStack is) {
		for (Inventory inv : getInventories())
			addBorderItem(inv, borderIndex, is);
	}

	public void addBorderItem(int inventoryIndex, int borderIndex, ItemStack is) {
		addBorderItem(getInventory(inventoryIndex), borderIndex, is);
	}

	public void addBorderItem(Inventory inv, int borderIndex, ItemStack is) {
		inv.setItem(convertBorderIndex(borderIndex, inv.getSize()), is);
	}

	public void addBorderItems(int[] borderIndexes, ItemStack... itemStacks) {
		for (Inventory inv : getInventories())
			addBorderItems(inv, borderIndexes, itemStacks);
	}

	public void addBorderItems(int inventoryIndex, int[] borderIndexes, ItemStack... itemStacks) {
		addBorderItems(getInventory(inventoryIndex), borderIndexes, itemStacks);
	}

	public void addBorderItems(Inventory inv, int[] borderIndexes, ItemStack... itemStacks) {
		int borderAdditor = getBorderAdditor(inv.getSize());
		int length = borderIndexes.length;
		for (int i = 0; i < length; i++)
			inv.setItem(convertBorderIndexPerAdditor(borderIndexes[i], borderAdditor), itemStacks[i]);
	}

	private static int convertBorderIndex(int borderIndex, int size) {
		if (borderIndex < 9)
			return borderIndex;
		return borderIndex + getBorderAdditor(size);
	}

	private static int convertBorderIndexPerAdditor(int borderIndex, int additor) {
		if (borderIndex < 9)
			return borderIndex;
		return borderIndex + additor;
	}

	private static int getBorderAdditor(int size) {
		if (size == 27)
			return 9;
		else if (size == 36)
			return 18;
		else if (size == 45)
			return 27;
		else if (size == 54)
			return 36;
		return 0;
	}

	public void setAnimatedBorder(boolean animated) {
		border = (byte) (animated ? 2 : 1);
	}

	@Override
	public void open(ServerPlayer serverPlayer) {
		open(serverPlayer.player);
	}

	@Override
	public void open(Player player) {
		player.openInventory(getInventory());
	}

	@Override
	public void open(HumanEntity player) {
		player.openInventory(getInventory());
	}

	public void open(ServerPlayer serverPlayer, int index) {
		open(serverPlayer.player, index);
	}

	public void open(Player player, int index) {
		player.openInventory(getInventory(index));
	}

	public void openNext(ServerPlayer serverPlayer, String inventoryName) {
		openNext(serverPlayer.player, inventoryName);
	}

	public void openNext(Player player, String inventoryName) {
		player.openInventory(getInventory(addToPageIndex(inventoryName)));
	}

	public void openPrev(ServerPlayer serverPlayer, String inventoryName) {
		openPrev(serverPlayer.player, inventoryName);
	}

	public void openPrev(Player player, String inventoryName) {
		player.openInventory(getInventory(substractFromPageIndex(inventoryName)));
	}

	@Override
	public void onInventoryOpen(InventoryOpenEvent event) {
		InventoryMenuHandler.inventoryOpen(this, event.getInventory());
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent event) {
		InventoryMenuHandler.inventoryClose(this, event.getInventory());
	}

	public void openBackLink(ServerPlayer serverPlayer) {
		backLink.accept(serverPlayer);
	}

	public void setBackLink(Consumer<ServerPlayer> backLink) {
		this.backLink = backLink;
	}

	public void setBackLink(Menu backLink) {
		this.backLink = backLink::open;
	}

	public void setBackLink(Inventory backLink) {
		this.backLink = (serverPlayer) -> serverPlayer.player.openInventory(backLink);
	}

	public void setBackLink(InventoryView backLink) {
		this.backLink = (serverPlayer) -> serverPlayer.player.openInventory(backLink);
	}

	public Consumer<ServerPlayer> getBackLink() {
		return backLink;
	}

	@Override
	public void onInventoryClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		playClickSound((Player) event.getWhoClicked());
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			Material material = is.getType();
			if (material == Material.GRAY_STAINED_GLASS_PANE || material == Material.LIGHT_GRAY_STAINED_GLASS_PANE) {
				event.setCancelled(true);
				openBackLink(serverPlayer);
				return;
			} else if (material == Material.LIME_STAINED_GLASS_PANE || material == Material.GREEN_STAINED_GLASS_PANE) {
				if (hasNextPrevButtons()) {
					event.setCancelled(true);
					openNext(serverPlayer, event.getView().getTitle());
					return;
				}
			} else if (material == Material.RED_STAINED_GLASS_PANE || material == Material.PINK_STAINED_GLASS_PANE) {
				if (hasNextPrevButtons()) {
					event.setCancelled(true);
					openPrev(serverPlayer, event.getView().getTitle());
					return;
				}
			}
		} else if (event.getSlot() == -999) {
			if (event.getCursor().getAmount() == 0)
				openBackLink(serverPlayer);
			return;
		}

		inventoryClickCallback(serverPlayer, event);
	}

	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		clickCallback.accept(serverPlayer, event);
	}

	public void setInventoryClickCallback(BiConsumer<ServerPlayer, InventoryClickEvent> clickCallback) {
		this.clickCallback = clickCallback;
	}

	@SafeVarargs
	public final void setInventoryClickCallback(BiConsumer<ServerPlayer, InventoryClickEvent>... clickCallbacks) {
		this.clickCallback = new ArrayClickCallback(clickCallbacks);
	}

	@SafeVarargs
	public final <T> void setInventoryClickCallback(T value,
			TriConsumer<ServerPlayer, InventoryClickEvent, T>... clickCallbacks) {
		this.clickCallback = new ObjectClickCallback<T>(value, clickCallbacks);
	}

	public BiConsumer<ServerPlayer, InventoryClickEvent> getInventoryClickCallback() {
		return clickCallback;
	}

	public void setAnimationHandler(InventoryMenuAnimationHandler animationHandler) {
		this.animationHandler = animationHandler;
	}

	public InventoryMenuAnimationHandler getAnimationHandler() {
		return animationHandler;
	}

	public boolean hasCustomAnimation() {
		return animationHandler != null;
	}

	public boolean hasNextPrevButtons() {
		return getSize() > 1;
	}

	public boolean hasBorder() {
		return border != 0;
	}

	public boolean hasAnimatedBorder() {
		return border == 2;
	}

	public int getMaxInventoryItems() {
		return hasBorder() ? MAX_ITEMS_WITH_BORDER : MAX_ITEMS;
	}

	public int getMaxInventoryItems(boolean hasBorder) {
		return hasBorder ? MAX_ITEMS_WITH_BORDER : MAX_ITEMS;
	}

	protected int addToPageIndex(String inventoryName) {
		int index = parseInventoryIndex(inventoryName);
		if (index >= getSize() - 1)
			return 0;
		return index + 1;
	}

	protected int substractFromPageIndex(String inventoryName) {
		int index = parseInventoryIndex(inventoryName);
		if (index <= 0)
			return getSize() - 1;
		return index - 1;
	}

	public static void createBorder(Inventory inventory) {
		createBorder(inventory, BORDER_ITEM, BORDER_ITEM_2);
	}

	public static void createBorder(Inventory inventory, ItemStack is) {
		for (int slot : getBorderSlots(inventory.getSize()))
			inventory.setItem(slot, is);
	}

	public static void createBorder(Inventory inventory, ItemStack is, ItemStack is2) {
		fillBorderSlotsAlternately(inventory, is, is2, getBorderSlots(inventory.getSize()));
	}

	public static void deleteBorder(Inventory inventory) {
		for (int slot : getBorderSlots(inventory.getSize()))
			inventory.setItem(slot, null);
	}

	public static void fillBorderSlotsAlternately(Inventory inventory, ItemStack is, ItemStack is2, int[] borderSlots) {
		int size = borderSlots.length;
		if (size % 2 == 0) {
			for (int i = 0; i < size; i += 2) {
				inventory.setItem(borderSlots[i], is);
				inventory.setItem(borderSlots[i + 1], is2);
			}
		} else {
			for (int i = 1; i < size; i += 2) {
				inventory.setItem(borderSlots[i - 1], is);
				inventory.setItem(borderSlots[i], is2);
			}
		}
	}

	public static void createBackButtons(Inventory inventory) {
		createBackButtons(inventory, BORDER_ITEM);
	}

	public static void createBackButtons(Inventory inventory, ItemStack is) {
		for (int slot : getBackSlots(inventory.getSize()))
			inventory.setItem(slot, is);
	}

	public static void createNextPrevButtons(Inventory inventory) {
		createNextPrevButtons(inventory, NEXT_ITEM, PREV_ITEM);
	}

	public static void createNextPrevButtons(Inventory inventory, ItemStack is) {
		createNextPrevButtons(inventory, is, is);
	}

	public static void createNextPrevButtons(Inventory inventory, ItemStack next, ItemStack prev) {
		int size = inventory.getSize();
		if (size == 54) {
			for (int slot : NEXT_SLOTS_5)
				inventory.setItem(slot, next);
			for (int slot : PREV_SLOTS_5)
				inventory.setItem(slot, prev);
		} else if (size == 45) {
			for (int slot : NEXT_SLOTS_4)
				inventory.setItem(slot, next);
			for (int slot : PREV_SLOTS_4)
				inventory.setItem(slot, prev);
		} else if (size == 36) {
			for (int slot : NEXT_SLOTS_3)
				inventory.setItem(slot, next);
			for (int slot : PREV_SLOTS_3)
				inventory.setItem(slot, prev);
		} else if (size == 27) {
			for (int slot : NEXT_SLOTS_2)
				inventory.setItem(slot, next);
			for (int slot : PREV_SLOTS_2)
				inventory.setItem(slot, prev);
		} else if (size == 18) {
			for (int slot : NEXT_SLOTS)
				inventory.setItem(slot, next);
			for (int slot : PREV_SLOTS)
				inventory.setItem(slot, prev);
		}
	}

	public static int getNextBorderIndex(int index) {
		if (index < 17)
			return 17;
		else if (index < 26)
			return 26;
		else if (index < 35)
			return 35;
		else if (index < 44)
			return 44;
		return 100;
	}

	public static int convertSlotToIndex(int slot) {
		if (slot > 45)
			return slot - 18;
		else if (slot > 36)
			return slot - 16;
		else if (slot > 27)
			return slot - 14;
		else if (slot > 18)
			return slot - 12;
		else
			return slot - 10;
	}

	public static int convertIndexToSlot(int index) {
		if (index > 27)
			return index + 18;
		else if (index > 20)
			return index + 16;
		else if (index > 13)
			return index + 14;
		else if (index > 6)
			return index + 12;
		else
			return index + 10;
	}

	public static int skipBorderSlot(int slot) {
		if (slot == 17)
			return 19;
		else if (slot == 26)
			return 28;
		else if (slot == 35)
			return 37;
		else if (slot == 44)
			return 46;
		return slot;
	}

	public static int[] getBorderSlots(int size) {
		if (size == 54)
			return BORDER_SLOTS_4;
		else if (size == 45)
			return BORDER_SLOTS_3;
		else if (size == 36)
			return BORDER_SLOTS_2;
		else if (size == 27)
			return BORDER_SLOTS;
		return null;
	}

	public static int[] getBorderSlotsNoNextPrev(int size) {
		if (size == 54)
			return BORDER_SLOTS_NO_NEXT_PREV_4;
		else if (size == 45)
			return BORDER_SLOTS_NO_NEXT_PREV_3;
		else if (size == 36)
			return BORDER_SLOTS_NO_NEXT_PREV_2;
		else if (size == 27)
			return BORDER_SLOTS_NO_NEXT_PREV;
		return null;
	}

	public static int[] getBackSlots(int size) {
		if (size == 54)
			return BACK_SLOTS_5;
		else if (size == 45)
			return BACK_SLOTS_4;
		else if (size == 36)
			return BACK_SLOTS_3;
		else if (size == 27)
			return BACK_SLOTS_2;
		else if (size == 18)
			return BACK_SLOTS;
		return null;
	}

	public static int parseInventoryIndex(String inventoryName) {
		try {
			return Integer
					.parseInt(inventoryName
							.substring(inventoryName.lastIndexOf(NAME_SEPERATOR) + 1, inventoryName.length()).trim())
					- 1;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static int parseIndex(String inventoryName, int slot, boolean hasBorder) {
		if (hasBorder)
			return parseBorderedIndex(inventoryName, slot);
		return parseIndex(inventoryName, slot);
	}

	public static int parseIndex(String inventoryName, int slot) {
		return parseIndex(inventoryName, slot, MAX_ITEMS);
	}

	public static int parseBorderedIndex(String inventoryName, int slot) {
		return parseBorderedIndex(inventoryName, slot, MAX_ITEMS_WITH_BORDER);
	}

	public static int parseIndex(String inventoryName, int slot, int maxItems) {
		return slot + parseInventoryIndex(inventoryName) * maxItems;
	}

	public static int parseBorderedIndex(String inventoryName, int slot, int maxItems) {
		return convertSlotToIndex(slot) + parseInventoryIndex(inventoryName) * maxItems;
	}

	public static int parseIndex(String inventoryName, int slot, int divider, int maxItems) {
		return slot / divider + parseInventoryIndex(inventoryName) * maxItems;
	}

	public static int parseBorderedIndex(String inventoryName, int slot, int divider, int maxItems) {
		return convertSlotToIndex(slot) / divider + parseInventoryIndex(inventoryName) * maxItems;
	}

	public static int calculateRowCount(int itemSize, boolean hasBorder) {
		if (itemSize == 0)
			itemSize = 1;

		if (hasBorder)
			return (int) Math.ceil((double) itemSize / 7) + 2;
		else
			return (int) Math.ceil((double) itemSize / 9) + 1;
	}

	public static boolean isSlotInCustomInventory(Inventory inventory, int slot) {
		if (slot < inventory.getSize())
			return true;
		return false;
	}

	public static boolean isSlotInCustomInventory(Inventory inventory, Set<Integer> slots) {
		for (Integer slot : slots) {
			if (slot < inventory.getSize())
				return true;
		}
		return false;
	}

	public static void displayInSlot(Inventory inv, ItemStack is, ItemStack prev, int slot, int ticks) {
		inv.setItem(slot, is);
		Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(), () -> inv.setItem(slot, prev), ticks);
	}

	public static void displayInSlot(Inventory inv, ItemStack is, int slot, int ticks) {
		displayInSlot(inv, is, inv.getItem(slot), slot, ticks);
	}

	public static void displayInSlot(InventoryClickEvent event, ItemStack is, int ticks) {
		Inventory inv = event.getInventory();
		int slot = event.getSlot();
		displayInSlot(inv, is, inv.getItem(slot), slot, ticks);
	}

	public static void displayInSlot(InventoryClickEvent event, ItemStack is, ItemStack prev, int ticks) {
		displayInSlot(event.getInventory(), is, prev, event.getSlot(), ticks);
	}

	public static void displayInSlot(Player player, ItemStack is, int slot) {
		int windowId = ((CraftPlayer) player).getHandle().activeContainer.windowId;
		PacketSender.send(player, PacketFactory.packetSetSlot(windowId, slot, is));
	}

	public static void displayInSlot(Player player, ItemStack is, ItemStack prev, int slot, int ticks) {
		int windowId = ((CraftPlayer) player).getHandle().activeContainer.windowId;
		PacketSender.send(player, PacketFactory.packetSetSlot(windowId, slot, is));
		Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(),
				() -> PacketSender.send(player, PacketFactory.packetSetSlot(windowId, slot, prev)), ticks);
	}

	public static int[] buildSlotArray(Inventory inv, int except) {
		return buildSlotArray(getBorderSlots(inv.getSize()), except);
	}

	public static int[] buildSlotArray(int[] source, int except) {
		int[] newArr = new int[source.length - 1];
		int added = 0;
		for (int i : source) {
			if (i == except)
				continue;
			newArr[added++] = i;
		}
		return newArr;
	}

	public static int[] buildSlotArray(Inventory inv, int... except) {
		return buildSlotArray(getBorderSlots(inv.getSize()), except);
	}

	public static int[] buildSlotArray(int[] source, int... except) {
		int[] newArr = new int[source.length - except.length];
		int added = 0;
		for (int i : source) {
			boolean shouldContinue = false;
			for (int e : except) {
				if (i == e) {
					shouldContinue = true;
					break;
				}
			}
			if (shouldContinue)
				continue;

			newArr[added++] = i;
		}
		return newArr;
	}

	public class ArrayClickCallback implements BiConsumer<ServerPlayer, InventoryClickEvent> {

		protected BiConsumer<ServerPlayer, InventoryClickEvent>[] callbacks;

		@SafeVarargs
		public ArrayClickCallback(BiConsumer<ServerPlayer, InventoryClickEvent>... clickCallbacks) {
			this.callbacks = clickCallbacks;
		}

		@SuppressWarnings("unchecked")
		public ArrayClickCallback(int size) {
			this.callbacks = new BiConsumer[size];
		}

		@Override
		public void accept(ServerPlayer serverPlayer, InventoryClickEvent event) {
			BiConsumer<ServerPlayer, InventoryClickEvent> callback = callbacks[parseIndex(event.getView().getTitle(),
					event.getSlot(), hasBorder())];
			if (callback != null)
				callback.accept(serverPlayer, event);
		}

		@SafeVarargs
		public final void setCallbacks(BiConsumer<ServerPlayer, InventoryClickEvent>... callbacks) {
			this.callbacks = callbacks;
		}

		@SafeVarargs
		public final void setCallbacks(int index, BiConsumer<ServerPlayer, InventoryClickEvent>... callbacks) {
			for (int i = 0; i < callbacks.length; i++)
				this.callbacks[index++] = callbacks[i];
		}

		public final BiConsumer<ServerPlayer, InventoryClickEvent>[] getCallbacks() {
			return callbacks;
		}

		public final void setCallback(int index, BiConsumer<ServerPlayer, InventoryClickEvent> callback) {
			callbacks[index] = callback;
		}

		public final BiConsumer<ServerPlayer, InventoryClickEvent> getCallback(int index) {
			return callbacks[index];
		}
	}

	public class ObjectClickCallback<T> implements BiConsumer<ServerPlayer, InventoryClickEvent> {

		protected T value;
		protected TriConsumer<ServerPlayer, InventoryClickEvent, T>[] callbacks;

		@SafeVarargs
		public ObjectClickCallback(T value, TriConsumer<ServerPlayer, InventoryClickEvent, T>... clickCallbacks) {
			this.value = value;
			this.callbacks = clickCallbacks;
		}

		@SuppressWarnings("unchecked")
		public ObjectClickCallback(T value, int size) {
			this.value = value;
			this.callbacks = new TriConsumer[size];
		}

		@Override
		public void accept(ServerPlayer serverPlayer, InventoryClickEvent event) {
			TriConsumer<ServerPlayer, InventoryClickEvent, T> callback = callbacks[parseIndex(
					event.getView().getTitle(), event.getSlot(), hasBorder())];
			if (callback != null)
				callback.accept(serverPlayer, event, value);
		}

		@SafeVarargs
		public final void setCallbacks(TriConsumer<ServerPlayer, InventoryClickEvent, T>... callbacks) {
			this.callbacks = callbacks;
		}

		@SafeVarargs
		public final void setCallbacks(int index, TriConsumer<ServerPlayer, InventoryClickEvent, T>... callbacks) {
			for (int i = 0; i < callbacks.length; i++)
				this.callbacks[index++] = callbacks[i];
		}

		public final TriConsumer<ServerPlayer, InventoryClickEvent, T>[] getCallbacks() {
			return callbacks;
		}

		public final void setCallback(int index, TriConsumer<ServerPlayer, InventoryClickEvent, T> callback) {
			callbacks[index] = callback;
		}

		public final TriConsumer<ServerPlayer, InventoryClickEvent, T> getCallback(int index) {
			return callbacks[index];
		}
	}
}
