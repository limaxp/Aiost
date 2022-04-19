package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.block.BlockMaterial;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class SearchBlockMenu extends SingleInventoryMenu {

	protected String text;

	public SearchBlockMenu(String name) {
		this(name, "");
	}

	public SearchBlockMenu(String name, String text) {
		super(name, 6, false);
		this.text = text;
		setBackLink(ServerPlayer::openMenuRequestPrevMenu);
		addBorderItem(9, MetaHelper.setMeta(Material.PAPER, GRAY + BOLD + "Search",
				Arrays.asList(GRAY + "Click to search for block")));
		if (text != null && !text.isEmpty())
			search(text);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		int slot = event.getSlot();
		if (event.getCurrentItem() == null)
			return;
		if (slot == 45)
			serverPlayer.doMenuRequest(new SingleMenuRequest(() -> TextMenu.create(BOLD + "Choose name", text)) {

				@Override
				public void openRequest(ServerPlayer serverPlayer) {
					SearchBlockMenu.this.open(serverPlayer);
				}

				@Override
				public void onResult(ServerPlayer serverPlayer, Object obj) {
					search((String) obj);
				}
			});
		else
			serverPlayer.setMenuRequestResult(event.getCurrentItem());
	}

	private void search(String text) {
		this.text = text;
		int j = 0;
		for (int i = 0; i < BlockMaterial.size(); i++) {
			Material material = BlockMaterial.get(i);
			if (material.name().contains(text.toUpperCase())) {
				ItemStack item = new ItemStack(material);
				getInventory().setItem(j++, item);
				if (j >= MAX_ITEMS)
					return;
			}
		}
	}
}
