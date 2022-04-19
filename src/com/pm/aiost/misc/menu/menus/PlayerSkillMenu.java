package com.pm.aiost.misc.menu.menus;

import java.util.BitSet;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.player.unlockable.UnlockableType;

public class PlayerSkillMenu extends UnlockableMenu {

	public PlayerSkillMenu(ServerPlayer serverPlayer, UnlockableType unlockableType) {
		super(serverPlayer, unlockableType);
	}

	@Override
	public void buildInventory(Inventory inv, int index) {
		short startID = (short) (index * getMaxInventoryItems());
		BitSet typeBitSet = serverPlayer.getUnlockables(unlockableType);
		set_(inv, 0, startID, getItemSize(), (unlockableID) -> {
			if (typeBitSet.get((short) unlockableID))
				return unlockableType.getItem(unlockableID * 2 + 1);
			return unlockableType.getItem(unlockableID * 2);
		});
	}

	@Override
	protected void unlockedClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		int slot = event.getSlot();
		int index = InventoryMenu.parseIndex(event.getView().getTitle(), slot);
		Inventory inv = event.getInventory();
		ItemStack item = event.getCurrentItem();
		if (isActivate(item)) {
			inv.setItem(slot, unlockableType.getItem(index * 2 + 1));
			unlockableType.remove(serverPlayer, (short) (index + 1));
		} else {
			inv.setItem(slot, activate(event.getCurrentItem()));
			unlockableType.set(serverPlayer, (short) (index + 1));
		}
	}
}
