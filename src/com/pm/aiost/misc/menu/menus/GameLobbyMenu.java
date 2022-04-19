package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.AQUA;
import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.game.Game;
import com.pm.aiost.game.GameLobby;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.request.ServerRequest;

public class GameLobbyMenu extends GameKitMenu {

	public static final int GAME_SETTING_ITEM_SLOT = 0;
	public static final int CHANGE_TEAM_ITEM_SLOT = 8;

	public static final ItemStack CHANGE_TEAM_ITEM = MetaHelper.setMeta(Material.RED_BANNER,
			AQUA + BOLD + "Change team", Arrays.asList(GRAY + "Click to change your team"));

	private static final ItemStack LOBBY_ITEM = MetaHelper.setMeta(Material.OAK_DOOR, RED + BOLD + "Leave game",
			Arrays.asList(GRAY + "Click to go back to lobby"));

	private static final ItemStack GAME_SETTING_ITEM = MetaHelper.setMeta(Material.COMPARATOR,
			RED + BOLD + "Game settings", Arrays.asList(GRAY + "Click to open setting menu"));

	private static final ItemStack NOT_HOST_ITEM = MetaHelper.setMeta(Material.BARRIER, RED + BOLD + "Host only!",
			Arrays.asList(GRAY + "Only the host can change settings"));

	private GameTeamMenu teamMenu;

	public GameLobbyMenu(GameLobby gameLobby) {
		this(gameLobby, gameLobby.getGame());
	}

	private GameLobbyMenu(GameLobby gameLobby, Game game) {
		super(BOLD + game.getType().name + " menu", game.getKits());
		teamMenu = new GameTeamMenu(game);
	}

	@Override
	public void buildInventory(Inventory inv, int index) {
		super.buildInventory(inv, index);
		addBorderItem(GAME_SETTING_ITEM_SLOT, GAME_SETTING_ITEM);
		addBorderItem(CHANGE_TEAM_ITEM_SLOT, CHANGE_TEAM_ITEM);
		addBorderItem(17, LOBBY_ITEM);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			if (is.getType() == Material.OAK_DOOR) {
				ServerRequest.getHandler().sendLobby(serverPlayer);
				return;
			}
			int slot = event.getSlot();
			if (slot == GAME_SETTING_ITEM_SLOT)
				openSettingMenu(serverPlayer, event);
			else if (slot == CHANGE_TEAM_ITEM_SLOT)
				teamMenu.open(serverPlayer);
			else
				kitInventoryClick(serverPlayer, event);
		}
	}

	private void openSettingMenu(ServerPlayer serverPlayer, InventoryClickEvent event) {
		Game game = serverPlayer.getGameData().game;
		if (!game.isHost(serverPlayer)) {
			InventoryMenu.displayInSlot(serverPlayer.player, NOT_HOST_ITEM, event.getCurrentItem(), event.getSlot(),
					30);
			serverPlayer.sendMessage(RED + "Only the host can change settings!");
			return;
		}
		game.getSettingMenu().open(serverPlayer);
	}
}
