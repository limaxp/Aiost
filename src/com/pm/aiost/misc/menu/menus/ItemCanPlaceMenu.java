package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.Arrays;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.other.Banner;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;

public class ItemCanPlaceMenu extends SingleInventoryMenu {

	private static final ItemStack ADD_BLOCK_ITEM = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Add block",
			Arrays.asList(GRAY + "Click to add a block"), Banner.plusPattern(DyeColor.BLACK, DyeColor.WHITE));

	public ItemCanPlaceMenu(ServerPlayer serverPlayer) {
		this("Can be placed on", serverPlayer);
	}

	public ItemCanPlaceMenu(String name, ServerPlayer serverPlayer) {
		super(BOLD + name, 6, false);
		NBTTagList nbtList = getList(NBTHelper.getNBT(ItemNBTMenu.getItem(serverPlayer)));
		int size = nbtList.size();
		ItemStack[] items = new ItemStack[size + 1];
		for (int i = 0; i < size; i++)
			items[i] = new ItemStack(NBTHelper.stringToMaterial(nbtList.getString(i)));
		items[size] = ADD_BLOCK_ITEM;
		set(items);
		setBackLink(ItemNBTMenu.getMenu());
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			if (is.getType() == Material.BLACK_BANNER)
				addBlockClick(serverPlayer, is, event);
			else
				removeBlockClick(serverPlayer, is, event);
		}
	}

	private void addBlockClick(ServerPlayer serverPlayer, ItemStack is, InventoryClickEvent event) {
		serverPlayer.doMenuRequest(new SingleMenuRequest(EnumerationMenus.BLOCK_MENU) {

			@Override
			public void openRequest(ServerPlayer serverPlayer) {
				serverPlayer.player.openInventory(event.getInventory());
			}

			@Override
			public void onResult(ServerPlayer serverPlayer, Object obj) {
				Material material = (Material) obj;
				Inventory inv = event.getInventory();
				int slot = event.getSlot();
				inv.setItem(slot + 1, is);
				inv.setItem(slot, new ItemStack(material));
				ItemNBTMenu.modifyNBT(serverPlayer, (nbtTag) -> NBTHelper.addMaterial(getList(nbtTag), material));
			}
		});
	}

	private void removeBlockClick(ServerPlayer serverPlayer, ItemStack is, InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		for (int i = event.getSlot() + 1; i < InventoryMenu.MAX_ITEMS; i++) {
			ItemStack item = inv.getItem(i);
			if (item != null)
				inv.setItem(i - 1, item);
			else {
				inv.setItem(i - 1, null);
				break;
			}
		}
		ItemNBTMenu.modifyNBT(serverPlayer, (nbtTag) -> NBTHelper.removeMaterial(getList(nbtTag), is.getType()));
	}

	protected NBTTagList getList(NBTTagCompound nbtTag) {
		return NBTHelper.getCanPlaceOnList(nbtTag);
	}

	public static class ItemCanDestroyMenu extends ItemCanPlaceMenu {

		public ItemCanDestroyMenu(ServerPlayer serverPlayer) {
			super("Can destroy", serverPlayer);
		}

		@Override
		protected NBTTagList getList(NBTTagCompound nbtTag) {
			return NBTHelper.getCanDestroyList(nbtTag);
		}
	}
}
