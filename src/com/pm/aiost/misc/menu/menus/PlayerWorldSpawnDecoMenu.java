package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BLUE;
import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.mojang.authlib.GameProfile;
import com.pm.aiost.entity.npc.profile.ProfileFetcher;
import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.CreateItemMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.misc.packet.entity.entities.EntityFurniture;
import com.pm.aiost.misc.packet.entity.entities.PacketEntityFallingBlock;
import com.pm.aiost.misc.packet.entity.entities.PacketEntityLiving;
import com.pm.aiost.misc.packet.entity.entities.PacketPlayer;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.server.v1_15_R1.EntityTypes;

public class PlayerWorldSpawnDecoMenu {

	private static final Object ENTITY_TYPE_MENU_IDENTIFIER = new Object();

	private static final Object PLAYER_PROFILE_MENU_IDENTIFIER = new Object();

	private static final InventoryMenu MENU = createMenu();

	private static InventoryMenu createMenu() {
		InventoryMenu menu = new SingleInventoryMenu(BOLD + "Spawn Deco Menu", 3, true);
		menu.set(
				MetaHelper.setMeta(Material.ARMOR_STAND, BLUE + BOLD + "Deco",
						Arrays.asList(GRAY + "Click to spawn a decoration")),

				MetaHelper.setMeta(Material.ZOMBIE_HEAD, GREEN + BOLD + "Entity",
						Arrays.asList(GRAY + "Click to spawn a deco entity")),

				MetaHelper.setMeta(Material.PLAYER_HEAD, GOLD + BOLD + "Player",
						Arrays.asList(GRAY + "Left click to choose profile",
								GRAY + "Right click to search for profile per name")),

				MetaHelper.setMeta(Material.STONE, GRAY + BOLD + "Block",
						Arrays.asList(GRAY + "Click to spawn a deco block")));
		menu.setInventoryClickCallback(PlayerWorldSpawnDecoMenu::menuClick);
		menu.setBackLink(PlayerWorldSpawnMenu.getMenu());
		return menu;
	}

	private static void menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {

			case ARMOR_STAND:
				serverPlayer.doMenuRequest(MENU, () -> new SingleMenuRequest(
						serverPlayer.getOrCreateMenu(CreateItemMenu.class, CreateItemMenu::new)) {

					@Override
					public void openRequest(ServerPlayer serverPlayer) {
						PlayerWorldSpawnDecoMenu.MENU.open(serverPlayer);
					}

					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						spawnFurniture(serverPlayer, (ItemStack) obj);
					}
				});
				break;

			case ZOMBIE_HEAD:
				serverPlayer.doMenuRequest(ENTITY_TYPE_MENU_IDENTIFIER,
						() -> new SingleMenuRequest(EnumerationMenus.ENTITY_TYPE_MENU) {

							@Override
							public void openRequest(ServerPlayer serverPlayer) {
								PlayerWorldSpawnDecoMenu.MENU.open(serverPlayer);
							}

							@Override
							public void onResult(ServerPlayer serverPlayer, Object obj) {
								spawnEntityLiving(serverPlayer, (EntityTypes<?>) obj);
							}
						});
				break;

			case PLAYER_HEAD:
				ClickType click = event.getClick();
				if (click == ClickType.LEFT || click == ClickType.SHIFT_LEFT)
					serverPlayer.doMenuRequest(PLAYER_PROFILE_MENU_IDENTIFIER,
							() -> new SingleMenuRequest(EnumerationMenus.GAME_RPOFILES_MENU) {

								@Override
								public void openRequest(ServerPlayer serverPlayer) {
									PlayerWorldSpawnDecoMenu.MENU.open(serverPlayer);
								}

								@Override
								public void onResult(ServerPlayer serverPlayer, Object obj) {
									spawnPlayer(serverPlayer, (GameProfile) obj);
								}
							});
				else if (click == ClickType.RIGHT || click == ClickType.SHIFT_RIGHT)
					createPlayerNameMenu().open(serverPlayer);
				break;

			case STONE:
				serverPlayer.doMenuRequest(MENU, () -> new SingleMenuRequest(EnumerationMenus.BLOCK_MENU) {

					@Override
					public void openRequest(ServerPlayer serverPlayer) {
						PlayerWorldSpawnDecoMenu.MENU.open(serverPlayer);
					}

					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						spawnBlock(serverPlayer, (Material) obj);
					}
				});
				break;

			default:
				break;
			}
		}
	}

	private static AnvilMenu createPlayerNameMenu() {
		AnvilMenu menu = new AnvilMenu(BOLD + "Choose name", MetaHelper.setMeta(Material.PAPER, "name")) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					spawnPlayer(serverPlayer,
							ProfileFetcher.fetch(event.getCurrentItem().getItemMeta().getDisplayName(), false));
					PlayerWorldSpawnDecoMenu.MENU.open(serverPlayer);
				}
			}
		};
		menu.setBackLink(PlayerWorldSpawnDecoMenu.MENU);
		return menu;
	}

	private static void spawnFurniture(ServerPlayer serverPlayer, ItemStack is) {
		EntityFurniture furniture = new EntityFurniture(serverPlayer.getServerWorld(), is);
		PacketEntityTypes.spawn(furniture, serverPlayer.player.getLocation());
	}

	private static void spawnEntityLiving(ServerPlayer serverPlayer, EntityTypes<?> type) {
		PacketEntityLiving entity = new PacketEntityLiving(serverPlayer.getServerWorld(), type);
		PacketEntityTypes.spawn(entity, serverPlayer.player.getLocation());
	}

	private static void spawnPlayer(ServerPlayer serverPlayer, GameProfile profile) {
		PacketPlayer player = new PacketPlayer(serverPlayer.getServerWorld(), profile);
		PacketEntityTypes.spawn(player, serverPlayer.player.getLocation());
	}

	private static void spawnBlock(ServerPlayer serverPlayer, Material material) {
		PacketEntityFallingBlock player = new PacketEntityFallingBlock(serverPlayer.getServerWorld(), material);
		PacketEntityTypes.spawn(player, serverPlayer.player.getLocation());
	}

	public static InventoryMenu getMenu() {
		return MENU;
	}
}
