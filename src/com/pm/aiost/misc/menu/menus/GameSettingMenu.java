package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.YELLOW;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.game.Game;
import com.pm.aiost.game.GamePlayer;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.ArrayInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.TextMenu;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.other.interfaces.TriConsumer;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class GameSettingMenu<T extends Game> extends ArrayInventoryMenu {

	private static final ItemStack[] ITEMS = new ItemStack[] { MetaHelper.setMeta(Material.IRON_CHESTPLATE,
			YELLOW + BOLD + "Lives", Arrays.asList(GRAY + "Click to change lives")) };

	public GameSettingMenu(T game, ItemStack... items) {
		super(BOLD + game.getType().name + " settings", items.length, true);
		setBackLink(ServerPlayer::openEventHandlerMenu);
		set(ITEMS);
		set(ITEMS.length, items);
		ObjectClickCallback<T> callback = new ObjectClickCallback<T>(game, ITEMS.length + items.length);
		setInventoryClickCallback(callback);
		initClickCallback(callback);
	}

	private void initClickCallback(ObjectClickCallback<T> callback) {
		callback.setCallbacks(0, GameSettingMenu::livesClick);
	}

	@SuppressWarnings("unchecked")
	@SafeVarargs
	protected final void setClickCallbacks(TriConsumer<ServerPlayer, InventoryClickEvent, T>... callbacks) {
		((ObjectClickCallback<T>) getInventoryClickCallback()).setCallbacks(ITEMS.length, callbacks);
	}

	private static void livesClick(ServerPlayer serverPlayer, InventoryClickEvent event, Game game) {
		event.setCancelled(true);
		serverPlayer.doMenuRequest(
				new SingleMenuRequest(TextMenu.createInteger(BOLD + "Choose lives", game.getStartLives())) {

					@Override
					public void openRequest(ServerPlayer serverPlayer) {
						serverPlayer.openInventory(event.getInventory());
					}

					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						int lives = (int) obj;
						game.setStartLives(lives);
						for (GamePlayer gamePlayer : game.getPlayerData())
							gamePlayer.setLives(lives);
					}
				});
	}
}
