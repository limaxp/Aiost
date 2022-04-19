package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.Arrays;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.CreateItemMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.other.Banner;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;

public class ItemEnchantmentsMenu extends SingleInventoryMenu {

	private static final ItemStack ADD_ENCHANTMENT_ITEM = Banner.create(Material.BLACK_BANNER,
			GRAY + BOLD + "Add enchantment", Arrays.asList(GRAY + "Click to add a enchantment"),
			Banner.plusPattern(DyeColor.BLACK, DyeColor.WHITE));

	public ItemEnchantmentsMenu(ServerPlayer serverPlayer) {
		super(BOLD + "Enchantments", 6, false);
		NBTTagList nbtList = getList(NBTHelper.getNBT(ItemNBTMenu.getItem(serverPlayer)));
		int size = nbtList.size();
		ItemStack[] items = new ItemStack[size + 1];
		for (int i = 0; i < size; i++) {
			NBTTagCompound enchantment = nbtList.getCompound(i);
			items[i] = createItem(enchantment.getString(NBTHelper.ID_KEY), enchantment.getShort(NBTHelper.LEVEL_KEY));
		}
		items[size] = ADD_ENCHANTMENT_ITEM;
		set(items);
		setBackLink((serverPlayer1) -> serverPlayer1.getMenu(CreateItemMenu.class).open(serverPlayer1));
	}

	private static ItemStack createItem(String id, short level) {
		return MetaHelper.setMeta(Material.ENCHANTED_BOOK,
				GRAY + BOLD + id.substring(id.indexOf(':') + 1) + " " + level, Arrays.asList(DARK_GRAY + id,
						GRAY + "Click to change enchantment level", GRAY + "Shift click to remove enchantment"));
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			if (is.getType() == Material.BLACK_BANNER)
				addEnchantmentClick(serverPlayer, is, event);
			else {
				ClickType click = event.getClick();
				if (click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT)
					removeEnchantmentClick(serverPlayer, is, event);
				else
					createLevelMenu(serverPlayer, is).open(serverPlayer);
			}
		}
	}

	private void addEnchantmentClick(ServerPlayer serverPlayer, ItemStack is, InventoryClickEvent event) {
		serverPlayer.doMenuRequest(new SingleMenuRequest(EnumerationMenus.ENCHANTMENT_MENU) {

			@Override
			public void openRequest(ServerPlayer serverPlayer) {
				serverPlayer.player.openInventory(event.getInventory());
			}

			@Override
			public void onResult(ServerPlayer serverPlayer, Object obj) {
				Inventory inv = event.getInventory();
				int slot = event.getSlot();
				inv.setItem(slot + 1, is);
				NamespacedKey key = ((Enchantment) obj).getKey();
				inv.setItem(slot, createItem(key.toString(), (short) 1));
				ItemNBTMenu.modifyNBT(serverPlayer,
						(nbtTag) -> NBTHelper.setEnchantment(getList(nbtTag), key.toString(), (short) 1));
			}
		});
	}

	private void removeEnchantmentClick(ServerPlayer serverPlayer, ItemStack is, InventoryClickEvent event) {
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
		ItemNBTMenu.modifyNBT(serverPlayer, (nbtTag) -> NBTHelper.removeEnchantment(getList(nbtTag),
				is.getItemMeta().getLore().get(0).substring(2)));
	}

	private AnvilMenu createLevelMenu(ServerPlayer serverPlayer, ItemStack is) {
		ItemMeta im = is.getItemMeta();
		String name = im.getDisplayName();
		int lastSpaceIndex = name.lastIndexOf(' ');
		AnvilMenu menu = new AnvilMenu(BOLD + "Enchantment Level",
				MetaHelper.setMeta(Material.PAPER, name.substring(lastSpaceIndex + 1))) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					short level;
					try {
						level = Short.parseShort(event.getCurrentItem().getItemMeta().getDisplayName());
					} catch (NumberFormatException e) {
						serverPlayer.player.sendMessage(ChatColor.RED + "Your input must be a number!");
						return;
					}
					String id = im.getLore().get(0).substring(2);
					ItemNBTMenu.modifyNBT(serverPlayer,
							(nbtTag) -> NBTHelper.setEnchantment(getList(nbtTag), id, level));
					MetaHelper.setMeta(is, name.substring(0, lastSpaceIndex) + ' ' + level);
					ItemEnchantmentsMenu.this.open(serverPlayer);
				}
			}
		};
		menu.setBackLink(ItemEnchantmentsMenu.this);
		return menu;
	}

	protected NBTTagList getList(NBTTagCompound nbtTag) {
		return NBTHelper.getEnchantmentList(nbtTag);
	}
}
