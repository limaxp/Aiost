package com.pm.aiost.misc.menu.menus.request.creation;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.request.requests.CallbackMenuRequest;
import com.pm.aiost.player.ServerPlayer;

public abstract class CreationMenu<T> extends SingleInventoryMenu {

	protected List<T> objects;
	protected final ItemStack addItem;
	protected Menu sourceMenu;

	public CreationMenu(String name, ItemStack addItem, Menu sourceMenu) {
		super(name, 6, false);
		objects = new ArrayList<T>();
		this.addItem = addItem;
		this.sourceMenu = sourceMenu;
		setBackLink(ServerPlayer::openMenuRequestPrevMenu);
		set(addItem);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			if (event.getSlot() == objects.size()) {
				serverPlayer.doMenuRequest(new CallbackMenuRequest(sourceMenu) {

					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						addObject(serverPlayer, obj, event.getSlot());
					}

					@Override
					public void openRequest(ServerPlayer serverPlayer) {
						CreationMenu.this.open(serverPlayer);
					}
				});
			} else {
				ClickType click = event.getClick();
				if (click == ClickType.LEFT)
					serverPlayer.setMenuRequestResult(objects.get(event.getSlot()));
				else if (click == ClickType.RIGHT)
					serverPlayer.doMenuRequest(new CallbackMenuRequest(sourceMenu) {

						@Override
						public void onResult(ServerPlayer serverPlayer, Object obj) {
							setObject(serverPlayer, obj, event.getSlot());
						}

						@Override
						public void openRequest(ServerPlayer serverPlayer) {
							CreationMenu.this.open(serverPlayer);
						}
					});
			}
		}
	}

	private void addObject(ServerPlayer serverPlayer, Object obj, int slot) {
		T value = createValue(serverPlayer, obj, slot);
		addNextItem(slot);
		setItem(value, slot);
		objects.add(value);
	}

	private void setObject(ServerPlayer serverPlayer, Object obj, int slot) {
		T value = createValue(serverPlayer, obj, slot);
		setItem(value, slot);
		objects.set(slot, value);
	}

	protected abstract T createValue(ServerPlayer serverPlayer, Object obj, int slot);

	private void addNextItem(int slot) {
		if (slot < 44)
			getInventory().setItem(slot + 1, addItem);
	}

	public void setItem(T value, int slot) {
		getInventory().setItem(slot, createItem(value));
	}

	protected abstract ItemStack createItem(T value);
}
