package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.effects.BuildStaffEffect;
import com.pm.aiost.event.effect.effects.MarkerPlacerEffect;
import com.pm.aiost.event.effect.effects.RegionCreatorEffect;
import com.pm.aiost.event.effect.effects.WorldBrushEffect;
import com.pm.aiost.event.effect.effects.WorldEditorEffect;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.player.ServerPlayer;

public class PlayerWorldToolMenu {

	public static final InventoryMenu MENU = createMenu();

	public static InventoryMenu createMenu() {
		InventoryMenu menu = new SingleInventoryMenu(BOLD + "Tool menu", 3, true);
		menu.set(
				MetaHelper.setMeta(Material.BLAZE_ROD, GREEN + BOLD + "Build staff",
						Arrays.asList(GRAY + "Click to get build staff")),

				MetaHelper.setMeta(Material.ARMOR_STAND, GREEN + BOLD + "Marker",
						Arrays.asList(GRAY + "Click to get marker tool")),

				MetaHelper.setMeta(Material.RED_BANNER, GREEN + BOLD + "Region",
						Arrays.asList(GRAY + "Click to get region tool")),

				MetaHelper.setMeta(Material.WOODEN_AXE, GREEN + BOLD + "World editor",
						Arrays.asList(GRAY + "Click to get world edit tool")),

				MetaHelper.setMeta(Material.ARROW, GREEN + BOLD + "World brush",
						Arrays.asList(GRAY + "Click to get world brush tool")),

				MetaHelper.setMeta(Material.DEBUG_STICK, Arrays.asList(GRAY + "Click to get debug stick")));
		menu.setInventoryClickCallback(PlayerWorldToolMenu::menuClick);
		menu.setBackLink(ServerPlayer::openEventHandlerMenu);
		return menu;
	}

	public static boolean menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {

			case BLAZE_ROD:
				createBuildStaff(serverPlayer);
				break;

			case ARMOR_STAND:
				giveMarkerItem(serverPlayer);
				break;

			case RED_BANNER:
				giveRegionItem(serverPlayer);
				break;

			case WOODEN_AXE:
				giveWorldEditItem(serverPlayer);
				break;

			case ARROW:
				giveWorldBrushItem(serverPlayer);
				break;

			case DEBUG_STICK:
				giveDebugStick(serverPlayer);
				break;

			default:
				return false;
			}
		}
		return true;
	}

	public static void createBuildStaff(ServerPlayer serverPlayer) {
		Effect effect = new BuildStaffEffect();
		serverPlayer.doMenuRequest(effect.getClass(),
				effect.getMenuRequest(serverPlayer, MENU::open, (serverPlayer1) -> {
					int effectId = serverPlayer1.getServerWorld().getWorldEffects().addTemp(effect);
					serverPlayer1.addItem(NBTHelper.setWorldEffect(createBuildStaff(effect), effectId));
					serverPlayer1.closeInventory();
				}));
	}

	private static ItemStack createBuildStaff(Effect effect) {
		List<String> lore = new ArrayList<String>();
		lore.add(GRAY + "Left click to break blocks");
		lore.add(GRAY + "Right click to place blocks");
		lore.add(null);
		effect.createDescription(lore);
		return MetaHelper.setMeta(new ItemStack(Material.BLAZE_ROD), GRAY + BOLD + "Build staff", lore);
	}

	public static void giveMarkerItem(ServerPlayer serverPlayer) {
		int effectId = serverPlayer.getServerWorld().getWorldEffects().addTemp(new MarkerPlacerEffect());
		serverPlayer.addItem(NBTHelper.setWorldEffect(createMarkerItem(), effectId));
	}

	private static ItemStack createMarkerItem() {
		List<String> lore = new ArrayList<String>();
		lore.add(GRAY + "Right click to place marker");
		lore.add(GRAY + "Shift click to choose marker");
		return MetaHelper.setMeta(new ItemStack(Material.ARMOR_STAND), GRAY + BOLD + "Marker", lore);
	}

	public static void giveRegionItem(ServerPlayer serverPlayer) {
		int effectId = serverPlayer.getServerWorld().getWorldEffects().addTemp(new RegionCreatorEffect());
		serverPlayer.addItem(NBTHelper.setWorldEffect(createRegionItem(), effectId));
	}

	private static ItemStack createRegionItem() {
		List<String> lore = new ArrayList<String>();
		lore.add(GRAY + "Left click to choose 1st position");
		lore.add(GRAY + "Right click to choose 2nd position");
		lore.add(GRAY + "Shift click to create region");
		return MetaHelper.setMeta(new ItemStack(Material.RED_BANNER), GRAY + BOLD + "Region", lore);
	}

	public static void giveWorldEditItem(ServerPlayer serverPlayer) {
		int effectId = serverPlayer.getServerWorld().getWorldEffects().addTemp(new WorldEditorEffect());
		serverPlayer.addItem(NBTHelper.setWorldEffect(createWorldEditItem(), effectId));
	}

	private static ItemStack createWorldEditItem() {
		List<String> lore = new ArrayList<String>();
		lore.add(GRAY + "Left click to choose 1st position");
		lore.add(GRAY + "Right click to choose 2nd position");
		lore.add(GRAY + "Shift click to open menu");
		return MetaHelper.setMeta(new ItemStack(Material.WOODEN_AXE), GRAY + BOLD + "World editor", lore);
	}

	public static void giveWorldBrushItem(ServerPlayer serverPlayer) {
		int effectId = serverPlayer.getServerWorld().getWorldEffects().addTemp(new WorldBrushEffect());
		serverPlayer.addItem(NBTHelper.setWorldEffect(createWorldBrushItem(), effectId));
	}

	private static ItemStack createWorldBrushItem() {
		List<String> lore = new ArrayList<String>();
		lore.add(GRAY + "Left click to fill brush");
		lore.add(GRAY + "Right click to erase brush");
		lore.add(GRAY + "Shift click to open menu");
		return MetaHelper.setMeta(new ItemStack(Material.ARROW), GRAY + BOLD + "World brush", lore);
	}

	public static void giveDebugStick(ServerPlayer serverPlayer) {
		serverPlayer.addItem(new ItemStack(Material.DEBUG_STICK));
	}
}
