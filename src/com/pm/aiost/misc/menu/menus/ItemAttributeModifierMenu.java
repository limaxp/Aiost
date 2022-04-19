package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pm.aiost.collection.list.FastArrayList;
import com.pm.aiost.item.custom.Slot;
import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.ArrayInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.misc.utils.nbt.GenericAttribute;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;

public class ItemAttributeModifierMenu extends ArrayInventoryMenu {

	public ItemAttributeModifierMenu(ServerPlayer serverPlayer) {
		super(BOLD + "Attribute modifiers", GenericAttribute.size() * 9, false);
		setCategories(BORDER_ITEM, GenericAttribute.getItems());
		NBTTagList nbtList = getList(NBTHelper.getNBT(ItemNBTMenu.getItem(serverPlayer)));
		@SuppressWarnings("unchecked")
		List<ItemStack>[] items = new List[GenericAttribute.size()];
		int size = nbtList.size();
		for (int i = 0; i < size; i++) {
			NBTTagCompound attribute = nbtList.getCompound(i);
			GenericAttribute genericAttribute = GenericAttribute.get(attribute.getString(NBTHelper.ATTRIBUTE_NAME_KEY));
			int attributeId = genericAttribute.id;
			List<ItemStack> list = items[attributeId];
			if (list == null) {
				list = new FastArrayList<ItemStack>();
				items[attributeId] = list;
			}
			list.add(createItem(genericAttribute, attribute.getString(NBTHelper.SLOT_KEY),
					attribute.getDouble(NBTHelper.AMOUNT_KEY)));
		}
		setCategorized(items);
		setBackLink(ItemNBTMenu.getMenu());
	}

	private static ItemStack createItem(GenericAttribute attribute, String slot, double value) {
		return MetaHelper.setMeta(attribute.item.clone(),
				Arrays.asList(DARK_GRAY + slot, DARK_GRAY + value, GRAY + "Left click to change attribute value",
						GRAY + "Right click to change attribute slot", GRAY + "Shift click to remove attribute"));
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			int slot = event.getSlot();
			int row = slot / 9;
			int index = InventoryMenu.parseInventoryIndex(event.getView().getTitle()) * 5 + row;
			if (slot == row * 9) {
				addAttributeClick(serverPlayer, index, row, event);
				return;
			}
			ClickType click = event.getClick();
			if (click == ClickType.LEFT)
				createValueMenu(serverPlayer, is, index).open(serverPlayer);
			if (click == ClickType.RIGHT)
				changeSlotClick(serverPlayer, is, index, event);
			if (click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT)
				removeClick(serverPlayer, is, slot, index, row, event);
		}
	}

	private void addAttributeClick(ServerPlayer serverPlayer, int attributeId, int row, InventoryClickEvent event) {
		GenericAttribute attribute = GenericAttribute.get(attributeId);
		Inventory inv = event.getInventory();
		int rowIndex = row * 9;
		int lastIndex = rowIndex + 9;
		for (int i = rowIndex + 2; i < lastIndex; i++) {
			if (inv.getItem(i) == null) {
				String slotName = Slot.MAIN_HAND.name;
				inv.setItem(i, createItem(attribute, slotName, (short) 1));
				ItemNBTMenu.modifyNBT(serverPlayer,
						(nbtTag) -> NBTHelper.addAttributeModifier(getList(nbtTag), attribute.name, 1.0, slotName));
				return;
			}
		}
		serverPlayer.player.sendMessage(RED + "Cannot add more attributes!");
	}

	private AnvilMenu createValueMenu(ServerPlayer serverPlayer, ItemStack is, int attributeId) {
		ItemMeta im = is.getItemMeta();
		List<String> lore = im.getLore();
		AnvilMenu menu = new AnvilMenu(BOLD + "Attribute value",
				MetaHelper.setMeta(Material.PAPER, lore.get(1).substring(2))) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					double value;
					try {
						value = Double.parseDouble(event.getCurrentItem().getItemMeta().getDisplayName());
					} catch (NumberFormatException e) {
						serverPlayer.player.sendMessage(ChatColor.RED + "Your input must be a number!");
						return;
					}
					GenericAttribute attribute = GenericAttribute.get(attributeId);
					ItemNBTMenu.modifyNBT(serverPlayer, (nbtTag) -> NBTHelper.setAttributeModifier(getList(nbtTag),
							attribute.name, lore.get(0).substring(2), value));
					lore.set(1, DARK_GRAY + value);
					im.setLore(lore);
					is.setItemMeta(im);
					ItemAttributeModifierMenu.this.open(serverPlayer);
				}
			}
		};
		menu.setBackLink(ItemAttributeModifierMenu.this);
		return menu;
	}

	private void changeSlotClick(ServerPlayer serverPlayer, ItemStack is, int attributeId, InventoryClickEvent event) {
		serverPlayer.doMenuRequest(new SingleMenuRequest(EnumerationMenus.EQUIPMENT_SLOT_MENU) {

			@Override
			public void openRequest(ServerPlayer serverPlayer) {
				serverPlayer.player.openInventory(event.getInventory());
			}

			@Override
			public void onResult(ServerPlayer serverPlayer, Object obj) {
				String slot = ((Slot) obj).name;
				ItemMeta im = is.getItemMeta();
				List<String> lore = im.getLore();
				lore.set(0, DARK_GRAY + slot);
				im.setLore(lore);
				is.setItemMeta(im);
				GenericAttribute attribute = GenericAttribute.get(attributeId);
				ItemNBTMenu.modifyNBT(serverPlayer, (nbtTag) -> NBTHelper.setAttributeModifierSlot(getList(nbtTag),
						attribute.name, slot, Double.parseDouble(lore.get(1).substring(2))));
			}
		});
	}

	private void removeClick(ServerPlayer serverPlayer, ItemStack is, int slot, int attributeId, int row,
			InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		int rowIndex = row * 9;
		int lastIndex = rowIndex + 9;
		int i;
		for (i = slot + 1; i < lastIndex; i++) {
			ItemStack item = inv.getItem(i);
			if (item != null)
				inv.setItem(i - 1, item);
			else
				break;
		}
		inv.setItem(i - 1, null);
		GenericAttribute attribute = GenericAttribute.get(attributeId);
		ItemNBTMenu.modifyNBT(serverPlayer, (nbtTag) -> NBTHelper.removeAttributeModifier(getList(nbtTag),
				attribute.name, is.getItemMeta().getLore().get(0).substring(2)));
	}

	protected NBTTagList getList(NBTTagCompound nbtTag) {
		return NBTHelper.getAttributeModifiersList(nbtTag);
	}
}
