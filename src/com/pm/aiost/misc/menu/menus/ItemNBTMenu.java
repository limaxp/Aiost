package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.PURPLE;

import java.util.Arrays;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.Aiost;
import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.ItemCanPlaceMenu.ItemCanDestroyMenu;
import com.pm.aiost.misc.menu.menus.request.CreateItemMenu;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.server.v1_15_R1.NBTTagCompound;

public class ItemNBTMenu {

	private static final InventoryMenu MENU = createMenu();

	private static InventoryMenu createMenu() {
		InventoryMenu menu = new SingleInventoryMenu(BOLD + "Item NBT Menu", 3, true);
		menu.set(
				MetaHelper.setMeta(Material.DIAMOND, PURPLE + BOLD + "Unbreakable",
						Arrays.asList(GRAY + "Click to change breakable status")),

				MetaHelper.setMeta(Material.STONE, PURPLE + BOLD + "Can place on",
						Arrays.asList(GRAY + "Click to change can place on block")),

				MetaHelper.setMeta(Material.IRON_PICKAXE, PURPLE + BOLD + "Can break",
						Arrays.asList(GRAY + "Click to change can break blocks")),

				MetaHelper.setMeta(Material.IRON_SWORD, PURPLE + BOLD + "Attribute modifiers",
						Arrays.asList(GRAY + "Click to change attribute modifiers")),

				MetaHelper.setMeta(Material.WHITE_BANNER, PURPLE + BOLD + "Hide flags",
						Arrays.asList(GRAY + "Click to change hide flags")),

				MetaHelper.setMeta(Material.CRAFTING_TABLE, PURPLE + BOLD + "Custom modeldata",
						Arrays.asList(GRAY + "Click to change custom model data")));
		menu.setInventoryClickCallback(ItemNBTMenu::menuClick);
		menu.setBackLink((serverPlayer) -> serverPlayer.getMenu(CreateItemMenu.class).open(serverPlayer));
		return menu;
	}

	private static void menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {

			case DIAMOND:
				unbreakableClick(serverPlayer, is, event.getSlot());
				break;

			case STONE:
				new ItemCanPlaceMenu(serverPlayer).open(serverPlayer);
				break;

			case IRON_PICKAXE:
				new ItemCanDestroyMenu(serverPlayer).open(serverPlayer);
				break;

			case IRON_SWORD:
				new ItemAttributeModifierMenu(serverPlayer).open(serverPlayer);
				break;

			case WHITE_BANNER:
				ItemHideFlagsMenu.getMenu().open(serverPlayer);
				break;

			case CRAFTING_TABLE:
				createChangeCustomModeldataMenu(serverPlayer).open(serverPlayer);
				break;

			default:
				break;
			}
		}
	}

	private static void unbreakableClick(ServerPlayer serverPlayer, ItemStack is, int slot) {
		ItemNBTMenu.modifyNBT(serverPlayer, (nbtTag) -> {
			boolean activated = NBTHelper.switchUnbreakable(nbtTag);
			Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(), () -> {
				ItemStack clone = is.clone();
				if (activated)
					clone.setType(Material.LIME_DYE);
				else
					clone.setType(Material.GRAY_DYE);
				InventoryMenu.displayInSlot(serverPlayer.player, clone, slot);
			}, 1);
		});
	}

	private static AnvilMenu createChangeCustomModeldataMenu(ServerPlayer serverPlayer) {
		AnvilMenu menu = new AnvilMenu(BOLD + "custom modeldata", MetaHelper.setMeta(Material.PAPER,
				Integer.toString(NBTHelper.getCustomModelData(NBTHelper.getNBT(getItem(serverPlayer)))))) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					int id;
					try {
						id = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName());
					} catch (NumberFormatException e) {
						serverPlayer.player.sendMessage(ChatColor.RED + "Your input must be a number!");
						return;
					}
					modifyNBT(serverPlayer, (nbtTag) -> NBTHelper.setCustomModelData(nbtTag, id));
					ItemNBTMenu.MENU.open(serverPlayer);
				}
			}
		};
		menu.setBackLink(ItemNBTMenu.MENU);
		return menu;
	}

	public static void modifyNBT(ServerPlayer serverPlayer, Consumer<NBTTagCompound> consumer) {
		CreateItemMenu createItemMenu = (CreateItemMenu) serverPlayer.getMenu(CreateItemMenu.class);
		createItemMenu.setItem(NBTHelper.modifyNBT(NMS.getNMS(createItemMenu.getItem()), consumer));
	}

	public static ItemStack getItem(ServerPlayer serverPlayer) {
		return ((CreateItemMenu) serverPlayer.getMenu(CreateItemMenu.class)).getItem();
	}

	public static InventoryMenu getMenu() {
		return MENU;
	}
}
