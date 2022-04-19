package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenuCustomAnimationHandler.InventoryMenuAnimationHandler;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class YesNoMenu extends SingleInventoryMenu {

	public static final List<String> CANCEL_LORE = Collections
			.unmodifiableList(Arrays.asList(GRAY + "Click to " + RED + BOLD + "cancel"));

	private static final InventoryMenuAnimationHandler ANIMATION_HANDLER = new InventoryMenuAnimationHandler() {

		private boolean hasToggledButtons;

		@Override
		protected void animate(Inventory inv) {
			int i;
			if (hasToggledButtons) {
				for (i = 10; i < 17; i++)
					inv.getItem(i).setType(Material.GREEN_STAINED_GLASS_PANE);
				for (i = 19; i < 26; i++)
					inv.getItem(i).setType(Material.GREEN_STAINED_GLASS_PANE);

				for (i = 28; i < 35; i++)
					inv.getItem(i).setType(Material.RED_STAINED_GLASS_PANE);
				for (i = 37; i < 44; i++)
					inv.getItem(i).setType(Material.RED_STAINED_GLASS_PANE);
				hasToggledButtons = false;
			} else {
				for (i = 10; i < 17; i++)
					inv.getItem(i).setType(Material.LIME_STAINED_GLASS_PANE);
				for (i = 19; i < 26; i++)
					inv.getItem(i).setType(Material.LIME_STAINED_GLASS_PANE);

				for (i = 28; i < 35; i++)
					inv.getItem(i).setType(Material.PINK_STAINED_GLASS_PANE);
				for (i = 37; i < 44; i++)
					inv.getItem(i).setType(Material.PINK_STAINED_GLASS_PANE);
				hasToggledButtons = true;
			}
		}
	};

	private BiConsumer<ServerPlayer, InventoryClickEvent> noCallback;

	public YesNoMenu(String name) {
		this(name, null, null);
	}

	public YesNoMenu(String name, List<String> yesDescription) {
		this(name, yesDescription, CANCEL_LORE);
	}

	public YesNoMenu(String name, List<String> yesDescription, List<String> noDescription) {
		super(name, 6, true);
		resetNoCallback();
		ItemStack yes = MetaHelper.setMeta(Material.GREEN_STAINED_GLASS_PANE, GREEN + BOLD + "Yes", yesDescription);
		ItemStack no = MetaHelper.setMeta(Material.RED_STAINED_GLASS_PANE, RED + BOLD + "No", noDescription);
		Inventory inv = getInventory();
		int i;
		for (i = 10; i < 17; i++)
			inv.setItem(i, yes);
		for (i = 19; i < 26; i++)
			inv.setItem(i, yes);
		for (i = 28; i < 35; i++)
			inv.setItem(i, no);
		for (i = 37; i < 44; i++)
			inv.setItem(i, no);
		setAnimationHandler(ANIMATION_HANDLER);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {
			case GREEN_STAINED_GLASS_PANE:
			case LIME_STAINED_GLASS_PANE:
				getYesCallback().accept(serverPlayer, event);
				break;

			case RED_STAINED_GLASS_PANE:
			case PINK_STAINED_GLASS_PANE:
				getNoCallback().accept(serverPlayer, event);
				break;

			default:
				break;
			}
		}
	}

	public void setCallback(BiConsumer<ServerPlayer, InventoryClickEvent> yesCallback,
			BiConsumer<ServerPlayer, InventoryClickEvent> noCallback) {
		setYesCallback(yesCallback);
		setNoCallback(noCallback);
	}

	public void setCallback(BiConsumer<ServerPlayer, InventoryClickEvent> callback) {
		setYesCallback(callback);
	}

	public BiConsumer<ServerPlayer, InventoryClickEvent> getCallback() {
		return getYesCallback();
	}

	public void setYesCallback(BiConsumer<ServerPlayer, InventoryClickEvent> yesCallback) {
		setInventoryClickCallback(yesCallback);
	}

	public void resetYesCallback() {
		setInventoryClickCallback(InventoryMenu.NULL_CLICK_CALLBACK);
	}

	public BiConsumer<ServerPlayer, InventoryClickEvent> getYesCallback() {
		return getInventoryClickCallback();
	}

	public void setNoCallback(BiConsumer<ServerPlayer, InventoryClickEvent> noCallback) {
		this.noCallback = noCallback;
	}

	public void resetNoCallback() {
		this.noCallback = (serverPlayer, event) -> this.openBackLink(serverPlayer);
	}

	public BiConsumer<ServerPlayer, InventoryClickEvent> getNoCallback() {
		return noCallback;
	}
}
