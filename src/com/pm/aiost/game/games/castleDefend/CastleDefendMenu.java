package com.pm.aiost.game.games.castleDefend;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.YELLOW;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.pm.aiost.misc.menu.menus.GameSettingMenu;
import com.pm.aiost.misc.menu.menus.request.TextMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenu;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.server.v1_15_R1.EntityTypes;

public class CastleDefendMenu extends GameSettingMenu<CastleDefend> {

	public CastleDefendMenu(CastleDefend game) {
		super(game,
				MetaHelper.setMeta(Material.ZOMBIE_HEAD, YELLOW + BOLD + "Wave",
						Arrays.asList(GRAY + "Click to change wave")),

				MetaHelper.setMeta(Material.ZOMBIE_HEAD, YELLOW + BOLD + "Lock entity types",
						Arrays.asList(GRAY + "Click to lock entity types")),

				MetaHelper.setMeta(Material.ZOMBIE_HEAD, YELLOW + BOLD + "Unlock entity types",
						Arrays.asList(GRAY + "Click to unlock entity types")));

		setClickCallbacks(CastleDefendMenu::waveClick, CastleDefendMenu::lockEntityClick,
				CastleDefendMenu::unlockEntityClick);
	}

	private static void waveClick(ServerPlayer serverPlayer, InventoryClickEvent event, CastleDefend game) {
		event.setCancelled(true);
		serverPlayer.doMenuRequest(new SingleMenuRequest(TextMenu.createInteger(BOLD + "Choose wave", game.getWave())) {

			@Override
			public void openRequest(ServerPlayer serverPlayer) {
				serverPlayer.openInventory(event.getInventory());
			}

			@Override
			public void onResult(ServerPlayer serverPlayer, Object obj) {
				game.setWave((int) obj);
			}
		});
	}

	private static void lockEntityClick(ServerPlayer serverPlayer, InventoryClickEvent event, CastleDefend game) {
		event.setCancelled(true);
		serverPlayer.doMenuRequest(new SingleMenuRequest(new EnumerationMenu<EntityTypes<?>>(BOLD + "Lock entity type",
				game.getEntityTypes(), EnumerationMenu::createItem)) {

			@Override
			public void openRequest(ServerPlayer serverPlayer) {
				serverPlayer.openInventory(event.getInventory());
			}

			@Override
			public void onResult(ServerPlayer serverPlayer, Object obj) {
				game.removeEntityType((EntityTypes<?>) obj);
			}
		});
	}

	private static void unlockEntityClick(ServerPlayer serverPlayer, InventoryClickEvent event, CastleDefend game) {
		event.setCancelled(true);
		serverPlayer
				.doMenuRequest(new SingleMenuRequest(new EnumerationMenu<EntityTypes<?>>(BOLD + "Unlock entity type",
						game.getLockedTypes(), EnumerationMenu::createItem)) {

					@Override
					public void openRequest(ServerPlayer serverPlayer) {
						serverPlayer.openInventory(event.getInventory());
					}

					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						game.addEntityType((EntityTypes<?>) obj);
					}
				});
	}
}
