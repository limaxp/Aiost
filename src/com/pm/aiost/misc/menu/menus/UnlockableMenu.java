package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_PURPLE;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.BitSet;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.LazyInventoryMenu;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.player.unlockable.UnlockableType;

public class UnlockableMenu extends LazyInventoryMenu {

	private static final String DEACTIVATE_TEXT = GRAY + "Click to " + RED + BOLD + "deactivate";
	private static final String ACTIVATED_TEXT = GRAY + "Status: " + DARK_PURPLE + BOLD + "activated";

	protected final ServerPlayer serverPlayer;
	protected final UnlockableType<?> unlockableType;

	public UnlockableMenu(ServerPlayer serverPlayer, UnlockableType<?> unlockableType) {
		super(BOLD + unlockableType.name, unlockableType.size() - 1, false);
		this.serverPlayer = serverPlayer;
		this.unlockableType = unlockableType;
	}

	@Override
	public void buildInventory(Inventory inv, int index) {
		short startID = (short) (index * getMaxInventoryItems());
		short activeID = (short) (unlockableType.get(serverPlayer) - 1);
		BitSet typeBitSet = serverPlayer.getUnlockables(unlockableType);
		set_(inv, 0, startID, getItemSize(), (unlockableID) -> {
			if (typeBitSet.get((short) unlockableID)) {
				if (unlockableID == activeID)
					return activate(unlockableType.getItem(unlockableID * 2 + 1));
				else
					return unlockableType.getItem(unlockableID * 2 + 1);
			}
			return unlockableType.getItem(unlockableID * 2);
		});
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			if (is.getType() == Material.BARRIER)
				lockedClick(serverPlayer, event);
			else
				unlockedClick(serverPlayer, event);
		}
	}

	protected void lockedClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		int slot = event.getSlot();
		int index = InventoryMenu.parseIndex(event.getView().getTitle(), slot);
		ItemMeta im = event.getCurrentItem().getItemMeta();
		BuyMenu.openBuyUnlockableMenu(serverPlayer, DARK_PURPLE + BOLD + ChatColor.stripColor(im.getDisplayName()),
				im.getLore(), unlockableType.id, (short) index, unlockableType.getPrice(index + 1),
				event.getInventory(), () -> event.getInventory().setItem(slot, unlockableType.getItem(index * 2 + 1)));
	}

	protected void unlockedClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		int slot = event.getSlot();
		int index = InventoryMenu.parseIndex(event.getView().getTitle(), slot);
		int oldIndex = unlockableType.get(serverPlayer) - 1;
		Inventory inv = event.getInventory();
		if (oldIndex == index) {
			inv.setItem(slot, unlockableType.getItem(oldIndex * 2 + 1));
			unlockableType.remove(serverPlayer);
			return;
		}
		if (oldIndex >= 0)
			((InventoryMenu) inv.getHolder()).set(oldIndex, unlockableType.getItem(oldIndex * 2 + 1));

		inv.setItem(slot, activate(event.getCurrentItem()));
		unlockableType.set(serverPlayer, (short) (index + 1));
	}

	protected static ItemStack activate(ItemStack is) {
		ItemStack clone = is.clone();
		ItemMeta im = clone.getItemMeta();
		im.setDisplayName(DARK_PURPLE + BOLD + ChatColor.stripColor(im.getDisplayName()));
		List<String> lore = im.getLore();
		lore.set(0, DEACTIVATE_TEXT);
		lore.set(lore.size() - 1, ACTIVATED_TEXT);
		im.setLore(lore);
		im.addEnchant(Enchantment.DIG_SPEED, 1, false);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		clone.setItemMeta(im);
		return clone;
	}

	protected static boolean isActivate(ItemStack is) {
		return is.getItemMeta().getEnchantLevel(Enchantment.DIG_SPEED) == 1;
	}
}
