package com.pm.aiost.misc.menu.menus;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.collection.list.IdentityArrayList;
import com.pm.aiost.collection.list.UnorderedIdentityArrayList;
import com.pm.aiost.game.Game;
import com.pm.aiost.game.GameType;
import com.pm.aiost.game.data.IGameData;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.FillableListInventoryMenu;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.messaging.ServerDataRequester;
import com.pm.aiost.server.request.ServerRequest;

public class GameJoinMenu extends FillableListInventoryMenu {

	private static final String CLICK_TEXT = ChatColor.GRAY + "Click to join game";

	private static final GameJoinMenu[] MENUS;
	private static final List<GameJoinMenu> VIEWED_MENUS = new UnorderedIdentityArrayList<GameJoinMenu>();

	static {
		Iterator<GameType<?>> iterator = AiostRegistry.GAMES.iterator();
		MENUS = new GameJoinMenu[AiostRegistry.GAMES.size()];
		int i = 0;
		while (iterator.hasNext())
			MENUS[i++] = new GameJoinMenu(iterator.next(), true);
	}

	private final GameType<?> type;
	private List<IGameData> games;
	private int viewerCount;

	private GameJoinMenu(GameType<?> type, boolean hasBorder) {
		super(ChatColor.BOLD + type.name, hasBorder);
		this.type = type;
		this.games = new IdentityArrayList<IGameData>();
		setBackLink(ServerPlayer::openMenuRequest);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null)
			Game.join(serverPlayer,
					games.get(InventoryMenu.parseBorderedIndex(event.getView().getTitle(), event.getSlot())),
					event.getInventory());
	}

	@Override
	public void onInventoryOpen(InventoryOpenEvent event) {
		super.onInventoryOpen(event);
		if (viewerCount++ == 0) {
			update();
			VIEWED_MENUS.add(this);
			ServerDataRequester.registerGameType(type);
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent event) {
		super.onInventoryClose(event);
		if (viewerCount-- == 1) {
			VIEWED_MENUS.remove(this);
			ServerDataRequester.unregisterGameType(type);
		}
	}

	private void update() {
		if (viewerCount > 0)
			update(ServerRequest.getHandler().getGames(type));
	}

	public void update(List<? extends IGameData> gameList) {
		int size = gameList.size();
		int oldSize = games.size();
		if (size > oldSize) {
			for (int i = oldSize; i < size; i++) {
				IGameData game = gameList.get(i);
				set(i, createItem(game));
				games.add(game);
			}
		} else if (size < oldSize) {
			for (int i = oldSize - 1; i >= size; i--) {
				set(i, (ItemStack) null);
				games.remove(i);
			}
		}
		for (int i = 0; i < oldSize; i++) {
			IGameData game = gameList.get(i);
			if (game != games.get(i)) {
				set(i, createItem(game));
				games.set(i, game);
			}
		}
	}

	public static void updateMenus() {
		int length = VIEWED_MENUS.size();
		for (int i = 0; i < length; i++)
			VIEWED_MENUS.get(i).update();
	}

	private static ItemStack createItem(IGameData game) {
		GameType<?> type = game.getType();
		return MetaHelper.setMeta(type.item, game.getName(),
				Arrays.asList(CLICK_TEXT, null,
						ChatColor.GRAY + "uuid: " + ChatColor.DARK_GRAY
								+ game.getUniqueId().toString().substring(0, 16),
						ChatColor.GRAY + "author: " + ChatColor.DARK_GRAY + game.getAuthorName(),
						ChatColor.GRAY + "player: " + ChatColor.DARK_GRAY + game.getPlayerSize(),
						ChatColor.GRAY + "min player: " + ChatColor.DARK_GRAY + game.getMinPlayer(),
						ChatColor.GRAY + "max player: " + ChatColor.DARK_GRAY + game.getMaxPlayer(),
						ChatColor.GRAY + "password: " + ChatColor.DARK_GRAY + (game.hasPassword() ? "yes" : "no")));
	}

	public static InventoryMenu getMenu(GameType<?> type) {
		return MENUS[type.index];
	}

	public static void openMenu(ServerPlayer serverPlayer, GameType<?> type) {
		MENUS[type.index].open(serverPlayer);
	}
}
