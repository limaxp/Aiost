package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.event.eventHandler.handler.PlayerWorldEventHandler;
import com.pm.aiost.event.eventHandler.handler.ReleasedWorldEventHandler;
import com.pm.aiost.game.GameType;
import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.WorldEffectsMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.requests.CallbackMenuRequest;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.other.WordFilter;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.request.ServerRequest;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.creation.WorldBuilder;
import com.pm.aiost.server.world.creation.WorldLoader;

public class PlayerWorldControlMenu extends SingleInventoryMenu {

	private static final int RELEASE_SLOT = 8;

	private static final ItemStack MAIN_MENU_ITEM = MetaHelper.setMeta(Material.GOLD_BLOCK, RED + BOLD + "Main menu",
			Arrays.asList(GRAY + "Click to open main menu"));

	private static final ItemStack PLAYER_LIST_ITEM = MetaHelper.setMeta(Material.PLAYER_HEAD, GREEN + BOLD + "Player",
			Arrays.asList(GRAY + "Click to open player list"));

	private static final ItemStack ITEM_MENU_ITEM = MetaHelper.setMeta(Material.BLAZE_ROD, GREEN + BOLD + "Item menu",
			Arrays.asList(GRAY + "Click to open item menu"));

	private static final ItemStack SPAWN_MENU_ITEM = MetaHelper.setMeta(Material.ZOMBIE_HEAD,
			GREEN + BOLD + "Spawn menu", Arrays.asList(GRAY + "Click to open spawn menu"));

	private static final ItemStack EFFECTS_MENU_ITEM = MetaHelper.setMeta(Material.FIREWORK_STAR,
			GREEN + BOLD + "Effects", Arrays.asList(GRAY + "Click to open world effects menu"));

	private static final ItemStack SETTING_ITEM = MetaHelper.setMeta(Material.COMPARATOR, GREEN + BOLD + "Setting",
			Arrays.asList(GRAY + "Click to open world settings"));

	private static final ItemStack TOOL_ITEM = MetaHelper.setMeta(Material.WOODEN_AXE, GREEN + BOLD + "Tools",
			Arrays.asList(GRAY + "Click to open tool menu"));

	private static final ItemStack RELEASE_ITEM = MetaHelper.setMeta(Material.DIAMOND_BLOCK,
			GREEN + BOLD + "Release world", Arrays.asList(GRAY + "Click to release this world"));

	private static final ItemStack LOBBY_ITEM = MetaHelper.setMeta(Material.OAK_DOOR, RED + BOLD + "Leave world",
			Arrays.asList(GRAY + "Click to go back to lobby"));

	private static final ItemStack FORBIDDEN_NAME_ITEM = MetaHelper.setMeta(Material.BARRIER,
			RED + BOLD + "Forbidden name!", Arrays.asList(GRAY + "The given name is not allowed"));

	private static final ItemStack[] ITEMS = new ItemStack[] { MAIN_MENU_ITEM, PLAYER_LIST_ITEM, ITEM_MENU_ITEM,
			SPAWN_MENU_ITEM, EFFECTS_MENU_ITEM, SETTING_ITEM, TOOL_ITEM };

	private PlayerWorldEventHandler handler;

	public PlayerWorldControlMenu(PlayerWorldEventHandler handler) {
		super(BOLD + "Player world menu", 3, true);
		this.handler = handler;
		set(ITEMS);
		addBorderItems(new int[] { 8, 17 }, RELEASE_ITEM, LOBBY_ITEM);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {

			case GOLD_BLOCK:
				MainMenu.getMenu().open(serverPlayer);
				break;

			case PLAYER_HEAD:
				new PlayerListMenu(handler.getRegion().getServerPlayer()).open(serverPlayer);
				break;

			case BLAZE_ROD:
				if (canEdit(serverPlayer))
					PlayerWorldItemMenu.getMenu().open(serverPlayer);
				break;

			case ZOMBIE_HEAD:
				if (canEdit(serverPlayer))
					PlayerWorldSpawnMenu.getMenu().open(serverPlayer);
				break;

			case FIREWORK_STAR:
				serverPlayer.doMenuRequest(EFFECTS_MENU_ITEM,
						new SingleMenuRequest(serverPlayer.getServerWorld().getOrCreateMenu(WorldEffectsMenu.class,
								() -> new WorldEffectsMenu(serverPlayer.getServerWorld()))) {

							@Override
							public void onResult(ServerPlayer serverPlayer, Object obj) {
							}

							@Override
							public void openRequest(ServerPlayer serverPlayer) {
								PlayerWorldControlMenu.this.open(serverPlayer);
							}
						});
				break;

			case COMPARATOR:
				if (isOwner(serverPlayer))
					WorldSettingMenu.getMenu().open(serverPlayer);
				break;

			case WOODEN_AXE:
				if (canEdit(serverPlayer))
					PlayerWorldToolMenu.MENU.open(serverPlayer);
				break;

			case DIAMOND_BLOCK:
				if (isOwner(serverPlayer) && isWorld(serverPlayer)) {
					if (handler.isReleased())
						createUpdateWorldMenu(((ReleasedWorldEventHandler) handler).getGameType()).open(serverPlayer);
					else {
						serverPlayer.doMenuRequest(RELEASE_ITEM,
								new CallbackMenuRequest(EnumerationMenus.GAME_TYPE_MENU) {

									@Override
									public void onResult(ServerPlayer serverPlayer, Object obj) {
										openReleaseWorldNameMenu(serverPlayer, (GameType<?>) obj);
									}

									@Override
									public void openRequest(ServerPlayer serverPlayer) {
										PlayerWorldControlMenu.this.open(serverPlayer);
									}
								});
					}
				}
				break;

			case OAK_DOOR:
				ServerRequest.getHandler().sendLobby(serverPlayer);
				break;

			default:
				break;
			}
		}
	}

	private void openReleaseWorldNameMenu(ServerPlayer serverPlayer, GameType<?> type) {
		AnvilMenu menu = new AnvilMenu(BOLD + "Choose name",
				MetaHelper.setMeta(Material.PAPER, handler.getName() + "_")) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					String name = event.getCurrentItem().getItemMeta().getDisplayName();
					if (!WordFilter.containsBlocked(name))
						releaseWorld(serverPlayer, name, type);
					else {
						displayInSlot(PlayerWorldControlMenu.this.getInventory(), FORBIDDEN_NAME_ITEM, RELEASE_SLOT,
								100);
						PlayerWorldControlMenu.this.open(serverPlayer);
					}
				}
			}
		};
		menu.setBackLink(this);
		menu.open(serverPlayer);
	}

	private void releaseWorld(ServerPlayer serverPlayer, String name, GameType<?> type) {
		ServerWorld serverWorld = (ServerWorld) handler.getRegion();
		String answer = type.checkRequired(serverWorld);
		if (answer == null) {
			WorldBuilder.save(serverWorld,
					(world) -> WorldLoader.releasePlayerWorld(handler.getUniqueID(), name, type, world));
			Player player = serverPlayer.player;
			player.sendTitle("", GREEN + "World release successful!", 10, 70, 20);
			serverPlayer.sendActionBar(GRAY + "as " + name);
			player.closeInventory();
		} else {
			displayInSlot(getInventory(),
					MetaHelper.setMeta(Material.BARRIER, RED + BOLD + "Release failed!", Arrays.asList(GRAY + answer)),
					RELEASE_SLOT, 100);
			this.open(serverPlayer);
		}
	}

	private YesNoMenu createUpdateWorldMenu(GameType<?> gameType) {
		YesNoMenu menu = new YesNoMenu(BOLD + "Update " + handler.getName() + "?",
				Arrays.asList(GRAY + "Click to " + GREEN + BOLD + "update" + GRAY + " world"));
		menu.setBackLink(this);
		menu.setYesCallback((serverPlayer, event) -> {
			updateWorld(serverPlayer, gameType);
			PlayerWorldControlMenu.this.open(serverPlayer);
		});
		return menu;
	}

	private void updateWorld(ServerPlayer serverPlayer, GameType<?> type) {
		ServerWorld serverWorld = (ServerWorld) handler.getRegion();
		String answer = type.checkRequired(serverWorld);
		if (answer == null) {
			WorldBuilder.save(serverWorld, (world) -> WorldLoader.saveReleasedWorld(handler.getUniqueID(), world));
			Player player = serverPlayer.player;
			player.sendTitle("", GREEN + "World update successful!", 10, 70, 20);
			player.closeInventory();
		} else {
			displayInSlot(getInventory(),
					MetaHelper.setMeta(Material.BARRIER, RED + BOLD + "Update Failed!", Arrays.asList(GRAY + answer)),
					RELEASE_SLOT, 100);
			this.open(serverPlayer);
		}
	}

	private boolean canEdit(ServerPlayer serverPlayer) {
		if (handler.canModifyWorld(serverPlayer))
			return true;
		else
			serverPlayer.player.sendMessage(RED + BOLD + "You are not allowed to edit this world!");
		return false;
	}

	private boolean isOwner(ServerPlayer serverPlayer) {
		if (handler.isOwner(serverPlayer))
			return true;
		else
			serverPlayer.player.sendMessage(RED + BOLD + "You have to be owner to do this!");
		return false;
	}

	private boolean isWorld(ServerPlayer serverPlayer) {
		if (handler.getRegion().isWorld())
			return true;
		else
			serverPlayer.player.sendMessage(RED + BOLD + "Only possible in world!");
		return false;
	}
}
