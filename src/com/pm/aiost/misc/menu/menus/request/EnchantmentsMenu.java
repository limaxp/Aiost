package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.collection.list.IdentityArrayList;
import com.pm.aiost.misc.menu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.utils.Banner;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class EnchantmentsMenu extends SingleInventoryMenu {

	private static final ItemStack ADD_ENCHANTMENT_ITEM = Banner.create(Material.BLACK_BANNER,
			GRAY + BOLD + "Add enchantment", Arrays.asList(GRAY + "Click to add a enchantment"),
			Banner.plusPattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack ACCEPT_ITEM = MetaHelper.setMeta(Material.NETHER_STAR,
			GOLD + BOLD + "Accept enchantments", Arrays.asList(GRAY + "Click to accept enchantments"));

	private final Map<Enchantment, Integer> enchantments;
	private final List<Enchantment> list;

	public EnchantmentsMenu(Map<Enchantment, Integer> enchantments) {
		super(BOLD + "Enchantments", 6, false);
		this.enchantments = new IdentityHashMap<Enchantment, Integer>(enchantments);
		int size = enchantments.size();
		this.list = new IdentityArrayList<Enchantment>(size);
		ItemStack[] items = new ItemStack[size + 1];
		int i = 0;
		for (Entry<Enchantment, Integer> enchantment : enchantments.entrySet()) {
			items[i++] = createItem(enchantment.getKey(), enchantment.getValue());
			list.add(enchantment.getKey());
		}

		items[size] = ADD_ENCHANTMENT_ITEM;
		set(items);
		addBorderItem(17, ACCEPT_ITEM);
		setBackLink(ServerPlayer::openMenuRequestPrev);
	}

	private static ItemStack createItem(Enchantment ench, int level) {
		return MetaHelper.setMeta(Material.ENCHANTED_BOOK, GRAY + BOLD + ench.getName().toLowerCase() + " " + level,
				Arrays.asList(GRAY + "Click to change enchantment level", GRAY + "Shift click to remove enchantment"));
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			if (is.getType() == Material.NETHER_STAR) {
				serverPlayer.setMenuRequestResult(enchantments);
				return;
			}

			if (is.getType() == Material.BLACK_BANNER)
				addEnchantmentClick(serverPlayer, event);
			else {
				ClickType click = event.getClick();
				if (click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT)
					removeEnchantmentClick(event);
				else
					createLevelMenu(event).open(serverPlayer);
			}
		}
	}

	private void addEnchantmentClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		serverPlayer.menuRequest(new SingleMenuRequest(EnumerationMenus.ENCHANTMENT_MENU, this::open, false) {

			@Override
			public void onResult(ServerPlayer serverPlayer, Object obj) {
				Enchantment ench = (Enchantment) obj;
				Inventory inv = event.getInventory();
				int slot = event.getSlot();
				inv.setItem(slot + 1, event.getCurrentItem());
				inv.setItem(slot, createItem(ench, 1));
				enchantments.put(ench, 1);
				list.add(ench);
			}
		});
	}

	private void removeEnchantmentClick(InventoryClickEvent event) {
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
		enchantments.remove(list.remove(getIndex(event)));
	}

	private AnvilMenu createLevelMenu(InventoryClickEvent lastEvent) {
		Enchantment ench = list.get(getIndex(lastEvent));
		AnvilMenu menu = new AnvilMenu(BOLD + "Enchantment Level", MetaHelper.setMeta(Material.PAPER, "_"));
		menu.setBackLink(EnchantmentsMenu.this);
		menu.setClickCallback((serverPlayer, event) -> {
			int level;
			try {
				level = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName());
			} catch (NumberFormatException e) {
				serverPlayer.player.sendMessage(ChatColor.RED + "Your input must be a number!");
				return;
			}

			MetaHelper.setMeta(lastEvent.getCurrentItem(), GRAY + BOLD + ench.getName().toLowerCase() + ' ' + level);
			enchantments.replace(ench, level);
			EnchantmentsMenu.this.open(serverPlayer);
		});
		return menu;
	}

	private int getIndex(InventoryClickEvent event) {
		return InventoryMenu.parseIndex(event.getView().getTitle(), event.getSlot());
	}
}
