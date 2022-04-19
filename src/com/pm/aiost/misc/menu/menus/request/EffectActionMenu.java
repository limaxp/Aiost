package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.bytes.ByteArrayList;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.bytes.ByteList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.event.effect.EffectAction;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.LazyInventoryMenu;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class EffectActionMenu extends LazyInventoryMenu {

	private static final ItemStack[] ITEMSTACKS;
	private static final ItemStack[] ACTIVATED_ITEMSTACKS;

	private static final ItemStack ACCEPT_ITEM = MetaHelper.setMeta(Material.NETHER_STAR,
			GOLD + BOLD + "Accept actions", Arrays.asList(GRAY + "Click to accept your actions"));

	private static final ItemStack NO_ACTION_ITEM = MetaHelper.setMeta(Material.BARRIER,
			RED + BOLD + "No action choosen!", Arrays.asList(GRAY + "You have to choose an action first"));

	private static final ItemStack RESET_ITEM = MetaHelper.setMeta(Material.LAVA_BUCKET, RED + BOLD + "Reset actions",
			Arrays.asList(GRAY + "Click to reset all actions"));

	private static final int MAX_ITEMS_PER_INVENTORY = InventoryMenu.MAX_ITEMS_WITH_BORDER - 2;

	static {
		List<Byte> effectActions = EffectAction.getMainActions();
		int size = effectActions.size();
		ITEMSTACKS = new ItemStack[size];
		ACTIVATED_ITEMSTACKS = new ItemStack[size];
		List<String> offLore = Arrays.asList(GRAY + "Click to choose this action");
		List<String> onLore = Arrays.asList(GRAY + "Click to remove this action");
		for (int i = 0; i < size; i++) {
			String name = EffectAction.getName(effectActions.get(i)).replace("_", " ");
			name = GRAY + BOLD + name.substring(0, 1).toUpperCase() + name.substring(1);
			ITEMSTACKS[i] = MetaHelper.setMeta(Material.GRAY_DYE, name, offLore);
			ACTIVATED_ITEMSTACKS[i] = MetaHelper.setMeta(Material.LIME_DYE, name, onLore);
		}
	}

	private ByteList actionList;

	public EffectActionMenu() {
		super(BOLD + "Choose Actions", EffectAction.getMainActions().size(), true);
		actionList = new ByteArrayList();
		setBackLink(ServerPlayer::openMenuRequestPrevMenu);
	}

	@Override
	public void buildInventory(Inventory inv, int index) {
		set(inv, 0, index * MAX_ITEMS_PER_INVENTORY, EffectAction.getMainActions().size(), (actionIndex) -> {
			return ITEMSTACKS[actionIndex];
		});
		int size = inv.getSize();
		inv.setItem(size - 11, ACCEPT_ITEM);
		inv.setItem(size - 12, RESET_ITEM);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getCurrentItem() != null) {
			int slot = event.getSlot();
			int size = event.getInventory().getSize();
			if (slot == size - 11)
				accept(serverPlayer, event);
			else if (slot == size - 12)
				reset(event);
			else
				actionClick(event, slot);
		}
	}

	protected void accept(ServerPlayer serverPlayer, InventoryClickEvent event) {
		if (actionList.size() > 0)
			serverPlayer.setMenuRequestResult(buildActionArray());
		else
			displayInSlot(event, NO_ACTION_ITEM, ACCEPT_ITEM, 30);
	}

	protected byte[] buildActionArray() {
		int size = actionList.size();
		byte[] arr = new byte[size];
		List<Byte> mainActions = EffectAction.getMainActions();
		for (int i = 0; i < size; i++)
			arr[i] = mainActions.get(actionList.getByte(i));
		return arr;
	}

	protected void reset(InventoryClickEvent event) {
		if (actionList.size() > 0) {
			for (byte b : actionList) {
				int inventoryIndex = b / MAX_ITEMS_PER_INVENTORY;
				getInventory(inventoryIndex).setItem(convertIndexToSlot(b - inventoryIndex * MAX_ITEMS_PER_INVENTORY),
						ITEMSTACKS[b]);
			}
			actionList.clear();
		}
	}

	protected void actionClick(InventoryClickEvent event, int slot) {
		byte action = (byte) parseBorderedIndex(event.getView().getTitle(), slot, MAX_ITEMS_PER_INVENTORY);
		if (actionList.contains(action)) {
			actionList.removeByte(actionList.indexOf(action));
			event.getInventory().setItem(slot, ITEMSTACKS[action]);
		} else {
			actionList.add(action);
			event.getInventory().setItem(slot, ACTIVATED_ITEMSTACKS[action]);
		}
	}
}
